package com.store_me.storeme.ui.store_setting.coupon.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.coupon.CouponData
import com.store_me.storeme.data.coupon.CouponStats
import com.store_me.storeme.data.response.CouponUsersResponse
import com.store_me.storeme.repository.storeme.CouponRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerCouponDetailViewModel @Inject constructor(
    private val couponRepository: CouponRepository
) : ViewModel() {
    private val _coupon = MutableStateFlow<CouponData?>(null)
    val coupon: StateFlow<CouponData?> = _coupon

    private val _couponStats = MutableStateFlow<CouponStats?>(null)
    val couponStats: StateFlow<CouponStats?> = _couponStats

    private val _couponUsers = MutableStateFlow<CouponUsersResponse?>(null)
    val couponUsers: StateFlow<CouponUsersResponse?> = _couponUsers

    fun updateCouponData(couponData: CouponData?) {
        _coupon.value = couponData
    }

    fun getCouponStats(couponId: String) {
        viewModelScope.launch {
            val response = couponRepository.getCouponStats(
                couponId = couponId
            )

            response.onSuccess {
                _couponStats.value = it.result
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun getCouponUsers(couponId: String) {
        viewModelScope.launch {
            val response = couponRepository.getCouponUsers(
                couponId = couponId
            )

            response.onSuccess {
                _couponUsers.value = it.result
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }
}