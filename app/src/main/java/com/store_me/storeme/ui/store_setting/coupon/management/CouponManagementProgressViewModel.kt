package com.store_me.storeme.ui.store_setting.coupon.management

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.enums.coupon.CouponCreationProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CouponManagementProgressViewModel: ViewModel() {
    private val _currentProgress = MutableStateFlow(CouponCreationProgress.VALUE)
    val currentProgress: StateFlow<CouponCreationProgress> = _currentProgress

    fun nextProgress() {
        val nextOrdinal = _currentProgress.value.ordinal + 1
        if(nextOrdinal < CouponCreationProgress.entries.size){
            _currentProgress.value = CouponCreationProgress.entries[nextOrdinal]
        }
    }

    fun previousProgress() {
        val previousOrdinal = _currentProgress.value.ordinal - 1
        if(previousOrdinal >= 0) {
            _currentProgress.value = CouponCreationProgress.entries[previousOrdinal]
        }
    }
}