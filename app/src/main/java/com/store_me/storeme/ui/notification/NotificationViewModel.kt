package com.store_me.storeme.ui.notification

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.ContentType
import com.store_me.storeme.data.NotificationWithStoreInfoData
import dagger.hilt.android.lifecycle.HiltViewModel

class NotificationViewModel(): ViewModel() {

    /**
     * notification 에 맞는 Text를 생성하는 함수
     * @param notification 알림 정보
     */
    fun getNotificationText(notification: NotificationWithStoreInfoData): String {
        return when(notification.contentType){
            ContentType.STORE_UPDATE -> {
                if(notification.contentDescription == null) {
                    "${notification.storeInfoData.storeName}에서 새로운 게시글을 업로드했어요."
                } else {
                    "${notification.storeInfoData.storeName}에서 ${notification.contentDescription}에 새로운 게시글을 업로드했어요."
                }
            }
            ContentType.COUPON_RECEIVED -> {
                if(notification.contentDescription == null) {
                    "${notification.storeInfoData.storeName}에서 사용 가능한 쿠폰을 받았어요!"
                } else {
                    "${notification.storeInfoData.storeName}에서 사용 가능한\n[${notification.contentDescription}]쿠폰을 받았어요!"
                }
            }
            ContentType.RESERVATION_DATE -> {
                if(notification.contentDescription == null) {
                    "오늘은 ${notification.storeInfoData.storeName}에서 예약이 있는 날이에요."
                } else {
                    "오늘은 ${notification.storeInfoData.storeName}에서 ${notification.contentDescription} 예약이 있는 날이에요."
                }
            }
        }
    }
}