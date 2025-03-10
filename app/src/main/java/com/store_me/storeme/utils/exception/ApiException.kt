package com.store_me.storeme.utils.exception

class ApiException(
    val code: Int?,
    message: String?
) : Exception(message)
