package com.store_me.storeme.data

import com.naver.maps.geometry.LatLng
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
    val favoriteCount: Int,
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

/**
 * 다른 Social Media 계정 정보
 */
data class SocialMediaAccountData(
    val urlList: List<String>
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
    val description: String = "",
)

/**
 * 영업 시간 상세 정보
 * @param openTime 오픈 시간
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

/**
 * 가게의 메뉴 목록 데이터
 * @param menus MenuData List
 */

data class StoreMenuData(
    val menus: List<MenuData>,
)

/**
 * 가게의 메뉴 데이터
 * @param menu 메뉴 이름
 * @param isSignature 메인 메뉴 여부
 * @param menuDescription 메뉴 설명
 * @param price 가격
 */
data class MenuData(
    val menu: String,
    val isSignature: Boolean,
    val menuDescription: String?,
    val price: Int?
)

/**
 * 가게의 상세 주소 정보
 * @param locationDetail 상세 주소
 * @param latLng 위도 경도 정보
 */
data class LocationInfo(
    val locationDetail: String,
    val latLng: LatLng?
)

/**
 * 커스텀 Label 목록
 * @param labelList 라벨 목록
 */
data class CustomLabelData(
    val labelList: List<String>
)

data class PreviewPostData(
    val imageUrl: String,
    val title: String,
    val datetime: String
)

data class LabelWithPostData(
    val label: String,
    val posts: List<PreviewPostData>
)

data class StoreDetailData(
    val storeInfo: StoreInfoData,
    val bannerImageUrl: String?,
    val socialMediaAccountData: SocialMediaAccountData,
    val storeHours: StoreHoursData,
    val storePhoneNumber: String,
    val locationInfo: LocationInfo,
    val storeMenu: StoreMenuData,
    val customLabel: CustomLabelData,
    val notice: String?,
    val labelWithPostData: List<LabelWithPostData>,
    val representPhoto: List<String>,
    val couponList: List<DetailCouponData>,
    val isStoryExist: Boolean,
    val isReviewExist: Boolean,
)

/**
 * 기본으로 제공되며 숨김 및 순서 변경이 불가능 한 Store Item
 */
enum class StoreNormalItem(val displayName: String) {
    OPENING_HOURS("영업시간"),
    CLOSED_DAY("휴무일"),
    LOCATION("위치 정보"),
}

/**
 * 숨김 및 순서 변경이 가능 한 Store Item
 */
enum class StoreHomeItem(val displayName: String) {
    NOTICE("공지사항"),
    INTRO("소개"),
    PHOTO("대표 사진"),
    COUPON("스토어 쿠폰"),
    MENU("메뉴"),
    STORY("스토어 스토리"),
    REVIEW("스토어 리뷰"),
    NEWS("소식")
}

/**
 * Store Home Item 의 순서 및 숨김 여부
 * @param item StoreHomeItem
 * @param isHidden 숨김 여부
 * @param order 순서
 */
data class StoreHomeItemData(
    val item: StoreHomeItem,
    val isHidden: Boolean = false,
    val order: Int
)