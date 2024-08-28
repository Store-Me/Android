package com.store_me.storeme.utils

import com.store_me.storeme.data.UserCouponWithStoreInfoData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DateTimeUtils {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * 현재 시각 기준으로 이후인지 확인하는 함수
     * @param datetime 확인할 DateTime
     * @sample "YYYY-MM-DDTHH:MM:SS"
     */
    fun isAfterDatetime(datetime: String): Boolean{
        val currentTime = LocalDateTime.now()

        return LocalDateTime.parse(datetime, formatter).isAfter(currentTime)
    }

    /**
     * "YYYY년 M월 D일 까지" 텍스트를 출력하는 함수
     * @param datetime 시간 정보
     */
    fun convertExpiredDateToKorean(datetime: String): String {
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일까지")

        val localDateTime = LocalDateTime.parse(datetime, formatter)
        return localDateTime.format(outputFormatter)
    }

    /**
     * 수령일 기준으로 정렬하는 함수
     */
    fun sortCouponsByReceivedDate(coupons: List<UserCouponWithStoreInfoData>): List<UserCouponWithStoreInfoData> {
        return coupons.sortedBy { LocalDateTime.parse(it.receivedDatetime, formatter) }
    }

    /**
     * 만료일 기준으로 정렬하는 함수
     */
    fun sortCouponsByExpiredDate(coupons: List<UserCouponWithStoreInfoData>): List<UserCouponWithStoreInfoData> {
        return coupons.sortedBy { LocalDateTime.parse(it.expirationDatetime, formatter) }
    }

    /**
     * 현재로부터 얼마 전인지 String으로 반환하는 함수
     * @param datetime 시간 정보
     */
    fun datetimeAgo(datetime: String): String {
        val targetTime = LocalDateTime.parse(datetime, formatter)
        val nowTime = LocalDateTime.now()

        val minutesAgo = ChronoUnit.MINUTES.between(targetTime, nowTime)
        val hoursAgo = ChronoUnit.HOURS.between(targetTime, nowTime)
        val daysAgo = ChronoUnit.DAYS.between(targetTime, nowTime)
        val weeksAgo = ChronoUnit.WEEKS.between(targetTime, nowTime)
        val monthsAgo = ChronoUnit.MONTHS.between(targetTime, nowTime)

        return when {
            minutesAgo < 60 -> "${minutesAgo}분 전"
            hoursAgo < 24 -> "${hoursAgo}시간 전"
            daysAgo < 7 -> "${daysAgo}일 전"
            weeksAgo < 5 -> "${weeksAgo}주 전"
            else -> "${monthsAgo}개월 전"
        }
    }

    fun getPeriodStatus(startTime: String, endTime: String): PeriodStatus {
        val currentTime = LocalDateTime.now()
        val start = LocalDateTime.parse(startTime, formatter)
        val end = LocalDateTime.parse(endTime, formatter)

        return when {
            currentTime.isBefore(start) -> PeriodStatus.BEFORE_START
            currentTime.isAfter(end) -> PeriodStatus.ENDED
            else -> PeriodStatus.ONGOING
        }
    }

    fun getBannerPeriodText(startTime: String, endTime: String): String {
        val outputFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
        val start = LocalDate.parse(startTime, formatter)
        val end = LocalDate.parse(endTime, formatter)

        val formattedStart = start.format(outputFormatter)
        val formattedEnd = end.format(outputFormatter)

        return "$formattedStart ~ $formattedEnd"
    }

    enum class PeriodStatus(val displayName: String){
        ONGOING("진행중"), ENDED("종료"), BEFORE_START("준비중")
    }

    enum class DayOfWeek(val displayName: String) {
        SUNDAY("일"),
        MONDAY("월"),
        TUESDAY("화"),
        WEDNESDAY("수"),
        THURSDAY("목"),
        FRIDAY("금"),
        SATURDAY("토"),
    }

    fun getClosedDayText(closedDay: List<Int>): String {
        val days = closedDay.map { DayOfWeek.values()[it].displayName }

        return when(closedDay.size) {
            1 -> "${days[0]}요일 휴무"
            else -> {
                "${days.joinToString(", ")}요일 휴무"
            }
        }
    }

    fun getSelectTimeText(hours: Int, minutes: Int): String {
        return "${hours}:${minutes.toString().padStart(2, '0')}"
    }

    fun splitTimeTextToInt(time: String): Pair<Int, Int> {
        val (hours, minutes) = time.split(":")

        return hours.toInt() to minutes.toInt()
    }
}

