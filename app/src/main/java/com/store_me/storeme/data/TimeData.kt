package com.store_me.storeme.data

import java.util.Locale

data class TimeData(
    val hour: Int,
    val minute: Int
)

fun TimeData.getText(): String {
    return try {
        String.format(Locale.US, "%02d:%02d", hour, minute)
    } catch (e: Exception) {
        ""
    }
}