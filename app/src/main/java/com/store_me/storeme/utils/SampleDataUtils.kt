package com.store_me.storeme.utils

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
    }
}