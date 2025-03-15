package com.store_me.storeme.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.auth.Auth
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.data.request.login.LoginRequest
import com.store_me.storeme.data.response.MyStoresResponse
import com.store_me.storeme.repository.storeme.CustomerRepository
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
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

    private val _myStoresResponse = MutableStateFlow<MyStoresResponse?>(null)
    val myStoresResponse: StateFlow<MyStoresResponse?> = _myStoresResponse

    private val _customerLoginSuccess = MutableStateFlow(false)
    val customerLoginSuccess: StateFlow<Boolean> = _customerLoginSuccess

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
                    ErrorEventBus.errorFlow.emit(it.message)

                    when(it.code) {
                        401 -> {
                            _isKakaoLoginFailed.value = true
                        }
                    }
                } else {
                    ErrorEventBus.errorFlow.emit(null)
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
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    private suspend fun loginSuccess() {
        getMyStores()
    }

    private suspend fun getMyStores() {
        val response = ownerRepository.getMyStores()

        response.onSuccess {
            if(it.stores.isEmpty()) {
                loginByCustomerAccount()
            } else {
                auth.updateAccountType(AccountType.OWNER)
                _myStoresResponse.value = it   //사장님은 가게 선택 후 로그인 완료
            }

            if(it.stores.isEmpty()) {
                //Customer
                loginByCustomerAccount()
            } else {
                //Owner

            }
        }.onFailure {
            if(it is ApiException) {
                ErrorEventBus.errorFlow.emit(it.message)
            } else {
                ErrorEventBus.errorFlow.emit(null)
            }
        }
    }

    fun selectStoreFinish(selectedStoreId: String) {
        auth.updateSelectedStoreId(selectedStoreId)

        auth.updateIsLoggedIn(true)
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
                _myStoresResponse.value = null

                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun updateCustomerLoginSuccess(customerLoginSuccess: Boolean) {
        _customerLoginSuccess.value = customerLoginSuccess
    }
}