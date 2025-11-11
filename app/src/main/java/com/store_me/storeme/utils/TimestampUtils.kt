package com.store_me.storeme.utils

import com.google.firebase.Timestamp
import java.time.Duration
import java.time.Instant

/**
 * 시간 경과를 출력하는 확장함수
 */
fun Timestamp.toTimeAgo(): String {
    val now = Instant.now()
    val timestampInstant = this.toDate().toInstant()
    val duration = Duration.between(timestampInstant, now)

    return when {
        duration.toMinutes() < 1 -> "방금 전"
        duration.toMinutes() < 60 -> "${duration.toMinutes()}분 전"
        duration.toHours() < 24 -> "${duration.toHours()}시간 전"
        duration.toDays() < 7 -> "${duration.toDays()}일 전"
        duration.toDays() < 30 -> "${duration.toDays() / 7}주 전"
        duration.toDays() < 365 -> "${duration.toDays() / 30}개월 전"
        else -> "${duration.toDays() / 365}년 전"
    }
}