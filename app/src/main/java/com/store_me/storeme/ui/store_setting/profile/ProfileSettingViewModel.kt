package com.store_me.storeme.ui.store_setting.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.auth.Auth
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
class ProfileSettingViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _backgroundImageUrl = MutableStateFlow<String?>(null)
    val backgroundImageUrl: StateFlow<String?> = _backgroundImageUrl

    private val _backgroundImageUri = MutableStateFlow<Uri?>(null)
    val backgroundImageUri: StateFlow<Uri?> = _backgroundImageUri

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    //이미지 업로드 상태
    private val _profileImageProgress = MutableStateFlow<Float>(0.0f)
    val profileImageProgress: StateFlow<Float> = _profileImageProgress

    private val _backgroundImageProgress = MutableStateFlow<Float>(0.0f)
    val backgroundImageProgress: StateFlow<Float> = _backgroundImageProgress

    fun updateBackgroundImageUri(backgroundImageUri: Uri?) {
        _backgroundImageUri.value = backgroundImageUri

        if(backgroundImageUri == null) {
            _backgroundImageUrl.value = null
        }
    }

    fun updateProfileImageUri(profileImageUri: Uri?) {
        _profileImageUri.value = profileImageUri

        if(profileImageUri == null) {
            _profileImageUrl.value = null
        }
    }

    fun updateProfileImageUrl(profileImageUrl: String?) {
        _profileImageUrl.value = profileImageUrl
    }

    fun updateBackgroundImageUrl(backgroundImageUrl: String?) {
        _backgroundImageUrl.value = backgroundImageUrl
    }

    fun clearProfileProgress() {
        _profileImageProgress.value = 0.0f
        _profileImageUri.value = null
    }

    fun clearBackgroundProgress() {
        _backgroundImageProgress.value = 0.0f
        _backgroundImageUri.value = null
    }

    fun uploadStoreProfileImage(storeName: String) {
        if(profileImageUri.value == null)
            return

        viewModelScope.launch {
            val response = imageRepository.uploadImage(
                folderName = StoragePaths.STORE_PROFILE_IMAGE,
                uri = profileImageUri.value!!,
                uniqueName = storeName
            ) {
                _profileImageProgress.value = it
            }

            response.onSuccess {
                _profileImageUrl.value = it
            }.onFailure {
                updateProfileImageUri(null)

                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun uploadStoreBackgroundImage(storeName: String) {
        if(backgroundImageUri.value == null)
            return

        viewModelScope.launch {
            val response = imageRepository.uploadImage(
                folderName = StoragePaths.STORE_BACKGROUND_IMAGE,
                uri = backgroundImageUri.value!!,
                uniqueName = storeName
            ) {
                _backgroundImageProgress.value = it
            }

            response.onSuccess {
                _backgroundImageUrl.value = it
            }.onFailure {
                updateBackgroundImageUri(null)

                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }
}