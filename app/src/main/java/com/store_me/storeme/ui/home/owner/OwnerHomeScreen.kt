@file:OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.home.owner

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.naver.maps.geometry.LatLng
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.tab_menu.StoreTabMenu
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.LinkSection
import com.store_me.storeme.ui.component.ProfileImageWithBorder
import com.store_me.storeme.ui.component.StoreMeScrollableTabRow
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.utils.ToastMessageUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

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
            storeDataViewModel.getStoreData(storeId = storeId!!)
            storeDataViewModel.getStoreBusinessHours(storeId = storeId!!)
            storeDataViewModel.getStoreLinks(storeId = storeId!!)
            storeDataViewModel.getStoreNotice(storeId = storeId!!)
            storeDataViewModel.getStoreFeaturedImages(storeId = storeId!!)
            storeDataViewModel.getStoreMenus(storeId = storeId!!)
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
fun OwnerHomeContentSection(navController: NavController, pagerState: PagerState) {
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

            }
            StoreTabMenu.COUPON -> {

            }
            StoreTabMenu.PHOTO -> {

            }
            StoreTabMenu.STORY -> {

            }
            StoreTabMenu.REVIEW -> {

            }
        }
    }
}

@Composable
fun NewsScreen(navController: NavController) {

}