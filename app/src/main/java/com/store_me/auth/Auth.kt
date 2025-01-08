package com.store_me.auth

import android.content.Context
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.utils.TokenPreferencesHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject



class Auth @Inject constructor(
    private val context: Context,

) {
    //로그인 상태
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    //계정 타입
    private val _accountType = MutableStateFlow(AccountType.OWNER)
    val accountType: StateFlow<AccountType> = _accountType

    //초기화
    fun init() {
        val refreshToken = TokenPreferencesHelper.getRefreshToken()
        val accessToken = TokenPreferencesHelper.getAccessToken()
    }

    /**
     * 계정 타입 설정 함수.
     * 사장님 계정시,OWNER
     * 일반 계정시, CUSTOMER
     * @param type 해당 값에 따라 계정의 타입을 설정
     */
    fun setAccountType(type: AccountType) {
        _accountType.value = type
    }

    /**
     * 계정 타입 변경 함수
     */
    fun changeAccountType() {
        _accountType.value = if(_accountType.value == AccountType.CUSTOMER) {
            AccountType.OWNER
        } else {
            AccountType.CUSTOMER
        }
    }

    /**
     * 로그아웃 함수
     */
    fun logout() {
        _isLoggedIn.value = false

        TokenPreferencesHelper.clearTokens()
    }

}