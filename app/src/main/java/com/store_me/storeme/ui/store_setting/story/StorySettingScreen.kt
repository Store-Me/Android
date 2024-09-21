@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.story

import android.net.Uri
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.store_me.storeme.ui.component.AddButton
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

    val videoUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        videoUri.value = uri
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
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    item {
                        if(videoUri.value != null)
                            VideoPlayer(uri = videoUri.value!!)

                    }

                }
            }
        )
    }
}

@Composable
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current

    // ExoPlayer 인스턴스 생성
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }

    // ExoPlayer를 포함하는 AndroidView 생성
    AndroidView(factory = {
        PlayerView(context).apply {
            player = exoPlayer
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                600
            )
        }
    })

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
                navController = navController,
                title = "스토어 스토리",
                isInTopAppBar = true
            ) },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White,
                scrolledContainerColor = White
            )
        )

        AddButton(
            text = "스토리 추가",
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
