package com.store_me.storeme.data

data class CouponData(
    val id: String,
    val name: String,
    val type: String,
    val value: String,
    val target: String,
    val quantity: Int?,
    val dueDate: String,
    val image: String?,
    val description: String?,
    val createdAt: String,
    val isEdited: Boolean
)
