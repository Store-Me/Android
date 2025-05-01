package com.store_me.storeme.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
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
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.store_me.storeme.R
import com.store_me.storeme.data.PostContentType
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.data.store.post.NormalPostData
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE

@Composable
fun NormalPostPreviewItem(
    storeInfoData: StoreInfoData,
    normalPost: NormalPostData
) {
    val imageUrls = normalPost.content.filter { it.type == PostContentType.IMAGE.name }.map { it.content }
    val texts = normalPost.content.filter { it.type == PostContentType.TEXT.name }.map { it.content }

    val state = rememberRichTextState()

    LaunchedEffect(texts) {
        if(texts.isNotEmpty()) {
            state.setHtml(texts.first())
        }
    }

    when(imageUrls.isEmpty()) {
        false -> {
            //Has Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                    .clip(RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = normalPost.title,
                                style = storeMeTextStyle(FontWeight.ExtraBold, 4),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                            )

                            IconButton(
                                onClick = {  }
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

                        Spacer(modifier = Modifier.height(12.dp))

                        if(texts.isNotEmpty()) {
                            RichText(
                                state = state,
                                style = storeMeTextStyle(FontWeight.Normal, 3),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    PostPreviewProfileBox(
                        storeInfoData = storeInfoData
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    IconButton(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(shape = CircleShape)
                            .shadow(elevation = 8.dp, shape = CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White
                        ),
                        onClick = {  }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_like_off),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(36.dp)
                        )
                    }

                    IconButton(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(shape = CircleShape)
                            .shadow(elevation = 8.dp, shape = CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White
                        ),
                        onClick = {  }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_comment),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(36.dp)
                        )
                    }
                }

            }
        }
        true -> {
            //No Image
        }
    }
}

@Composable
fun PostPreviewProfileBox(
    storeInfoData: StoreInfoData
) {
    Box(
        modifier = Modifier
            .height(48.dp)
            .background(color = Color.White, shape = CircleShape)
            .clip(CircleShape)
            .shadow(elevation = 8.dp, shape = CircleShape)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
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