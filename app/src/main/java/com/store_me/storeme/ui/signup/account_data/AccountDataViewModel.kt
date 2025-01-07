package com.store_me.storeme.ui.signup.account_data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.R
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDataViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
): ViewModel() {
    private val _accountId = MutableStateFlow("")
    val accountId: StateFlow<String> = _accountId

    private val _accountPw = MutableStateFlow("")
    val accountPw: StateFlow<String> = _accountPw

    private val _accountPwConfirm = MutableStateFlow("")
    val accountPwConfirm: StateFlow<String> = _accountPwConfirm

    private val _accountIdDuplicate = MutableStateFlow<Boolean?>(null)
    val accountIdDuplicate: StateFlow<Boolean?> = _accountIdDuplicate

    private val _isPasswordMatching = MutableStateFlow(false)
    val isPasswordMatching: StateFlow<Boolean> = _isPasswordMatching

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            combine(
                _accountPw,
                _accountPwConfirm
            ) { accountPw, accountPwConfirm ->
                accountPw == accountPwConfirm && accountPw.isNotBlank() && accountPwConfirm.isNotBlank()
            }.collect { isMatching ->
                _isPasswordMatching.value = isMatching
            }
        }
    }

    fun updateAccountId(newAccountId: String) {
        _accountId.value = newAccountId
    }

    fun updateAccountPw(newAccountPw: String) {
        _accountPw.value = newAccountPw
    }

    fun updateAccountPwConfirm(newAccountPwConfirm: String) {
        _accountPwConfirm.value = newAccountPwConfirm
    }

    fun updateErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }

    fun checkDuplicate() {
        viewModelScope.launch {
            val result = userRepository.checkAccountIdDuplicate(accountId = _accountId.value)

            result.onSuccess {
                _accountIdDuplicate.value = it
            }.onFailure {
                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }
    }

    fun clearAccountIdDuplicate() {
        _accountIdDuplicate.value = null
    }
}