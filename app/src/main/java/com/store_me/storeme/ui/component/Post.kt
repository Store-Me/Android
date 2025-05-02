package com.store_me.storeme.ui.component

import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.data.PostContentType
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.data.store.post.NormalPostData
import com.store_me.storeme.ui.theme.PostBackgroundColor0
import com.store_me.storeme.ui.theme.PostBackgroundColor1
import com.store_me.storeme.ui.theme.PostBackgroundColor2
import com.store_me.storeme.ui.theme.PostBackgroundColor3
import com.store_me.storeme.ui.theme.PostBackgroundColor4
import com.store_me.storeme.ui.theme.PostBackgroundColor5
import com.store_me.storeme.ui.theme.PostBackgroundColor6
import com.store_me.storeme.ui.theme.PostBackgroundColor7
import com.store_me.storeme.ui.theme.PostBackgroundColor8
import com.store_me.storeme.ui.theme.PostBackgroundColor9
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE


/**
 * Normal Post Preview Composable
 * @param modifier Modifier
 * @param storeInfoData StoreInfoData
 * @param normalPost NormalPostData
 * @param onPostClick onClick of Post
 * @param onProfileClick onClick of Profile
 */
@Composable
fun NormalPostPreviewItem(
    modifier: Modifier = Modifier,
    storeInfoData: StoreInfoData,
    normalPost: NormalPostData,
    onPostClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val imageUrls = normalPost.content.filter { it.type == PostContentType.IMAGE.name }.map { it.content }
    val texts = normalPost.content.filter { it.type == PostContentType.TEXT.name }.map { it.content }

    val plainText = remember(texts) {
        texts.joinToString(separator = " ") {
            Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                .toString()
                .replace("\n", " ")
                .trim()
        }
    }


    when(imageUrls.isEmpty()) {
        false -> {
            //Has Image
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                    .background(color = Color.White, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                    .clickable { onPostClick() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    SkeletonAsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 1f)
                            .clip(shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)),
                        imageUrl = imageUrls.first(),
                        contentScale = ContentScale.Crop,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 1f)
                            .padding(horizontal = 20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(36.dp))

                        TitleAndMenuRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            title = normalPost.title,
                            onMenuClick = {

                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if(texts.isNotEmpty()) {
                            Text(
                                text = plainText,
                                style = storeMeTextStyle(FontWeight.Normal, 0),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                StoreInfoAndButtonsRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    storeInfoData = storeInfoData,
                    onProfileClick = onProfileClick,
                    onLikeClick = {

                    },
                    onCommentClick = {

                    }
                )
            }
        }
        true -> {
            //이미지 없는 경우
            val backgroundColor = when(normalPost.createdAt.seconds % 10) {
                0L -> PostBackgroundColor0
                1L -> PostBackgroundColor1
                2L -> PostBackgroundColor2
                3L -> PostBackgroundColor3
                4L -> PostBackgroundColor4
                5L -> PostBackgroundColor5
                6L -> PostBackgroundColor6
                7L -> PostBackgroundColor7
                8L -> PostBackgroundColor8
                9L -> PostBackgroundColor9
                else -> PostBackgroundColor0
            }

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                    .background(color = Color.White, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                    .clickable { onPostClick() },
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 1f)
                            .background(color = backgroundColor, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                            .padding(horizontal = 20.dp)
                    ) {

                        Spacer(modifier = Modifier.height(12.dp))

                        TitleAndMenuRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            title = normalPost.title,
                            onMenuClick = {

                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if(texts.isNotEmpty()) {
                            Text(
                                text = plainText,
                                style = storeMeTextStyle(FontWeight.Normal, 0),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(44.dp))
                }

                StoreInfoAndButtonsRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    storeInfoData = storeInfoData,
                    onProfileClick = onProfileClick,
                    onLikeClick = {

                    },
                    onCommentClick = {

                    }
                )
            }
        }
    }
}

@Composable
fun TitleAndMenuRow(
    modifier: Modifier,
    title: String,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = storeMeTextStyle(FontWeight.ExtraBold, 4),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
        )

        IconButton(
            onClick = onMenuClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun StoreInfoAndButtonsRow(
    modifier: Modifier,
    storeInfoData: StoreInfoData,
    onProfileClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            PostPreviewProfileBox(
                storeInfoData = storeInfoData,
                onProfileClick = onProfileClick
            )
        }

        IconButton(
            modifier = Modifier
                .size(48.dp)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .clip(shape = CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White
            ),
            onClick = onLikeClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_like_off),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
            )
        }

        IconButton(
            modifier = Modifier
                .size(48.dp)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .clip(shape = CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White
            ),
            onClick = onCommentClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_comment),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun PostPreviewProfileBox(
    storeInfoData: StoreInfoData,
    onProfileClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(48.dp)
            .shadow(elevation = 4.dp, shape = CircleShape)
            .background(color = Color.White, shape = CircleShape)
            .clickable { onProfileClick() }
            .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                accountType = AccountType.OWNER,
                url = storeInfoData.storeProfileImage
            )

            Text(
                text = storeInfoData.storeName,
                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}