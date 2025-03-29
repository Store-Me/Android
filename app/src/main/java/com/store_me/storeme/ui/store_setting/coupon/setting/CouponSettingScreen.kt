@file:OptIn(ExperimentalPagerApi::class, ExperimentalSharedTransitionApi::class)

package com.store_me.storeme.ui.store_setting.coupon.setting

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.CouponAvailable
import com.store_me.storeme.data.CouponQuantity
import com.store_me.storeme.data.CouponType
import com.store_me.storeme.data.OwnerCouponDetailData
import com.store_me.storeme.ui.component.StoreMeTabContent
import com.store_me.storeme.ui.component.StoreMeTabRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.store_setting.coupon.detail.OwnerCouponDetailScreen
import com.store_me.storeme.ui.theme.CouponAvailableTextColor
import com.store_me.storeme.ui.theme.CouponCardColor
import com.store_me.storeme.ui.theme.CouponExpiredAvailableBoxColor
import com.store_me.storeme.ui.theme.CouponExpiredTextColor
import com.store_me.storeme.ui.theme.CreateCouponArrowColor
import com.store_me.storeme.ui.theme.SelectedSortTypeColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.absoluteValue

val LocalCouponSettingViewModel = staticCompositionLocalOf<CouponSettingViewModel> {
    error("No LocalCouponSettingViewModel provided")
}

@Composable
fun CouponSettingScreen(
    navController: NavController,
    couponSettingViewModel: CouponSettingViewModel = viewModel()
) {
    val pagerState = rememberPagerState()

    val tabTitles = enumValues<CouponSettingViewModel.CouponTabTitles>().map { it.displayName }

    var showDetails by remember { mutableStateOf(false) }
    var selectedCouponId = ""


    CompositionLocalProvider(LocalCouponSettingViewModel provides couponSettingViewModel) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = showDetails,
                label = ""
            ) { targetState ->
                if(!targetState) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = White,
                        topBar = { TitleWithDeleteButton(
                            title = "쿠폰 관리"
                        ) {
                            navController.popBackStack()
                        } },
                        content = { innerPadding ->
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                            ) {
                                StoreMeTabRow(pagerState = pagerState, tabTitles = tabTitles)

                                StoreMeTabContent(tabTitles = tabTitles, state = pagerState) {
                                    when(it){
                                        CouponSettingViewModel.CouponTabTitles.CREATE.ordinal -> {
                                            CreateCouponSection(navController)
                                        }

                                        CouponSettingViewModel.CouponTabTitles.LIST.ordinal -> {
                                            CouponListSection(
                                                navController,
                                                animatedVisibilityScope = this@AnimatedContent,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            ) { couponId ->
                                                selectedCouponId = couponId
                                                showDetails = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                } else {
                    OwnerCouponDetailScreen(
                        selectedCouponId = selectedCouponId,
                        animatedVisibilityScope = this@AnimatedContent,
                        sharedTransitionScope = this@SharedTransitionLayout
                    ) {
                        showDetails = false
                    }
                }
            }
        }


    }
}

@Composable
fun CreateCouponSection(navController: NavController) {

    LazyColumn {
        item { GuideImageSection() }

        item {
            CreateCouponButton(CouponType.DISCOUNT) {
                navController.navigate(OwnerRoute.CouponCreate(CouponType.DISCOUNT.name).fullRoute)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            CreateCouponButton(CouponType.GIVEAWAY) {
                navController.navigate(OwnerRoute.CouponCreate(CouponType.GIVEAWAY.name).fullRoute)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            CreateCouponButton(CouponType.OTHER) {
                navController.navigate(OwnerRoute.CouponCreate(CouponType.OTHER.name).fullRoute)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun GuideImageSection() {
    val guideImageList = listOf(R.drawable.coupon_guid_image_1, R.drawable.coupon_guid_image_2, R.drawable.coupon_guid_image_3, R.drawable.coupon_guid_image_4)
    val pagerState = rememberPagerState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HorizontalPager(
            count = guideImageList.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.1f),
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffset
            val alpha = 1f - pageOffset.absoluteValue.coerceIn(0f, 0.3f)
            val scale = 1f - (0.1f * pageOffset.absoluteValue.coerceIn(0f, 1f))

            Image(
                painter = painterResource(id = guideImageList[page]),
                contentDescription = "$page 번째 가이드 이미지",
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1.0f)
                    .alpha(alpha)
                    .scale(scale)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page)
                            }
                        },
                        interactionSource = null,
                        indication = null
                    )
            )
        }
    }
}

@Composable
fun CreateCouponButton(couponType: CouponType, onClick: () -> Unit) {
    val (iconId, text) = when(couponType) {
        CouponType.DISCOUNT -> {
            Pair(R.drawable.ic_coupon_discount, "할인 쿠폰 만들기")
        }
        CouponType.GIVEAWAY -> {
            Pair(R.drawable.ic_coupon_heart, "증정 쿠폰 만들기")
        }
        CouponType.OTHER -> {
            Pair(R.drawable.ic_coupon_star, "기타 혜택 쿠폰 만들기")
        }
    }

    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SubHighlightColor,
            contentColor = Black
        ),
        contentPadding = PaddingValues(20.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "쿠폰 아이콘",
            modifier = Modifier
                .size(24.dp),
            tint = Unspecified
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "이동 아이콘",
            modifier = Modifier
                .size(18.dp),
            tint = CreateCouponArrowColor
        )
    }
}

@Composable
fun CouponListSection(navController: NavController, animatedVisibilityScope: AnimatedVisibilityScope, sharedTransitionScope: SharedTransitionScope, onDetail: (String) -> Unit) {
    val couponDetailList by Auth.couponDetailList.collectAsState()

    LazyColumn (
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(couponDetailList) { index, item ->
            CouponItem(
                item = item,
                onAddNews = {  },
                onEditCoupon = { /*NavigationUtils().navigateOwnerNav(navController, MainActivity.OwnerNavItem.EDIT_COUPON, additionalData = it)*/ },
                onDetail = { onDetail(item.couponInfoData.couponId) },
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            )
        }
    }
}

@Composable
fun CouponItem(
    item: OwnerCouponDetailData,
    onAddNews: () -> Unit,
    onEditCoupon: (String) -> Unit,
    onDetail: () -> Unit, animatedVisibilityScope: AnimatedVisibilityScope, sharedTransitionScope: SharedTransitionScope,
){
    with(sharedTransitionScope) {
        CouponInfo(item, modifier = Modifier.sharedElement(rememberSharedContentState(key = item.couponInfoData.couponId), animatedVisibilityScope))
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(color = CouponCardColor)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "소식에 넣기",
            style = storeMeTextStyle(FontWeight.ExtraBold, 0),
            modifier = Modifier
                .weight(1f)
                .height(
                    SizeUtils.textSizeToDp(
                        density = LocalDensity.current,
                        diffValue = 0,
                        offset = 40
                    )
                )
                .clickable {
                    onAddNews()
                }
                .wrapContentHeight(Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )

        Text(
            text = "쿠폰 수정",
            style = storeMeTextStyle(FontWeight.ExtraBold, 0),
            modifier = Modifier
                .weight(1f)
                .height(
                    SizeUtils.textSizeToDp(
                        density = LocalDensity.current,
                        diffValue = 0,
                        offset = 40
                    )
                )
                .clickable {
                    onEditCoupon(item.couponInfoData.couponId)
                }
                .wrapContentHeight(Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )

        Text(
            text = "쿠폰 상세보기",
            style = storeMeTextStyle(FontWeight.ExtraBold, 0),
            modifier = Modifier
                .weight(1f)
                .height(
                    SizeUtils.textSizeToDp(
                        density = LocalDensity.current,
                        diffValue = 0,
                        offset = 40
                    )
                )
                .clickable {
                    onDetail()
                }
                .wrapContentHeight(Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CouponInfo(item: OwnerCouponDetailData, modifier: Modifier = Modifier) {
    val date = LocalDate.of(item.couponInfoData.dueDate.year, item.couponInfoData.dueDate.month, item.couponInfoData.dueDate.day)

    val isActivate = date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now())

    val textColor = if(isActivate) Black else CouponExpiredTextColor

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(width = 2.dp, color = CouponCardColor, shape = RoundedCornerShape(15.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActivateText(isActivate)

                    if(item.couponInfoData.quantity is CouponQuantity.Limit)
                        Text(
                            text = "${(item.couponInfoData.quantity as CouponQuantity.Limit).quantity - item.usedCouponData.receivedCount}개 남음",
                            style = storeMeTextStyle(FontWeight.ExtraBold, -2),
                            color = textColor
                        )
                }

                Text(
                    text = item.couponInfoData.name,
                    style = storeMeTextStyle(FontWeight.ExtraBold, 4),
                    color = textColor
                )

                AvailableText(item.couponInfoData.available, isActivate)

                Text(
                    text = "${item.couponInfoData.dueDate.year}년 ${item.couponInfoData.dueDate.month}월 ${item.couponInfoData.dueDate.day}일 까지",
                    style = storeMeTextStyle(FontWeight.Bold, -2),
                    color = textColor
                )
            }

            Column(
                modifier = Modifier
                    .padding(end = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                AsyncImage(
                    model = item.couponInfoData.image,
                    contentDescription = "쿠폰 이미지",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .alpha(0.5f),
                    error = painterResource(id = R.drawable.store_null_image)
                )
            }

        }
    }
}

@Composable
fun ActivateText(isActivate: Boolean) {

    Box(
        modifier = Modifier
            .background(
                color = if(isActivate) SelectedSortTypeColor else CouponExpiredTextColor,
                shape = CircleShape
            )
    ) {
        Text(
            text = if(isActivate) "발급중" else "만료",
            style = storeMeTextStyle(FontWeight.Bold, 0),
            color = White,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun AvailableText(available: CouponAvailable, isActivate: Boolean) {
    val text = when(available) {
        CouponAvailable.REPEAT -> { "재방문 손님 전용" }
        CouponAvailable.ALL -> { "모든 손님" }
    }

    Box(
        modifier = Modifier
            .background(
                color = if(isActivate) CouponCardColor else CouponExpiredAvailableBoxColor,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Text(
            text = text,
            style = storeMeTextStyle(FontWeight.Bold, -4),
            color = CouponAvailableTextColor,
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 3.dp)
        )
    }
}