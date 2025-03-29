package com.store_me.storeme.ui.main.navigation

import com.store_me.storeme.R
import com.store_me.storeme.ui.main.navigation.customer.CustomerRoute
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute

sealed class BottomNavItem(
    open val title: Int,
    open val icon: Int,
    open val selectedIcon: Int,
    open val screenRoute: String
) {
    sealed class Owner {
        data object Home : BottomNavItem(
            title = R.string.owner_home,
            icon = R.drawable.bottom_owner_home,
            selectedIcon = R.drawable.bottom_owner_home_selected,
            screenRoute = OwnerRoute.Home.path
        )
        data object CustomerManagement : BottomNavItem(
            title = R.string.customer_management,
            icon = R.drawable.bottom_customer_management,
            selectedIcon = R.drawable.bottom_customer_management_selected,
            screenRoute = OwnerRoute.CustomerManagement.path
        )
        data object Add : BottomNavItem(
            title = R.string.owner_add,
            icon = R.drawable.bottom_owner_add,
            selectedIcon = R.drawable.bottom_owner_add_selected,
            screenRoute = OwnerRoute.Add.path
        )
        data object StoreTalk : BottomNavItem(
            title = R.string.store_talk,
            icon = R.drawable.bottom_storetalk,
            selectedIcon = R.drawable.bottom_storetalk_selected,
            screenRoute = OwnerRoute.StoreTalk.path
        )
        data object StoreInfo : BottomNavItem(
            title = R.string.store_info,
            icon = R.drawable.bottom_store_info,
            selectedIcon = R.drawable.bottom_store_info_selected,
            screenRoute = OwnerRoute.StoreInfo.path
        )

        companion object {
            val items: List<BottomNavItem> = listOf(
                Home, CustomerManagement, Add, StoreTalk, StoreInfo
            )
        }
    }

    sealed class Customer {
        data object Home : BottomNavItem(
            title = R.string.home,
            icon = R.drawable.bottom_home,
            selectedIcon = R.drawable.bottom_home_selected,
            screenRoute = CustomerRoute.Home.path
        )
        data object Favorite : BottomNavItem(
            title = R.string.favorite,
            icon = R.drawable.bottom_favorite,
            selectedIcon = R.drawable.bottom_favorite_selected,
            screenRoute = CustomerRoute.Favorite.path
        )
        data object NearPlace : BottomNavItem(
            title = R.string.near_place,
            icon = R.drawable.bottom_nearplace,
            selectedIcon = R.drawable.bottom_nearplace_selected,
            screenRoute = CustomerRoute.NearPlace.path
        )
        data object StoreTalk : BottomNavItem(
            title = R.string.store_talk,
            icon = R.drawable.bottom_storetalk,
            selectedIcon = R.drawable.bottom_storetalk_selected,
            screenRoute = CustomerRoute.StoreTalk.path
        )
        data object Profile : BottomNavItem(
            title = R.string.profile,
            icon = R.drawable.bottom_mymenu,
            selectedIcon = R.drawable.bottom_mymenu_selected,
            screenRoute = CustomerRoute.MyMenu.path
        )

        companion object {
            val items: List<BottomNavItem> = listOf(
                Home, Favorite, NearPlace, StoreTalk, Profile
            )
        }
    }
}