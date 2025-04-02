package com.store_me.storeme.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Auth {

    //계정 정보 관련
    private val _userData = MutableStateFlow(UserData("도구리", "도구리야", "https://i.namu.wiki/i/vJ_iVx2uAFkYUmfaxSwP0QSDbPjRz-OzilacQpDBLQmls9oOM0pV4qUk8mCbgL41v4_wGV-kdotau0LIpZu261XmIpWq0qLg3gKfSSBg78Px_EGRyNlmZk6d5N6KKx6zgsZArniJ3t2cwmB4IvS-0A.webp"))
    val userData: StateFlow<UserData> = _userData

    /*   영업 시간 정보 및 휴무일 설정   */
    private val _storeHoursData = MutableStateFlow(StoreHoursData(emptyList(), emptyList(), emptyList(),""))
    val storeHoursData: StateFlow<StoreHoursData> = _storeHoursData

    fun setStoreHoursData(storeHoursData: StoreHoursData) {
        _storeHoursData.value = storeHoursData
    }
}