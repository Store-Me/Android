package com.store_me.storeme.ui.signup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.signup.CustomerSignupKakao
import com.store_me.storeme.data.model.signup.OwnerSignupApp
import com.store_me.storeme.data.model.signup.OwnerSignupKakao
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.utils.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
): ViewModel() {
    //계정 타입
    private val _accountType = MutableStateFlow<AccountType?>(null)
    val accountType: StateFlow<AccountType?> = _accountType
    //로그인 타입
    private val _loginType = MutableStateFlow<LoginType?>(null)
    val loginType: StateFlow<LoginType?> = _loginType


    fun setLoginType(loginType: LoginType) {
        _loginType.value = loginType
    }

    fun setAccountType(accountType: AccountType) {
        _accountType.value = accountType
    }



    fun customerSignupApp(customerSignupApp: CustomerSignupApp, profileImage: Uri?) {
        val profileImageFile =
            if(profileImage != null)
                FileUtils.createMultipart(context, profileImage, "profileImageFile")
            else
                null

        if(profileImage != null && profileImageFile == null) {
            onErrorImageFile()

            return
        }

        viewModelScope.launch {
            userRepository.customerSignupApp(
                customerSignupApp = customerSignupApp,
                profileImage = profileImageFile
            )
        }
    }

    fun customerSignupKakao(customerSignupKakao: CustomerSignupKakao, profileImage: Uri?) {
        val profileImageFile =
            if(profileImage != null)
                FileUtils.createMultipart(context, profileImage, "profileImageFile")
            else
                null

        if(profileImage != null && profileImageFile == null) {
            onErrorImageFile()

            return
        }

        viewModelScope.launch {
            userRepository.customerSignupKakao(
                customerSignupKakao = customerSignupKakao,
                profileImage = profileImageFile
            )
        }
    }

    fun ownerSignupApp(ownerSignupApp: OwnerSignupApp, storeProfileImage: Uri?, storeImages: List<Uri>) {
        val storeProfileImageFile =
            if(storeProfileImage != null)
                FileUtils.createMultipart(context, storeProfileImage, "storeProfileImageFile")
            else
                null

        val storeImageFiles =
            if(storeImages.isNotEmpty())
                FileUtils.createMultipart(context, storeImages, "storeImageFileList")
            else
                null

        if((storeProfileImage != null && storeProfileImageFile == null) || (storeImages.isNotEmpty() && storeImageFiles == null)) {
            onErrorImageFile()

            return
        }

        viewModelScope.launch {
            userRepository.ownerSignupApp(
                ownerSignupApp = ownerSignupApp,
                storeProfileImage = storeProfileImageFile,
                storeImageList = storeImageFiles
            )
        }
    }

    fun ownerSignupKakao(ownerSignupKakao: OwnerSignupKakao, storeProfileImage: Uri?, storeImages: List<Uri>) {
        val storeProfileImageFile =
            if(storeProfileImage != null)
                FileUtils.createMultipart(context, storeProfileImage, "storeProfileImageFile")
            else
                null

        val storeImageFiles =
            if(storeImages.isNotEmpty())
            FileUtils.createMultipart(context, storeImages, "storeImageFileList")
        else
            null

        if((storeProfileImage != null && storeProfileImageFile == null) || (storeImages.isNotEmpty() && storeImageFiles == null)) {
            onErrorImageFile()

            return
        }

        viewModelScope.launch {
            userRepository.ownerSignupKakao(
                ownerSignupKakao = ownerSignupKakao,
                storeProfileImage = storeProfileImageFile,
                storeImageList = storeImageFiles
            )
        }
    }

    private fun onErrorImageFile() {

    }
}