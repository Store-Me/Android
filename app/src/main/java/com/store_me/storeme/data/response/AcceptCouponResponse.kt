package com.store_me.storeme.data.response

data class AcceptCouponResponse(
    val storeId: String,
    val couponId: String,
    val acceptedAt: String,
)
