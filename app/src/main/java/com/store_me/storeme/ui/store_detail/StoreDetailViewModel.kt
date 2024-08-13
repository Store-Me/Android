package com.store_me.storeme.ui.store_detail

import androidx.lifecycle.ViewModel
import com.store_me.storeme.utils.SampleDataUtils

class StoreDetailViewModel : ViewModel() {
    val storeDetailData = SampleDataUtils.sampleDetailData
    val couponDetailData = SampleDataUtils.sampleCouponDetailData


}