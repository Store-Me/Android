package com.store_me.storeme.ui.store_setting.story

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.utils.VideoUtils

@Composable
fun StoryManagementScreen(
    navController: NavController,
    storySettingViewModel: StorySettingViewModel
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if(uri == null)
            return@rememberLauncherForActivityResult

        storySettingViewModel.updateUri(uri)
    }

    val videoUri by storySettingViewModel.uri.collectAsState()

    LaunchedEffect(Unit) {
        launcher.launch("video/*")
    }

    videoUri?.let {
        VideoFrameSelectorScreen(
            videoUri = it,
            onThumbnailSelected = {

            }
        )
    }
}

@Composable
fun VideoFrameSelectorScreen(
    videoUri: Uri,
    onThumbnailSelected: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var totalLength by remember {
        mutableStateOf(VideoUtils.getVideoDurationMillis(context, videoUri))
    }
    var currentTimeMs by remember { mutableStateOf(1000L) }
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }

    // Auto-load the default 1s thumbnail on enter
    LaunchedEffect(videoUri, currentTimeMs) {
        val frame = VideoUtils.getThumbnailFromVideoUri(context, videoUri, currentTimeMs)
        thumbnail = frame
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "썸네일로 사용할 프레임을 선택하세요",
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (thumbnail != null) {
                Image(
                    bitmap = thumbnail!!.asImageBitmap(),
                    contentDescription = "썸네일 미리보기",
                    modifier = Modifier.fillMaxSize()
                )
            } else {

            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "선택 시간 (초):")
            Slider(
                value = currentTimeMs.toFloat(),
                onValueChange = {
                    currentTimeMs = it.toLong()
                },
                valueRange = 0f..totalLength.toFloat(), // 최대 30초
                steps = 1000,
                modifier = Modifier.weight(1f)
            )

            Text(text = "${currentTimeMs / 1000}s")
        }

        Button(
            onClick = {
                thumbnail?.let { onThumbnailSelected(it) }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = thumbnail != null
        ) {
            Text("이 프레임으로 선택")
        }
    }
}
