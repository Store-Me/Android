package com.store_me.storeme.data


/**
 * StoreId 포함된 쿠폰 Data Class
 * @param couponId 쿠폰 ID
 * @param storeId 가게 ID
 * @param content 쿠폰 내용
 */
data class CouponWithStoreIdData(
    val couponId: String,
    val storeId: String,
    val content: String
)

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

enum class CouponType(val displayName: String) {
    DISCOUNT("할인"), GIVEAWAY("증정"), OTHER("기타 혜택");

    companion object {
        fun fromString(value: String): CouponType? {
            return values().find { it.name == value }
        }
    }
}

enum class CouponDiscountType {
    PRICE, RATE;

    companion object {
        fun fromString(value: String): CouponDiscountType? {
            return values().find { it.name == value }
        }
    }
}

enum class CouponAvailable {
    ALL, REPEAT;

    companion object {
        fun fromString(value: String): CouponAvailable? {
            return CouponAvailable.values().find { it.name == value }
        }
    }
}

sealed class CouponQuantity {
    data object Infinite : CouponQuantity()
    data class Limit(val quantity: Int) : CouponQuantity()

    companion object {
        fun fromString(value: String): CouponQuantity? {
            return when {
                value.equals("Infinite", ignoreCase = true) -> Infinite
                value.startsWith("Limit", ignoreCase = true) -> {
                    val quantity = value.split(":").getOrNull(1)?.toIntOrNull() ?: return null
                    Limit(quantity)
                }
                else -> null
            }
        }
    }
}

/**
 * Coupon 정보 데이터
 * @param couponId 쿠폰 ID
 * @param name 쿠폰 이름
 * @param available 모든 사용자 / 재방문 사용자 (ALL / REPEAT)
 * @param quantity 무제한 / 제한 (INFINITE / LIMIT, QUANTITY)
 * @param dueDate 마감일 (year, month, date)
 * @param image 쿠폰 이미지 Url
 * @param description 쿠폰 설명
 * @param createdAt 생성 시간 "YYYY-MM-DDTHH:MM:SS"
 */
sealed class CouponInfoData(
    open val couponId: String,
    open val name: String,
    open val available: CouponAvailable,
    open val quantity: CouponQuantity,
    open val dueDate: DateData,
    open val image: String,
    open val description: String,
    open val createdAt: String,
) {
    data class Discount(
        val discountType: CouponDiscountType,
        val discountValue: Int,
        override val couponId: String,
        override val name: String,
        override val available: CouponAvailable,
        override val quantity: CouponQuantity,
        override val dueDate: DateData,
        override val image: String,
        override val description: String,
        override val createdAt: String
    ) : CouponInfoData(couponId, name, available, quantity, dueDate, image, description, createdAt)

    data class Giveaway(
        val content: String,
        override val couponId: String,
        override val name: String,
        override val available: CouponAvailable,
        override val quantity: CouponQuantity,
        override val dueDate: DateData,
        override val image: String,
        override val description: String,
        override val createdAt: String
    ) : CouponInfoData(couponId, name, available, quantity, dueDate, image, description, createdAt)

    data class Other(
        val content: String,
        override val couponId: String,
        override val name: String,
        override val available: CouponAvailable,
        override val quantity: CouponQuantity,
        override val dueDate: DateData,
        override val image: String,
        override val description: String,
        override val createdAt: String
    ) : CouponInfoData(couponId, name, available, quantity, dueDate, image, description, createdAt)
}

/**
 * 사용자 사용에 따른 결과값
 * @param receivedCount 쿠폰 수령한 사용자
 * @param usedCount 쿠폰 사용한 사용자
 */
data class UsedCouponData(
    val receivedCount: Int,
    val usedCount: Int
)

data class OwnerCouponDetailData(
    val couponInfoData: CouponInfoData,
    val usedCouponData: UsedCouponData
)

data class CouponWithUserData(
    val couponId: String,
    val userReceivedCouponList: List<UserReceivedCouponData>,
    val userUsedCouponList: List<UserUsedCouponData>
)

data class UserReceivedCouponData(
    val userData: UserData
)

data class UserUsedCouponData(
    val userData: UserData,
    val usedAt: String,
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
 * StoreId가 포함된 사용자가 보유한 쿠폰 Data Class
 * @param privateCouponId 사용자가 보유한 쿠폰 고유 ID
 * @param couponId 쿠폰 Id
 * @param receivedDatetime 쿠폰 수령 날짜
 * @param expirationDatetime 쿠폰 만료 날짜
 * @param isUsed 사용 여부
 * @sample
 * val coupon = UserCouponData(
 *     privateCouponId = "CP123456789",
 *     storeId = "19619A",
 *     content = "10% 할인 쿠폰",
 *     receivedDatetime = "2024-01-01T10:00:00",
 *     expirationDatetime = "2024-12-31T23:59:59",
 *     isUsed = false
 * )
 */
data class UserCouponWithStoreIdData(
    val privateCouponId: String,
    val couponId: String,
    val receivedDatetime: String,
    val expirationDatetime: String,
    val isUsed: Boolean
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