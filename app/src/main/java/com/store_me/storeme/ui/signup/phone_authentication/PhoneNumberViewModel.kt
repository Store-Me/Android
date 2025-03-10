package com.store_me.storeme.ui.signup.phone_authentication

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.R
import com.store_me.storeme.data.model.verification.ConfirmCode
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.ui.component.filterNonNumeric
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneNumberViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
): ViewModel() {
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _verificationId = MutableStateFlow("")
    val verificationId: StateFlow<String> = _verificationId

    private val _smsSentSuccess = MutableStateFlow(false)
    val smsSentSuccess: StateFlow<Boolean> = _smsSentSuccess

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode

    private val _verificationSuccess = MutableStateFlow<Boolean?>(false)
    val verificationSuccess: StateFlow<Boolean?> = _verificationSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun updatePhoneNumber(newPhoneNumber: String) {
        _phoneNumber.value = filterNonNumeric(newPhoneNumber)
    }

    fun updateVerificationCode(newVerificationCode: String) {
        _verificationCode.value = newVerificationCode
    }

    fun updateErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }

    fun sendSmsMessage(phoneNumber: String, activity: Activity) {
        val phoneNumberWithCountryCode = "+82" + phoneNumber.substring(1)

        viewModelScope.launch {
            val result = userRepository.sendSmsMessage(phoneNumber = phoneNumberWithCountryCode, activity = activity)

            result.onSuccess {
                if(it != null) {
                    _verificationId.value = it
                }

                _smsSentSuccess.value = true
            }.onFailure {
                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }
    }

    fun confirmCode() {
        viewModelScope.launch {
            val result = userRepository.confirmVerificationCode(verificationId = verificationId.value, verificationCode = verificationCode.value)

            result.onSuccess {
                _verificationSuccess.value = it

                if(!it) {
                    _errorMessage.value = "인증번호가 올바르지 않습니다."
                }
            }.onFailure {
                _verificationSuccess.value = false

                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }
    }

    fun clearSmsSentSuccess() {
        _smsSentSuccess.value = false
    }

    fun clearVerificationSuccess() {
        _verificationSuccess.value = null
    }
}