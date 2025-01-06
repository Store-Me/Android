package com.store_me.storeme.ui.signup.account_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.repository.storeme.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDataViewModel @Inject constructor(
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

    fun checkDuplicate() {
        viewModelScope.launch {
            val result = userRepository.checkAccountIdDuplicate(accountId = _accountId.value)

            result.onSuccess {
                _accountIdDuplicate.value = it
            }.onFailure {

            }
        }
    }

    fun clearAccountIdDuplicate() {
        _accountIdDuplicate.value = null
    }
}