package com.store_me.storeme.utils

import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.DefaultPostData
import com.store_me.storeme.data.MyPickData
import com.store_me.storeme.data.NotificationData
import com.store_me.storeme.data.StoreInfoData

class SampleDataUtils {
    companion object {
        fun sampleBannerImage(): MutableList<String> {
            return mutableListOf(
                "https://via.placeholder.com/500x100",
                "https://via.placeholder.com/750x150",
                "https://via.placeholder.com/1000x200",
                "https://via.placeholder.com/1250x250",
                "https://via.placeholder.com/1500x300",
            )
        }

        fun sampleTodayStore(): MutableList<StoreInfoData>{
            return mutableListOf(
                StoreInfoData("궁댕이마카롱", "https://via.placeholder.com/500x500", "카페"),
                StoreInfoData("진국인집", "https://via.placeholder.com/500x500", "음식점"),
                StoreInfoData("쿠키팡팡", "https://via.placeholder.com/500x500", "카페"),
                StoreInfoData("쭉쭉피자", "https://via.placeholder.com/500x500", "음식점"),
                StoreInfoData("초코나라머핀공주", "https://via.placeholder.com/500x500", "카페"),
                StoreInfoData("피자나라치킨공주", "https://via.placeholder.com/500x500", "음식점"),
                StoreInfoData("갓생살GTM", "https://via.placeholder.com/500x500", "운동"),
            )
        }

        fun sampleCoupon(): MutableList<CouponData>{
            return mutableListOf(
                CouponData("바르다만두선생", "https://via.placeholder.com/500x500", "고기/김치만두 2개 증정"),
                CouponData("진국인집", "https://via.placeholder.com/500x500", "2000원 할인 쿠폰"),
                CouponData("쿠키팡팡", "https://via.placeholder.com/500x500", "쿠키 3개 증정"),
                CouponData("쭉쭉피자", "https://via.placeholder.com/500x500", "치즈 크러스트 엣지 무료 교환권"),
                CouponData("초코나라머핀공주", "https://via.placeholder.com/500x500", "초코머핀 1개 무료 교환권"),
                CouponData("피자나라치킨공주", "https://via.placeholder.com/500x500", "치킨무 1개 무료 교환권"),
                CouponData("갓생살GTM", "https://via.placeholder.com/500x500", "PT 1회 이용권\n+ 1개월 할인권"),
            )
        }

        fun sampleNotification(): MutableList<NotificationData>{
            return mutableListOf(
                NotificationData("구매", "3시간 전", "https://via.placeholder.com/100x100", "진국인집 [4000원 쿠폰]을 받았어요!", false),
                NotificationData("예약", "1시간 전", "https://via.placeholder.com/100x100", "오늘은 초코나라 머핀공주에서 예약한 초코머핀 수령일이에요.", false),
                NotificationData("구매", "6시간 전", "https://via.placeholder.com/100x100", "쿠키팡팡에서 찜한 레드벨벳치즈케잌맛 쿠키가 구매 가능해요.", false),
                NotificationData("예약", "12시간 전", "https://via.placeholder.com/100x100", "갓생살GYM에서 새로운 공지사항을 게시했어요. 확인해보세요.", false)
            )
        }

        fun sampleMyPick(): MutableList<MyPickData>{
            return mutableListOf(
                MyPickData("https://via.placeholder.com/100x100", "바르다만두선생", true),
                MyPickData("https://via.placeholder.com/100x100", "진국인집", false),
                MyPickData("https://via.placeholder.com/100x100", "쭉쭉피자", false),
                MyPickData("https://via.placeholder.com/100x100", "초코나라머핀공주", true),
                MyPickData("https://via.placeholder.com/100x100", "갓생살GYM", false),
                MyPickData("https://via.placeholder.com/100x100", "강동짬뽕", true),
                MyPickData("https://via.placeholder.com/100x100", "김밥나라 구파발 1호점", false),
                MyPickData("https://via.placeholder.com/100x100", "피자나라치킨공주", false),
                MyPickData("https://via.placeholder.com/100x100", "이얼싼", false),
                MyPickData("https://via.placeholder.com/100x100", "코끼리마트", true)
            )
        }

        fun samplePost(): MutableList<DefaultPostData>{
            return mutableListOf(
                DefaultPostData("https://via.placeholder.com/100x100", "바르다만두선생", "강남구", "1분 전", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"), "1번 게시물 제목입니다.", "내용입니다. 내용이지롱~내용입니다. 내용이지롱~내용입니다. 내용이지롱~내용입니다. 내용이지롱~내용입니다. 내용이지롱~내용입니다. 내용이지롱~내용입니다. 내용이지롱~"),
                DefaultPostData("https://via.placeholder.com/100x100", "진국인집", "은평구", "1시간 전", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"), "2번 게시물 제목입니다.", "2번 게시물 내용입니다"),
                DefaultPostData("https://via.placeholder.com/100x100", "초코나라머핀공주", "광진구", "2시간 전", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"), "3번 게시물 제목입니다.", "3번 게시물 내용입니다"),
                DefaultPostData("https://via.placeholder.com/100x100", "피자나라치킨공주", "서초구", "5시간 전", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"), "4번 게시물 제목입니다.", "4번 게시물 내용입니다"),
                DefaultPostData("https://via.placeholder.com/100x100", "코끼리마트", "다산동", "1일 전", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"), "5번 게시물 제목입니다.", "4번 게시물 내용입니다"),
                DefaultPostData("https://via.placeholder.com/100x100", "맘모스커피", "중구", "1년 전", listOf("https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100", "https://via.placeholder.com/200x100"), "6번 게시물 제목입니다.", "6번 게시물 내용입니다")
            )
        }
    }
}