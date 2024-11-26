package com.store_me.storeme.ui.create_account

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.repository.storeme.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _profileImage = MutableStateFlow<Uri?>(null)
    val profileImage: StateFlow<Uri?> = _profileImage


    fun setImageUri(uri: Uri) {
        _profileImage.value =  uri
    }

    fun signup() {
        viewModelScope.launch {
            val result = userRepository.customerSignupApp(customerSignupApp = CustomerSignupApp(
                accountId = "joonbo97",
                password = "tlawnsqh123",
                phoneNumber = "01071649416",
                nickname= "딕네임",
                privacyConsent = true,
                marketingConsent = true,
                verificationCode = "iesDfY"),
                profileImage = profileImage.value
            )
        }
    }
}