package com.store_me.storeme.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.auth.Auth
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.data.request.login.LoginRequest
import com.store_me.storeme.data.response.StoreListResponse
import com.store_me.storeme.repository.storeme.CustomerRepository
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
    private val auth: Auth,
    private val userRepository: UserRepository,
    private val ownerRepository: OwnerRepository,
    private val customerRepository: CustomerRepository
): ViewModel() {
    private val _isKakaoLoginFailed = MutableStateFlow(false)
    val isKakaoLoginFailed: StateFlow<Boolean> = _isKakaoLoginFailed

    private val _kakaoId = MutableStateFlow("")
    val kakaoId: StateFlow<String> = _kakaoId

    private val _accountId = MutableStateFlow("")
    val accountId: StateFlow<String> = _accountId

    private val _accountPw = MutableStateFlow("")
    val accountPw: StateFlow<String> = _accountPw

    private val _storeListResponse = MutableStateFlow<StoreListResponse?>(null)
    val storeListResponse: StateFlow<StoreListResponse?> = _storeListResponse

    private val _customerLoginSuccess = MutableStateFlow(false)
    val customerLoginSuccess: StateFlow<Boolean> = _customerLoginSuccess

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

            val response = userRepository.login(loginRequest = LoginRequest(accountId = null, password = null, kakaoId = kakaoId))

            response.onSuccess {
                //로그인 성공 시
                auth.updateLoginType(LoginType.KAKAO)
                loginSuccess()
            }.onFailure {
                //로그인 실패 시
                if(it is ApiException){
                    _errorMessage.value = it.message

                    when(it.code) {
                        401 -> {
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
            val response = userRepository.login(loginRequest = LoginRequest(accountId = accountId.value, password = accountPw.value, kakaoId = null))

            response.onSuccess {
                //로그인 성공 시
                auth.updateLoginType(LoginType.APP)
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
        getStoreList()
    }

    private suspend fun getStoreList() {
        val response = ownerRepository.getStoreList()

        response.onSuccess {
            if(it.storeInfoList.isEmpty()) {
                //Customer
                loginByCustomerAccount()
            } else {
                //Owner
                auth.updateAccountType(AccountType.OWNER)
                _storeListResponse.value = it   //사장님은 가게 선택 후 로그인 완료
            }
        }.onFailure {
            if(it is ApiException) {
                _errorMessage.value = it.message
            } else {
                _errorMessage.value = context.getString(R.string.unknown_error_message)
            }
        }
    }

    fun selectStoreFinish(selectedStoreId: Long) {
        auth.updateSelectedStoreId(selectedStoreId)

        auth.updateIsLoggedIn(true)


        /*viewModelScope.launch {
            val response = ownerRepository.getStoreData(selectedStoreId)

            response.onSuccess {
                auth.updateStoreData(it)


            }.onFailure {
                _storeListResponse.value = null

                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }*/
    }

    /**
     * Customer 정보 받는 함수
     */
    fun loginByCustomerAccount() {
        viewModelScope.launch {
            val response = customerRepository.getCustomerInfo()

            response.onSuccess {
                updateCustomerLoginSuccess(true)
                auth.updateAccountType(AccountType.CUSTOMER)
                auth.updateIsLoggedIn(true)
            }.onFailure {
                _storeListResponse.value = null

                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }
    }

    fun updateCustomerLoginSuccess(customerLoginSuccess: Boolean) {
        _customerLoginSuccess.value = customerLoginSuccess
    }
}