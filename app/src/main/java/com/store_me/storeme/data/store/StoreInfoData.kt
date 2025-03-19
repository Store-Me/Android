package com.store_me.storeme.data.store

data class StoreInfoData(
    val storeName: String,
    val storeProfileImage: String?,
    val storeDescription: String?,
    val storeCategory: String,
    val storeDetailCategory: String?,
    val storeLocation: String,
    val storeLocationAddress: String,
    val storeLocationCode: Long,
    val storeLocationDetail: String?,
    val storeLat: Double?,
    val storeLng: Double?,
    val storePhoneNumber: String?,
    val storeIntro: String?,
    val backgroundImage: String?
)
