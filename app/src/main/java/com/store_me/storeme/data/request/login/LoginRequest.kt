package com.store_me.storeme.data.request.login

data class LoginRequest(
    val accountId: String?,
    val password: String?,
    val kakaoId: String?
)
