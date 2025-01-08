package com.store_me.storeme.data.response

data class ReissueResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiredTime: String
)
