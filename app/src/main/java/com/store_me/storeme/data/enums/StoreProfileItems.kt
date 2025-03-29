package com.store_me.storeme.data.enums

import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute

enum class StoreProfileItems(
    val displayName: String,
    val route: OwnerRoute
) {
    IntroSetting(
        displayName = "소개",
        route = OwnerRoute.IntroSetting
    ),
    BusinessHoursSetting(
        displayName = "영업 시간",
        route = OwnerRoute.BusinessHoursSetting
    ),
    PhoneNumberSetting(
        displayName = "전화번호",
        route = OwnerRoute.PhoneNumberSetting),
    LocationSetting(
        displayName = "위치",
        route = OwnerRoute.LocationSetting),
    ProfileEdit(
        displayName = "프로필 수정",
        route = OwnerRoute.ProfileEdit),
    StoreManagement(
        displayName = "가게정보 관리",
        route = OwnerRoute.StoreManagement)
}