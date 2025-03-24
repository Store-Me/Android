package com.store_me.storeme.data.store

/**
 * Business Hour Data Class
 * @param openingTime 영업 시작 시간 (HH:mm)
 * @param closingTime 영업 종료 시간 (HH:mm)
 * @param startBreak 브레이크 타임 시작 시간 (HH:mm)
 * @param endBreak 브레이크 타임 종료 시간 (HH:mm)
 * @param isHoliday 휴무일 여부
 */
data class BusinessHourData(
    val openingTime: String?,
    val closingTime: String?,
    val startBreak: String?,
    val endBreak: String?,
    val isHoliday: Boolean
)
