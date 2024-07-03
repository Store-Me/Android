package com.store_me.storeme.data


/**
 * 쿠폰 Data Class
 * @param storeName 가게 이름
 * @param storeImage 가게 이미지
 * @param content 쿠폰 내용
 */
data class CouponData(
    val storeName: String,
    val storeImage: String,
    val content: String
)