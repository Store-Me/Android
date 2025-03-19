package com.store_me.storeme.data.response

data class PatchResponse<T>(
    val message: String,
    val result: T
)