package com.store_me.storeme.ui.main.navigation.customer

sealed class CustomerRoute(
    open val path: String,
    open val parent: CustomerRoute? = null
) {
    val fullRoute: String
        get() = parent?.fullRoute?.let { "$it/$path" } ?: path

    //BottomNav
    data object Home : CustomerRoute("home")
    data object Favorite: CustomerRoute("favorite")
    data object NearPlace: CustomerRoute("nearPlace")
    data object StoreTalk: CustomerRoute("storeTalk")
    data object MyMenu: CustomerRoute("myMenu")

    data class Notification(override val parent: CustomerRoute): CustomerRoute("notification", parent)
    data class MyCoupon(override val parent: CustomerRoute): CustomerRoute("myCoupon", parent)

    data class LocationSetting(override val parent: CustomerRoute): CustomerRoute("locationSetting", parent = parent)

    data class Banners(override val parent: CustomerRoute): CustomerRoute("banners", parent)
    data class BannerDetail(override val parent: CustomerRoute, val bannerId: String?): CustomerRoute(if(bannerId != null) "bannerDetail/$bannerId" else "bannerDetail", parent)

    data class StoreDetail(override val parent: CustomerRoute, val storeId: String?): CustomerRoute(if(storeId != null) "storeDetail/$storeId" else "storeDetail", parent)
}