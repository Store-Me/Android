package com.store_me.storeme.ui.banner

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.BannerDetailData
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.SampleDataUtils.Companion.sampleBannerDetailData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BannerDetailViewModel(): ViewModel() {
    private val _dataFetched = MutableStateFlow<Boolean>(false)
    val dataFetched: StateFlow<Boolean> = _dataFetched

    private val _bannerDetailData = MutableStateFlow<BannerDetailData?>(null)
    val bannerDetailData: StateFlow<BannerDetailData?> = _bannerDetailData

    fun getBannerData(){
        if(!_dataFetched.value) {
            _bannerDetailData.value = sampleBannerDetailData()
            _dataFetched.value = true
        }
    }

    fun getBannerState(): DateTimeUtils.PeriodStatus{
        return DateTimeUtils().getPeriodStatus(_bannerDetailData.value!!.startDate, _bannerDetailData.value!!.endDate)
    }

    fun getBannerPeriodText(): String {
        return DateTimeUtils().getBannerPeriodText(_bannerDetailData.value!!.startDate, _bannerDetailData.value!!.endDate)
    }
}
