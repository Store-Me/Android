package com.store_me.storeme.data

/**
 * 알림 목록에 사용되는 데이터
 * @param notificationType 알림의 종류 (NORMAL, RESERVATION)
 * @param datetime 알림 발생 시간
 * @param storeId 가게 ID
 * @param contentType 내용 Type
 * @param isRead 읽음 여부
 */
data class NotificationWithStoreIdData(
    val notificationType: NotificationType,
    val datetime: String,
    val storeId: String,
    val contentType: ContentType,
    val contentDescription: String?,
    var isRead: Boolean
)

/**
 * 알림 목록에 사용되는 데이터
 * @param notificationType 알림의 종류 (NORMAL, RESERVATION)
 * @param datetime 알림 발생 시간
 * @param storeInfoData 가게 Info
 * @param contentType 내용 Type
 * @param contentDescription 세부 내용
 * @param isRead 읽음 여부
 */
data class NotificationWithStoreInfoData(
    val notificationType: NotificationType,
    val datetime: String,
    val storeInfoData: StoreInfoData,
    val contentType: ContentType,
    val contentDescription: String?,
    var isRead: Boolean
)

enum class NotificationType(val displayName: String) {
    ALL("전체"),
    NORMAL("소식"),
    RESERVATION("예약"),
}

enum class ContentType{
    COUPON_RECEIVED,

    RESERVATION_DATE,

    STORE_UPDATE,
}
