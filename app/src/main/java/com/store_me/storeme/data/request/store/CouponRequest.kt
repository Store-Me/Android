package com.store_me.storeme.data.request.store

data class CouponRequest(
    val name: String,
    val type: String, // "Discount" or "Giveaway" or "Other"
    val value: String,
    val target: String,
    val quantity: Long?,
    val dueDate: String,
    val image: String?,
    val description: String?
)
