package com.store_me.storeme.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object Auth {
    enum class AccountType{
        CUSTOMER, OWNER
    }

    //로그인 상태 관련
    private val _isLoggedIn = mutableStateOf<Boolean>(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    /**
     * 로그인 상태 설정 함수.
     * 로그인 성공시, true.
     * 로그인 실패시, false.
     * @param stateValue 해당 값에 따라 로그인 상태를 설정
     */
    fun setLoginState(stateValue: Boolean) {
        _isLoggedIn.value = stateValue
    }

    //계정 타입 관련
    private val accountType = AccountType.CUSTOMER.name
}