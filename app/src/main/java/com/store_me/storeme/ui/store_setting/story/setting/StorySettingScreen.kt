@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)

package com.store_me.storeme.ui.store_setting.story.setting

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.StoryData
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.component.SaveAndAddButton
import com.store_me.storeme.ui.component.SkeletonBox
import com.store_me.storeme.ui.component.StoryPlayer
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.theme.ExpiredColor
import com.store_me.storeme.ui.theme.LightBlack
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.LikeCountUtils
import com.store_me.storeme.data.enums.StoreCategory
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.store_me.storeme.utils.toTimeAgo
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun StorySettingScreen(
    navController: NavController,
    storyViewModel: StoryViewModel
) {
    val storeDataViewModel = LocalStoreDataViewModel.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyGridState()

    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val stories by storyViewModel.stories.collectAsState()

    var deleteStoryItem by remember { mutableStateOf<StoryData?>(null) }
    var showStory by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    fun onClose() {
        navController.popBackStack()
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex to totalItemCount
        }.distinctUntilChanged()
            .collect { (lastVisible, total) ->
                if (lastVisible != null && lastVisible >= total - 1) {
                    storyViewModel.getStoreStories()
                }
            }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = { TitleWithDeleteButtonAndRow(
            title = "스토리 관리",
            scrollBehavior = scrollBehavior,
            onClose = { onClose() }
        ) {
            SaveAndAddButton(
                addButtonText = "스토리 추가",
                hasDifference = false,
                onAddClick = {
                    navController.navigate(OwnerRoute.StoryManagement.fullRoute)
                },
                onSaveClick = {
                    //NOTHING
                }
            )
        } },
        content = { innerPadding ->
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(horizontal = 20.dp),
                state = listState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    itemsIndexed(stories) { index, item ->
                        StoryThumbnailItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    indication = ripple(bounded = true),
                                    interactionSource = remember { MutableInteractionSource() },
                                    onLongClick = {
                                        deleteStoryItem = item
                                    },
                                    onClick = {
                                        selectedIndex = index
                                        showStory = true
                                    }
                                )
                            ,
                            thumbnailUrl = stories[index].thumbNail
                        )
                    }
                }
            )
        }
    )

    if(deleteStoryItem != null) {
        WarningDialog(
            title = "스토리를 삭제할까요?",
            content = "선택된 스토리가 삭제되며, 삭제 이후 복구되지 않아요.",
            actionText = "삭제",
            onDismiss = { deleteStoryItem = null },
            onAction = {
                storyViewModel.deleteStoreStories(
                    storyId = deleteStoryItem?.id ?: ""
                )
                deleteStoryItem = null
            }
        )
    }

    if(showStory) {
        StoryDetailDialog(
            storeInfoData = storeInfoData!!,
            selectedIndex = selectedIndex,
            stories = stories,
            onDismiss = { showStory = false },
            onLike = {
                storyViewModel.updateStoryLike(it)
            }
        )
    }
}

@Composable
fun StoryDetailDialog(
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
    storeInfoData: StoreInfoData,
    selectedIndex: Int,
    stories: List<StoryData>,
    onDismiss: () -> Unit,
    onLike: (StoryData) -> Unit
) {
    val gradientColor by remember { mutableStateOf(listOf(
        Color.Black.copy(alpha = 0.6f),
        Color.Transparent
    )) }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = properties
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            contentAlignment = Alignment.TopCenter
        ) {
            //중앙에 위치한 Player
            StoryPlayer(
                modifier = Modifier
                    .align(Alignment.Center),
                videoUrl = stories[selectedIndex].video
            )

            //상단 Gradient
            Canvas(modifier = Modifier.height(200.dp)) {
                drawRect(brush = Brush.verticalGradient(colors = gradientColor))
            }

            //상단 닫기 버튼
            TitleWithDeleteButton(
                title = "",
                tint = Color.White
            ) {
                onDismiss()
            }

            //하단 설명
            StoryDescription(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                storeInfoData = storeInfoData,
                storyData = stories[selectedIndex]
            ) {
                onLike(stories[selectedIndex])
            }
        }
    }
}

@Composable
fun StoryDescription(
    modifier: Modifier,
    storeInfoData: StoreInfoData,
    storyData: StoryData,
    onLike: () -> Unit
) {
    var showAllDescription by remember { mutableStateOf(false) }

    LaunchedEffect(storyData) {
        showAllDescription = false
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
            .background(color = LightBlack.copy(alpha = 0.7f))
            .clickable(
                onClick = { showAllDescription = !showAllDescription },
                indication = null,
                interactionSource = null
            )
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileImage(
                accountType = AccountType.OWNER,
                url = storeInfoData.storeProfileImage,
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape = CircleShape)
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = storeInfoData.storeName,
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = Color.White
                )

                FlowRow(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = storeInfoData.storeLocation,
                        style = storeMeTextStyle(FontWeight.Normal, -1),
                        color = ExpiredColor
                    )

                    Text(
                        text = "·",
                        style = storeMeTextStyle(FontWeight.Normal, -1),
                        color = ExpiredColor
                    )

                    Text(
                        text = StoreCategory.valueOf(storeInfoData.storeCategory).displayName,
                        style = storeMeTextStyle(FontWeight.Normal, -1),
                        color = ExpiredColor
                    )

                    Text(
                        text = "·",
                        style = storeMeTextStyle(FontWeight.Normal, -1),
                        color = ExpiredColor
                    )

                    Text(
                        text = storyData.createdAt.toTimeAgo(),
                        style = storeMeTextStyle(FontWeight.Normal, -1),
                        color = ExpiredColor
                    )

                    Text(
                        text = "·",
                        style = storeMeTextStyle(FontWeight.Normal, -1),
                        color = ExpiredColor
                    )

                    Text(
                        text = "좋아요 " + LikeCountUtils.convertLikeCount(storyData.likesCount) + "개",
                        style = storeMeTextStyle(FontWeight.Normal, -1),
                        color = ExpiredColor
                    )
                }
            }

            IconButton(
                onClick = { onLike() }
            ) {
                Icon(
                    painter = painterResource(if(storyData.userLiked) R.drawable.ic_like_on else R.drawable.ic_like_off),
                    modifier = Modifier
                        .size(24.dp),
                    contentDescription = null,
                    tint = if(storyData.userLiked) Color.Unspecified else Color.White
                )
            }
        }

        if(storyData.description.isNotEmpty()) {
            Text(
                text = storyData.description,
                style = storeMeTextStyle(fontWeight = FontWeight.Normal, 0),
                color = Color.White,
                maxLines = if(showAllDescription) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )
        }
    }
}

@Composable
fun StoryThumbnailItem(modifier: Modifier, thumbnailUrl: String) {
    SubcomposeAsyncImage(
        model = thumbnailUrl,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)),
        contentScale = ContentScale.Fit,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                SkeletonBox(
                    modifier = Modifier.fillMaxSize(),
                    isLoading = true
                ) {}
            }
        },
        success = { state ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Image(
                    painter = state.painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {

            }
        }
    )
}
