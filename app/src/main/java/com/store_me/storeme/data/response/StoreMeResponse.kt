package com.store_me.storeme.data.response

data class StoreMeResponse<T>(
    val message: String,
    val result: T
)