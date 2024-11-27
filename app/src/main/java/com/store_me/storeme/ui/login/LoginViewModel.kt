package com.store_me.storeme.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.utils.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun loginWithKakao(kakaoId: String) {
        viewModelScope.launch {

            when(kakaoId){
                null -> {

                }
                else -> {
                    val response = userRepository.loginWithKakao(kakaoId)

                    response.onSuccess {

                    }.onFailure {
                        if(it is ApiException){

                        } else {

                        }
                    }
                }
            }
        }
    }
}