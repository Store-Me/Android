@file:OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class,)

package com.store_me.storeme.ui.home.owner

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.naver.maps.geometry.LatLng
import com.store_me.storeme.R
import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.tab_menu.StoreTabMenu
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.LinkSection
import com.store_me.storeme.ui.component.ProfileImageWithBorder
import com.store_me.storeme.ui.component.StoreMeScrollableTabRow
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.ToastMessageUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.store_me.storeme.data.coupon.CouponData
import com.store_me.storeme.data.StampCouponData
import com.store_me.storeme.data.enums.StoreHomeItem
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponInfo
import com.store_me.storeme.ui.store_setting.stamp.RewardItem
import com.store_me.storeme.ui.store_setting.stamp.StampCouponItem
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.utils.DateTimeUtils

@Composable
fun OwnerHomeScreen(
    navController: NavController
) {
    val auth = LocalAuth.current
    val storeDataViewModel = LocalStoreDataViewModel.current

    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current

    val pagerState = rememberPagerState()

    val tabTitles = enumValues<StoreTabMenu>().map { it.displayName }

    val storeId by auth.storeId.collectAsState()

    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()
    val businessHours by storeDataViewModel.businessHours.collectAsState()
    val links by storeDataViewModel.links.collectAsState()

    val backgroundSectionHeight = remember { mutableStateOf(0) } //픽셀 단위
    val profileSectionHeight = remember { mutableStateOf(0) } //픽셀 단위


    LaunchedEffect(storeId) {
        if(storeId != null) {
            storeDataViewModel.getStoreData()
            storeDataViewModel.getStoreBusinessHours()
            storeDataViewModel.getStoreLinks()
            storeDataViewModel.getStoreNotice()
            storeDataViewModel.getStoreFeaturedImages()
            storeDataViewModel.getStoreMenus()
            storeDataViewModel.getStoreCoupons()
            storeDataViewModel.getStampCoupon()
        }
    }

    LaunchedEffect(storeInfoData?.storeLat, storeInfoData?.storeLng) {
        if(storeInfoData?.storeLat != null && storeInfoData?.storeLng != null) {
            storeDataViewModel.getStoreImage(LatLng(storeInfoData!!.storeLat!!, storeInfoData!!.storeLng!!))
        }
    }

    Scaffold(
        containerColor = White,
        content = { innerPadding -> // 컨텐츠 영역
            when(storeInfoData) {
                null -> {

                }

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        LazyColumn {
                            item {
                                Box(
                                    modifier = Modifier
                                        .background(color = White)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    BackgroundSection(
                                        imageUrl = storeInfoData!!.backgroundImage,
                                        modifier = Modifier
                                            .onGloballyPositioned {
                                                backgroundSectionHeight.value = it.size.height
                                            },
                                        showCanvas = storeInfoData!!.backgroundImage != null
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                    ) {
                                        Spacer(modifier = Modifier.height(with(LocalDensity.current) { (backgroundSectionHeight.value).toDp() - 64.dp }))

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 20.dp)
                                                .onGloballyPositioned {
                                                    profileSectionHeight.value = it.size.height
                                                },
                                            verticalAlignment = Alignment.Bottom
                                        ) {
                                            //프로필 이미지
                                            ProfileImageWithBorder(
                                                accountType = AccountType.OWNER,
                                                url = storeInfoData!!.storeProfileImage,
                                                modifier = Modifier
                                            )

                                            Spacer(modifier = Modifier.weight(1f))
                                            Column (
                                                modifier = Modifier
                                                    .padding(bottom = 16.dp)
                                            ) {
                                                //링크 정보
                                                LinkSection(
                                                    storeLink = links ?: emptyList(),
                                                    onShareClick = {  },
                                                    onEditClick = {
                                                        navController.navigate(OwnerRoute.LinkSetting.fullRoute)
                                                    },
                                                    accountType = AccountType.OWNER
                                                )
                                            }

                                        }

                                        StoreHomeInfoSection(
                                            storeInfoData = storeInfoData!!,
                                            businessHours = businessHours ?: BusinessHoursResponse(),
                                        ) {
                                            navController.navigate(it.route.fullRoute)
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            item {
                                DefaultHorizontalDivider(thickness = 8.dp)
                            }

                            stickyHeader {
                                StoreMeScrollableTabRow(pagerState = pagerState, tabTitles = tabTitles)
                            }

                            item {
                                OwnerHomeContentSection(navController = navController, pagerState = pagerState)
                            }
                        }
                    }
                }
            }
        }
    )



    BackHandler {
        val currentTime = System.currentTimeMillis()
        if(currentTime - backPressedTime < 2000){
            (context as? ComponentActivity)?.finishAffinity()
        } else {
            backPressedTime = currentTime
            ToastMessageUtils.showToast(context, R.string.backpress_message)
        }
    }
}

@Composable
fun OwnerHomeContentSection(
    navController: NavController,
    pagerState: PagerState
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val menuCategories by storeDataViewModel.menuCategories.collectAsState()
    val featuredImages by storeDataViewModel.featuredImages.collectAsState()
    val stampCoupon by storeDataViewModel.stampCoupon.collectAsState()
    val coupons by storeDataViewModel.coupons.collectAsState()
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    HorizontalPager(
        count = StoreTabMenu.entries.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) { page ->
        when(StoreTabMenu.entries[page]) {
            StoreTabMenu.HOME -> {
                OwnerStoreHomeSection(navController)
            }
            StoreTabMenu.NEWS -> {
                NewsScreen(navController)
            }
            StoreTabMenu.MENU -> {
                MenuListSection(menuCategories = menuCategories)
            }
            StoreTabMenu.COUPON -> {
                ActivateCouponSection(
                    storeInfoData = storeInfoData!!,
                    coupons = coupons
                )
            }
            StoreTabMenu.STAMP -> {
                stampCoupon?.let { StampCouponSection(stampCoupon = it) { homeItem ->
                    navController.navigate(homeItem.route.fullRoute)
                } }
            }
            StoreTabMenu.PHOTO -> {
                FeaturedImageSection(featuredImages = featuredImages)
            }
            StoreTabMenu.STORY -> {

            }
            StoreTabMenu.REVIEW -> {

            }
        }
    }
}

@Composable
fun StampCouponSection(
    stampCoupon: StampCouponData?,
    onClick: (StoreHomeItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        if(stampCoupon != null) {

            StampCouponItem(stampCoupon = stampCoupon)

            Spacer(modifier = Modifier.height(12.dp))

            when(stampCoupon.rewardInterval) {
                5 -> {
                    RewardItem(indexText = "보상 1",rewardText = stampCoupon.rewardFor5 ?: "올바르지 않은 값입니다.")
                    Spacer(modifier = Modifier.height(8.dp))
                    RewardItem(indexText = "보상 2",rewardText = stampCoupon.rewardFor10)
                }
                10 -> {
                    RewardItem(indexText = "보상 2",rewardText = stampCoupon.rewardFor10)
                }
            }

        } else {
            Text(
                text = "발급중인 스탬프 쿠폰이 없습니다. 스탬프를 새로 생성해보세요.",
                style = storeMeTextStyle(FontWeight.Normal, 0),
                color = GuideColor
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        DefaultButton(
            buttonText = "스탬프 쿠폰 수정",
            diffValue = 2,
            colors = ButtonDefaults.buttonColors(
                containerColor = SubHighlightColor,
                contentColor = Color.Black
            )
        ) {
            onClick(StoreHomeItem.STAMP_COUPON)
        }
    }
}

/**
 * Coupon List
 */
@Composable
fun ActivateCouponSection(
    storeInfoData: StoreInfoData,
    coupons: List<CouponData>
) {
    val filteredCoupons = coupons.filter { DateTimeUtils.isAfterDatetime(it.dueDate) }.chunked(2)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        filteredCoupons.forEach { coupon ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CouponInfo(
                    coupon = coupon.first(),
                    modifier = Modifier
                        .weight(1f),
                    storeName = storeInfoData.storeName
                )

                if(coupon.size != 1) {
                    CouponInfo(
                        coupon = coupon.last(),
                        modifier = Modifier
                            .weight(1f),
                        storeName = storeInfoData.storeName
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * StoreHome MenuList
 */
@Composable
fun MenuListSection(menuCategories: List<MenuCategoryData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        menuCategories.forEach { menuCategory ->
            if(menuCategory.menus.isNotEmpty()) {
                Text(
                    text = menuCategory.categoryName,
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )

                DefaultHorizontalDivider()

                menuCategory.menus.forEach { menu ->
                    MenuItem(menu)

                    DefaultHorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun FeaturedImageSection(featuredImages: List<FeaturedImageData>) {

}

@Composable
fun NewsScreen(navController: NavController) {

}