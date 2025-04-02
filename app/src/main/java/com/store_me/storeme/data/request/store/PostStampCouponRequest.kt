package com.store_me.storeme.data.request.store

data class PostStampCouponRequest(
    val name: String,
    val rewardInterval: Int,
    val rewardFor5: String?,
    val rewardFor10: String,
    val dueDate: String,
    val password: String,
    val description: String?
)