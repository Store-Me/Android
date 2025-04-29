package com.store_me.storeme.data

/**
 * StoreInfo 포함된 쿠폰 Data Class
 * @param couponId 쿠폰 ID
 * @param storeInfo 가게 정보
 * @param content 쿠폰 내용
 */
data class CouponWithStoreInfoData(
    val couponId: String,
    val storeInfo: StoreInfoData,
    val content: String
)


/**
 * 쿠폰의 상세 정보
 */
data class DetailCouponData(
    val couponId: String,
    val content: String,
    val dueDate: String?,
    val leftCount: Int?,
)

/**
 * StoreInfo가 포함된 사용자가 보유한 쿠폰 Data Class
 * @param privateCouponId 사용자가 보유한 쿠폰 고유 ID
 * @param storeInfo 가게 ID
 * @param content 쿠폰 내용
 * @param receivedDatetime 쿠폰 수령 날짜
 * @param expirationDatetime 쿠폰 만료 날짜
 * @param isUsed 사용 여부
 * @sample
 * val coupon = UserCouponData(
 *     privateCouponId = "CP123456789",
 *     storeInfo = StoreInfoData()
 *     content = "10% 할인 쿠폰",
 *     receivedDatetime = "2024-01-01T10:00:00",
 *     expirationDatetime = "2024-12-31T23:59:59",
 *     isUsed = false
 * )
 */
data class UserCouponWithStoreInfoData(
    val privateCouponId: String,
    val storeInfo: StoreInfoData,
    val content: String,
    val receivedDatetime: String,
    val expirationDatetime: String,
    val isUsed: Boolean
)