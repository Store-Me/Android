package com.store_me.storeme.data.model.signup

data class CustomerSignupKakao(
    val kakaoId: String,
    val phoneNumber: String,
    val nickname: String,
    val privacyConsent: Boolean,
    val marketingConsent: Boolean,
    val verificationCode: String,
)