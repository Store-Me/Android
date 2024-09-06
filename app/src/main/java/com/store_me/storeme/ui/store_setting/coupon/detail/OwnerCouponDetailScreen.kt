@file:OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class
)

package com.store_me.storeme.ui.store_setting.coupon.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.CouponQuantity
import com.store_me.storeme.data.OwnerCouponDetailData
import com.store_me.storeme.data.UserReceivedCouponData
import com.store_me.storeme.data.UserUsedCouponData
import com.store_me.storeme.ui.component.StoreMeTabContent
import com.store_me.storeme.ui.component.StoreMeTabRow
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAtDetail
import com.store_me.storeme.ui.store_setting.coupon.detail.OwnerCouponDetailViewModel.CouponDetailTabTitles
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponInfo
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.ExpiredTextColor
import com.store_me.storeme.ui.theme.PostTimeTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils

val LocalOwnerCouponDetailViewModel = staticCompositionLocalOf<OwnerCouponDetailViewModel> {
    error("No LocalOwnerCouponDetailViewModel provided")
}

@Composable
fun OwnerCouponDetailScreen(
    selectedCouponId: String,animatedVisibilityScope: AnimatedVisibilityScope, sharedTransitionScope: SharedTransitionScope,
    ownerCouponDetailViewModel: OwnerCouponDetailViewModel = viewModel(),
    onBack: () -> Unit
){
    val selectedCouponDetail = Auth.couponDetailList.value.find {
        it.couponInfoData.couponId == selectedCouponId
    }

    val pagerState = rememberPagerState()
    val tabTitles = enumValues<CouponDetailTabTitles>().map { it.displayName }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    BackHandler {
        onBack()
    }

    CompositionLocalProvider(LocalOwnerCouponDetailViewModel provides ownerCouponDetailViewModel) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = White,
            topBar = { OwnerCouponDetailTopAppBar(scrollBehavior){ onBack() } },

            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {

                        if(selectedCouponDetail != null) {
                            LazyColumn {
                                item {
                                    with(sharedTransitionScope){
                                        CouponInfo(item = selectedCouponDetail, modifier = Modifier
                                            .padding(horizontal = 20.dp)
                                            .sharedElement(
                                                rememberSharedContentState(key = selectedCouponDetail.couponInfoData.couponId),
                                                animatedVisibilityScope
                                            ))
                                    }
                                }

                                item { Spacer(modifier = Modifier.height(20.dp)) }

                                item { Text(
                                    text = "( 쿠폰 생성일 ) ${DateTimeUtils().dateTimeToDateText(selectedCouponDetail.couponInfoData.createdAt)}",
                                    style = storeMeTextStyle(FontWeight.Bold, -2),
                                    color = ExpiredTextColor,
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                ) }

                                item { Spacer(modifier = Modifier.height(20.dp)) }

                                item { HorizontalDivider(color = DefaultDividerColor, thickness = 1.dp) }

                                item { Spacer(modifier = Modifier.height(20.dp)) }

                                item { CouponCountSection(selectedCouponDetail) }

                                item { Spacer(modifier = Modifier.height(20.dp)) }

                                stickyHeader { StoreMeTabRow(pagerState = pagerState, tabTitles = tabTitles) }

                                item {
                                    StoreMeTabContent(tabTitles = tabTitles, state = pagerState) {
                                        UserListForCoupon(CouponDetailTabTitles.entries[it])
                                    }
                                }
                            }
                        }
                }
            }
        )
    }
}

@Composable
fun CouponCountSection(selectedCouponDetail: OwnerCouponDetailData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if(selectedCouponDetail.couponInfoData.quantity is CouponQuantity.Limit){
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "발급한 쿠폰 수",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${(selectedCouponDetail.couponInfoData.quantity as CouponQuantity.Limit).quantity}",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "받은 쿠폰 수",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${selectedCouponDetail.usedCouponData.receivedCount}",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "사용한 쿠폰 수",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${selectedCouponDetail.usedCouponData.usedCount}",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2)
            )
        }
    }
}

@Composable
fun UserListForCoupon(couponDetailTabTitles: CouponDetailTabTitles) {
    val ownerCouponDetailViewModel = LocalOwnerCouponDetailViewModel.current

    when(couponDetailTabTitles) {
        CouponDetailTabTitles.RECEIVED -> {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            ) {
                ownerCouponDetailViewModel.sampleCouponWithUserData.userReceivedCouponList.forEach {
                    ReceivedUserItem(item = it)
                }
            }
        }

        CouponDetailTabTitles.USED -> {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ownerCouponDetailViewModel.sampleCouponWithUserData.userUsedCouponList.forEach {
                    UsedUserItem(item = it)
                }
            }
        }
    }
}

@Composable
fun ReceivedUserItem(item: UserReceivedCouponData) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = item.userData.profileImage,
            contentDescription = "사용자 이미지",
            modifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape),
            error = painterResource(id = R.drawable.store_null_image)
        )

        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = item.userData.nickName,
            style = storeMeTextStyle(FontWeight.ExtraBold, 0),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun UsedUserItem(item: UserUsedCouponData) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.userData.profileImage,
            contentDescription = "사용자 이미지",
            modifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape),
            error = painterResource(id = R.drawable.store_null_image)
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.userData.nickName,
                style = storeMeTextStyle(FontWeight.ExtraBold, 0),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "( 사용 완료 ) ${DateTimeUtils().dateTimeToDateTimeText(item.usedAt)}",
                style = storeMeTextStyle(FontWeight.ExtraBold, 0),
                color = PostTimeTextColor,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun OwnerCouponDetailTopAppBar(scrollBehavior: TopAppBarScrollBehavior, onBack: () -> Unit) {
    TopAppBar(
        title = { TitleWithDeleteButtonAtDetail(title = "쿠폰 상세보기", isInTopAppBar = true){
            onBack()
        } },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White,
            scrolledContainerColor = White
        )
    )
}
