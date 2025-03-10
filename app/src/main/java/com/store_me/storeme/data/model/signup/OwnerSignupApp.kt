package com.store_me.storeme.data.model.signup

data class OwnerSignupApp(
    val accountId: String,
    val password: String,
    val phoneNumber: String,
    val privacyConsent: Boolean,
    val marketingConsent: Boolean,
    val verificationCode: String,

    val storeName: String,
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
