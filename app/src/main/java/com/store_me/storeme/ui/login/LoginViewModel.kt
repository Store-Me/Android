package com.store_me.storeme.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.ui.main.LOGIN_FAIL
import com.store_me.storeme.utils.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val ownerRepository: OwnerRepository
): ViewModel() {
    private val _isKakaoLoginFailed = MutableStateFlow(false)
    val isKakaoLoginFailed: StateFlow<Boolean> = _isKakaoLoginFailed

    private val _kakaoId = MutableStateFlow("")
    val kakaoId: StateFlow<String> = _kakaoId

    fun clearKakaoLoginFailedState() {
        _isKakaoLoginFailed.value = false
    }

    private fun updateKakaoId(newKakaoId: String) {
        _kakaoId.value = newKakaoId
    }

    fun loginWithKakao(kakaoId: String) {
        updateKakaoId(kakaoId)

        viewModelScope.launch {

            when(kakaoId){
                null -> {

                }
                else -> {
                    val response = userRepository.loginWithKakao(kakaoId)

                    response.onSuccess {
                        ownerRepository.getStoreList()
                    }.onFailure {
                        if(it is ApiException){
                            when(it.code) {
                                LOGIN_FAIL -> {
                                    _isKakaoLoginFailed.value = true
                                }
                            }
                        } else {

                        }
                    }
                }
            }
        }
    }

    fun loginWithApp(accountId: String, accountPw: String) {
        viewModelScope.launch {
            val response = userRepository.loginWithApp(accountId = accountId, accountPw = accountPw)

            response.onSuccess {

            }.onFailure {

            }
        }
    }
}