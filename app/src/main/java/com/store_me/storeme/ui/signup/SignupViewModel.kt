package com.store_me.storeme.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.data.model.signup.CustomerSignupRequest
import com.store_me.storeme.data.model.signup.OwnerSignupRequest
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    //계정 타입
    private val _accountType = MutableStateFlow<AccountType?>(null)
    val accountType: StateFlow<AccountType?> = _accountType
    //로그인 타입
    private val _loginType = MutableStateFlow<LoginType?>(null)
    val loginType: StateFlow<LoginType?> = _loginType

    private val _isSignupFinish = MutableStateFlow(false)
    val isSignupFinish: StateFlow<Boolean> = _isSignupFinish

    fun setLoginType(loginType: LoginType) {
        _loginType.value = loginType
    }

    fun setAccountType(accountType: AccountType) {
        _accountType.value = accountType
    }

    fun customerSignup(customerSignupRequest: CustomerSignupRequest) {
        viewModelScope.launch {
            val response = userRepository.customerSignup(
                customerSignupRequest = customerSignupRequest
            )

            signupResponse(response)
        }
    }

    fun ownerSignup(ownerSignupRequest: OwnerSignupRequest) {
        viewModelScope.launch {
            val response = userRepository.ownerSignup(
                ownerSignupRequest = ownerSignupRequest
            )

            signupResponse(response)
        }
    }

    private suspend fun signupResponse(response: Result<Unit>) {
        response.onSuccess {
            _isSignupFinish.value = true
        }.onFailure {
            if(it is ApiException) {
                ErrorEventBus.errorFlow.emit(it.message)
            } else {
                ErrorEventBus.errorFlow.emit(null)
            }
        }
    }
}