package com.store_me.storeme.ui.store_setting.coupon.setting

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.CouponData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CouponSettingViewModel @Inject constructor() : ViewModel() {
    private val _coupons = MutableStateFlow<List<CouponData>>(emptyList())
    val coupons: StateFlow<List<CouponData>> = _coupons

    fun updateCoupons(coupons: List<CouponData>) {
        _coupons.value = coupons
    }
}