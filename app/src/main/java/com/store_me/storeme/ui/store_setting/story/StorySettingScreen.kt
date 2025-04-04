@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.story

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.ui.component.SaveAndAddButton
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute

@Composable
fun StorySettingScreen(
    navController: NavController,
    storySettingViewModel: StorySettingViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val stories by storySettingViewModel.stories.collectAsState()

    var hasDifference by remember { mutableStateOf(false) }


    fun onClose() {
        navController.popBackStack()
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
                hasDifference = hasDifference,
                onAddClick = {
                    navController.navigate(OwnerRoute.StoryManagement.fullRoute)
                },
                onSaveClick = {
                    //TODO SAVE
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

                }
            )
        }
    )

}