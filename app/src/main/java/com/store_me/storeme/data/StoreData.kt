package com.store_me.storeme.data

import com.store_me.storeme.utils.StoreCategory

/**
 * 쿠폰 Data Class
 * @param storeId 가게 ID
 * @param storeName 가게 이름
 * @param storeImage 가게 이미지
 * @param category 카테고리
 * @param location 지역
 * @param locationCode 지역코드
 *
 * @sample
 * Category List
 * RESTAURANT,
 * CAFE,
 * BEAUTY,
 * MEDICAL,
 * EXERCISE,
 * SALON
 */
data class StoreInfoData(
    val storeId: String,
    val storeName: String,
    val storeImage: String,
    val category: StoreCategory,
    val location: String,
    val locationCode: Int,
)