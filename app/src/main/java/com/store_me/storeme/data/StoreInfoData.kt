package com.store_me.storeme.data

import com.store_me.storeme.utils.StoreCategory

/**
 * 가게  Data Class
 * @param storeId 가게 ID
 * @param storeName 가게 이름
 * @param storeImage 가게 이미지
 * @param category 카테고리
 * @param customCategory 사용자 설정 카테고리
 * @param location 지역 (XX 동)
 * @param locationCode 지역코드
 *
 * @sample
 * Category List
 * RESTAURANT,
 * CAFE,
 * BEAUTY,
 * MEDICAL,
 * EXERCISE,
 * SALON
 */
data class StoreInfoData(
    val storeId: String,
    val storeName: String,
    val storeImage: String,
    val storeDescription: String,
    val category: StoreCategory,
    val customCategory: String,
    val location: String,
    val locationCode: Int,
    val favoriteCount: Long,
)

/**
 * 가게의 Promotion에 대한 정보
 * @param storeId 가게 ID
 * @param isCouponExist 쿠폰 존재 여부
 * @param isEventExist 이벤트 존재 여부
 */
data class StorePromotionData(
    val storeId: String,
    val isCouponExist: Boolean,
    val isEventExist: Boolean
)

enum class SocialMediaAccountType{
    INSTAGRAM, NAVER, BAND, YOUTUBE, WEB
}

/**
 * 가게 영업시간 정보
 * @param openingHours 영업 시간 List
 * @param closedDay 정기 휴일 [0 ~ 6]
 * @param description 영업 시간 관련 기타 정보
 * @sample StoreHoursData(listOf(
 *     DailyHoursData("09:00", "21:00"),
 *     DailyHoursData("09:00", "21:00"),
 *     DailyHoursData("09:00", "21:00"),
 *     DailyHoursData("09:00", "21:00"),
 *     DailyHoursData("09:00", "21:00"),
 *     DailyHoursData("09:00", "21:00"),
 *     DailyHoursData("10:00", "22:00")
 * ))
 */
data class StoreHoursData(
    val openingHours: List<DailyHoursData>,
    val closedDay: List<Int>?,
    val temporaryOpeningHours: List<TemporaryOpeningHours>,
    val description: String = "",
)

/**
 * 영업 시간 상세 정보
 * @param openHours 오픈 Hours 정보
 * @param openMinutes 오픈 Minutes 정보
 * @param closeTime 마감 시간
 * @param hasBreakTime 브레이크 타임 존재 여부
 * @param startBreakTime 브레이크 타임 시작
 * @param endBreakTime 브레이크 타임 종료
 * @param isAlwaysOpen 24시간 여부
 */
data class DailyHoursData(
    val openHours: Int,
    val openMinutes: Int,
    val closeHours: Int,
    val closeMinutes: Int,
    val startBreakHours: Int,
    val startBreakMinutes: Int,
    val endBreakHours: Int,
    val endBreakMinutes: Int,
    val hasBreakTime: Boolean,
    val isAlwaysOpen: Boolean,
)

sealed class TemporaryOpeningHours {
    data class Closed(
        val startYear: Int,
        val startMonth: Int,
        val startDay: Int,
        val endYear: Int,
        val endMonth: Int,
        val endDay: Int,
    ) : TemporaryOpeningHours()

    data class Adjusted(
        val startYear: Int,
        val startMonth: Int,
        val startDay: Int,
        val endYear: Int,
        val endMonth: Int,
        val endDay: Int,
        val dailyHoursData: DailyHoursData
    ) : TemporaryOpeningHours()
}