@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.home

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.CouponWithStoreInfoData
import com.store_me.storeme.data.StoreInfoData
import com.store_me.storeme.ui.component.BannerLayout
import com.store_me.storeme.ui.component.LocationLayout
import com.store_me.storeme.ui.component.NotificationIcon
import com.store_me.storeme.ui.component.SearchField
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.theme.DownloadCouponColor
import com.store_me.storeme.ui.theme.HomeCouponTitleTextColor
import com.store_me.storeme.ui.theme.HomeSearchBoxColor
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.ToastMessageUtils

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel
) {
    val storeList = SampleDataUtils.sampleTodayStore()
    val couponData = SampleDataUtils.sampleCoupon()

    var backPressedTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        containerColor = White,
        topBar = { TopLayout(navController = navController, scrollBehavior = scrollBehavior) },
        content = { innerPadding -> // 컨텐츠 영역
            Column(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(innerPadding)
            ) {
                LazyColumn {
                    item { LocationLayout(navController, locationViewModel)}
                    item { BannerLayout(navController = navController) }
                    item { BasicStoreListLayout(navController = navController, storeList, title = "\uD83D\uDD25 오늘의 가게", description = "오늘 사람들이 많이 찾은 가게") }
                    item { CouponLayout(navController = navController, couponList = couponData) }
                    item { BasicStoreListLayout(navController = navController, storeList, title = "\u2728 새로 생긴 가게", description = "우리동네 신상 가게") }
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
fun TopLayout(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(start = 4.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_home),
                    contentDescription = "로고",
                    modifier = Modifier
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                SearchField(modifier = Modifier
                    .weight(1f),
                    hint = "내 주변 가게를 찾아보세요."
                ) {

                }

                Spacer(modifier = Modifier.width(10.dp))

                NotificationIcon(navController = navController)
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White,
            scrolledContainerColor = White
        ),
    )



}

@Composable
fun BasicStoreListLayout(navController: NavController, storeList: MutableList<StoreInfoData>, title: String, description: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = title,
            style = storeMeTypography.labelLarge,
            modifier = Modifier.padding(start = 20.dp)
        )

        Text(
            text = description,
            style = storeMeTypography.titleSmall,
            fontSize = 10.sp,
            color = UnselectedItemColor,
            modifier = Modifier.padding(start = 20.dp, top = 8.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 20.dp)
        ) {
            items(storeList) { store ->
                Column(
                    modifier = Modifier
                        .padding(end = 7.dp)
                        .width(120.dp)
                        .clickable {
                            NavigationUtils().navigateNormalNav(
                                navController,
                                MainActivity.NormalNavItem.STORE_DETAIL,
                                store.storeName
                            )
                        }
                ) {
                    AsyncImage(
                        model = store.storeImage,
                        contentDescription = "${store.storeName} 사진",
                        modifier = Modifier
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Column(modifier = Modifier.padding(start = 7.dp, end = 7.dp)){
                        Box(
                            modifier = Modifier
                                .background(UnselectedItemColor, shape = RoundedCornerShape(4.dp))
                                .wrapContentSize()
                                .padding(horizontal = 6.dp)
                        ) {
                            Text(
                                text = store.category.displayName,
                                fontFamily = appFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = store.storeName, style = storeMeTypography.labelSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun CouponLayout(navController: NavController, couponList: MutableList<CouponWithStoreInfoData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\u2764 쿠폰 있는 가게",
                style = storeMeTypography.labelLarge,
                color = HomeCouponTitleTextColor,
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            MyCouponIconText {
                NavigationUtils().navigateNormalNav(navController, MainActivity.NormalNavItem.MY_COUPON)
            }

            Spacer(modifier = Modifier.width(20.dp))
        }

        Text(
            text = "쿠폰으로 혜택받을 수 있는 가게",
            style = storeMeTypography.titleSmall,
            fontSize = 10.sp,
            color = UnselectedItemColor,
            modifier = Modifier.padding(start = 20.dp, top = 8.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 20.dp)
        ) {
            items(couponList) { coupon ->
                Column(
                    modifier = Modifier
                        .padding(end = 7.dp)
                        .width(120.dp)
                        .height(240.dp)
                        .clickable {
                            NavigationUtils().navigateNormalNav(
                                navController,
                                MainActivity.NormalNavItem.STORE_DETAIL,
                                coupon.storeInfo.storeName
                            )
                        }
                ) {
                    AsyncImage(
                        model = coupon.storeInfo.storeImage,
                        contentDescription = "${coupon.storeInfo.storeName} 사진",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    )
                    Column(
                        modifier = Modifier
                            .size(120.dp)
                            .background(
                                color = HomeSearchBoxColor,
                                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                            )
                            .padding(start = 12.dp, end = 12.dp)
                    ){
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = coupon.storeInfo.storeName,
                            fontFamily = appFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            maxLines = 1,
                            letterSpacing = 0.7.sp,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = coupon.content,
                            style = storeMeTypography.titleSmall.copy(
                                lineHeight = 20.sp
                            ),
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            modifier = Modifier
                                .size(width = 110.dp, height = 25.dp),
                            onClick = {
                                // 쿠폰 다운로드 클릭 이벤트
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DownloadCouponColor,
                                contentColor = White
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "쿠폰 받기",
                                fontFamily = appFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MyCouponIconText(onClick: () -> Unit){
    Row(
        modifier = Modifier
            .clickable(
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_coupon),
            contentDescription = "쿠폰 아이콘",
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "받은 쿠폰함",
            style = storeMeTypography.titleSmall
        )

        Spacer(modifier = Modifier.width(2.dp))

        Icon(
            painter = painterResource(id = R.drawable.arrow_right),
            contentDescription = "화살표 아이콘",
            modifier = Modifier.size(8.dp)
        )
    }
}