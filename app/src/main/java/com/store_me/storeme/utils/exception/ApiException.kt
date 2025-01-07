package com.store_me.storeme.utils.exception

class ApiException(
    val code: String?,
    message: String?
) : Exception(message)
