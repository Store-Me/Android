package com.store_me.storeme.ui.signup.customer

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.R
import com.store_me.storeme.repository.storeme.ImageRepository
import com.store_me.storeme.utils.StoragePaths
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerDataViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun updateNickName(newNickName: String) {
        _nickName.value = newNickName
    }

    fun updateProfileImage(newProfileImage: Uri?) {
        _profileImage.value = newProfileImage

        if(newProfileImage == null)
            _profileImageUrl.value = null
    }

    fun updateErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
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
                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }
    }
}