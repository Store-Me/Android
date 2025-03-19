package com.store_me.storeme.data.store

data class BusinessHourData(
    val openingTime: String?,
    val closingTime: String?,
    val startBreak: String?,
    val endBreak: String?,
    val isHoliday: Boolean
)
