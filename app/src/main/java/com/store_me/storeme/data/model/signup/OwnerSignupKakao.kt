package com.store_me.storeme.data.model.signup

/**
 * Owner 의 Kakao 계정 회원가입
 * @param kakaoId 카카오 계정 아이디
 */
data class OwnerSignupKakao(
    val kakaoId: String,
    val phoneNumber: String,
    val privacyConsent: Boolean,
    val marketingConsent: Boolean,
    val verificationCode: String,

    val storeName: String,
    val storeDescription: String?,
    val storeCategory: String,
    val storeDetailCategory: String?,
    val storeLocation: String,
    val storeLocationCode: Long,
    val storeLocationDetail: String?,
    val storeLat: Double?,
    val storeLng: Double?,
    val storePhoneNumber: String?,
    val storeIntro: String?
)