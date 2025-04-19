package com.store_me.auth

import android.content.Context
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.preference.TokenPreferencesHelper
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

    //Owner 계정에서 선택된 StoreId
    private val _storeId = MutableStateFlow<String?>(null)
    val storeId: StateFlow<String?> = _storeId

    fun getStoreId(): String {
        return storeId.value ?: ""
    }

    /**
     * 계정 타입 설정 함수.
     * 사장님 계정시,OWNER
     * 일반 계정시, CUSTOMER
     * @param newAccountType 해당 값에 따라 계정의 타입을 설정
     */
    fun updateAccountType(newAccountType: AccountType) {
        _accountType.value = newAccountType
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
     * 로그인 값 변경
     */
    fun updateIsLoggedIn(isLoggedIn: Boolean) {
        _isLoggedIn.value = isLoggedIn

        if(!isLoggedIn) {
            TokenPreferencesHelper.clearTokens()
        }
    }

    /**
     * OWNER 계정에서 선택된 StoreId 변경 함수
     */
    fun updateSelectedStoreId(storeId: String) {
        _storeId.value = storeId
    }
}