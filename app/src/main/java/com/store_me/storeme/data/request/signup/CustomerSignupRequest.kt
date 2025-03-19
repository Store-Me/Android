package com.store_me.storeme.data.request.signup

data class CustomerSignupRequest(
    val accountId: String?,
    val password: String?,
    val kakaoId: String?,
    val phoneNumber: String,
    val nickname: String,
    val profileImage: String?,
    val privacyConsent: Boolean,
    val marketingConsent: Boolean
)
