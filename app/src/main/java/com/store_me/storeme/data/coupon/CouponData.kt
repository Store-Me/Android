package com.store_me.storeme.data.coupon

data class CouponData(
    val couponId: String,
    val name: String,
    val type: String,
    val value: String,
    val target: String,
    val quantity: Long?,
    val dueDate: String,
    val image: String?,
    val description: String?,
    val createdAt: String,
    val isEdited: Boolean
)
