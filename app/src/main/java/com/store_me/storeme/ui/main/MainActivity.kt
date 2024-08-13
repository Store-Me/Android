
package com.store_me.storeme.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.banner.BannerDetailScreen
import com.store_me.storeme.ui.banner.BannerListScreen
import com.store_me.storeme.ui.home.HomeScreen
import com.store_me.storeme.ui.location.LocationScreen
import com.store_me.storeme.ui.login.LoginActivity
import com.store_me.storeme.ui.my_menu.MyMenuScreen
import com.store_me.storeme.ui.mycoupon.MyCouponScreenWithBottomSheet
import com.store_me.storeme.ui.mystore.MyStoreScreenWithBottomSheet
import com.store_me.storeme.ui.near_place.NearPlaceScreen
import com.store_me.storeme.ui.notification.NotificationScreen
import com.store_me.storeme.ui.store_detail.StoreDetailScreen
import com.store_me.storeme.ui.store_talk.StoreTalkScreen
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.storeMeTypography
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!Auth.isLoggedIn.value){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        setContent {
            StoreMeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun MainScreen() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            Box(Modifier.padding(it)) {
                NavigationGraph(navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = BottomNavItem.UserHome.screenRoute){

            //기본 Bottom Item
            composable(BottomNavItem.UserHome.screenRoute) { HomeScreen(navController, locationViewModel = hiltViewModel()) }
            composable(BottomNavItem.Favorite.screenRoute) { MyStoreScreenWithBottomSheet() }
            composable(BottomNavItem.NearPlace.screenRoute) { NearPlaceScreen(navController, locationViewModel = hiltViewModel()) }
            composable(BottomNavItem.StoreTalk.screenRoute) { StoreTalkScreen(navController) }
            composable(BottomNavItem.Profile.screenRoute) { MyMenuScreen(navController) }

            //HOME > NOTIFICATION
            composable(USER_HOME + NormalNavItem.NOTIFICATION.name) { NotificationScreen(navController) }
            //MY_MENU > NOTIFICATION
            composable(MY_MENU + NormalNavItem.NOTIFICATION.name) { NotificationScreen(navController) }

            //HOME > MY_COUPON
            composable(USER_HOME + NormalNavItem.MY_COUPON.name) { MyCouponScreenWithBottomSheet(navController) }
            //MY_MENU > MY_COUPON
            composable(MY_MENU + NormalNavItem.MY_COUPON.name) { MyCouponScreenWithBottomSheet(navController) }

            //HOME > LOCATION
            composable(USER_HOME + NormalNavItem.LOCATION.name) { LocationScreen(navController, locationViewModel = hiltViewModel()) }
            //NEAR PLACE > NOTIFICATION
            composable(NEAR_PLACE + NormalNavItem.LOCATION.name) { LocationScreen(navController, locationViewModel = hiltViewModel()) }

            /**
             * Any To Banner (List / Detail)
             */
            composable(USER_HOME + NormalNavItem.BANNER_LIST.name) { BannerListScreen(navController) }
            composable(NEAR_PLACE + NormalNavItem.BANNER_LIST.name) { BannerListScreen(navController) }
            composable(MY_MENU + NormalNavItem.BANNER_LIST.name) { BannerListScreen(navController) }

            composable(USER_HOME + NormalNavItem.BANNER_DETAIL.name + "/{bannerId}") { backStackEntry ->
                val bannerId = backStackEntry.arguments?.getString("bannerId")
                BannerDetailScreen(navController, bannerId = bannerId ?: "")
            }
            composable(NEAR_PLACE + NormalNavItem.BANNER_DETAIL.name + "/{bannerId}") { backStackEntry ->
                val bannerId = backStackEntry.arguments?.getString("bannerId")
                BannerDetailScreen(navController, bannerId = bannerId ?: "")
            }
            composable(MY_MENU + NormalNavItem.BANNER_DETAIL.name + "/{bannerId}") { backStackEntry ->
                val bannerId = backStackEntry.arguments?.getString("bannerId")
                BannerDetailScreen(navController, bannerId = bannerId ?: "")
            }

            composable(USER_HOME + NormalNavItem.STORE_DETAIL.name + "/{storeId}") { backStackEntry ->
                val storeId = backStackEntry.arguments?.getString("storeId")
                StoreDetailScreen(navController, storeId = storeId ?: "")
            }
        }
    }

    @Composable
    private fun BottomNavigationBar(navController: NavHostController) {
        val items = listOf(
            BottomNavItem.UserHome,
            BottomNavItem.Favorite,
            BottomNavItem.NearPlace,
            BottomNavItem.StoreTalk,
            BottomNavItem.Profile
        )

        Column {
            HorizontalDivider(color = UnselectedItemColor, thickness = 0.2.dp)

            NavigationBar (
                containerColor = White,
                modifier = Modifier
                    .height(67.dp),
                tonalElevation = 0.dp
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach{ item ->
                    val isSelected = when {
                        currentRoute == item.screenRoute -> true
                        currentRoute?.startsWith(USER_HOME) == true && item.screenRoute == BottomNavItem.UserHome.screenRoute -> true
                        currentRoute?.startsWith(FAVORITE) == true && item.screenRoute == BottomNavItem.Favorite.screenRoute -> true
                        currentRoute?.startsWith(NEAR_PLACE) == true && item.screenRoute == BottomNavItem.NearPlace.screenRoute -> true
                        currentRoute?.startsWith(STORE_TALK) == true && item.screenRoute == BottomNavItem.StoreTalk.screenRoute -> true
                        currentRoute?.startsWith(MY_MENU) == true && item.screenRoute == BottomNavItem.Profile.screenRoute -> true
                        else -> false
                    }

                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = if (isSelected) item.selectedIcon else item.icon),
                                contentDescription = stringResource(id = item.title),
                                modifier = Modifier
                                    .width(27.dp)
                                    .height(27.dp),
                                tint = Color.Unspecified
                            )
                        },
                        label = {
                            val textColor = if(isSelected) Black else UnselectedItemColor
                            Text(stringResource(id = item.title), style = storeMeTypography.titleSmall, fontSize = 10.sp, color = textColor) },
                        selected = isSelected,
                        alwaysShowLabel = true,
                        onClick = {
                            navController.navigate(item.screenRoute) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) { saveState = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,

                        ),
                    )
                }
            }

        }
    }

    sealed class BottomNavItem(val title: Int, val icon: Int, val selectedIcon: Int, val screenRoute: String) {
        data object UserHome : BottomNavItem(R.string.home, R.drawable.bottom_home, R.drawable.bottom_home_selected, USER_HOME)
        data object Favorite : BottomNavItem(R.string.favorite, R.drawable.bottom_favorite, R.drawable.bottom_favorite_selected, FAVORITE)
        data object NearPlace : BottomNavItem(R.string.near_place, R.drawable.bottom_nearplace, R.drawable.bottom_nearplace_selected, NEAR_PLACE)
        data object StoreTalk : BottomNavItem(R.string.store_talk, R.drawable.bottom_storetalk, R.drawable.bottom_storetalk_selected, STORE_TALK)
        data object Profile : BottomNavItem(R.string.profile, R.drawable.bottom_mymenu, R.drawable.bottom_mymenu_selected, MY_MENU)
    }

    enum class NormalNavItem {
        NOTIFICATION,
        LOCATION,
        BANNER_LIST,
        BANNER_DETAIL,
        STORE_DETAIL,
        MY_COUPON
    }
}