package com.store_me.storeme.data

data class StoreData(
    val storeName: String,
    val storeProfileImageUrl: String,
    val storeFeaturedImageUrl: String?,
    val storeImageInfoList: List<StoreImageData>,
    val storeBannerImageUrl: String?,
    val storeCategory: String,
    val storeDetailCategory: String,

    val storeLocation: String,
    val storeLocationAddress: String,
    val storeLocationCode: Long,
    val storeLocationDetail: String?,
    val storeLat: Double?,
    val storeLng: Double?,
    val storePhoneNumber: String?,
    val storeNotice: String?,
    val storeIntro: String?
)
