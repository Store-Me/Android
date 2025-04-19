@file:OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.store_setting.coupon.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.R
import com.store_me.storeme.data.coupon.CouponData
import com.store_me.storeme.data.coupon.CouponStats
import com.store_me.storeme.data.enums.tab_menu.CouponDetailTabMenu
import com.store_me.storeme.ui.component.SkeletonBox
import com.store_me.storeme.ui.component.StoreMeTabContent
import com.store_me.storeme.ui.component.StoreMeTabRow
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponInfo
import com.store_me.storeme.ui.theme.LightestHighlightColor
import com.store_me.storeme.ui.theme.PostTimeTextColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import timber.log.Timber

@Composable
fun OwnerCouponDetailScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    ownerCouponDetailViewModel: OwnerCouponDetailViewModel,
    onEdit: (String) -> Unit
){
    val pagerState = rememberPagerState()
    val tabTitles = remember { CouponDetailTabMenu.getDisplayNames() }

    val coupon by ownerCouponDetailViewModel.coupon.collectAsState()
    val couponStats by ownerCouponDetailViewModel.couponStats.collectAsState()
    val couponUsers by ownerCouponDetailViewModel.couponUsers.collectAsState()

    LaunchedEffect(Unit) {
        coupon?.couponId?.let {
            Timber.d("couponId: $it")
            ownerCouponDetailViewModel.getCouponStats(couponId = it)
            ownerCouponDetailViewModel.getCouponUsers(couponId = it)
        }
    }

    if(coupon != null) {
        LazyColumn {
            item {
                CouponDetailInfo(
                    animatedVisibilityScope = animatedVisibilityScope,
                    sharedTransitionScope = sharedTransitionScope,
                    coupon = coupon!!,
                    couponStats = couponStats,
                    onShowAll = {

                    },
                    onEdit = { onEdit(coupon!!.type) },
                    onAttach = {

                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            stickyHeader { StoreMeTabRow(pagerState = pagerState, tabTitles = tabTitles) }

            item {
                StoreMeTabContent(tabTitles = tabTitles, pagerState = pagerState) {
                    when(it) {
                        CouponDetailTabMenu.Received.ordinal -> {
                            //TODO 수령 사용자
                        }
                        CouponDetailTabMenu.Used.ordinal -> {
                            //TODO 사용 사용자
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CouponDetailInfo(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    coupon: CouponData,
    couponStats: CouponStats?,
    onShowAll: () -> Unit,
    onEdit: () -> Unit,
    onAttach: () -> Unit
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    var couponHeight by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        with(sharedTransitionScope){
            CouponInfo(
                coupon = coupon,
                modifier = Modifier
                    .weight(1f)
                    .sharedElement(
                        rememberSharedContentState(key = coupon.couponId),
                        animatedVisibilityScope
                    )
                    .onGloballyPositioned {
                        couponHeight = it.size.height
                    },
                storeName = storeDataViewModel.storeInfoData.value!!.storeName,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(with(LocalDensity.current) { (couponHeight).toDp() }),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CouponDetailIconBox(
                modifier = Modifier
                    .weight(1f),
                title = "쿠폰 내용 보기",
                iconResource = R.drawable.ic_coupon
            ) {
                onShowAll()
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CouponDetailBox(
                    modifier = Modifier
                        .weight(1f),
                    title = "쿠폰 생성일",
                    content = DateTimeUtils.formatToDate(coupon.createdAt)
                )

                CouponDetailBox(
                    modifier = Modifier
                        .weight(1f),
                    title = "쿠폰 만료일",
                    content = DateTimeUtils.formatToDate(coupon.dueDate)
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .weight(1f),
                    isLoading = couponStats == null
                ) {
                    CouponDetailBox(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = "발급된 쿠폰",
                        content = couponStats!!.receivedCount.toString()
                    )
                }

                SkeletonBox(
                    modifier = Modifier
                        .weight(1f),
                    isLoading = couponStats == null
                ) {
                    CouponDetailBox(
                        modifier = Modifier
                            .weight(1f),
                        title = "사용된 쿠폰",
                        content = couponStats!!.usedCount.toString()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CouponDetailIconBox(
                    modifier = Modifier
                        .weight(1f),
                    title = "수정하기",
                    iconResource = R.drawable.ic_edit
                ) {
                    onEdit()
                }

                CouponDetailIconBox(
                    modifier = Modifier
                        .weight(1f),
                    title = "소식에 첨부",
                    iconResource = R.drawable.ic_attach
                ) {
                    onAttach()
                }
            }
        }
    }
}

@Composable
fun CouponDetailBox(modifier: Modifier, title: String, content: String) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = LightestHighlightColor, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .clip(shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = storeMeTextStyle(FontWeight.ExtraBold, -1),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = content,
            style = storeMeTextStyle(FontWeight.Normal, -1),
            color = PostTimeTextColor
        )
    }
}

@Composable
fun CouponDetailIconBox(modifier: Modifier, title: String, iconResource: Int, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = SubHighlightColor, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .clip(shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = iconResource),
            modifier = Modifier
                .size(20.dp),
            contentDescription = "쿠폰 생성일",
            tint = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            style = storeMeTextStyle(FontWeight.ExtraBold, -1),
            color = Color.Black
        )
    }
}
