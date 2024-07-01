package com.store_me.storeme.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object Auth {
    enum class AccountType{
        CUSTOMER, OWNER
    }

    //로그인 상태 관련
    private val _isLoggedIn = mutableStateOf(false)
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
    private var _accountType = AccountType.CUSTOMER.name
    val accountType = _accountType

    /**
     * 계정 타입 설정 함수.
     * 사장님 계정시,OWNER
     * 일반 계정시, CUSTOMER
     * @param type 해당 값에 따라 계정의 타입을 설정
     */
    fun setAccountType(type: String) {
        _accountType = type
    }
}