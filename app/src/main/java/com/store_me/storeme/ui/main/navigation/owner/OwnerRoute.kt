package com.store_me.storeme.ui.main.navigation.owner

sealed class OwnerRoute(
    open val path: String,
    open val parent: OwnerRoute? = null
) {
    val fullRoute: String
        get() = parent?.fullRoute?.let { "$it/$path" } ?: path

    //BottomNav
    data object Home : OwnerRoute("home")
    data object CustomerManagement: OwnerRoute("customerManagement")
    data object Add: OwnerRoute("add")
    data object StoreTalk: OwnerRoute("storeTalk")
    data object StoreInfo: OwnerRoute("storeInfo")

    data object StoreManagement: OwnerRoute("storeManagement", parent = Home)
    data object ProfileEdit: OwnerRoute("profileEdit", parent = Home)

    //StoreProfile
    data object IntroSetting: OwnerRoute("introSetting", parent = Home)
    data object BusinessHoursSetting: OwnerRoute("businessHoursSetting", parent = Home)
    data object PhoneNumberSetting: OwnerRoute("phoneNumberSetting", parent = Home)
    data object LocationSetting: OwnerRoute("locationSetting", parent = Home)

    data object LinkSetting: OwnerRoute("linkSetting", parent = Home)

    data object NoticeSetting: OwnerRoute("noticeSetting", parent = Home)

    data object FeaturedImageSetting: OwnerRoute("featuredImageSetting", parent = Home)

    data class MenuSetting(val selectedMenuName: String?): OwnerRoute(if(selectedMenuName != null) "menuSetting/$selectedMenuName" else "menuSetting", parent = Home)
    data class MenuManagement(val selectedMenuName: String?): OwnerRoute(if(selectedMenuName != null) "menuManagement/$selectedMenuName" else "menuManagement", parent = Home)
    data object MenuCategorySetting: OwnerRoute("menuCategorySetting", parent = Home)
    data class MenuCategoryManagement(val selectedCategoryName: String?): OwnerRoute(if(selectedCategoryName != null) "menuCategoryEdit/$selectedCategoryName" else "menuCategoryEdit", parent = Home)

    data object CouponSetting: OwnerRoute("couponSetting", parent = Home)
    data class CouponCreate(val selectedCouponType: String): OwnerRoute("create/$selectedCouponType", CouponSetting) {
        companion object {
            fun template(): String = "${CouponSetting.fullRoute}/create/{selectedCouponType}"
        }
    }
    data class CouponEdit(val selectedCouponType: String, val couponId: String): OwnerRoute("edit/$selectedCouponType/$couponId", CouponSetting) {
        companion object {
            fun template(): String = "${CouponSetting.fullRoute}/edit/{selectedCouponType}/{couponId}"
        }
    }

    //스탬프
    data object StampCouponSetting: OwnerRoute("stampCouponSetting", parent = Home)
    data object StampCouponCreate: OwnerRoute("stampCouponCreate", StampCouponSetting)

    //스토리
    data object StorySetting: OwnerRoute("storySetting", parent = Home)
    data object StoryManagement: OwnerRoute("storyManagement", parent = StorySetting)


    data object ReviewSetting: OwnerRoute("reviewSetting", parent = Home)
    data object NewsSetting: OwnerRoute("newsSetting", parent = Home)




}