package com.store_me.storeme.ui.main.navigation.customer

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.store_me.storeme.ui.banner.BannerDetailScreen
import com.store_me.storeme.ui.banner.BannerListScreen
import com.store_me.storeme.ui.home.customer.CustomerHomeScreen
import com.store_me.storeme.ui.location.LocationScreen
import com.store_me.storeme.ui.my_menu.MyMenuScreen
import com.store_me.storeme.ui.mycoupon.MyCouponScreenWithBottomSheet
import com.store_me.storeme.ui.mystore.MyStoreScreenWithBottomSheet
import com.store_me.storeme.ui.near_place.NearPlaceScreen
import com.store_me.storeme.ui.notification.NotificationScreen
import com.store_me.storeme.ui.store_detail.StoreDetailScreen
import com.store_me.storeme.ui.store_talk.StoreTalkScreen

@Composable
fun CustomerNavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = CustomerRoute.Home.fullRoute){

        //Bottom Navigation Screen
        composable(CustomerRoute.Home.fullRoute) { CustomerHomeScreen(navController, locationViewModel = hiltViewModel()) }
        composable(CustomerRoute.Favorite.fullRoute) { MyStoreScreenWithBottomSheet() }
        composable(CustomerRoute.NearPlace.fullRoute) { NearPlaceScreen(navController, locationViewModel = hiltViewModel()) }
        composable(CustomerRoute.StoreTalk.fullRoute) { StoreTalkScreen(navController) }
        composable(CustomerRoute.MyMenu.fullRoute) { MyMenuScreen(navController) }

        composable(CustomerRoute.Notification(parent = CustomerRoute.Home).fullRoute) { NotificationScreen(navController) }
        composable(CustomerRoute.Notification(parent = CustomerRoute.MyMenu).fullRoute) { NotificationScreen(navController) }

        composable(CustomerRoute.MyCoupon(CustomerRoute.Home).fullRoute) { MyCouponScreenWithBottomSheet(navController) }
        composable(CustomerRoute.MyCoupon(CustomerRoute.MyMenu).fullRoute) { MyCouponScreenWithBottomSheet(navController) }

        composable(CustomerRoute.LocationSetting(CustomerRoute.Home).fullRoute) { LocationScreen(navController, locationViewModel = hiltViewModel()) }
        composable(CustomerRoute.LocationSetting(CustomerRoute.NearPlace).fullRoute) { LocationScreen(navController, locationViewModel = hiltViewModel()) }

        composable(CustomerRoute.Banners(CustomerRoute.Home).fullRoute) { BannerListScreen(navController) }
        composable(CustomerRoute.Banners(CustomerRoute.NearPlace).fullRoute) { BannerListScreen(navController) }
        composable(CustomerRoute.Banners(CustomerRoute.MyMenu).fullRoute) { BannerListScreen(navController) }

        composable(CustomerRoute.BannerDetail(CustomerRoute.Home, null).fullRoute + "/{bannerId}") { backStackEntry ->
            val bannerId = backStackEntry.arguments?.getString("bannerId")
            BannerDetailScreen(navController, bannerId = bannerId ?: "")
        }
        composable(CustomerRoute.BannerDetail(CustomerRoute.NearPlace, null).fullRoute + "/{bannerId}") { backStackEntry ->
            val bannerId = backStackEntry.arguments?.getString("bannerId")
            BannerDetailScreen(navController, bannerId = bannerId ?: "")
        }
        composable(CustomerRoute.BannerDetail(CustomerRoute.MyMenu, null).fullRoute + "/{bannerId}") { backStackEntry ->
            val bannerId = backStackEntry.arguments?.getString("bannerId")
            BannerDetailScreen(navController, bannerId = bannerId ?: "")
        }

        composable(CustomerRoute.StoreDetail(CustomerRoute.Home, null).fullRoute + "/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            StoreDetailScreen(navController, storeId = storeId ?: "")
        }
    }
}