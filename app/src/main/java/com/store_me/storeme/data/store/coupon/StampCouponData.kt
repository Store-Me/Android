package com.store_me.storeme.data.store.coupon

data class StampCouponData(
    val name: String,
    val rewardInterval: Int,
    val rewardFor5: String?,
    val rewardFor10: String,
    val dueDate: String,
    val description: String?
)
