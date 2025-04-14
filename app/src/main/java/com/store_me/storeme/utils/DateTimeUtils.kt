package com.store_me.storeme.utils

import com.store_me.storeme.data.DateData
import com.store_me.storeme.data.TimeData
import com.store_me.storeme.data.UserCouponWithStoreInfoData
import com.store_me.storeme.data.store.BusinessHourData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

object DateTimeUtils {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * LocalDateTime 문자열에서 LocalDate 추출
     */
    fun extractLocalDate(localDateTimeText: String?): LocalDate? {
        return try {
            LocalDateTime.parse(localDateTimeText, formatter).toLocalDate()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * LocalDateTime 문자열에서 TimeData 추출
     */
    fun extractTimeData(localDateTimeText: String?): TimeData? {
        return try {
            val time = LocalDateTime.parse(localDateTimeText, formatter).toLocalTime()
            TimeData(hour = time.hour, minute = time.minute)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * LocalDate와 TimeData를 ISO 형식의 문자열로 변환하는 함수
     */
    fun toIsoDateTimeString(date: LocalDate, time: TimeData): String {
        val dateTime = LocalDateTime.of(date.year, date.month, date.dayOfMonth, time.hour, time.minute, 59)
        return dateTime.format(formatter)
    }

    /**
     * YYYY-MM-DDTHH:MM:SS 형식의 문자열 데이터를 현재 기준 사용 가능 여부를 반환하는 함수
     */
    fun isValid(dueDateText: String): Boolean {
        return try {
            val dueDate = LocalDateTime.parse(dueDateText, formatter)
            val currentTime = LocalDateTime.now()

            currentTime.isBefore(dueDate) || currentTime.isEqual(dueDate)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * YYYY-MM-DDTHH:MM:SS 형식의 문자열 데이터를 YYYY.MM.DD 로 반환하는 함수
     */
    fun formatToDate(dueDateText: String?): String {
        return try {
            val date = LocalDate.parse(dueDateText, formatter)
            date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        } catch (e: Exception) {
            ""
        }
    }

    fun formatToDate(localDate: LocalDate): String {
        return try {
            localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * YYYY-MM-DDTHH:MM:SS 형식의 문자열 데이터를 YY.MM.DD 로 반환하는 함수
     */
    fun formatToShortDate(dueDateText: String): String {
        return try {
            val date = LocalDate.parse(dueDateText, formatter)
            date.format(DateTimeFormatter.ofPattern("yy.MM.dd"))  // 여기만 변경
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 현재 시각 기준으로 이후인지 확인하는 함수
     * @param datetime 확인할 DateTime
     * @sample "YYYY-MM-ddTHH:MM:SS"
     */
    fun isAfterDatetime(datetime: String): Boolean{
        val currentTime = LocalDateTime.now()

        return LocalDateTime.parse(datetime, formatter).isAfter(currentTime)
    }

    /**
     * "YYYY년 M월 D일" 텍스트를 출력하는 함수
     * @param datetime 시간 정보
     */
    fun convertExpiredDateToKorean(datetime: String?): String {
        return try {
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")

            val localDateTime = LocalDateTime.parse(datetime, formatter)

            localDateTime.format(outputFormatter)
        } catch (e: Exception) {
            ""
        }
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
        val days = closedDay.map { DayOfWeek.entries[it].displayName }

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
     * YYYY-MM-DD 데이터를 받아 YYYY년 MM월 DD일 형식으로 반환하는 함수
     */
    fun getDateString(dateString: String, showYear: Boolean = true, showMonth: Boolean = true, showDay: Boolean = true, withDayOfWeek: Boolean = false): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(dateString, formatter)

            listOfNotNull(
                if (showYear) "${localDate.year}년" else null,
                if (showMonth) "${localDate.monthValue}월" else null,
                if (showDay) "${localDate.dayOfMonth}일" else null,
                if (withDayOfWeek) DayOfWeek.entries[localDate.dayOfWeek.value % 7].displayName else null
            ).joinToString(" ")

        } catch (e: Exception) {
            "잘못된 날짜 형식"
        }
    }

    fun getTimeData(timeString: String?): TimeData? {
        return try {
            if(timeString == null) return null

            val parts = timeString.split(":")
            if (parts.size != 2) return null
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()
            TimeData(hour, minute)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 오늘 요일을 반환하는 함수
     */
    fun getTodayWeekday(): Int {
        val calendar = Calendar.getInstance()
        return (calendar.get(Calendar.DAY_OF_WEEK) - 1)
    }

    /**
     * 특정 LocalDate의 요일을 반환하는 함수
     */
    fun getWeekDay(localDate: LocalDate): Int {
        val calendar = Calendar.getInstance().apply {
            set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)
        }
        return (calendar.get(Calendar.DAY_OF_WEEK) - 1)
    }

    /**
     * 현재 시간을 "HH:mm" 형식으로 가져오는 함수
     */
    fun getCurrentTime(): String {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun getBusinessHoursText(currentTime: String, businessHourData: BusinessHourData): String {
        if (businessHourData.isHoliday) {
            return "휴무일"
        }

        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val current = LocalTime.parse(currentTime, formatter)
        val opening = businessHourData.openingTime?.let { LocalTime.parse(it, formatter) }
        val closing = businessHourData.closingTime?.let { LocalTime.parse(it, formatter) }
        val breakStart = businessHourData.startBreak?.let { LocalTime.parse(it, formatter) }
        val breakEnd = businessHourData.endBreak?.let { LocalTime.parse(it, formatter) }

        // 영업 시간이 설정되지 않은 경우
        if (opening == null || closing == null) {
            return "영업 종료"
        }

        // 영업 시작 전이거나 영업 종료 후인 경우
        if (current.isBefore(opening) || current.isAfter(closing)) {
            return "영업 종료"
        }

        return when(breakEnd != null && breakStart != null) {
            true -> {
                //Break time 있는 경우
                when {
                    current.isBefore(breakStart) -> {
                        "영업 중" + " · " + businessHourData.startBreak + "에 브레이크타임"
                    }
                    current.isAfter(breakEnd) -> {
                        "영업 중" + " · " + businessHourData.closingTime + "에 영업 종료"
                    }
                    else -> {
                        "브레이크타임" + " · " + businessHourData.endBreak + "에 영업 시작"
                    }
                }
            }
            false -> {
                //Break time 없는 경우
                "영업 중" + " · " + businessHourData.closingTime + "에 영업 종료"
            }
        }
    }
}