package com.store_me.storeme.data.request

data class CouponRequest(
    val name: String,
    val type: String,
    val value: Int,
    val target: String,
    val quantity: Int?,
    val dueDate: String,
    val image: String?,
    val description: String?
)
