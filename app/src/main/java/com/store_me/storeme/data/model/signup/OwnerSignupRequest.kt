package com.store_me.storeme.data.model.signup

data class OwnerSignupRequest(
    val accountId: String?,
    val password: String?,
    val kakaoId: String?,
    val phoneNumber: String,
    val privacyConsent: Boolean,
    val marketingConsent: Boolean,

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
    val storeIntro: String?
)
