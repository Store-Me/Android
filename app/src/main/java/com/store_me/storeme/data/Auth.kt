package com.store_me.storeme.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object Auth {
    enum class AccountType{
        CUSTOMER, OWNER
    }

    //로그인 상태 관련
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

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
    private var _accountType = AccountType.OWNER
    val accountType = _accountType

    /**
     * 계정 타입 설정 함수.
     * 사장님 계정시,OWNER
     * 일반 계정시, CUSTOMER
     * @param type 해당 값에 따라 계정의 타입을 설정
     */
    fun setAccountType(type: AccountType) {
        _accountType = type
    }

    fun changeAccountType() {
        _accountType = if(accountType == AccountType.CUSTOMER) {
            AccountType.OWNER
        } else {
            AccountType.CUSTOMER
        }
    }

    //계정 정보 관련
    private val _userData = MutableStateFlow(UserData("도구리", "도구리야", "https://i.namu.wiki/i/vJ_iVx2uAFkYUmfaxSwP0QSDbPjRz-OzilacQpDBLQmls9oOM0pV4qUk8mCbgL41v4_wGV-kdotau0LIpZu261XmIpWq0qLg3gKfSSBg78Px_EGRyNlmZk6d5N6KKx6zgsZArniJ3t2cwmB4IvS-0A.webp"))
    val userData: StateFlow<UserData> = _userData

    /**
     * 사용자 계정 정보 설정
     */
    fun setUserData(userData: UserData) {
        _userData.value = userData
    }

    fun updateProfileImage(newProfileImage: String) {
        _userData.update {
            it.copy(profileImage = newProfileImage)
        }
    }

    /**
     * 사장님 계정 정보 설정
     */
    private val _linkListData = MutableStateFlow<SocialMediaAccountData?>(null)
    val linkListData: StateFlow<SocialMediaAccountData?> = _linkListData

    fun setLinkListData(socialMediaAccountData: SocialMediaAccountData?) {
        _linkListData.value = socialMediaAccountData
    }
}