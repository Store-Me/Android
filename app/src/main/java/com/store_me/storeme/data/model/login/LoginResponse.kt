package com.store_me.storeme.data.model.login

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiredTime: String
)