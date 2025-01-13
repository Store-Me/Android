package com.store_me.storeme.data.response

data class StoreMeResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T
)
