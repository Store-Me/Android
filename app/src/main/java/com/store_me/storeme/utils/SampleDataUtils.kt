package com.store_me.storeme.utils

import com.store_me.storeme.data.BannerData
import com.store_me.storeme.data.ChatRoomWithStoreInfoData
import com.store_me.storeme.data.ContentType
import com.store_me.storeme.data.CouponWithStoreInfoData
import com.store_me.storeme.data.NormalPostWithStoreInfoData
import com.store_me.storeme.data.MyPickWithStoreInfoData
import com.store_me.storeme.data.NotificationType
import com.store_me.storeme.data.NotificationWithStoreInfoData
import com.store_me.storeme.data.StoreInfoData
import com.store_me.storeme.data.UserCouponWithStoreInfoData

class SampleDataUtils {


    companion object {
        private val sampleStoreInfo = mutableListOf(
            StoreInfoData("0","궁댕이마카롱", "https://via.placeholder.com/500x500", StoreCategory.CAFE, "강남구", 1138069000),
            StoreInfoData("1","진국인집", "https://via.placeholder.com/500x500", StoreCategory.RESTAURANT, "강남구", 1138069000),
            StoreInfoData("2","쿠키팡팡", "https://via.placeholder.com/500x500", StoreCategory.CAFE, "강남구", 1138069000),
            StoreInfoData("3","쭉쭉피자", "https://via.placeholder.com/500x500", StoreCategory.RESTAURANT, "강남구", 1138069000),
            StoreInfoData("4","초코나라머핀공주", "https://via.placeholder.com/500x500", StoreCategory.CAFE, "강남구", 1138069000),
            StoreInfoData("5","피자나라치킨공주", "https://via.placeholder.com/500x500", StoreCategory.RESTAURANT, "강남구", 1138069000),
            StoreInfoData("6","갓생살GTM", "https://via.placeholder.com/500x500", StoreCategory.EXERCISE, "강남구", 1138069000),
        )

        fun sampleBannerImage() : MutableList<BannerData>{
            return mutableListOf(
                BannerData("0","스토어미와", "https://via.placeholder.com/500x100"),
                BannerData("0","스토어미와 함께하는 ", "https://via.placeholder.com/750x150"),
                BannerData("0","스토어미와 함께하는 리뷰", "https://via.placeholder.com/1000x200"),
                BannerData("0","스토어미와 함께하는 리뷰 이벤트", "https://via.placeholder.com/1250x250"),
                BannerData("0","스토어미와 함께하는 리뷰 이벤트스토어미와 함께하는 리뷰 이벤트스토어미와 함께하는 리뷰 이벤트", "https://via.placeholder.com/1500x300"),
            )
        }

        fun sampleTodayStore(): MutableList<StoreInfoData>{
            return sampleStoreInfo
        }

        fun sampleCoupon(): MutableList<CouponWithStoreInfoData>{
            return mutableListOf(
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[0], "고기/김치만두 2개 증정"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[1], "2000원 할인 쿠폰"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[2], "쿠키 3개 증정"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[3], "치즈 크러스트 엣지 무료 교환권"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[4], "초코머핀 1개 무료 교환권"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[5], "치킨무 1개 무료 교환권"),
                CouponWithStoreInfoData("COUPON_ID", sampleStoreInfo[6], "PT 1회 이용권 + 1개월 할인권"),
            )
        }

        fun sampleUserCoupon(): MutableList<UserCouponWithStoreInfoData>{
            return mutableListOf(
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[0], "고기/김치만두 2개 증정", "2024-07-01T13:06:39", "2024-07-14T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[1], "2000원 할인 쿠폰", "2024-07-02T13:06:39", "2024-07-13T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[2], "쿠키 3개 증정", "2024-07-03T13:06:39", "2024-07-12T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[3], "치즈 크러스트 엣지 무료 교환권", "2024-07-04T13:06:39", "2024-07-11T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[4], "초코머핀 1개 무료 교환권", "2024-07-05T13:06:39", "2024-07-10T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[5], "치킨무 1개 무료 교환권", "2024-07-06T13:06:39", "2024-07-09T13:06:39", false),
                UserCouponWithStoreInfoData( "ASD", sampleStoreInfo[6], "PT 1회 이용권 + 1개월 할인권", "2024-07-07T13:06:39", "2024-07-08T13:06:39", false),
            )
        }

        fun sampleNotification(): MutableList<NotificationWithStoreInfoData>{
            return mutableListOf(
                NotificationWithStoreInfoData(NotificationType.NORMAL, "2024-07-10T13:06:39",sampleStoreInfo[0], ContentType.COUPON_RECEIVED, "마카롱 종류 300개 이용권",false),
                NotificationWithStoreInfoData(NotificationType.RESERVATION, "2024-07-11T13:06:39",sampleStoreInfo[1], ContentType.RESERVATION_DATE, "다데기",false),
                NotificationWithStoreInfoData(NotificationType.NORMAL, "2024-07-12T13:06:39",sampleStoreInfo[2], ContentType.COUPON_RECEIVED, null,false),
                NotificationWithStoreInfoData(NotificationType.NORMAL, "2024-07-13T13:06:39",sampleStoreInfo[3], ContentType.STORE_UPDATE, "이벤트 알림",false)
            )
        }

        fun sampleMyPick(): MutableList<MyPickWithStoreInfoData>{
            return mutableListOf(
                MyPickWithStoreInfoData(sampleStoreInfo[0], true),
                MyPickWithStoreInfoData(sampleStoreInfo[1], false),
                MyPickWithStoreInfoData(sampleStoreInfo[2], false),
                MyPickWithStoreInfoData(sampleStoreInfo[3], true),
                MyPickWithStoreInfoData(sampleStoreInfo[4], false),
                MyPickWithStoreInfoData(sampleStoreInfo[5], true),
                MyPickWithStoreInfoData(sampleStoreInfo[6], false),
            )
        }

        fun samplePost(): MutableList<NormalPostWithStoreInfoData>{
            return mutableListOf(
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0], "2024-07-01T13:06:39", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"),"1번 게시물 제목입니다.", "1번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0], "2024-07-01T13:06:39", null,"2번 게시물 제목입니다.", "2번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0], "2024-07-01T13:06:39", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"),"3번 게시물 제목입니다.", "3번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0], "2024-07-01T13:06:39", null,"4번 게시물 제목입니다.", "4번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0], "2024-07-01T13:06:39", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"),"5번 게시물 제목입니다.", "4번 게시물 내용입니다"),
                NormalPostWithStoreInfoData("https://via.placeholder.com/100x100", sampleStoreInfo[0], "2024-07-01T13:06:39", null,"6번 게시물 제목입니다.", "6번 게시물 내용입니다")
            )
        }

        fun sampleChatRooms(): MutableList<ChatRoomWithStoreInfoData>{
            return mutableListOf(
                ChatRoomWithStoreInfoData("1", "궁뎅이마카롱", sampleStoreInfo[0], "안녕하세요", "17:30", 30),
                ChatRoomWithStoreInfoData("1", "궁뎅이마카롱", sampleStoreInfo[1], "안녕하세요", "17:30", 30),
                ChatRoomWithStoreInfoData("2", "쿠키팡팡", sampleStoreInfo[2], "저번에도 노쇼하셨잖아요 손님같으면 손님한테 예약 받겠어요?", "9:50", 1),
                ChatRoomWithStoreInfoData("3", "진국인집", sampleStoreInfo[3], "저희 주방장들은 모두 머리가 없는데 머리카락이 어떻게 나와요 손님", "1일 전", 99),
                ChatRoomWithStoreInfoData("4", "고기의신", sampleStoreInfo[4], "넵! 내일 20시 예약 완료했습니다!", "1주 전", 1),
                ChatRoomWithStoreInfoData("5", "우리동네과일가게", sampleStoreInfo[5], "모두 직접 가져오는 과일입니다.", "1달 전", 500),
                ChatRoomWithStoreInfoData("6", "바르다만두선생", sampleStoreInfo[6], "이번 만두는 바르지못해 폐기하였습니다.", "1년 전", 0),
            )
        }
    }
}