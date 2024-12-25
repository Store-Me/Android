package com.store_me.storeme.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.model.verification.ConfirmCode
import com.store_me.storeme.repository.storeme.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneNumberViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _smsSentSuccess = MutableStateFlow(false)
    val smsSentSuccess: StateFlow<Boolean> = _smsSentSuccess

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode

    private val _verificationSuccess = MutableStateFlow<Boolean?>(null)
    val verificationSuccess: StateFlow<Boolean?> = _verificationSuccess

    fun updatePhoneNumber(newPhoneNumber: String) {
        _phoneNumber.value = newPhoneNumber
    }

    fun updateVerificationCode(newVerificationCode: String) {
        _verificationCode.value = newVerificationCode
    }

    fun sendSmsMessage(phoneNumber: String) {
        viewModelScope.launch {
            val result = userRepository.sendSmsMessage(phoneNumber = phoneNumber)

            result.onSuccess {
                _smsSentSuccess.value = true
            }.onFailure {
                _smsSentSuccess.value = false
            }
        }
    }

    fun confirmCode() {
        viewModelScope.launch {
            val result = userRepository.confirmVerificationCode(confirmCode = ConfirmCode(phoneNumber = _phoneNumber.value, verificationCode = _verificationCode.value))

            result.onSuccess {
                _verificationSuccess.value = true
            }.onFailure {
                _verificationSuccess.value = false
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