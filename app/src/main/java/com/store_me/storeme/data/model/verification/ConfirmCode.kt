package com.store_me.storeme.data.model.verification

data class ConfirmCode(
    val phoneNumber: String,
    val verificationCode: String
)