@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.mycoupon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.UserCouponWithStoreInfoData
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.CouponDividerLineColor
import com.store_me.storeme.ui.theme.TabDividerLineColor
import com.store_me.storeme.ui.theme.ExpiredBoxColor
import com.store_me.storeme.ui.theme.ExpiredIconColor
import com.store_me.storeme.ui.theme.ExpiredTextColor
import com.store_me.storeme.ui.theme.PostTimeTextColor
import com.store_me.storeme.ui.theme.SelectedSortTypeColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.ValidIconColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.DateTimeUtils
import kotlinx.coroutines.launch

val LocalMyCouponViewModel = staticCompositionLocalOf<MyCouponViewModel> {
    error("No MyCouponViewModel provided")
}
@Composable
fun MyCouponScreenWithBottomSheet(navController: NavController, myCouponViewModel: MyCouponViewModel = hiltViewModel()){

    CompositionLocalProvider(LocalMyCouponViewModel provides myCouponViewModel) {
        var isSheetShow by remember { mutableStateOf(false) }

        if(isSheetShow) {
            CouponBottomSheet(
                onClick = { isSheetShow = false },
                onDismiss =  { isSheetShow = false }
            )
        }

        LaunchedEffect(Unit) {
            myCouponViewModel.getCouponData()
        }

        Scaffold(
            containerColor = White,
            topBar = { TitleWithDeleteButton(title = "받은 쿠폰함") {
                navController.popBackStack()
            } },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ){
                    CouponTabLayout(myCouponViewModel) { isSheetShow = true }
                }
            }
        )
    }
}

@Composable
fun CouponBottomSheet(onDismiss: () -> Unit, onClick: () -> Unit){
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet (
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = White,
        onDismissRequest = { onDismiss() },
        content = {
            SelectSortTypeBottomSheet { onClick() }
        },
        dragHandle = null
    )
}

@Composable
fun SelectSortTypeBottomSheet(onClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        items(MyCouponViewModel.SortType.values()) { sortType ->
            SortTypeItem(sortType) {
                onClick()
            }

            HorizontalDivider(color = CouponDividerLineColor, thickness = 1.dp)
        }
    }
}

@Composable
fun SortTypeItem(sortType: MyCouponViewModel.SortType, onOptionSelected: () -> Unit) {
    val myCouponViewModel = LocalMyCouponViewModel.current

    val currentSortType = myCouponViewModel.currentSortType.collectAsState().value
    val text = myCouponViewModel.getSortButtonText(sortType)
    val textColor = if(currentSortType == sortType) SelectedSortTypeColor else Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                myCouponViewModel.changeSortType(sortType)
                onOptionSelected()
            }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if(currentSortType == sortType){
            Icon(
                imageVector =  ImageVector.vectorResource(R.drawable.ic_check_off),
                contentDescription = sortType.name,
                tint = SelectedSortTypeColor,
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = (-17).dp)
            )

            Spacer(modifier = Modifier.padding(end = 10.dp))
        }

        Text(
            text = text,
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            color = textColor,
            modifier = if(currentSortType == sortType) Modifier.offset(x = (-17).dp) else Modifier.offset(x = 0.dp)
        )
    }
}

@Composable
fun CouponTabLayout(myCouponViewModel: MyCouponViewModel, showSheet: () -> Unit) {
    val validCouponList by myCouponViewModel.myValidCouponList.collectAsState()
    val expiredCouponList by myCouponViewModel.myExpiredCouponList.collectAsState()

    val pagerState = rememberPagerState(pageCount = { myCouponViewModel.tabTitles.size })
    val scope = rememberCoroutineScope()

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            contentColor = Black,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(2.dp)
                        .fillMaxWidth(0.9f),
                    color = Black
                )
            },
            divider = {
                HorizontalDivider(
                    color = TabDividerLineColor,
                    thickness = 2.dp
                )
            }
        ) {
            myCouponViewModel.tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = storeMeTypography.titleSmall,
                            color = if(index == pagerState.currentPage) Black else TabDividerLineColor
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.Top
        ) { page ->
            when(myCouponViewModel.tabTitles[page]) {
                myCouponViewModel.tabTitles[0] -> CouponsList(true, validCouponList) { showSheet() }
                myCouponViewModel.tabTitles[1] -> CouponsList(false, expiredCouponList) { showSheet() }
                else -> CouponsList(true, validCouponList) { showSheet() }
            }
        }
    }
}

@Composable
fun CouponsList(isValid: Boolean, coupons: List<UserCouponWithStoreInfoData>, showSheet: () -> Unit) {

    LazyColumn {

        item {
            Row (
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(end = 20.dp, start = 20.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if(isValid){
                    Spacer(modifier = Modifier.weight(1f))

                    SetSortTypeButton {
                        showSheet()
                    }
                } else {
                    Text(
                        text = "만료된 쿠폰은 만료일 기준 최대 7일 까지 조회가 가능합니다.",
                        style = storeMeTypography.titleSmall,
                        color = ExpiredTextColor
                    )
                }
            }
        }

        itemsIndexed(coupons) { index, coupon ->
            CouponItem(isValid, coupon)

            if (index < coupons.size - 1) {
                HorizontalDivider(color = CouponDividerLineColor, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun SetSortTypeButton(onClick: () -> Unit){
    val myCouponViewModel = LocalMyCouponViewModel.current

    val currentSortType = myCouponViewModel.currentSortType.collectAsState().value

    val buttonText = myCouponViewModel.getSortButtonText(currentSortType)

    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(26.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = buttonText, style = storeMeTypography.labelSmall)

        Spacer(modifier = Modifier.width(5.dp))

        Icon(
            imageVector =  ImageVector.vectorResource(R.drawable.ic_arrow_down),
            contentDescription = "정렬 목록 열기",
            modifier = Modifier
                .size(10.dp)
        )
    }
}

@Composable
fun CouponItem(isValid: Boolean, coupon: UserCouponWithStoreInfoData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = coupon.storeInfo.storeImage,
                contentDescription = "${coupon.storeInfo.storeName} 사진",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(

            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = coupon.storeInfo.storeName,
                    style = storeMeTypography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = coupon.storeInfo.location + " · " + coupon.storeInfo.category,
                    fontFamily = appFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = PostTimeTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(15.dp))

        CouponContentSection(isValid, coupon)

        Spacer(modifier = Modifier.height(15.dp))

    }
}

@Composable
fun CouponContentSection(isValid: Boolean, coupon: UserCouponWithStoreInfoData) {
    val boxColor = if(isValid) SubHighlightColor else ExpiredBoxColor
    val contentColor = if(isValid) Black else ExpiredTextColor

    Row(
        modifier = Modifier
            .background(
                color = boxColor,
                shape = RoundedCornerShape(15.dp)
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(top = 15.dp, bottom = 15.dp, start = 10.dp)
        ) {

            Text(
                text = coupon.content,
                fontFamily = appFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = DateTimeUtils.convertDateTimeToKorean(coupon.expirationDatetime) + "까지",
                fontFamily = appFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = ExpiredTextColor
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))

        CouponCircleIcon(isValid, coupon.isUsed)

    }
}

@Composable
fun CouponCircleIcon(isValid: Boolean, isUsed: Boolean) {
    val boxColor = if(isValid) ValidIconColor else ExpiredIconColor

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = boxColor, shape = CircleShape)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        if(isValid){
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_coupon_downloaded),
                contentDescription = "다운로드 된 쿠폰 아이콘",
                tint = White,
                modifier = Modifier
                    .size(24.dp)
            )
        } else {

            Text(
                text = if(isUsed) "사용\n만료" else "기한\n만료",
                fontFamily = appFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = White,
                lineHeight = 12.sp
            )
        }
    }
}
