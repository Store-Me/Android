@file:OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.R
import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.StoreInfoData
import com.store_me.storeme.ui.theme.DownloadCouponColor
import com.store_me.storeme.ui.theme.HomeCouponTitleTextColor
import com.store_me.storeme.ui.theme.HomeSearchBoxColor
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.ToastMessageUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.yield

@Composable
fun HomeScreen(navController: NavController) {
    val storeList = SampleDataUtils.sampleTodayStore()
    val couponData = SampleDataUtils.sampleCoupon()

    Scaffold(
        topBar = { TopLayout() },
        content = { innerPadding -> // 컨텐츠 영역
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item { LocationLayout()}
                item { BannerLayout() }
                item { BasicStoreListLayout(navController = navController, storeList, title = "\uD83D\uDD25 오늘의 가게", description = "오늘 사람들이 많이 찾은 가게")}
                item {CouponLayout(navController = navController, couponData = couponData)}
                item {BasicStoreListLayout(navController = navController, storeList, title = "\u2728 새로 생긴 가게", description = "우리동네 신상 가게")}
            }
        }
    )
}

@Composable
fun TopLayout() {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.storeme_logo_black),
                    contentDescription = "로고",
                    modifier = Modifier
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                SearchField(modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.notification_off),
                    contentDescription = "알림",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(7.dp)
                )
                Spacer(modifier = Modifier.width(13.dp))
            }
        },
    )
}
@Composable
fun SearchField(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .height(36.dp)
            .background(HomeSearchBoxColor, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            textStyle = storeMeTypography.bodySmall,
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(
                                text = "내 주변 가게를 찾아보세요.",
                                color = Color.Gray,
                                style = storeMeTypography.bodySmall
                            )
                        }
                        innerTextField()
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
                        contentDescription = "검색",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun LocationLayout(){
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 20.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = false, radius = 25.dp),  // Ripple 색상

                    onClick = {
                        ToastMessageUtils.showToast(context, "지역 설정")
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "강남구",
                style = storeMeTypography.labelMedium
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "지역 설정 아이콘",
                modifier = Modifier.size(12.dp)
            )
        }
        Spacer(Modifier.weight(1f)) //중간 공백

        SetLocationButton(
            onClick = {
            /*  */
            }
        )
    }
}

@Composable
fun SetLocationButton(onClick: () -> Unit){
    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(30.dp)
            .padding(end = 20.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = "동네 설정", style = storeMeTypography.labelSmall)
    }
}

@Composable
fun BannerLayout() {
    val bannerUrls = SampleDataUtils.sampleBannerImage()
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    BoxWithConstraints (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        val boxWidth = maxWidth
        val bannerHeight = boxWidth / 5

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
        ) {
            HorizontalPager(
                count = bannerUrls.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bannerHeight)
            ) { page ->
                LoadBannerImages(bannerUrls[page], page)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 내부 하단 우측에 위치
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${bannerUrls.size}",
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 9.sp,
                    color = Color.White
                )
            }
        }

        //자동 슬라이드
        LaunchedEffect(pagerState) {
            while (true) {
                yield()
                delay(10000)
                scope.launch {
                    val nextPage = (pagerState.currentPage + 1) % bannerUrls.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }
}

@Composable
fun LoadBannerImages(bannerUrl: String, page: Int){
    AsyncImage(
        model = bannerUrl,
        contentDescription = "배너 $page",
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(15.dp))
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
                        .clickable { navController.navigate("storeDetail/${store.storeName}") }
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
                                .padding(vertical = 4.dp, horizontal = 6.dp)
                        ) {
                            Text(
                                text = store.category,
                                fontFamily = appFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
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
fun CouponLayout(navController: NavController, couponData: MutableList<CouponData>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = "\u2764 쿠폰 있는 가게",
            style = storeMeTypography.labelLarge,
            color = HomeCouponTitleTextColor,
            modifier = Modifier.padding(start = 20.dp)
        )

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
            items(couponData) { coupon ->
                Column(
                    modifier = Modifier
                        .padding(end = 7.dp)
                        .width(120.dp)
                        .height(240.dp)
                        .clickable { navController.navigate("storeDetail/${coupon.storeName}") }
                ) {
                    AsyncImage(
                        model = coupon.storeImage,
                        contentDescription = "${coupon.storeName} 사진",
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
                            text = coupon.storeName,
                            fontFamily = appFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = coupon.content,
                            style = storeMeTypography.bodySmall,
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
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "쿠폰 다운로드",
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