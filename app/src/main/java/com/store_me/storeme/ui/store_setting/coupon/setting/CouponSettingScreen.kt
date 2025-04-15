@file:OptIn(ExperimentalPagerApi::class, ExperimentalSharedTransitionApi::class)

package com.store_me.storeme.ui.store_setting.coupon.setting

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.R
import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.coupon.CouponTarget
import com.store_me.storeme.data.enums.coupon.CouponType
import com.store_me.storeme.data.enums.tab_menu.CouponTabMenu
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.component.StoreMeTabContent
import com.store_me.storeme.ui.component.StoreMeTabRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.store_setting.coupon.detail.OwnerCouponDetailScreen
import com.store_me.storeme.ui.store_setting.coupon.detail.OwnerCouponDetailViewModel
import com.store_me.storeme.ui.theme.CouponDueDateBoxColor
import com.store_me.storeme.ui.theme.CouponDueDateIconColor
import com.store_me.storeme.ui.theme.CouponQuantityBoxColor
import com.store_me.storeme.ui.theme.CouponQuantityIconColor
import com.store_me.storeme.ui.theme.CreateCouponArrowColor
import com.store_me.storeme.ui.theme.DisabledColor
import com.store_me.storeme.ui.theme.DividerColor
import com.store_me.storeme.ui.theme.FinishedColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.PopularBoxColor
import com.store_me.storeme.ui.theme.PopularTextColor
import com.store_me.storeme.ui.theme.RecommendBoxColor
import com.store_me.storeme.ui.theme.RecommendTextColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun CouponSettingScreen(
    navController: NavController,
    couponSettingViewModel: CouponSettingViewModel = hiltViewModel(),
    ownerCouponDetailViewModel: OwnerCouponDetailViewModel = hiltViewModel()
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val originalCoupons by storeDataViewModel.coupons.collectAsState()

    val pagerState = rememberPagerState()

    val selectedCoupon by ownerCouponDetailViewModel.coupon.collectAsState()

    fun onClose() {
        if(selectedCoupon != null) {
            ownerCouponDetailViewModel.updateCouponData(null)
        } else {
            navController.popBackStack()
        }
    }

    LaunchedEffect(originalCoupons) {
        couponSettingViewModel.updateCoupons(originalCoupons)
    }

    BackHandler {
        onClose()
    }

    SharedTransitionLayout {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = Color.White,
            topBar = {
                TitleWithDeleteButton(
                    title = "쿠폰 관리"
                ) {
                    onClose()
                }
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = selectedCoupon
                    ) {
                        if(it != null) {
                            OwnerCouponDetailScreen(
                                animatedVisibilityScope = this,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                ownerCouponDetailViewModel = ownerCouponDetailViewModel,
                                onEdit = { couponType ->
                                    navController.navigate(OwnerRoute.CouponEdit(selectedCouponType = couponType, couponId = it.couponId).fullRoute)
                                }
                            )
                        } else {
                            CouponManagementScreen(
                                navController = navController,
                                pagerState = pagerState,
                                couponSettingViewModel = couponSettingViewModel,
                                animatedVisibilityScope = this,
                                sharedTransitionScope = this@SharedTransitionLayout
                            ) { couponData ->
                                ownerCouponDetailViewModel.updateCouponData(couponData)
                            }
                        }
                    }

                    AnimatedVisibility(visible = selectedCoupon == null) {

                    }

                    AnimatedVisibility(visible = selectedCoupon != null) {

                    }
                }
            }
        )
    }
}

@Composable
fun CouponManagementScreen(
    navController: NavController,
    pagerState: PagerState,
    couponSettingViewModel: CouponSettingViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onShowDetail: (CouponData) -> Unit
) {
    val tabTitles = remember { CouponTabMenu.getDisplayNames() }

    val coupons by couponSettingViewModel.coupons.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        StoreMeTabRow(pagerState = pagerState, tabTitles = tabTitles)

        StoreMeTabContent(pagerState = pagerState, tabTitles = tabTitles) {
            when(it){
                CouponTabMenu.Create.ordinal -> {
                    //쿠폰 만들기 화면
                    CreateCouponSection { couponType ->
                        navController.navigate(OwnerRoute.CouponCreate(couponType.name).fullRoute)
                    }
                }

                CouponTabMenu.CouponList.ordinal -> {
                    //쿠폰 목록 화면
                    CouponListSection(
                        coupons = coupons,
                        animatedVisibilityScope = animatedVisibilityScope,
                        sharedTransitionScope = sharedTransitionScope
                    ) { couponData ->
                        onShowDetail(couponData)
                    }
                }
            }
        }
    }
}

@Composable
fun CreateCouponSection(onCreate: (CouponType) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { GuideImageSection() }

        items(CouponType.entries) {
            CreateCouponButton(it) {
                onCreate(it)
            }
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
                    .clip(shape = RoundedCornerShape(10))
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
        CouponType.Discount -> {
            Pair(R.drawable.ic_coupon_discount, "할인 쿠폰 만들기")
        }
        CouponType.Giveaway -> {
            Pair(R.drawable.ic_coupon_heart, "증정 쿠폰 만들기")
        }
        CouponType.Other -> {
            Pair(R.drawable.ic_coupon_star, "기타 혜택 쿠폰 만들기")
        }
    }

    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(30),
        colors = ButtonDefaults.buttonColors(
            containerColor = SubHighlightColor,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(20.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "쿠폰 아이콘",
            modifier = Modifier
                .size(24.dp),
            tint = Color.Unspecified
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
fun CouponListSection(
    coupons: List<CouponData>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onShowDetail: (CouponData) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(coupons) { coupon ->
            CouponItem(
                coupon = coupon,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope
            ) {
                onShowDetail(coupon)
            }
        }
    }
}

@Composable
fun CouponItem(
    coupon: CouponData,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onShowDetail: (String) -> Unit
){
    val storeDataViewModel = LocalStoreDataViewModel.current

    with(sharedTransitionScope) {
        CouponInfo(
            modifier = Modifier
                .sharedElement(
                    state = rememberSharedContentState(key = coupon.couponId),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .clickable(
                    onClick = { onShowDetail(coupon.couponId)},
                    interactionSource = null,
                    indication = null
                ),
            coupon = coupon,
            storeName = storeDataViewModel.storeInfoData.value!!.storeName
        )
    }
}

@Composable
fun CouponInfo(
    coupon: CouponData,
    modifier: Modifier = Modifier,
    storeName: String
) {
    val isActivate by remember { mutableStateOf(DateTimeUtils.isValid(coupon.dueDate)) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .border(width = 2.dp, color = SubHighlightColor, shape = RoundedCornerShape(10))
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            //쿠폰 이미지
            CouponImage(
                isActivate = isActivate,
                imageUrl = coupon.image,
                storeName = storeName
            )

            //쿠폰 증정 내용
            CouponContent(
                isActivate = isActivate,
                coupon = coupon
            )

            //쿠폰 제공 대상
            CouponTarget(
                isActivate = isActivate,
                couponTarget = coupon.target
            )

            //활성 여부
            CouponActivateButton(
                isActivate = isActivate
            )

            //쿠폰 수량, 마감 기한
            CouponLimit(
                isActivate = isActivate,
                coupon = coupon
            )
        }
    }
}

@Composable
fun CouponContent(
    isActivate: Boolean,
    coupon: CouponData
) {
    val valueText = when(coupon.type) {
        CouponType.Discount.name -> coupon.value + if((coupon.value.toLongOrNull() ?: 0) > 100) "원" else "%" + " 할인"
        CouponType.Giveaway.name -> "${coupon.value} 증정"
        CouponType.Other.name -> coupon.value
        else -> ""
    }

    val textColor = if(isActivate) Color.Black else DisabledColor

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = coupon.name,
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = valueText,
            style = storeMeTextStyle(FontWeight.Bold, 0),
            color = textColor,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CouponImage(
    isActivate: Boolean,
    imageUrl: String?,
    storeName: String
) {
    val colorSteps = arrayOf(
        0.7f to Color.Transparent,
        0.85f to Color.Black.copy(alpha = 0.1f),
        1.0f to Color.Black.copy(alpha = 0.3f)
    )

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        ProfileImage(
            url = imageUrl,
            accountType = AccountType.OWNER,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(10))
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = colorSteps
                    ),
                    shape = RoundedCornerShape(10)
                )
        )

        Text(
            text = storeName ?: "",
            style = storeMeTextStyle(FontWeight.Bold, 1),
            color = Color.White,
            modifier = Modifier
                .padding(8.dp)
        )

        if(!isActivate) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRect(color = Color.White.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun CouponTarget(couponTarget: String, isActivate: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        when(couponTarget) {
            CouponTarget.All.name -> {
                Text(
                    text = CouponTarget.All.displayName,
                    style = storeMeTextStyle(FontWeight.Bold, -2),
                    modifier = Modifier
                        .background(
                            color = if(isActivate) RecommendBoxColor else DisabledColor,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(4.dp),
                    color = if(isActivate) RecommendTextColor else DividerColor
                )
            }
            CouponTarget.Notification.name -> {
                Text(
                    text = CouponTarget.Notification.displayName,
                    style = storeMeTextStyle(FontWeight.Bold, -2),
                    modifier = Modifier
                        .background(
                            color = if(isActivate) PopularBoxColor else DisabledColor,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(4.dp),
                    color = if(isActivate) PopularTextColor else DividerColor
                )
            }
        }
    }
}

@Composable
fun CouponActivateButton(isActivate: Boolean) {
    val buttonText = if(isActivate) "발급중" else "종료된 쿠폰"
    val contentColor = if(isActivate) Color.White else DisabledColor
    val containerColor = if(isActivate) HighlightColor else DividerColor

    DefaultButton(
        buttonText = buttonText,
        enabled = false,
        diffValue = 0,
        fontWeight = FontWeight.Bold,
        colors = ButtonDefaults.buttonColors(
            disabledContentColor = contentColor,
            disabledContainerColor = containerColor
        )
    ) {

    }
}

@Composable
fun CouponLimit(
    isActivate: Boolean,
    coupon: CouponData
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_coupon_stack),
                contentDescription = "쿠폰 수량 아이콘",
                modifier = Modifier
                    .size(24.dp)
                    .background(color = CouponQuantityBoxColor, shape = RoundedCornerShape(15))
                    .padding(4.dp),
                tint = CouponQuantityIconColor
            )

            if(coupon.quantity != null) {
                //수량 제한이 있는 경우
                Text(
                    text = coupon.quantity.toString(),
                    style = storeMeTextStyle(FontWeight.Bold, -1),
                    color = Color.Black
                )

                Text(
                    text = "장",
                    style = storeMeTextStyle(FontWeight.Bold, -1),
                    color = GuideColor
                )
            } else {
                Text(
                    text = "제한없음",
                    style = storeMeTextStyle(FontWeight.Bold, -1),
                    color = Color.Black
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = "시간 아이콘",
                modifier = Modifier
                    .size(24.dp)
                    .background(color = if(isActivate) CouponDueDateBoxColor else FinishedColor, shape = RoundedCornerShape(15))
                    .padding(4.dp),
                tint = if(isActivate) CouponDueDateIconColor else Color.White
            )

            if(isActivate) {
                Text(
                    text = DateTimeUtils.formatToShortDate(coupon.dueDate),
                    style = storeMeTextStyle(FontWeight.Bold, -1),
                    color = Color.Black
                )

                Text(
                    text = "까지",
                    style = storeMeTextStyle(FontWeight.Bold, -1),
                    color = GuideColor
                )
            } else {
                Text(
                    text = "기한 만료",
                    style = storeMeTextStyle(FontWeight.Bold, -1),
                    color = FinishedColor
                )
            }
        }
    }
}