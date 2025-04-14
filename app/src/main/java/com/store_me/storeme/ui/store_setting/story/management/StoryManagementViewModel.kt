package com.store_me.storeme.ui.store_setting.story.management

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.StoryData
import com.store_me.storeme.data.request.store.PostStoryRequest
import com.store_me.storeme.data.response.PagingResponse
import com.store_me.storeme.repository.storeme.ImageRepository
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.StoragePaths
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryManagementViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val ownerRepository: OwnerRepository
) : ViewModel() {
    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri

    private val _videoUrl = MutableStateFlow<String?>(null)
    val videoUrl: StateFlow<String?> = _videoUrl

    private val _thumbnailUrl = MutableStateFlow<String?>(null)
    val thumbnailUrl: StateFlow<String?> = _thumbnailUrl

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _progress = MutableStateFlow(0.0f)
    val progress: StateFlow<Float> = _progress

    private val _postResult = MutableStateFlow<PagingResponse<List<StoryData>>?>(null)
    val postResult: StateFlow<PagingResponse<List<StoryData>>?> = _postResult

    fun updateVideoUri(uri: Uri?) {
        _videoUri.value = uri
    }

    fun updateVideoUrl(url: String?) {
        _videoUrl.value = url
    }

    fun updateThumbnailUrl(url: String?) {
        _thumbnailUrl.value = url
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun uploadStory(storeName: String, videoUri: Uri, imageUri: Uri, mimeType: String) {
        uploadVideo(storeName, videoUri, mimeType)
        uploadThumbnail(storeName, imageUri)
    }

    private fun uploadVideo(storeName: String, videoUri: Uri, mimeType: String){
        _progress.value = 0f

        viewModelScope.launch {
            val response = imageRepository.uploadVideo(
                folderName = StoragePaths.STORE_STORIES,
                uri = videoUri,
                uniqueName = storeName,
                mimeType = mimeType
            ) {
                _progress.value = it
            }

            response.onSuccess {
                updateVideoUrl(it)
            }.onFailure {
                updateVideoUrl(null)
                updateVideoUri(null)
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    private fun uploadThumbnail(storeName: String, imageUri: Uri) {
        viewModelScope.launch {
            val response = imageRepository.uploadImage(
                folderName = StoragePaths.STORE_STORIES,
                uri = imageUri,
                uniqueName = storeName
            ) {  }

            response.onSuccess {
                updateThumbnailUrl(it)
            }.onFailure {
                updateThumbnailUrl(null)
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun postStoreStory(storeId: String) {
        viewModelScope.launch {
            if(videoUrl.value == null || thumbnailUrl.value == null) {
                ErrorEventBus.errorFlow.emit("동영상 업로드가 완료되지 않았습니다.")
                return@launch
            }

            val response = ownerRepository.postStoreStory(
                storeId = storeId,
                postStoreStoryRequest = PostStoryRequest(
                    video = videoUrl.value!!,
                    thumbNail = thumbnailUrl.value!!,
                    description = description.value
                )
            )

            response.onSuccess {
                _postResult.value = it
                SuccessEventBus.successFlow.emit("스토리가 생성되었습니다.")
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }
}