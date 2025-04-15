package com.store_me.storeme.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.auth.Auth
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.data.request.login.LoginRequest
import com.store_me.storeme.data.response.CustomerInfoResponse
import com.store_me.storeme.data.response.MyStore
import com.store_me.storeme.repository.storeme.CustomerRepository
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

    private val _myStores = MutableStateFlow<List<MyStore>?>(null)
    val myStores: StateFlow<List<MyStore>?> = _myStores

    private val _customerInfo = MutableStateFlow<CustomerInfoResponse?>(null)
    val customerInfo: StateFlow<CustomerInfoResponse?> = _customerInfo

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
                onLoginSuccess()
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
                onLoginSuccess()
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

    private fun onLoginSuccess() {
        viewModelScope.launch {
            val storeDeferred = async { ownerRepository.getMyStores() }
            val customerDeferred = async { customerRepository.getCustomerInfo() }

            val storeResponse = storeDeferred.await()
            val customerResponse = customerDeferred.await()

            var storeFailed = false
            var customerFailed = false

            storeResponse.onSuccess {
                _myStores.value = it.stores
            }.onFailure {
                storeFailed = true
            }

            customerResponse.onSuccess {
                _customerInfo.value = it
            }.onFailure {
                customerFailed = true
            }

            if (storeFailed && customerFailed) {
                ErrorEventBus.errorFlow.emit("계정정보 조회에 실패하였습니다.")
            }
        }
    }

    fun loginAsOwner(selectedMyStore: MyStore) {
        auth.updateAccountType(AccountType.OWNER)
        auth.updateSelectedStoreId(selectedMyStore.storeId)
        auth.updateIsLoggedIn(true)
    }

    fun loginAsCustomer() {
        auth.updateAccountType(AccountType.CUSTOMER)
        auth.updateIsLoggedIn(true)
    }
}