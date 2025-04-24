package com.store_me.storeme.ui.main.navigation.owner

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.store_me.storeme.ui.home.owner.OwnerHomeScreen
import com.store_me.storeme.ui.link.LinkSettingScreen
import com.store_me.storeme.ui.post.SelectPostTypeScreen
import com.store_me.storeme.ui.post.label.LabelSettingScreen
import com.store_me.storeme.ui.store_info.StoreInfoScreen
import com.store_me.storeme.ui.store_setting.NewsSettingScreen
import com.store_me.storeme.ui.store_setting.StoreSettingScreen
import com.store_me.storeme.ui.store_setting.business_hours.BusinessHoursSettingScreen
import com.store_me.storeme.ui.store_setting.coupon.management.CouponManagementScreen
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
import com.store_me.storeme.ui.store_setting.stamp.StampCouponCreateScreen
import com.store_me.storeme.ui.store_setting.stamp.StampCouponSettingScreen
import com.store_me.storeme.ui.store_setting.story.management.StoryManagementScreen
import com.store_me.storeme.ui.store_setting.story.setting.StorySettingScreen
import com.store_me.storeme.ui.store_setting.story.setting.StorySettingViewModel

@Composable
fun OwnerHomeNavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = OwnerRoute.Home.fullRoute){
        composable(OwnerRoute.Home.fullRoute) { OwnerHomeScreen(navController) }

        //기본 프로필
        composable(OwnerRoute.IntroSetting.fullRoute) { IntroSettingScreen(navController) }
        composable(OwnerRoute.BusinessHoursSetting.fullRoute) { BusinessHoursSettingScreen(navController) }
        composable(OwnerRoute.PhoneNumberSetting.fullRoute) { PhoneNumberSettingScreen(navController) }
        composable(OwnerRoute.LocationSetting.fullRoute) { LocationSettingScreen(navController) }
        composable(OwnerRoute.ProfileEdit.fullRoute) { ProfileSettingScreen(navController) }
        composable(OwnerRoute.StoreManagement.fullRoute) { StoreSettingScreen(navController) }

        //링크 관리
        composable(OwnerRoute.LinkSetting.fullRoute) { LinkSettingScreen(navController) }
        //공지 관리
        composable(OwnerRoute.NoticeSetting.fullRoute) { NoticeSettingScreen(navController) }
        //대표 이미지 관리
        composable(OwnerRoute.FeaturedImageSetting.fullRoute) { FeaturedImageSettingScreen(navController) }

        /**
         * 메뉴 관련 화면
         */
        //메뉴 관리
        composable(OwnerRoute.MenuSetting(null).fullRoute) { backStackEntry ->
            val sharedMenuSettingViewModel: MenuSettingViewModel = hiltViewModel(backStackEntry)

            MenuSettingScreen(
                navController = navController,
                menuSettingViewModel = sharedMenuSettingViewModel
            )
        }
        //메뉴 관리 + 선택된 메뉴
        composable(OwnerRoute.MenuSetting(null).fullRoute + "/{selectedMenuName}") { backStackEntry ->
            val selectedMenuName = backStackEntry.arguments?.getString("selectedMenuName")
            val sharedMenuSettingViewModel: MenuSettingViewModel = hiltViewModel(backStackEntry)

            MenuSettingScreen(
                navController = navController,
                menuSettingViewModel = sharedMenuSettingViewModel,
                selectedMenuName = selectedMenuName ?: "")
        }
        //메뉴 추가
        composable(OwnerRoute.MenuManagement(null).fullRoute) { backStackEntry ->
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
        //메뉴 수정
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
        //메뉴 카테고리
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
        //특정 메뉴 카테고리
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

        /**
         * 쿠폰 관련 화면
         */
        //쿠폰 관리
        composable(OwnerRoute.CouponSetting.fullRoute) {
            CouponSettingScreen(navController)
        }
        //쿠폰 생성
        composable(OwnerRoute.CouponCreate.template()) { backStackEntry ->
            val selectedCouponType = backStackEntry.arguments?.getString("selectedCouponType")
            CouponManagementScreen(navController, selectedCouponType = selectedCouponType ?: "", couponId = null)
        }
        //쿠폰 수정
        composable(OwnerRoute.CouponEdit.template()) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString("couponId") ?: ""
            val selectedCouponType = backStackEntry.arguments?.getString("selectedCouponType") ?: ""
            CouponManagementScreen(navController, selectedCouponType = selectedCouponType, couponId = couponId)
        }

        /**
         * 스탬프 관련 화면
         */
        //스탬프 관리
        composable(OwnerRoute.StampCouponSetting.fullRoute) {
            StampCouponSettingScreen(navController)
        }
        //스탬프 생성
        composable(OwnerRoute.StampCouponCreate.fullRoute) {
            StampCouponCreateScreen(navController)
        }

        /**
         * 스토리 관련 화면
         */
        //스토리 관리
        composable(OwnerRoute.StorySetting.fullRoute) { backStackEntry ->
            val sharedStorySettingViewModel: StorySettingViewModel = hiltViewModel(backStackEntry)

            StorySettingScreen(navController, storySettingViewModel = sharedStorySettingViewModel)
        }
        //스토리 추가
        composable(OwnerRoute.StoryManagement.fullRoute) {
            val sharedStorySettingViewModel: StorySettingViewModel =
                if(navController.previousBackStackEntry != null)
                    hiltViewModel(navController.previousBackStackEntry!!)
                else
                    hiltViewModel()

            StoryManagementScreen(navController, storySettingViewModel = sharedStorySettingViewModel)
        }

        composable(OwnerRoute.ReviewSetting.fullRoute) { ReviewSettingScreen(navController) }
        composable(OwnerRoute.NewsSetting.fullRoute) { NewsSettingScreen(navController) }
    }
}

@Composable
fun OwnerCustomerManagementNavigationGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = OwnerRoute.CustomerManagement.fullRoute
    ) {
        composable(OwnerRoute.CustomerManagement.fullRoute) {

        }
    }
}

@Composable
fun OwnerAddNavigationGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = OwnerRoute.Add.fullRoute
    ) {
        composable(OwnerRoute.Add.fullRoute) {
            SelectPostTypeScreen(navController)
        }

        composable(OwnerRoute.LabelSetting.fullRoute) { LabelSettingScreen(navController) }
    }
}

@Composable
fun OwnerStoreTalkNavigationGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = OwnerRoute.StoreTalk.fullRoute
    ) {
        composable(OwnerRoute.StoreTalk.fullRoute) {

        }
    }
}

@Composable
fun OwnerStoreInfoNavigationGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = OwnerRoute.StoreInfo.fullRoute
    ) {
        composable(OwnerRoute.StoreInfo.fullRoute) { StoreInfoScreen(navController) }
    }
}