package com.store_me.storeme.utils

import com.store_me.storeme.data.DateData
import com.store_me.storeme.data.UserCouponWithStoreInfoData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
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
     * 현재 날짜를 "2024년 9월 1일" 형식으로 반환하는 함수
     */
    fun dateTimeToDateText(datetime: String): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")

        return LocalDateTime.parse(datetime, formatter).format(dateFormatter)
    }

    /**
     * 현재 날짜와 시간을 "2024년 9월 1일 17:09" 형식으로 반환하는 함수
     */
    fun dateTimeToDateTimeText(datetime: String): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm")

        return LocalDateTime.parse(datetime, formatter).format(dateTimeFormatter)
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

    fun localDateToDateData(date: LocalDate): DateData {
        return DateData(
            year = date.year,
            month = date.month.value,
            day = date.dayOfMonth
        )
    }

    fun dateDataToLocalDate(date: DateData): LocalDate {
        return LocalDate.of(date.year, date.month, date.day)
    }

    fun getSelectTimeText(hours: Int, minutes: Int): String {
        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
    }

    fun getDayOfWeek(year: Int, month: Int, day: Int): String {
        val date = LocalDate.of(year, month, day)

        return DayOfWeek.entries[date.dayOfWeek.value % 7].displayName
    }

    /**
     * DateTime 을 Long 으로 변환하는 함수
     */
    fun dateTimeToLong(dateTime: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(dateTime, formatter)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}