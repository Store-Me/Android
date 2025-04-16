package com.store_me.storeme.ui.store_setting.story.management

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.EditableStoryItem
import com.store_me.storeme.ui.component.SimpleOutLinedTextField
import com.store_me.storeme.ui.component.SimpleTextField
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.signup.GuideTextBoxItem
import com.store_me.storeme.ui.store_setting.story.setting.StorySettingViewModel
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.MimeUtils
import com.store_me.storeme.utils.VideoUtils
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel

@Composable
fun StoryManagementScreen(
    navController: NavController,
    storySettingViewModel: StorySettingViewModel,
    storyManagementViewModel: StoryManagementViewModel = hiltViewModel()
) {
    val loadingViewModel = LocalLoadingViewModel.current
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if(uri == null)
            return@rememberLauncherForActivityResult

        storyManagementViewModel.updateVideoUri(uri)
    }

    val videoUri by storyManagementViewModel.videoUri.collectAsState()
    val videoUrl by storyManagementViewModel.videoUrl.collectAsState()

    val description by storyManagementViewModel.description.collectAsState()

    val progress by storyManagementViewModel.progress.collectAsState()

    val postResult by storyManagementViewModel.postResult.collectAsState()

    var showBackWarnDialog by remember { mutableStateOf(false) }

    fun onClose() {
        if(videoUri != null || description.isNotEmpty()) {
            showBackWarnDialog = true
        } else {
            navController.popBackStack()
        }
    }

    LaunchedEffect(postResult) {
        if(postResult != null) {
            storySettingViewModel.updateStories(postResult!!.result)
            storySettingViewModel.updateHasNextPage(postResult!!.pagination.hasNextPage)
            storySettingViewModel.updateLastCreatedAt(postResult!!.pagination.lastCreatedAt)
            
            navController.popBackStack()
        }
    }

    LaunchedEffect(videoUri) {
        if(videoUri == null) {
            storyManagementViewModel.updateVideoUrl(null)
            return@LaunchedEffect
        }

        val thumbnailBitmap = VideoUtils.getThumbnailFromVideoUri(context = context, videoUri = videoUri!!)
        if(thumbnailBitmap == null) {
            //Thumbnail 추출 실패 시 Uri 초기화
            storyManagementViewModel.updateVideoUri(null)
            return@LaunchedEffect
        }

        //Thumbnail Uri 변환
        val thumbnailUri = VideoUtils.thumbnailBitmapToUri(context = context, bitmap = thumbnailBitmap)
        if(thumbnailUri == null) {
            //Thumbnail Uri 변환 실패 시 Uri 초기화
            storyManagementViewModel.updateVideoUri(null)
            return@LaunchedEffect
        }

        //Mime Type
        val mimeType = MimeUtils.getMimeType(context, videoUri!!)
        if (mimeType == null) {
            // MIME 타입 없을 경우 Uri 초기화
            storyManagementViewModel.updateVideoUri(null)
            return@LaunchedEffect
        }

        //스토리 업로드
        storyManagementViewModel.uploadStory(
            videoUri = videoUri!!,
            imageUri = thumbnailUri,
            mimeType = mimeType
        )
    }

    BackHandler {
        onClose()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = { TitleWithDeleteButton (
            title = "스토리 추가",
            onClose = { onClose() }
        )  },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EditableStoryItem(
                    videoUri = videoUri,
                    videoUrl = videoUrl,
                    progress = progress,
                    onAdd = {
                        launcher.launch("video/*")
                    },
                    onDelete = {
                        storyManagementViewModel.updateVideoUri(null)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                GuideTextBoxItem(
                    title = "스토리 가이드",
                    content = "짧은 영상을 업로드 하여 손님들에게 공유할 수 있어요.\n\n영상의 길이는 5초 ~ 1분 사이의 영상을 권장하고 있어요."
                )

                Text(
                    text = "스토리 설명",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                )

                SimpleTextField(
                    value = description,
                    onValueChange = {
                        storyManagementViewModel.updateDescription(it)
                    },
                    placeholderText = "스토리에 대한 간략한 설명을 입력해주세요.",
                    singleLine = false,
                )

                DefaultHorizontalDivider()

                TextLengthRow(
                    text = description,
                    limitSize = 100
                )

                Spacer(modifier = Modifier.height(40.dp))

                DefaultButton(
                    buttonText = "저장",
                ) {
                    loadingViewModel.showLoading()

                    storyManagementViewModel.postStoreStory()
                }
            }
        }
    )

    if(showBackWarnDialog) {
        BackWarningDialog(
            onDismiss = { showBackWarnDialog = false },
            onAction = { navController.popBackStack() }
        )
    }
}