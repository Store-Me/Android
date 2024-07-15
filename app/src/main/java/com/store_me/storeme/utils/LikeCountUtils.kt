package com.store_me.storeme.utils

class LikeCountUtils {
    fun convertLikeCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fm", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fk", count / 1_000.0)
            else -> count.toString()
        }
    }
}