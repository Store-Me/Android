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

    /*   쿠폰 관리   */
    private val _couponDetailList = MutableStateFlow(listOf(
        OwnerCouponDetailData(
            couponInfoData = CouponInfoData.Other(
                couponId = "DefaultCouponId",
                content = "혜택이다",
                name = "초코쿠키 25개 무료 체험권",
                available = CouponAvailable.REPEAT,
                quantity = CouponQuantity.Infinite,
                dueDate = DateData(2024, 9, 3),
                image = "",
                description = "",
                createdAt = "2024-09-01T10:00:00"
            ),
            usedCouponData = UsedCouponData(0, 0)
        ),OwnerCouponDetailData(
            couponInfoData = CouponInfoData.Discount(
                couponId = "DefaultCouponId2",
                discountType = CouponDiscountType.PRICE,
                discountValue = 4000,
                name = "초코쿠키 25개 무료 체험권2",
                available = CouponAvailable.REPEAT,
                quantity = CouponQuantity.Infinite,
                dueDate = DateData(2024, 9, 23),
                image = "",
                description = "",
                createdAt = "2024-09-01T10:00:00"
            ),
            usedCouponData = UsedCouponData(0, 0)
        )
        )
    )
    val couponDetailList: StateFlow<List<OwnerCouponDetailData>> = _couponDetailList

    fun addCouponDetailData(ownerCouponDetailData: OwnerCouponDetailData){
        _couponDetailList.value = _couponDetailList.value.toMutableList().apply {
            add(ownerCouponDetailData)
        }
    }

    fun deleteCouponDetailData(ownerCouponDetailData: OwnerCouponDetailData){
        _couponDetailList.value = _couponDetailList.value.toMutableList().apply {
            remove(ownerCouponDetailData)
        }
    }

    fun updateCouponDetailData(updatedCouponDetailData: OwnerCouponDetailData) {
        _couponDetailList.value = _couponDetailList.value.map { coupon ->
            if (coupon.couponInfoData.couponId == updatedCouponDetailData.couponInfoData.couponId) {
                updatedCouponDetailData
            } else {
                coupon
            }
        }
    }
}