package com.store_me.storeme.utils

import java.util.Locale

object LikeCountUtils {
    fun convertLikeCount(count: Long): String {
        return when {
            count < 1_000 -> {
                count.toString()
            }
            count < 10_000 -> {
                "${count / 1_000}천"
            }
            count < 1_000_000 -> {
                String.format(Locale.KOREA, "%.1f만", count / 10_000.0)
            }
            count < 100_000_000 -> {
                if (count % 10_000 == 0L) {
                    "${count / 10_000}만"
                } else {
                    String.format(Locale.KOREA, "%.1f만", count / 10_000.0)
                }
            }
            else -> {
                if (count % 100_000_000 == 0L) {
                    "${count / 100_000_000}억"
                } else {
                    String.format(Locale.KOREA, "%.1f억", count / 100_000_000.0)
                }
            }
        }
    }
}
