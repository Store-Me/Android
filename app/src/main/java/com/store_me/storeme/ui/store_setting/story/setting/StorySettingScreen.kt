@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.store_setting.story.setting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.data.StoryData
import com.store_me.storeme.ui.component.SaveAndAddButton
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.utils.BACKGROUND_ROUNDING_VALUE
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun StorySettingScreen(
    navController: NavController,
    storySettingViewModel: StorySettingViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyGridState()

    val stories by storySettingViewModel.stories.collectAsState()

    var deleteStoryItem by remember { mutableStateOf<StoryData?>(null) }

    fun onClose() {
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        //초기 로드
        storySettingViewModel.getStoreStories()
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex to totalItemCount
        }.distinctUntilChanged()
            .collect { (lastVisible, total) ->
                if (lastVisible != null && lastVisible >= total - 1) {
                    storySettingViewModel.getStoreStories()
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
                        StoryItem(
                            modifier = Modifier
                                .combinedClickable(
                                    indication = ripple(bounded = true),
                                    interactionSource = remember { MutableInteractionSource() },
                                    onLongClick = {
                                        deleteStoryItem = item
                                    },
                                    onClick = {
                                        //TODO SHOW STORY
                                    }
                                )
                            ,
                            storyData = item
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
                storySettingViewModel.deleteStoreStories(
                    storyId = deleteStoryItem?.id ?: ""
                )
                deleteStoryItem = null
            }
        )
    }
}

@Composable
fun StoryItem(modifier: Modifier, storyData: StoryData) {
    AsyncImage(
        model = storyData.thumbNail,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(BACKGROUND_ROUNDING_VALUE))
    )
}