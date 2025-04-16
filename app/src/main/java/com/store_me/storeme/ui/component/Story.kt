package com.store_me.storeme.ui.component

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.store_me.storeme.R
import com.store_me.storeme.ui.theme.GuideColor
import androidx.core.net.toUri
import androidx.media3.ui.AspectRatioFrameLayout
import com.store_me.storeme.ui.theme.HighlightColor
import kotlinx.coroutines.delay

@Composable
fun EditableStoryItem(
    videoUri: Uri?,
    videoUrl: String?,
    progress: Float,
    onAdd: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .aspectRatio(9f / 16f)
            .clip(shape = RoundedCornerShape(18.dp))
            .clickable {
                if(videoUri == null) onAdd() else onDelete()
            }
    ) {
        when {
            videoUri == null && videoUrl == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(width = 1.dp, color = GuideColor, shape = RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "스토리 추가",
                        modifier = Modifier
                            .size(32.dp),
                        tint = GuideColor
                    )
                }
            }
            videoUri != null && videoUrl == null-> {
                LoadingProgress(
                    progress = progress,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(shape = RoundedCornerShape(18.dp))
                )
            }
            videoUri != null && videoUrl != null -> {
                LoopingVideoPreview(
                    uri = null,
                    url = videoUrl,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }

    }
}

@OptIn(UnstableApi::class)
@Composable
fun LoopingVideoPreview(
    uri: Uri?,
    url: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ExoPlayer 초기화
    val exoPlayer = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    // LaunchedEffect에서 uri 또는 url을 처리
    LaunchedEffect(uri, url) {
        // URL이 null이 아니면 URL을 Uri로 변환하여 처리
        val mediaUri = uri ?: url?.toUri()
        if (mediaUri != null) {
            exoPlayer.setMediaItem(MediaItem.fromUri(mediaUri))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    // DisposableEffect로 리소스 해제
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Lifecycle 관리 (pause, resume)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // AndroidView로 PlayerView 설정
    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        update = { playerView ->
            playerView.player = exoPlayer
            exoPlayer.playWhenReady = true
        },
        modifier = modifier
    )
}

@Composable
fun StoryPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    var isPlaying by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(1L) }

    // ExoPlayer 상태 업데이트
    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                duration = exoPlayer.duration.coerceAtLeast(1L)
            }

            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                isPlaying = isPlayingNow
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentPosition = exoPlayer.currentPosition
            delay(100)
        }
    }

    Column (modifier = modifier.fillMaxSize()) {
        // 동영상 플레이어
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable {
                    isPlaying = !isPlaying
                    exoPlayer.playWhenReady = isPlaying
                }
        ) {
            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        player = exoPlayer
                        useController = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // 재생 바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val percent = offset.x / size.width
                        val newPosition = (duration * percent).toLong()
                        exoPlayer.seekTo(newPosition)
                        currentPosition = newPosition
                    }
                }
        ) {
            // 전체 바
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GuideColor, CircleShape)
            )
            // 진행된 부분
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(currentPosition / duration.toFloat())
                    .background(HighlightColor, CircleShape)
            )
        }
    }
}