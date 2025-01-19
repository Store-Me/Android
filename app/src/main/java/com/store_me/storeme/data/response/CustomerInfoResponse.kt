package com.store_me.storeme.data.response

data class CustomerInfoResponse(
    val accountId: String,
    val phoneNumber: String,
    val profileImageUrl: String,
    val hasAppId: Boolean,
    val hasKakaoId: Boolean
)