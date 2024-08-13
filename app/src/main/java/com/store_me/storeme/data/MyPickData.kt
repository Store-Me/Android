package com.store_me.storeme.data

/**
 * User 가 Favorite 한 StoreList 를 위한 데이터
 * 마이스토어 > 상단 마이픽 항목 데이터
 * @param storeId 가게 ID 정보
 * @param isNewExist 내가 확인하지 않은 새로운 게시글이 있을 경우
 */
data class MyPickWithStoreIdData(
    val storeId: String,
    val isNewExist: Boolean
)

/**
 * User 가 Favorite 한 StoreList 를 위한 데이터
 * 마이스토어 > 상단 마이픽 항목 데이터
 * @param storeInfoData 가게 정보
 * @param isNewExist 내가 확인하지 않은 새로운 게시글이 있을 경우
 */
data class MyPickWithStoreInfoData(
    val storeInfoData: StoreInfoData,
    val isNewExist: Boolean
)