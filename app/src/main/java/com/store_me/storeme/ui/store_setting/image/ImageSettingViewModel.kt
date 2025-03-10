package com.store_me.storeme.ui.store_setting.image

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.auth.Auth
import com.store_me.storeme.R
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.utils.FileUtils
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: Auth,
    private val ownerRepository: OwnerRepository
): ViewModel() {

    //선택된 이미지 목록
    private val _selectedImages = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImages: StateFlow<List<Uri>> = _selectedImages

    //오류 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun updateSelectedImages(uris: List<Uri>) {
        _selectedImages.value = uris
    }

    fun addStoreImage() {
        val storeImages =
            FileUtils.createMultipart(context, selectedImages.value, "storeImageFileList")

        val storeId = auth.storeId.value

        if(storeImages == null) {
            updateErrorMessage(context.getString(R.string.fail_convert_multipart))

            return
        }

        if(storeId == null) {
            updateErrorMessage(context.getString(R.string.wrong_store_id))

            return
        }

        viewModelScope.launch {
            val response = ownerRepository.addStoreImages(storeId = storeId, storeImageList = storeImages)

            response.onSuccess {
                updateSelectedImages(emptyList())
            }.onFailure {
                if(it is ApiException) {
                    updateErrorMessage(it.message)
                } else {
                    updateErrorMessage(context.getString(R.string.unknown_error_message))
                }
            }
        }

    }

    fun updateErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }
}