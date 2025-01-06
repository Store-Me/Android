package com.store_me.storeme.utils

object ValidationUtils {
    fun isValidId(accountId: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9]{4,20}$")
        return regex.matches(accountId)
    }

    fun isValidPw(accountPw: String): Boolean {
        return accountPw.length in 4 .. 20
    }
}