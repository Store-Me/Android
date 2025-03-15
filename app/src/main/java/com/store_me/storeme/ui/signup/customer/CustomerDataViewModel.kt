package com.store_me.storeme.ui.signup.customer

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class CustomerDataViewModel @Inject constructor(
    private val imageRepository: ImageRepository
): ViewModel() {
    private val _nickName = MutableStateFlow("")
    val nickName: StateFlow<String> = _nickName

    private val _profileImage = MutableStateFlow<Uri?>(null)
    val profileImage: StateFlow<Uri?> = _profileImage

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl

    private val _progress = MutableStateFlow<Float>(0.0f)
    val progress: StateFlow<Float> = _progress

    fun updateNickName(newNickName: String) {
        _nickName.value = newNickName
    }

    fun updateProfileImage(newProfileImage: Uri?) {
        _profileImage.value = newProfileImage

        if(newProfileImage == null)
            _profileImageUrl.value = null
    }

    fun uploadImage(accountId: String) {
        if(profileImage.value == null)
            return

        viewModelScope.launch {

            val response = imageRepository.uploadImage(
                folderName = StoragePaths.PROFILE_IMAGE,
                uri = profileImage.value!!,
                accountId = accountId
            ) {
                _progress.value = it
            }

            response.onSuccess {
                _profileImageUrl.value = it
            }.onFailure {
                updateProfileImage(null)

                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }
}