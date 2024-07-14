
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
import androidx.compose.ui.Alignment
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
import com.store_me.storeme.ui.banner.BannerListScreen
import com.store_me.storeme.ui.home.HomeScreen
import com.store_me.storeme.ui.location.LocationScreen
import com.store_me.storeme.ui.login.LoginActivity
import com.store_me.storeme.ui.mycoupon.MyCouponScreenWithBottomSheet
import com.store_me.storeme.ui.mystore.MyStoreScreenWithBottomSheet
import com.store_me.storeme.ui.notification.NotificationScreen
import com.store_me.storeme.ui.store_detail.StoreDetailScreen
import com.store_me.storeme.ui.store_talk.StoreTalkScreen
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.storeMeTypography
import dagger.hilt.android.AndroidEntryPoint

/**
 * ScreenRoute 생성함수
 * @param bottomItem 활성화 할 Bottom Item
 * @param screenName 이동 할 화면 이름
 * @param additionalData 추가로 전송할 데이터
 */
fun createScreenRoute(bottomItem: String, screenName: String, additionalData: String? = null): String{
    return if(additionalData.isNullOrEmpty()){
        "$bottomItem$screenName"
    } else {
        "$bottomItem$screenName/$additionalData"
    }
}

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
        NavHost(navController, startDestination = BottomNavItem.UserHome.screenRoute,
            /*enterTransition = { fadeIn(animationSpec = tween(0)) },
            exitTransition = { fadeOut(animationSpec = tween(0)) },
            popEnterTransition = { fadeIn(animationSpec = tween(0)) },
            popExitTransition = { fadeOut(animationSpec = tween(0)) }*/){
            composable(BottomNavItem.UserHome.screenRoute) { HomeScreen(navController, locationViewModel = hiltViewModel()) }
            composable(BottomNavItem.Favorite.screenRoute) { MyStoreScreenWithBottomSheet() }
            composable(BottomNavItem.NearPlace.screenRoute) { NearPlaceScreen() }
            composable(BottomNavItem.StoreTalk.screenRoute) { StoreTalkScreen() }
            composable(BottomNavItem.Profile.screenRoute) { ProfileScreen() }

            composable(NormalNavItem.NOTIFICATION.screenRoutes[0] + NormalNavItem.NOTIFICATION.name) { NotificationScreen(navController) }
            composable(NormalNavItem.LOCATION.screenRoutes[0] + NormalNavItem.LOCATION.name) { LocationScreen(navController, locationViewModel = hiltViewModel()) }
            composable(NormalNavItem.BANNER_LIST.screenRoutes[0] + NormalNavItem.BANNER_LIST.name) { BannerListScreen(navController) }
            composable(NormalNavItem.MY_COUPON.screenRoutes[0] + NormalNavItem.MY_COUPON.name) { MyCouponScreenWithBottomSheet(navController) }
            composable(NormalNavItem.STORE_DETAIL.screenRoutes[0] + NormalNavItem.STORE_DETAIL.name + "/{storeName}") { backStackEntry ->
                val storeName = backStackEntry.arguments?.getString("storeName")
                StoreDetailScreen(navController, storeName = storeName ?: "")
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
                        currentRoute?.startsWith(PROFILE) == true && item.screenRoute == BottomNavItem.Profile.screenRoute -> true
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
        data object Profile : BottomNavItem(R.string.profile, R.drawable.bottom_mymenu, R.drawable.bottom_mymenu_selected, PROFILE)
    }

    enum class NormalNavItem(val screenRoutes: List<String>) {
        NOTIFICATION(BOTTOM_ITEM_LIST),
        LOCATION(BOTTOM_ITEM_LIST),
        BANNER_LIST(BOTTOM_ITEM_LIST),
        STORE_DETAIL(BOTTOM_ITEM_LIST),
        MY_COUPON(BOTTOM_ITEM_LIST)
    }

    @Composable
    fun NearPlaceScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "NearPlace Screen")
        }
    }

    @Composable
    fun ProfileScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Profile Screen")
        }
    }
}