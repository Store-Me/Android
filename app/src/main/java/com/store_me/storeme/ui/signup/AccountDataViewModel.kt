package com.store_me.storeme.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.repository.storeme.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDataViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _accountId = MutableStateFlow("")
    val accountId: StateFlow<String> = _accountId

    private val _accountIdDuplicate = MutableStateFlow<Boolean?>(null)
    val accountIdDuplicate: StateFlow<Boolean?> = _accountIdDuplicate

    fun updateAccountId(newAccountId: String) {
        _accountId.value = newAccountId
    }

    fun clearAccountId() {
        _accountId.value = ""
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