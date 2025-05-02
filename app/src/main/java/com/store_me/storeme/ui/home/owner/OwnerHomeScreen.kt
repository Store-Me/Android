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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.tab_menu.StoreTabMenu
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.LinkSection
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.component.StoreMeScrollableTabRow
import com.store_me.storeme.ui.home.owner.tab.CouponTab
import com.store_me.storeme.ui.home.owner.tab.FeaturedImageTab
import com.store_me.storeme.ui.home.owner.tab.MenuTab
import com.store_me.storeme.ui.home.owner.tab.PostTab
import com.store_me.storeme.ui.home.owner.tab.ReviewTab
import com.store_me.storeme.ui.home.owner.tab.StampTab
import com.store_me.storeme.ui.home.owner.tab.StoreHomeTab
import com.store_me.storeme.ui.home.owner.tab.StoryTab
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.store_setting.post.PostViewModel
import com.store_me.storeme.ui.store_setting.review.ReviewViewModel
import com.store_me.storeme.ui.store_setting.story.setting.StoryViewModel
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.TEXT_ROUNDING_VALUE
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun OwnerHomeScreen(
    navController: NavController,
    storyViewModel: StoryViewModel,
    reviewViewModel: ReviewViewModel,
    postViewModel: PostViewModel
) {
    val auth = LocalAuth.current
    val storeDataViewModel = LocalStoreDataViewModel.current

    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val pagerState = rememberPagerState(pageCount = { StoreTabMenu.entries.size })

    val tabTitles = enumValues<StoreTabMenu>().map { it.displayName }

    val storeId by auth.storeId.collectAsState()
    val lastLoadedStoreId by storeDataViewModel.lastLoadedStoreId.collectAsState()

    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()
    val businessHours by storeDataViewModel.businessHours.collectAsState()
    val links by storeDataViewModel.links.collectAsState()

    val backgroundSectionHeight = remember { mutableStateOf(0) } //픽셀 단위
    val profileSectionHeight = remember { mutableStateOf(0) } //픽셀 단위

    //스토어 정보 조회
    LaunchedEffect(storeId) {
        if(lastLoadedStoreId != storeId) {
            storeDataViewModel.updateLastLoadedStoreId(storeId)
            storeDataViewModel.getStoreData()
            storeDataViewModel.getStoreBusinessHours()
            storeDataViewModel.getStoreLinks()
            storeDataViewModel.getStoreNotice()
            storeDataViewModel.getStoreFeaturedImages()
            storeDataViewModel.getStoreMenus()
            storeDataViewModel.getStoreCoupons()
            storeDataViewModel.getStampCoupon()

            //게시글 조회
            storeDataViewModel.getLabels()
            postViewModel.getNormalPost(null)   //전체 게시글 조회

            //첫 페이지 스토리 조회
            storyViewModel.getStoreStories()

            //리뷰 조회
            reviewViewModel.getReviewCount()
            reviewViewModel.getReviews()
        }
    }

    LaunchedEffect(listState, pagerState.currentPage) {
        snapshotFlow {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex to totalItemCount
        }.distinctUntilChanged()
            .collect { (lastVisible, total) ->
                if (pagerState.currentPage == StoreTabMenu.STORY.ordinal && lastVisible != null && lastVisible >= total - 1) {
                    storyViewModel.getStoreStories()
                }
            }
    }

    Scaffold(
        containerColor = Color.White,
        content = { innerPadding -> // 컨텐츠 영역
            when(storeInfoData) {
                null -> {

                }

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        LazyColumn(
                            state = listState
                        ) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .background(color = Color.White)
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
                                            Box(
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .background(color = Color.White, shape = RoundedCornerShape(TEXT_ROUNDING_VALUE))
                                                    .clip(shape = RoundedCornerShape(TEXT_ROUNDING_VALUE))
                                                    .padding(4.dp),
                                            ) {
                                                ProfileImage(
                                                    accountType = AccountType.OWNER,
                                                    url = storeInfoData!!.storeProfileImage,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .clip(shape = RoundedCornerShape(TEXT_ROUNDING_VALUE))
                                                )
                                            }

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

                                        StoreInfoSection(
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
                                OwnerHomeContentSection(
                                    navController = navController,
                                    pagerState = pagerState,
                                    storyViewModel = storyViewModel,
                                    reviewViewModel = reviewViewModel,
                                    postViewModel = postViewModel
                                )
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
            scope.launch {
                ErrorEventBus.errorFlow.emit(context.getString(R.string.backpress_message))
            }
        }
    }
}

@Composable
fun OwnerHomeContentSection(
    navController: NavController,
    pagerState: PagerState,
    storyViewModel: StoryViewModel,
    reviewViewModel: ReviewViewModel,
    postViewModel: PostViewModel
) {
    val scope = rememberCoroutineScope()
    val storeDataViewModel = LocalStoreDataViewModel.current

    val menuCategories by storeDataViewModel.menuCategories.collectAsState()
    val featuredImages by storeDataViewModel.featuredImages.collectAsState()
    val stampCoupon by storeDataViewModel.stampCoupon.collectAsState()
    val coupons by storeDataViewModel.coupons.collectAsState()
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val labels by storeDataViewModel.labels.collectAsState()

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.Top,
        userScrollEnabled = false
    ) { page ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            when(StoreTabMenu.entries[page]) {
                StoreTabMenu.HOME -> {
                    StoreHomeTab(
                        navController,
                        storyViewModel = storyViewModel,
                        reviewViewModel = reviewViewModel,
                        postViewModel = postViewModel
                    )
                }
                StoreTabMenu.POST -> {
                    PostTab(
                        storeInfoData = storeInfoData!!,
                        labels = labels,
                        postViewModel = postViewModel
                    )
                }
                StoreTabMenu.MENU -> {
                    MenuTab(menuCategories = menuCategories)
                }
                StoreTabMenu.COUPON -> {
                    CouponTab(
                        storeInfoData = storeInfoData!!,
                        coupons = coupons
                    )
                }
                StoreTabMenu.STAMP -> {
                    StampTab(stampCoupon = stampCoupon)
                }
                StoreTabMenu.FEATURED_IMAGES -> {
                    FeaturedImageTab(
                        storeInfoData = storeInfoData!!,
                        featuredImages = featuredImages
                    )
                }
                StoreTabMenu.STORY -> {
                    StoryTab(
                        storyViewModel = storyViewModel
                    )
                }
                StoreTabMenu.REVIEW -> {
                    ReviewTab(
                        reviewViewModel = reviewViewModel,
                        onClickMenu = {
                            scope.launch {
                                pagerState.animateScrollToPage(StoreTabMenu.MENU.ordinal)
                            }
                        },
                        onBack = {
                            scope.launch {
                                pagerState.animateScrollToPage(StoreTabMenu.HOME.ordinal)
                            }
                        }
                    )
                }
            }
        }
    }
}