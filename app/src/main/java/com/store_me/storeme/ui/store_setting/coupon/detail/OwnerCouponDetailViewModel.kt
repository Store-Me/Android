package com.store_me.storeme.ui.store_setting.coupon.detail

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.CouponData
import com.store_me.storeme.repository.storeme.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OwnerCouponDetailViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {
    private val _coupon = MutableStateFlow<CouponData?>(null)
    val coupon: StateFlow<CouponData?> = _coupon

    fun updateCouponData(couponData: CouponData?) {
        _coupon.value = couponData
    }
}