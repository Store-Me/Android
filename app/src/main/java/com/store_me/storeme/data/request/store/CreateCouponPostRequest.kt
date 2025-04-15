package com.store_me.storeme.data.request.store

data class CreateCouponPostRequest(
    val title: String,
    val description: String,
    val couponId: String?,
    val isStampCoupon: Boolean
)
