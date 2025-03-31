package com.store_me.storeme.ui.main.navigation.owner

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.store_me.storeme.ui.home.owner.OwnerHomeScreen
import com.store_me.storeme.ui.link.LinkSettingScreen
import com.store_me.storeme.ui.post.SelectPostTypeScreen
import com.store_me.storeme.ui.store_info.StoreInfoScreen
import com.store_me.storeme.ui.store_setting.NewsSettingScreen
import com.store_me.storeme.ui.store_setting.StoreSettingScreen
import com.store_me.storeme.ui.store_setting.business_hours.BusinessHoursSettingScreen
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponScreen
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponSettingScreen
import com.store_me.storeme.ui.store_setting.image.FeaturedImageSettingScreen
import com.store_me.storeme.ui.store_setting.intro.IntroSettingScreen
import com.store_me.storeme.ui.store_setting.location.LocationSettingScreen
import com.store_me.storeme.ui.store_setting.menu.MenuSettingScreen
import com.store_me.storeme.ui.store_setting.menu.MenuSettingViewModel
import com.store_me.storeme.ui.store_setting.menu.category.MenuCategoryManagementScreen
import com.store_me.storeme.ui.store_setting.menu.category.MenuCategorySettingScreen
import com.store_me.storeme.ui.store_setting.menu.category.MenuCategorySettingViewModel
import com.store_me.storeme.ui.store_setting.menu.management.MenuManagementScreen
import com.store_me.storeme.ui.store_setting.notice.NoticeSettingScreen
import com.store_me.storeme.ui.store_setting.phone_number.PhoneNumberSettingScreen
import com.store_me.storeme.ui.store_setting.profile.ProfileSettingScreen
import com.store_me.storeme.ui.store_setting.review.ReviewSettingScreen
import com.store_me.storeme.ui.store_setting.story.StorySettingScreen

@Composable
fun OwnerNavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = OwnerRoute.Home.fullRoute){
        //Bottom Navigation Screen
        composable(OwnerRoute.Home.fullRoute) { OwnerHomeScreen(navController) }
        composable(OwnerRoute.CustomerManagement.fullRoute) {

        }
        composable(OwnerRoute.Add.fullRoute) { SelectPostTypeScreen(navController) }
        composable(OwnerRoute.StoreTalk.fullRoute) {

        }
        composable(OwnerRoute.StoreInfo.fullRoute) { StoreInfoScreen(navController) }

        //기본 프로필
        composable(OwnerRoute.IntroSetting.fullRoute) { IntroSettingScreen(navController) }
        composable(OwnerRoute.BusinessHoursSetting.fullRoute) { BusinessHoursSettingScreen(navController) }
        composable(OwnerRoute.PhoneNumberSetting.fullRoute) { PhoneNumberSettingScreen(navController) }
        composable(OwnerRoute.LocationSetting.fullRoute) { LocationSettingScreen(navController) }
        composable(OwnerRoute.ProfileEdit.fullRoute) { ProfileSettingScreen(navController) }
        composable(OwnerRoute.StoreManagement.fullRoute) { StoreSettingScreen(navController) }

        composable(OwnerRoute.LinkSetting.fullRoute) { LinkSettingScreen(navController) }

        composable(OwnerRoute.NoticeSetting.fullRoute) { NoticeSettingScreen(navController) }
        composable(OwnerRoute.FeaturedImageSetting.fullRoute) { FeaturedImageSettingScreen(navController) }

        //메뉴
        composable(OwnerRoute.MenuSetting(null).fullRoute) { backStackEntry ->
            val sharedMenuSettingViewModel: MenuSettingViewModel = hiltViewModel(backStackEntry)

            MenuSettingScreen(
                navController = navController,
                menuSettingViewModel = sharedMenuSettingViewModel
            )
        }
        composable(OwnerRoute.MenuSetting(null).fullRoute + "/{selectedMenuName}") { backStackEntry ->
            val selectedMenuName = backStackEntry.arguments?.getString("selectedMenuName")
            val sharedMenuSettingViewModel: MenuSettingViewModel = hiltViewModel(backStackEntry)

            MenuSettingScreen(
                navController = navController,
                menuSettingViewModel = sharedMenuSettingViewModel,
                selectedMenuName = selectedMenuName ?: "")
        }

        composable(OwnerRoute.MenuManagement(null).fullRoute) { backStackEntry ->
            //viewModel 공유
            val sharedMenuSettingViewModel: MenuSettingViewModel =
                if(navController.previousBackStackEntry != null)
                    hiltViewModel(navController.previousBackStackEntry!!)
                else
                    hiltViewModel()

            MenuManagementScreen(
                navController = navController,
                selectedMenuName = "",
                menuSettingViewModel = sharedMenuSettingViewModel
            )
        }
        composable(OwnerRoute.MenuManagement(null).fullRoute + "/{selectedMenuName}") { backStackEntry ->
            val selectedMenuName = backStackEntry.arguments?.getString("selectedMenuName")
            val sharedMenuSettingViewModel: MenuSettingViewModel =
                if(navController.previousBackStackEntry != null)
                    hiltViewModel(navController.previousBackStackEntry!!)
                else
                    hiltViewModel()

            MenuManagementScreen(
                navController = navController,
                selectedMenuName = selectedMenuName ?: "",
                menuSettingViewModel = sharedMenuSettingViewModel
            )
        }

        composable(OwnerRoute.MenuCategorySetting.fullRoute) { backStackEntry ->
            //viewModel 공유
            val sharedMenuCategorySettingViewModel: MenuCategorySettingViewModel = hiltViewModel(backStackEntry)
            val sharedMenuSettingViewModel: MenuSettingViewModel =
                if(navController.previousBackStackEntry != null)
                    hiltViewModel(navController.previousBackStackEntry!!)
                else
                    hiltViewModel()

            MenuCategorySettingScreen(
                navController = navController,
                menuSettingViewModel = sharedMenuSettingViewModel,
                menuCategorySettingViewModel = sharedMenuCategorySettingViewModel
            )
        }
        composable(OwnerRoute.MenuCategoryManagement(null).fullRoute + "/{selectedCategoryName}") { backStackEntry ->
            val selectedCategoryName = backStackEntry.arguments?.getString("selectedCategoryName")

            val sharedMenuCategorySettingViewModel: MenuCategorySettingViewModel =
                if(navController.previousBackStackEntry != null)
                    hiltViewModel(navController.previousBackStackEntry!!)
                else
                    hiltViewModel()

            MenuCategoryManagementScreen(
                navController = navController,
                selectedCategoryName = selectedCategoryName ?: "",
                menuCategorySettingViewModel = sharedMenuCategorySettingViewModel
            )
        }

        //쿠폰
        composable(OwnerRoute.CouponSetting.fullRoute) {
            CouponSettingScreen(navController)
        }
        composable(OwnerRoute.CouponCreate(null).fullRoute + "/{selectedCouponType}") { backStackEntry ->
            val selectedCouponType = backStackEntry.arguments?.getString("selectedCouponType")
            CreateCouponScreen(navController, selectedCouponType = selectedCouponType ?: "")
        }

        composable(OwnerRoute.StorySetting.fullRoute) { StorySettingScreen(navController) }
        composable(OwnerRoute.ReviewSetting.fullRoute) { ReviewSettingScreen(navController) }
        composable(OwnerRoute.ReviewSetting.fullRoute) { NewsSettingScreen(navController) }
    }
}