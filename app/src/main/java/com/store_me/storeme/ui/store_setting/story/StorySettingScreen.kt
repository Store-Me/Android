@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.story

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.LargeButton
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner

val LocalStorySettingViewModel = staticCompositionLocalOf<StorySettingViewModel> {
    error("No StorySettingViewModel")
}

@Composable
fun StorySettingScreen(
    navController: NavController,
    storySettingViewModel: StorySettingViewModel = viewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current

    val videoUris by storySettingViewModel.videoUris.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if(uri == null)
            return@rememberLauncherForActivityResult

        storySettingViewModel.addVideo(uri)
    }

    CompositionLocalProvider(LocalStorySettingViewModel provides storySettingViewModel) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = White,
            topBar = { StorySettingTopLayout(navController = navController, scrollBehavior = scrollBehavior) {
                launcher.launch("video/*")
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
                        items(videoUris) {
                            StoryItem(uri = it)
                        }
                    }
                )
            }
        )
    }
}

@Composable
fun StoryItem(uri: Uri) {
    AsyncImage(
        model = uri,
        contentDescription = "스토리",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
    )
}

@Composable
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current

    // ExoPlayer 인스턴스 생성
    val exoPlayer = ExoPlayer.Builder(context).build()

    val mediaSource = remember(uri) {
        MediaItem.fromUri(uri)
    }

    // ExoPlayer를 포함하는 AndroidView 생성
    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
    )

    //MediaSource를 ExoPlayer에 할당
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // 컴포지션이 종료될 때 ExoPlayer 해제
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@Composable
fun StorySettingTopLayout(navController: NavController, scrollBehavior: TopAppBarScrollBehavior, onAdd: () -> Unit) {
    Column {
        TopAppBar(title = {
            TitleWithDeleteButton(
                title = "스토어 스토리",
                isInTopAppBar = true
            ) {
                navController.popBackStack()
            } },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White,
                scrolledContainerColor = White
            )
        )

        LargeButton(
            text = "스토리 추가",
            iconResource = R.drawable.ic_circle_plus,
            containerColor = Color.Black,
            contentColor = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            onAdd()
        }
    }
}
