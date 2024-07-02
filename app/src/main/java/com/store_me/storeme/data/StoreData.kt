package com.store_me.storeme.data

/**
 * 쿠폰 Data Class
 * @param storeName 가게 이름
 * @param storeImage 가게 이미지
 * @param category 카테고리
 */
data class StoreInfoData(
    val storeName: String,
    val storeImage: String,
    val category: String,
)

/**
 * 카테고리 Enum Class
 */
enum class Category {
    RESTAURANT, CAFE, BEAUTY, SALON, EXERCISE,
}