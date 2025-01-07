package com.store_me.storeme.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.R
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.ui.main.LOGIN_FAIL
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val ownerRepository: OwnerRepository
): ViewModel() {
    private val _isKakaoLoginFailed = MutableStateFlow(false)
    val isKakaoLoginFailed: StateFlow<Boolean> = _isKakaoLoginFailed

    private val _kakaoId = MutableStateFlow("")
    val kakaoId: StateFlow<String> = _kakaoId

    private val _accountId = MutableStateFlow("")
    val accountId: StateFlow<String> = _accountId

    private val _accountPw = MutableStateFlow("")
    val accountPw: StateFlow<String> = _accountPw

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun clearKakaoLoginFailedState() {
        _isKakaoLoginFailed.value = false
    }

    private fun updateKakaoId(newKakaoId: String) {
        _kakaoId.value = newKakaoId
    }

    fun updateAccountId(newAccountId: String) {
        _accountId.value = newAccountId
    }

    fun updateAccountPw(newAccountPw: String) {
        _accountPw.value = newAccountPw
    }

    fun updateErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }

    fun loginWithKakao(kakaoId: String) {
        updateKakaoId(kakaoId)

        viewModelScope.launch {

            val response = userRepository.loginWithKakao(kakaoId)

            response.onSuccess {
                //로그인 성공 시
                loginSuccess()
            }.onFailure {
                //로그인 실패 시
                if(it is ApiException){
                    _errorMessage.value = it.message

                    when(it.code) {
                        LOGIN_FAIL -> {
                            _isKakaoLoginFailed.value = true
                        }
                    }
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }
    }

    fun loginWithApp() {
        viewModelScope.launch {
            val response = userRepository.loginWithApp(accountId = accountId.value, accountPw = accountPw.value)

            response.onSuccess {
                //로그인 성공 시
                loginSuccess()
            }.onFailure {
                //로그인 실패 시
                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }
    }

    private suspend fun loginSuccess() {
        val response = ownerRepository.getStoreList()

        response.onSuccess {

        }.onFailure {
            if(it is ApiException) {
                _errorMessage.value = it.message
            } else {
                _errorMessage.value = context.getString(R.string.unknown_error_message)
            }
        }
    }
}