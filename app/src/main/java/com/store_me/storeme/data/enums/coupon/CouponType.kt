package com.store_me.storeme.data.enums.coupon

enum class CouponType(val displayName: String) {
    Discount("할인"), Giveaway("증정"), Other("기타 혜택");

    companion object {
        fun fromString(value: String): CouponType? {
            return entries.find { it.name == value }
        }
    }
}