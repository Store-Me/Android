@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.data.PostContentType
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.data.store.post.NormalPostData
import com.store_me.storeme.ui.theme.ErrorColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.PostBackgroundUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import kotlinx.coroutines.launch

/**
 * Normal Post Preview Composable
 * @param modifier Modifier
 * @param storeInfoData StoreInfoData
 * @param normalPost NormalPostData
 * @param onPostClick onClick of Post
 * @param onProfileClick onClick of Profile
 * @param onLikeClick onClick of Like
 * @param onCommentClick onClick of Comment
 * @param onClickEdit onClick of Edit
 * @param onClickDelete onClick of Delete
 * @param onClickReport onClick of Report
 */
@Composable
fun NormalPostPreviewItem(
    modifier: Modifier = Modifier,
    storeInfoData: StoreInfoData,
    normalPost: NormalPostData,
    onPostClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    onClickReport: () -> Unit
) {
    val auth = LocalAuth.current
    val accountType by auth.accountType.collectAsState()

    val scope = rememberCoroutineScope()

    val imageUrls = normalPost.content.filter { it.type == PostContentType.IMAGE.name }.map { it.content }
    val texts = normalPost.content.filter { it.type == PostContentType.TEXT.name }.map { it.content }

    val sheetState =  rememberModalBottomSheetState()
    var showMenuBottomSheet by remember { mutableStateOf(false) }

    var deleteNormalPost by remember { mutableStateOf<NormalPostData?>(null) }

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
                                showMenuBottomSheet = true
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
                    normalPost = normalPost,
                    onProfileClick = onProfileClick,
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick
                )
            }
        }
        true -> {
            //이미지 없는 경우
            val backgroundColor = PostBackgroundUtils.getPostBackgroundColor(normalPost.createdAt)

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
                                showMenuBottomSheet = true
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
                    normalPost = normalPost,
                    onProfileClick = onProfileClick,
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick
                )
            }
        }
    }

    if(showMenuBottomSheet) {
        fun dismissBottomSheet() {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if(!sheetState.isVisible) {
                    showMenuBottomSheet = false
                }
            }
        }

        DefaultBottomSheet(
            sheetState = sheetState,
            hasDeleteButton = false,
            onDismiss = { dismissBottomSheet() }
        ) {
            NormalPostBottomSheetContent(
                accountType = accountType,
                onClickEdit = {
                    onClickEdit()
                    dismissBottomSheet()
                },
                onClickDelete = {
                    deleteNormalPost = normalPost
                    dismissBottomSheet()
                },
                onClickReport = {
                    onClickReport()
                    dismissBottomSheet()
                }
            )
        }
    }

    deleteNormalPost?.let {
        WarningDialog(
            title = "소식을 삭제할까요?",
            warningContent = it.title,
            content = "위의 소식이 삭제되며, 삭제 이후 복구되지않아요.",
            actionText = "삭제",
            onDismiss = {
               deleteNormalPost = null
            },
            onAction = {
                deleteNormalPost = null
                onClickDelete()
            }
        )
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
    normalPost: NormalPostData,
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
                painter = painterResource(id = if(normalPost.userLiked) R.drawable.ic_like_on else R.drawable.ic_like_off),
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

/**
 * 일반 게시글 메뉴 아이콘 클릭 시 보여지는 BottomSheet
 * @param accountType AccountType
 * @param onClickEdit onClick of Edit
 * @param onClickDelete onClick of Delete
 * @param onClickReport onClick of Report
 */
@Composable
fun NormalPostBottomSheetContent(
    accountType: AccountType,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    onClickReport: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(accountType) {
            AccountType.OWNER -> {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onClickEdit,
                ) {
                    Text(
                        text = "수정하기",
                        style = storeMeTextStyle(FontWeight.Bold, 2, color = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onClickDelete
                ) {
                    Text(
                        text = "삭제하기",
                        style = storeMeTextStyle(FontWeight.Bold, 2, color = ErrorColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            AccountType.CUSTOMER -> {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onClickReport
                ) {
                    Text(
                        text = "신고하기",
                        style = storeMeTextStyle(FontWeight.Bold, 2, color = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}