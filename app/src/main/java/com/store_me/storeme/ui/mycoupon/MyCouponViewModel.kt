package com.store_me.storeme.ui.mycoupon

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.UserCouponWithStoreIdData
import com.store_me.storeme.data.UserCouponWithStoreInfoData
import com.store_me.storeme.utils.CategoryUtils
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.SampleDataUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MyCouponViewModel : ViewModel(){
    val tabTitles = listOf("사용가능", "만료쿠폰")

    private val _myValidCouponList = MutableStateFlow<MutableList<UserCouponWithStoreInfoData>>(mutableListOf())
    val myValidCouponList: StateFlow<MutableList<UserCouponWithStoreInfoData>> get() = _myValidCouponList

    private val _myExpiredCouponList = MutableStateFlow<MutableList<UserCouponWithStoreInfoData>>(mutableListOf())
    val myExpiredCouponList: StateFlow<MutableList<UserCouponWithStoreInfoData>> get() = _myExpiredCouponList



    fun getCouponData(){
        val validCoupon = mutableListOf<UserCouponWithStoreInfoData>()
        val expiredCoupon = mutableListOf<UserCouponWithStoreInfoData>()

        //TODO Get Coupon Data 받아서 SampleDataUtils.sampleUserCoupon() 를 대체 하면 됨

        SampleDataUtils.sampleUserCoupon().forEach {
            if(checkCouponValidation(it))
                validCoupon.add(it)
            else
                expiredCoupon.add(it)
        }

        sortCoupons()

        _myValidCouponList.update { validCoupon }
        _myExpiredCouponList.update { expiredCoupon }
    }

    /**
     * Check Coupon Validation
     */
    private fun checkCouponValidation(coupon: UserCouponWithStoreInfoData): Boolean{
        return DateTimeUtils().isAfterDatetime(coupon.expirationDatetime) && !coupon.isUsed
    }


    //순서 정렬 관련
    private val _currentSortType = MutableStateFlow(SortType.RECEIVED_DATE)
    val currentSortType: StateFlow<SortType> get() = _currentSortType

    enum class SortType {
        RECEIVED_DATE, EXPIRATION_DATE
    }

    fun changeSortType(sortType: SortType) {
        _currentSortType.update { sortType }
        sortCoupons()
    }

    private fun sortCoupons(){
        when(currentSortType.value){
            SortType.RECEIVED_DATE -> {
                _myValidCouponList.update { DateTimeUtils().sortCouponsByReceivedDate(myValidCouponList.value).toMutableList() }
            }
            SortType.EXPIRATION_DATE -> {
                _myValidCouponList.update { DateTimeUtils().sortCouponsByExpiredDate(myValidCouponList.value).toMutableList() }
            }
        }
    }

    fun getSortButtonText(sortType: SortType): String{
        return when(sortType){
            SortType.RECEIVED_DATE -> { "최근 받은 순" }
            SortType.EXPIRATION_DATE -> { "만료 임박 순" }
        }
    }
}