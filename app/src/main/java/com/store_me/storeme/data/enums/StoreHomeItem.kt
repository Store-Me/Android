package com.store_me.storeme.data.enums

import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute

enum class StoreHomeItem(
    val displayName: String,
    val route: OwnerRoute
) {
    NOTICE(
        displayName = "공지사항",
        route = OwnerRoute.NoticeSetting
    ),
    FEATURED_IMAGES(
        displayName = "사진",
        route = OwnerRoute.FeaturedImageSetting
    ),
    MENU(
        displayName = "메뉴",
        route = OwnerRoute.MenuSetting(null)
    ),
    COUPON(
        displayName = "쿠폰",
        route = OwnerRoute.CouponSetting
    ),
    STAMP_COUPON(
        displayName = "스탬프",
        route = OwnerRoute.StampCouponSetting
    ),
    STORY(
        displayName = "스토어 스토리",
        route = OwnerRoute.StorySetting
    ),
    REVIEW(
        displayName = "스토어 리뷰",
        route = OwnerRoute.ReviewSetting
    ),
    NEWS(
        displayName = "소식",
        route = OwnerRoute.NewsSetting
    )
}