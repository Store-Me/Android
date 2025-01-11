
package com.store_me.storeme.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.StoreNormalItem
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.ui.banner.BannerDetailScreen
import com.store_me.storeme.ui.banner.BannerListScreen
import com.store_me.storeme.ui.home.customer.CustomerHomeScreen
import com.store_me.storeme.ui.home.owner.OwnerHomeScreen
import com.store_me.storeme.ui.link.LinkSettingScreen
import com.store_me.storeme.ui.location.LocationScreen
import com.store_me.storeme.ui.my_menu.MyMenuScreen
import com.store_me.storeme.ui.mycoupon.MyCouponScreenWithBottomSheet
import com.store_me.storeme.ui.mystore.MyStoreScreenWithBottomSheet
import com.store_me.storeme.ui.near_place.NearPlaceScreen
import com.store_me.storeme.ui.notification.NotificationScreen
import com.store_me.storeme.ui.onboarding.OnboardingActivity
import com.store_me.storeme.ui.post.AddPostScreen
import com.store_me.storeme.ui.store_detail.StoreDetailScreen
import com.store_me.storeme.ui.store_setting.closed_day.ClosedDaySettingScreen
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponSettingScreen
import com.store_me.storeme.ui.store_setting.IntroSettingScreen
import com.store_me.storeme.ui.store_setting.LocationSettingScreen
import com.store_me.storeme.ui.store_setting.menu.MenuSettingScreen
import com.store_me.storeme.ui.store_setting.NewsSettingScreen
import com.store_me.storeme.ui.store_setting.NoticeSettingScreen
import com.store_me.storeme.ui.store_setting.opening_hours.OpeningHoursSettingScreen
import com.store_me.storeme.ui.store_setting.PhotoSettingScreen
import com.store_me.storeme.ui.store_setting.review.ReviewSettingScreen
import com.store_me.storeme.ui.store_setting.StoreSettingScreen
import com.store_me.storeme.ui.store_setting.story.StorySettingScreen
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponScreen
import com.store_me.storeme.ui.store_setting.coupon.edit.EditCouponScreen
import com.store_me.storeme.ui.store_setting.menu.add.AddMenuScreen
import com.store_me.storeme.ui.store_setting.menu.category.EditMenuCategoryScreen
import com.store_me.storeme.ui.store_setting.menu.category.MenuCategorySettingScreen
import com.store_me.storeme.ui.store_setting.menu.edit.EditMenuScreen
import com.store_me.storeme.ui.store_setting.profile.ProfileSettingScreen
import com.store_me.storeme.ui.store_talk.StoreTalkScreen
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.composition_locals.LocalAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: com.store_me.auth.Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StoreMeTheme {
                CompositionLocalProvider(
                    LocalAuth provides auth
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White),
                        color = Color.Transparent
                    ) {
                        val isLoggedIn by auth.isLoggedIn.collectAsState()
                        val accountType by auth.accountType.collectAsState()

                        if(!isLoggedIn) {
                            NavigateToOnboarding()
                        } else {
                            when(accountType){
                                AccountType.CUSTOMER -> { CustomerScreen() }
                                AccountType.OWNER -> { OwnerScreen() }
                            }
                        }
                    }
                }
            }
        }
    }
    @Composable
    private fun NavigateToOnboarding() {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            val intent = Intent(context, OnboardingActivity::class.java)
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }
    }

    @Composable
    private fun CustomerScreen() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            Box(Modifier.padding(it)) {
                CustomerNavigationGraph(navController)
            }
        }
    }

    @Composable
    private fun OwnerScreen() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            Box(Modifier.padding(it)) {
                OwnerNavigationGraph(navController)
            }
        }
    }

    @Composable
    fun CustomerNavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = CustomerBottomNavItem.CustomerHome.screenRoute){

            //기본 Bottom Item
            composable(CustomerBottomNavItem.CustomerHome.screenRoute) { CustomerHomeScreen(navController, locationViewModel = hiltViewModel()) }
            composable(CustomerBottomNavItem.Favorite.screenRoute) { MyStoreScreenWithBottomSheet() }
            composable(CustomerBottomNavItem.NearPlace.screenRoute) { NearPlaceScreen(navController, locationViewModel = hiltViewModel()) }
            composable(CustomerBottomNavItem.StoreTalk.screenRoute) { StoreTalkScreen(navController) }
            composable(CustomerBottomNavItem.Profile.screenRoute) { MyMenuScreen(navController) }

            //HOME > NOTIFICATION
            composable(USER_HOME + CustomerNavItem.NOTIFICATION.name) { NotificationScreen(navController) }
            //MY_MENU > NOTIFICATION
            composable(MY_MENU + CustomerNavItem.NOTIFICATION.name) { NotificationScreen(navController) }

            //HOME > MY_COUPON
            composable(USER_HOME + CustomerNavItem.MY_COUPON.name) { MyCouponScreenWithBottomSheet(navController) }
            //MY_MENU > MY_COUPON
            composable(MY_MENU + CustomerNavItem.MY_COUPON.name) { MyCouponScreenWithBottomSheet(navController) }

            //HOME > LOCATION
            composable(USER_HOME + CustomerNavItem.LOCATION.name) { LocationScreen(navController, locationViewModel = hiltViewModel()) }
            //NEAR PLACE > NOTIFICATION
            composable(NEAR_PLACE + CustomerNavItem.LOCATION.name) { LocationScreen(navController, locationViewModel = hiltViewModel()) }

            /**
             * Any To Banner (List / Detail)
             */
            composable(USER_HOME + CustomerNavItem.BANNER_LIST.name) { BannerListScreen(navController) }
            composable(NEAR_PLACE + CustomerNavItem.BANNER_LIST.name) { BannerListScreen(navController) }
            composable(MY_MENU + CustomerNavItem.BANNER_LIST.name) { BannerListScreen(navController) }

            composable(USER_HOME + CustomerNavItem.BANNER_DETAIL.name + "/{bannerId}") { backStackEntry ->
                val bannerId = backStackEntry.arguments?.getString("bannerId")
                BannerDetailScreen(navController, bannerId = bannerId ?: "")
            }
            composable(NEAR_PLACE + CustomerNavItem.BANNER_DETAIL.name + "/{bannerId}") { backStackEntry ->
                val bannerId = backStackEntry.arguments?.getString("bannerId")
                BannerDetailScreen(navController, bannerId = bannerId ?: "")
            }
            composable(MY_MENU + CustomerNavItem.BANNER_DETAIL.name + "/{bannerId}") { backStackEntry ->
                val bannerId = backStackEntry.arguments?.getString("bannerId")
                BannerDetailScreen(navController, bannerId = bannerId ?: "")
            }

            composable(USER_HOME + CustomerNavItem.STORE_DETAIL.name + "/{storeId}") { backStackEntry ->
                val storeId = backStackEntry.arguments?.getString("storeId")
                StoreDetailScreen(navController, storeId = storeId ?: "")
            }
        }
    }

    @Composable
    fun OwnerNavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = OwnerBottomNavItem.OwnerHome.screenRoute){
            //기본 Bottom Item
            composable(OwnerBottomNavItem.OwnerHome.screenRoute) { OwnerHomeScreen(navController) }
            composable(OwnerBottomNavItem.CustomerManagement.screenRoute) {  }
            composable(OwnerBottomNavItem.OwnerAdd.screenRoute) { AddPostScreen(navController) }
            composable(OwnerBottomNavItem.StoreTalk.screenRoute) {  }
            composable(OwnerBottomNavItem.StoreInfo.screenRoute) {  }

            /*
             * Start Screen 이 OWNER_HOME
             */
            //HOME > LINK_SETTING
            composable(OWNER_HOME + OwnerNavItem.LINK_SETTING) { LinkSettingScreen(navController) }
            //HOME > STORE_SETTING
            composable(OWNER_HOME + OwnerNavItem.STORE_SETTING) { StoreSettingScreen(navController) }

            //HOME > NORMAL
            composable(OWNER_HOME + StoreNormalItem.OPENING_HOURS) { OpeningHoursSettingScreen(navController) }
            composable(OWNER_HOME + StoreNormalItem.CLOSED_DAY) { ClosedDaySettingScreen(navController) }
            composable(OWNER_HOME + StoreNormalItem.LOCATION) { LocationSettingScreen(navController) }
            //HOME > HOME ITEM
            composable(OWNER_HOME + StoreHomeItem.NOTICE) { NoticeSettingScreen(navController) }
            composable(OWNER_HOME + StoreHomeItem.INTRO) { IntroSettingScreen(navController) }
            composable(OWNER_HOME + StoreHomeItem.PHOTO) { PhotoSettingScreen(navController) }
            composable(OWNER_HOME + StoreHomeItem.COUPON) { CouponSettingScreen(navController) }
            composable(OWNER_HOME + StoreHomeItem.MENU) { MenuSettingScreen(navController) }
            composable(OWNER_HOME + StoreHomeItem.MENU + "/{selectedMenuName}") { backStackEntry ->
                val selectedMenuName = backStackEntry.arguments?.getString("selectedMenuName")
                MenuSettingScreen(navController, selectedMenuName = selectedMenuName ?: "")
            }
            composable(OWNER_HOME + StoreHomeItem.STORY) { StorySettingScreen(navController) }
            composable(OWNER_HOME + StoreHomeItem.REVIEW) { ReviewSettingScreen(navController) }
            composable(OWNER_HOME + StoreHomeItem.NEWS) { NewsSettingScreen(navController) }

            composable(OWNER_HOME + OwnerNavItem.CREATE_COUPON + "/{selectedCouponType}") {backStackEntry ->
                val selectedCouponType = backStackEntry.arguments?.getString("selectedCouponType")
                CreateCouponScreen(navController, selectedCouponType = selectedCouponType ?: "")
            }

            composable(OWNER_HOME + OwnerNavItem.EDIT_COUPON + "/{selectedCouponId}") {backStackEntry ->
                val selectedCouponId = backStackEntry.arguments?.getString("selectedCouponId")
                EditCouponScreen(navController, selectedCouponId = selectedCouponId ?: "")
            }

            //HOME > MENU
            composable(OWNER_HOME + OwnerNavItem.ADD_MENU) { AddMenuScreen(navController) }
            composable(OWNER_HOME + OwnerNavItem.EDIT_MENU + "/{selectedMenuName}") { backStackEntry ->
                val selectedMenuName = backStackEntry.arguments?.getString("selectedMenuName")
                EditMenuScreen(navController, selectedMenuName = selectedMenuName ?: "")
            }
            composable(OWNER_HOME + OwnerNavItem.MENU_CATEGORY_SETTING) { MenuCategorySettingScreen(navController) }

            composable(OWNER_HOME + OwnerNavItem.EDIT_MENU_CATEGORY + "/{selectedCategoryName}") { backStackEntry ->
                val selectedCategoryName = backStackEntry.arguments?.getString("selectedCategoryName")
                EditMenuCategoryScreen(navController, selectedCategoryName = selectedCategoryName ?: "")
            }

            composable(OWNER_HOME + OwnerNavItem.EDIT_PROFILE) { ProfileSettingScreen(navController) }
        }
    }

    @Composable
    private fun BottomNavigationBar(navController: NavHostController) {
        val auth = LocalAuth.current

        val accountType by auth.accountType.collectAsState()

        val customerItems = listOf(
            CustomerBottomNavItem.CustomerHome,
            CustomerBottomNavItem.Favorite,
            CustomerBottomNavItem.NearPlace,
            CustomerBottomNavItem.StoreTalk,
            CustomerBottomNavItem.Profile
        )

        val ownerItems = listOf(
            OwnerBottomNavItem.OwnerHome,
            OwnerBottomNavItem.CustomerManagement,
            OwnerBottomNavItem.OwnerAdd,
            OwnerBottomNavItem.StoreTalk,
            OwnerBottomNavItem.StoreInfo
        )

        val items = when(accountType){
            AccountType.CUSTOMER -> customerItems
            AccountType.OWNER -> ownerItems
        }

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

                items.forEach { item ->
                    val isSelected =
                        when(accountType){
                            AccountType.CUSTOMER -> {
                                when {
                                    currentRoute == item.screenRoute -> true
                                    currentRoute?.startsWith(USER_HOME) == true && item.screenRoute == CustomerBottomNavItem.CustomerHome.screenRoute -> true
                                    currentRoute?.startsWith(FAVORITE) == true && item.screenRoute == CustomerBottomNavItem.Favorite.screenRoute -> true
                                    currentRoute?.startsWith(NEAR_PLACE) == true && item.screenRoute == CustomerBottomNavItem.NearPlace.screenRoute -> true
                                    currentRoute?.startsWith(STORE_TALK) == true && item.screenRoute == CustomerBottomNavItem.StoreTalk.screenRoute -> true
                                    currentRoute?.startsWith(MY_MENU) == true && item.screenRoute == CustomerBottomNavItem.Profile.screenRoute -> true
                                    else -> false
                                }
                            }
                            AccountType.OWNER -> {
                                when {
                                    currentRoute == item.screenRoute -> true
                                    currentRoute?.startsWith(OWNER_HOME) == true && item.screenRoute == OwnerBottomNavItem.OwnerHome.screenRoute -> true
                                    currentRoute?.startsWith(CUSTOMER_MANAGEMENT) == true && item.screenRoute == OwnerBottomNavItem.CustomerManagement.screenRoute -> true
                                    currentRoute?.startsWith(OWNER_ADD) == true && item.screenRoute == OwnerBottomNavItem.OwnerAdd.screenRoute -> true
                                    currentRoute?.startsWith(STORE_TALK) == true && item.screenRoute == OwnerBottomNavItem.StoreTalk.screenRoute -> true
                                    currentRoute?.startsWith(STORE_INFO) == true && item.screenRoute == OwnerBottomNavItem.StoreInfo.screenRoute -> true
                                    else -> false
                                }
                            }
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

    sealed class BottomNavItem(val title: Int, val icon: Int, val selectedIcon: Int, val screenRoute: String)

    sealed class CustomerBottomNavItem(title: Int, icon: Int, selectedIcon: Int, screenRoute: String): BottomNavItem(title, icon, selectedIcon, screenRoute) {
        data object CustomerHome : CustomerBottomNavItem(R.string.home, R.drawable.bottom_home, R.drawable.bottom_home_selected, USER_HOME)
        data object Favorite : CustomerBottomNavItem(R.string.favorite, R.drawable.bottom_favorite, R.drawable.bottom_favorite_selected, FAVORITE)
        data object NearPlace : CustomerBottomNavItem(R.string.near_place, R.drawable.bottom_nearplace, R.drawable.bottom_nearplace_selected, NEAR_PLACE)
        data object StoreTalk : CustomerBottomNavItem(R.string.store_talk, R.drawable.bottom_storetalk, R.drawable.bottom_storetalk_selected, STORE_TALK)
        data object Profile : CustomerBottomNavItem(R.string.profile, R.drawable.bottom_mymenu, R.drawable.bottom_mymenu_selected, MY_MENU)
    }

    sealed class OwnerBottomNavItem(title: Int, icon: Int, selectedIcon: Int, screenRoute: String): BottomNavItem(title, icon, selectedIcon, screenRoute) {
        data object OwnerHome : OwnerBottomNavItem(R.string.owner_home, R.drawable.bottom_owner_home, R.drawable.bottom_owner_home_selected, OWNER_HOME)
        data object CustomerManagement : OwnerBottomNavItem(R.string.customer_management, R.drawable.bottom_customer_management, R.drawable.bottom_customer_management_selected, CUSTOMER_MANAGEMENT)
        data object OwnerAdd : OwnerBottomNavItem(R.string.owner_add, R.drawable.bottom_owner_add, R.drawable.bottom_owner_add_selected, OWNER_ADD)
        data object StoreTalk : OwnerBottomNavItem(R.string.store_talk, R.drawable.bottom_storetalk, R.drawable.bottom_storetalk_selected, STORE_TALK)
        data object StoreInfo : OwnerBottomNavItem(R.string.store_info, R.drawable.bottom_store_info, R.drawable.bottom_store_info_selected, STORE_INFO)
    }

    enum class CustomerNavItem {
        NOTIFICATION,
        LOCATION,
        BANNER_LIST,
        BANNER_DETAIL,
        STORE_DETAIL,
        MY_COUPON
    }

    enum class OwnerNavItem {
        LINK_SETTING, STORE_SETTING, CREATE_COUPON, EDIT_COUPON, ADD_MENU, EDIT_MENU, MENU_CATEGORY_SETTING, EDIT_MENU_CATEGORY, EDIT_PROFILE
    }
}