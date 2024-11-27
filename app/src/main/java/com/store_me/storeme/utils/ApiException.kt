package com.store_me.storeme.utils

class ApiException(val code: String?, message: String?) : Exception(message)
