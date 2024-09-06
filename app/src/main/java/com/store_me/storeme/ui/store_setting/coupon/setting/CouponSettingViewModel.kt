package com.store_me.storeme.ui.store_setting.coupon.setting

import androidx.lifecycle.ViewModel

class CouponSettingViewModel: ViewModel() {
    enum class CouponTabTitles(val displayName: String){
        CREATE("만들기"), LIST("쿠폰 목록")
    }
}