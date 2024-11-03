package com.store_me.storeme.ui.store_setting.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileSettingViewModel: ViewModel() {
    private val _backgroundImage = MutableStateFlow<String?>(null)
    val backgroundImage: StateFlow<String?> = _backgroundImage

    fun updateBackgroundImage(newImageUrl: String) {
        _backgroundImage.value = newImageUrl
    }

    private val _profileImage = MutableStateFlow<String?>(null)
    val profileImage: StateFlow<String?> = _profileImage

    fun updateProfileImage(newImageUrl: String) {
        _profileImage.value = newImageUrl
    }

    private val _storeName = MutableStateFlow<String>("")
    val storeName: StateFlow<String> = _storeName

    fun updateStoreName(storeName: String) {
        _storeName.value = storeName
    }
}