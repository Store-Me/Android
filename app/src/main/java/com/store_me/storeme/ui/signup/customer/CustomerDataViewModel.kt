package com.store_me.storeme.ui.signup.customer

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CustomerDataViewModel: ViewModel() {
    private val _nickName = MutableStateFlow("")
    val nickName: StateFlow<String> = _nickName

    private val _profileImage = MutableStateFlow<Uri?>(null)
    val profileImage: StateFlow<Uri?> = _profileImage

    fun updateNickName(newNickName: String) {
        _nickName.value = newNickName
    }

    fun updateProfileImage(newProfileImage: Uri?) {
        _profileImage.value = newProfileImage
    }
}