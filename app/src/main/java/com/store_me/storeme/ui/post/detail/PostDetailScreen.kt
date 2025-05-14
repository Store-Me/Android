@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.post.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.store_me.storeme.R
import com.store_me.storeme.data.PostContentType
import com.store_me.storeme.data.store.post.NormalPostData
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.NormalPostBottomSheetContent
import com.store_me.storeme.ui.component.SkeletonAsyncImage
import com.store_me.storeme.ui.component.StoreInfoAndButtonsRow
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.store_setting.post.PostViewModel
import com.store_me.storeme.ui.theme.FinishedColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.PostBackgroundUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.store_me.storeme.utils.toTimeAgo
import kotlinx.coroutines.launch

@Composable
fun PostDetailScreen(
    navController: NavController,
    postViewModel: PostViewModel,
) {
    val auth = LocalAuth.current
    val storeDataViewModel = LocalStoreDataViewModel.current
    val loadingViewModel = LocalLoadingViewModel.current
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()
    val selectedNormalPost by postViewModel.selectedNormalPost.collectAsState()

    val accountType by auth.accountType.collectAsState()

    val scope = rememberCoroutineScope()
    val sheetState =  rememberModalBottomSheetState()
    var showMenuBottomSheet by remember { mutableStateOf(false) }

    var deleteNormalPost by remember { mutableStateOf<NormalPostData?>(null) }

    val images = remember(selectedNormalPost) {
        selectedNormalPost?.content?.filter { it.type == PostContentType.IMAGE.name } ?: emptyList()
    }

    LaunchedEffect(Unit) {
        selectedNormalPost?.let { postViewModel.postNormalPostViews(it) }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {

        },
        bottomBar = {

        }
    ) { innerPadding ->
        selectedNormalPost?.let { post ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    //최상단 이미지 Item
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(2f / 1f)
                        ) {
                            if(images.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(color = PostBackgroundUtils.getPostBackgroundColor(post.createdAt))
                                )
                            } else {
                                SkeletonAsyncImage(
                                    imageUrl = images.first().content,
                                    modifier = Modifier
                                        .matchParentSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(color = Color.Black.copy(alpha = 0.3f))
                            )
                        }
                    }

                    item {
                        storeInfoData?.let { storeInfo ->
                            StoreInfoAndButtonsRow(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .offset(y = (-24).dp),
                                storeInfoData = storeInfo,
                                normalPost = post,
                                onProfileClick = {

                                },
                                onLikeClick = {
                                    postViewModel.likeNormalPost(normalPost = post)
                                },
                                onCommentClick = {
                                    //TODO COMMENT
                                }
                            )
                        }
                    }

                    item {
                        NormalPostContent(
                            normalPost = post,
                            onShareClick = {

                            },
                            onMenuClick = {
                                showMenuBottomSheet = true
                            }
                        )
                    }

                    item {
                        DefaultHorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                        )
                    }
                }
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
                    //TODO NAVIGATE TO EDIT
                    dismissBottomSheet()
                },
                onClickDelete = {
                    deleteNormalPost = selectedNormalPost
                    dismissBottomSheet()
                },
                onClickReport = {
                    //TODO REPORT
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
                loadingViewModel.showLoading()
                postViewModel.deletePost(it.id)
                deleteNormalPost = null
            }
        )
    }
}

@Composable
fun NormalPostContent(
    normalPost: NormalPostData,
    onShareClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //Title Row
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = normalPost.title,
                style = storeMeTextStyle(FontWeight.ExtraBold, 8),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = onMenuClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = "메뉴",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        //Like Count, Comment Count, Share Row
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "좋아요한 사람 ${normalPost.likesCount}명 · 댓글 ${normalPost.commentsCount}개",
                style = storeMeTextStyle(FontWeight.Bold, -1, FinishedColor),
                modifier = Modifier
                    .weight(1f)
            )

            DefaultButton(
                leftIconResource = R.drawable.ic_share,
                leftIconTint = Color.Black,
                buttonText = "공유 하기",
                diffValue = 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SubHighlightColor,
                    contentColor = Color.Black
                ),
                onClick = onShareClick,
                modifier = Modifier.width(IntrinsicSize.Max)
            )
        }

        //본문 내용
        normalPost.content.forEach { content ->
            when(content.type) {
                PostContentType.IMAGE.name -> {
                    SkeletonAsyncImage(
                        imageUrl = content.content,
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                    ) {

                    }
                }
                PostContentType.TEXT.name -> {
                    val state = rememberRichTextState()

                    LaunchedEffect(Unit) {
                        state.setHtml(content.content)
                    }

                    RichText(
                        state = state,
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = storeMeTextStyle(FontWeight.Normal, 2),
                    )
                }
                PostContentType.EMOJI.name -> {

                }
            }
        }

        //시간 정보 및 조회수
        Text(
            text = "${normalPost.createdAt.toTimeAgo()} · 조회 ${normalPost.views}",
            style = storeMeTextStyle(FontWeight.Bold, -1, FinishedColor),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}