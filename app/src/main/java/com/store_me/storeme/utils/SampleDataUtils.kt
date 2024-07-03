package com.store_me.storeme.utils

import com.store_me.storeme.data.CouponData
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
    }
}