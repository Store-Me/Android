package com.store_me.storeme.data.response

import com.store_me.storeme.data.store.coupon.CouponData

data class CouponResponse(
    val storeId: String,
    val coupon: CouponData
)
