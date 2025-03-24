package com.store_me.storeme.data.store

/**
 * Temporary Business Data Class
 * @param startDate 영업 시작일 (YYYY-MM-DD)
 * @param endDate 영업 종료일 (YYYY-MM-DD)
 * @param businessHour 영업 시간
 */
data class TemporaryBusinessData(
    val startDate: String,
    val endDate: String,
    val businessHour: BusinessHourData
)
