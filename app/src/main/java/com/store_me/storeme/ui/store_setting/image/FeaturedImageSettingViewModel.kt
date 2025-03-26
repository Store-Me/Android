package com.store_me.storeme.ui.store_setting.image

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.repository.storeme.ImageRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.StoragePaths
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturedImageSettingViewModel @Inject constructor(
    private val imageRepository: ImageRepository
): ViewModel() {

    private val _featuredImages = MutableStateFlow<List<FeaturedImageData>>(emptyList())
    val featuredImages: StateFlow<List<FeaturedImageData>> = _featuredImages

    private val _croppedImageUri = MutableStateFlow<Uri?>(null)
    val croppedImageUri: StateFlow<Uri?> = _croppedImageUri

    private val _croppedImageUrl = MutableStateFlow<String?>(null)
    val croppedImageUrl: StateFlow<String?> = _croppedImageUrl

    private val _uploadProgress = MutableStateFlow<Float>(0.0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress

    fun updateFeaturedImages(featuredImages: List<FeaturedImageData>) {
        _featuredImages.value = featuredImages
    }

    fun reorderFeaturedImages(fromIndex: Int, toIndex: Int) {
        val currentFeaturedImages = _featuredImages.value.toMutableList()
        val movedItem = currentFeaturedImages.removeAt(fromIndex)
        currentFeaturedImages.add(toIndex, movedItem)
        _featuredImages.value = currentFeaturedImages.toList()
    }

    fun addFeaturedImage(featuredImage: FeaturedImageData) {
        val currentFeaturedImages = _featuredImages.value.toMutableList()
        currentFeaturedImages.add(featuredImage)
        _featuredImages.value = currentFeaturedImages
    }

    fun editFeaturedImage(index: Int, featuredImage: FeaturedImageData) {
        _featuredImages.value = _featuredImages.value.toMutableList().apply {
            this[index] = featuredImage
        }
    }

    fun deleteFeaturedImage(index: Int) {
        val currentFeaturedImages = _featuredImages.value.toMutableList()
        currentFeaturedImages.removeAt(index)
        _featuredImages.value = currentFeaturedImages
    }

    fun updateCroppedImageUri(uri: Uri?) {
        _croppedImageUri.value = uri
    }

    fun updateCroppedImageUrl(url: String?) {
        _croppedImageUrl.value = url
    }

    fun uploadStoreFeaturedImage(storeName: String) {
        if(croppedImageUri.value == null)
            return

        viewModelScope.launch {
            val response = imageRepository.uploadImage(
                folderName = StoragePaths.STORE_IMAGES,
                uri = croppedImageUri.value!!,
                uniqueName = storeName
            ) {
                _uploadProgress.value = it
            }

            response.onSuccess {
                updateCroppedImageUrl(it)

                _uploadProgress.value = 0.0f
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