@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.story.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.data.StoryData
import com.store_me.storeme.ui.component.SaveAndAddButton
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.utils.composition_locals.LocalAuth

@Composable
fun StorySettingScreen(
    navController: NavController,
    storySettingViewModel: StorySettingViewModel
) {
    val auth = LocalAuth.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val stories by storySettingViewModel.stories.collectAsState()

    fun onClose() {
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        storySettingViewModel.getStoreStories(auth.storeId.value!!, null)
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
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    itemsIndexed(stories) { index, item ->
                        StoryItem(storyData = item)
                    }
                }
            )
        }
    )
}

@Composable
fun StoryItem(storyData: StoryData) {

    //TODO LONG PRESS DELETE
    AsyncImage(
        model = storyData.thumbNail,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
    )
}