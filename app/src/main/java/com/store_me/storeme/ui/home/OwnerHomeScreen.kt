@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class
)

package com.store_me.storeme.ui.home

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.DailyHoursData
import com.store_me.storeme.data.StoreDetailData
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.StoreHomeItemData
import com.store_me.storeme.ui.component.DefaultEditButton
import com.store_me.storeme.ui.component.LinkSection
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.theme.CopyButtonColor
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.ManagementButtonColor
import com.store_me.storeme.ui.theme.OwnerHomeLikeCountColor
import com.store_me.storeme.ui.theme.StoreDetailIconColor
import com.store_me.storeme.ui.theme.TabDividerLineColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.LikeCountUtils
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.ToastMessageUtils
import kotlinx.coroutines.launch

val LocalOwnerHomeViewModel = staticCompositionLocalOf<OwnerHomeViewModel> {
    error("No OwnerHomeViewModel provided")
}

@Composable
fun OwnerHomeScreen(
    navController: NavController,
    ownerHomeViewModel: OwnerHomeViewModel = hiltViewModel()
) {
    var backPressedTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current

    val pagerState = rememberPagerState()

    CompositionLocalProvider(LocalOwnerHomeViewModel provides ownerHomeViewModel) {


        Scaffold(
            containerColor = White,
            content = { innerPadding -> // 컨텐츠 영역
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    LazyColumn {
                        item { if(!ownerHomeViewModel.storeData.bannerImageUrl.isNullOrEmpty()) BackgroundSection(ownerHomeViewModel.storeData.bannerImageUrl) }
                        item { ProfileSection(navController = navController)}
                        stickyHeader { TabMenuSection(navController = navController, pagerState) }
                        item { OwnerHomeContentSection(navController = navController, pagerState) }
                    }
                }
            }
        )
    }


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
fun BackgroundSection(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "배경 이미지",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ProfileSection(navController: NavController) {
    val linkListData by Auth.linkListData.collectAsState()

    Column {
        Spacer(modifier = Modifier.height(15.dp))

        ProfileInfoSection()

        Spacer(modifier = Modifier.height(15.dp))

        EditProfileSection()

        Spacer(modifier = Modifier.height(15.dp))

        LinkSection(
            linkListData,
            modifier= Modifier.weight(1f),
            onShareClick = {  },
            onEditClick = {
                NavigationUtils().navigateOwnerNav(
                    navController,
                    MainActivity.OwnerNavItem.LINK_SETTING
                )
            }
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun ProfileInfoSection() {
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current

    val subText =
        if(ownerHomeViewModel.storeData.storeInfo.customCategory.isNullOrEmpty())
            ownerHomeViewModel.storeData.storeInfo.location
        else
            ownerHomeViewModel.storeData.storeInfo.location + " · " + ownerHomeViewModel.storeData.storeInfo.customCategory

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        val (like, row) = createRefs()

        LikeIconWithCount(ownerHomeViewModel.storeData.storeInfo.favoriteCount,
            modifier = Modifier
                .constrainAs(like){
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(row) {
                    start.linkTo(parent.start)
                    end.linkTo(like.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = ownerHomeViewModel.storeData.storeInfo.storeImage,
                contentDescription = "${ownerHomeViewModel.storeData.storeInfo.storeName} 사진",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(text = ownerHomeViewModel.storeData.storeInfo.storeName, style = storeMeTextStyle(FontWeight.ExtraBold, 6))

                Spacer(modifier= Modifier.height(10.dp))

                Text(text = subText, style = storeMeTextStyle(FontWeight.Bold, 0))
            }
        }
    }
}

@Composable
fun LikeIconWithCount(count: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .clickable { }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_like_off),
            contentDescription = "좋아요",
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    onClick = { },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false)
                )
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = LikeCountUtils().convertLikeCount(count),
            style = storeMeTextStyle(FontWeight.Bold, -1),
            maxLines = 1,
            color = OwnerHomeLikeCountColor
        )
    }
}

@Composable
fun EditProfileSection() {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        DefaultEditButton(text = "프로필 수정", modifier = Modifier.weight(1f)) {

        }

        Spacer(modifier = Modifier.width(10.dp))

        DefaultEditButton(text = "가게정보 관리", modifier = Modifier.weight(1f), containerColor = ManagementButtonColor, contentColor = White) {

        }
    }
}

@Composable
fun TabMenuSection(navController: NavController, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = White,
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
        OwnerHomeViewModel.OwnerHomeTabMenu.entries.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = title.displayName,
                        style = storeMeTextStyle(FontWeight.ExtraBold, 0),
                        color = if(index == pagerState.currentPage) Black else TabDividerLineColor
                    )
                }
            )
        }
    }
}

@Composable
fun OwnerHomeContentSection(navController: NavController, pagerState: PagerState) {

    HorizontalPager(
        count = OwnerHomeViewModel.OwnerHomeTabMenu.entries.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxHeight(),
        verticalAlignment = Alignment.Top
    ) { page ->
        when(OwnerHomeViewModel.OwnerHomeTabMenu.entries[page]) {
            OwnerHomeViewModel.OwnerHomeTabMenu.HOME -> {
                OwnerStoreHomeScreen(navController)
            }
            OwnerHomeViewModel.OwnerHomeTabMenu.NEWS -> {
                NewsScreen(navController)
            }
        }
    }
}

@Composable
fun OwnerStoreHomeScreen(navController: NavController) {
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current

    val storeData = ownerHomeViewModel.storeData
    val storeHomeItems by ownerHomeViewModel.storeHomeItems.collectAsState()


    @Composable
    fun StoreHomeIcon(id: Int) {
        Icon(
            imageVector = ImageVector.vectorResource(id = id),
            contentDescription = "스토어 홈 아이콘",
            modifier = Modifier
                .size(18.dp),
            tint = StoreDetailIconColor
        )
    }

    @Composable
    fun CopyButton(onClick: () -> Unit){
        Row(
            modifier = Modifier
                .clickable(
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, color = CopyButtonColor)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy),
                contentDescription = "스토어 홈 아이콘",
                modifier = Modifier
                    .size(12.dp),
                tint = CopyButtonColor
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "복사",
                style = storeMeTextStyle(FontWeight.Bold, -1), color = CopyButtonColor,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }

    @Composable
    fun OpeningHoursSection(onClick: () -> Unit) {
        val openingHours = storeData.storeHours.openingHours
        val closedDay = storeData.storeHours.closedDay

        @Composable
        fun OpeningHoursForDay(index: Int, hoursData: DailyHoursData, isClosed: Boolean, modifier: Modifier = Modifier) {
            val dayOfWeek = DateTimeUtils.DayOfWeek.entries[index].displayName

            val timeText = if(isClosed) "정기 휴무" else hoursData.openTime + " - " + hoursData.closeTime

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(index == 0) {
                    Spacer(modifier = Modifier.width(20.dp))

                    StoreHomeIcon(id = R.drawable.ic_clock) //size = 18.dp

                    Spacer(modifier = Modifier.width(10.dp))
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }


                Text(text = dayOfWeek, style = storeMeTextStyle(FontWeight.Bold, 1), modifier = Modifier.padding(vertical = 5.dp))

                Spacer(modifier = Modifier.width(15.dp))

                Text(text = timeText, style = storeMeTextStyle(FontWeight.Bold, 1), modifier = Modifier.padding(vertical = 5.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
        ) {
            
            when {
                openingHours.isNullOrEmpty() -> {
                    Text(text = "영업 시간을 입력해주세요.", style = storeMeTextStyle(FontWeight.Bold, 1), color = UndefinedTextColor, modifier = Modifier.padding(vertical = 10.dp))
                }

                else -> {
                    openingHours.forEachIndexed{ index, hoursData ->
                        OpeningHoursForDay(index, hoursData, closedDay?.contains(index) ?: false)
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }

    @Composable
    fun ClosedDaySection(onClick: () -> Unit) {
        val closedDay = storeData.storeHours.closedDay

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(20.dp))

                StoreHomeIcon(id = R.drawable.ic_calendar)

                Spacer(modifier = Modifier.width(10.dp))

                when {
                    closedDay == null -> {
                        Text(
                            text = "휴무일을 설정해주세요.",
                            style = storeMeTextStyle(FontWeight.Bold, 1),
                            color = UndefinedTextColor,
                            )
                    }

                    closedDay.isEmpty() -> {
                        Text(
                            text = "휴무일 없음",
                            style = storeMeTextStyle(FontWeight.Bold, 1),

                        )
                    }

                    else -> {
                        Text(
                            text = DateTimeUtils().getClosedDayText(closedDay),
                            style = storeMeTextStyle(FontWeight.Bold, 1),

                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    @Composable
    fun PhoneNumberSection(onClick: () -> Unit) {
        val phoneNumber = storeData.storePhoneNumber

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(20.dp))

                StoreHomeIcon(id = R.drawable.ic_call)

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = phoneNumber, style = storeMeTextStyle(FontWeight.Bold, 1), modifier = Modifier.padding(vertical = 10.dp))

                Spacer(modifier = Modifier.width(5.dp))

                CopyButton {
                    ownerHomeViewModel.copyToClipboard()
                }
            }
        }
    }

    @Composable
    fun LocationSection(onClick: () -> Unit) {
        val locationInfo = storeData.locationInfo

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 20.dp)
            ) {
                Spacer(modifier = Modifier.width(20.dp))

                StoreHomeIcon(id = R.drawable.ic_marker)

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = locationInfo?.locationDetail ?: "위치 정보를 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Bold, 1),
                    color = if(locationInfo == null) UndefinedTextColor else Black
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    @Composable
    fun InfoSection() {
        val context = LocalContext.current

        Column {
            Spacer(modifier = Modifier.height(20.dp))

            OpeningHoursSection {

            }

            ClosedDaySection {

            }

            PhoneNumberSection {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${ownerHomeViewModel.storeData.storePhoneNumber}")
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }

            LocationSection {

            }

            HorizontalDivider(color = DefaultDividerColor, thickness = 1.dp, modifier = Modifier.padding(bottom = 20.dp))
        }
    }

    Column {
        InfoSection()

        storeHomeItems
            .sortedBy { it.order }
            .forEach { item ->
                if(!item.isHidden) {
                    StoreHomeItemSection(item)

                    HorizontalDivider(color = DefaultDividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 20.dp))
                }
            }

        Spacer(modifier = Modifier.height(200.dp))
    }
}

@Composable
fun StoreHomeItemSection(storeHomeItem: StoreHomeItemData) {
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current
    val storeData = ownerHomeViewModel.storeData

    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(text = storeHomeItem.item.displayName, style = storeMeTextStyle(FontWeight.ExtraBold, 2))

        Spacer(modifier = Modifier.height(15.dp))

        when(storeHomeItem.item) {
            StoreHomeItem.NOTICE -> {
                when {
                    storeData.notice.isNullOrEmpty() -> {
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                        
                        DefaultEditButton(
                            text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item, true),
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .fillMaxWidth()
                        ) {

                        }
                    }
                    else -> {
                        NoticeSection()

                        DefaultEditButton(
                            text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item, false),
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .fillMaxWidth()
                        ) {

                        }
                    }
                }
            }
            StoreHomeItem.INTRO -> {
                when {
                    storeData.storeInfo.storeDescription.isNullOrEmpty() -> {
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )

                        DefaultEditButton(
                            text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item, true),
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .fillMaxWidth()
                        ) {

                        }
                    }
                    else -> {
                        IntroSection(storeData)

                        DefaultEditButton(
                            text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item, false),
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .fillMaxWidth()
                        ) {

                        }
                    }
                }

            }
            StoreHomeItem.PHOTO -> {
                when {
                    storeData.representPhoto.isNullOrEmpty() -> {
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                    }
                    else -> {
                        PhotoSection()
                    }
                }

                DefaultEditButton(
                    text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item),
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {

                }
            }
            StoreHomeItem.COUPON -> {
                when {
                    storeData.couponList.isNullOrEmpty() -> {
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                    }
                    else -> {
                        CouponSection()
                    }
                }

                DefaultEditButton(
                    text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item),
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {

                }
            }
            StoreHomeItem.MENU -> {
                when (storeData.storeMenu) {
                    null -> {
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                    }
                    else -> {
                        MenuSection()
                    }
                }

                DefaultEditButton(
                    text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item),
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {

                }
            }
            StoreHomeItem.STORY -> {
                when {
                    storeData.isStoryExist -> {
                        StorySection()
                    }
                    else -> {
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                    }
                }

                DefaultEditButton(
                    text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item),
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {

                }
            }
            StoreHomeItem.REVIEW -> {
                when {
                    storeData.isReviewExist -> {
                        ReviewSection()
                    }
                    else -> {
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                    }
                }

                DefaultEditButton(
                    text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item),
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {

                }
            }
            StoreHomeItem.NEWS -> {
                when (storeData.customLabel) {
                    null -> {
                        //커스텀 라벨이 없는 경우
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                    }
                    else -> {
                        when (storeData.labelWithPostData) {
                            null -> {
                                //커스텀 라벨은 있지만, 게시글이 없는 경우
                                Text(
                                    text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                                    style = storeMeTextStyle(FontWeight.Normal, 0),
                                    color = UndefinedTextColor
                                )
                            }
                            else -> {
                                //커스텀 라벨과 게시글 모두 있는 경우
                                NewsSection(storeData)
                            }
                        }
                    }
                }

                DefaultEditButton(
                    text = ownerHomeViewModel.getEditButtonText(storeHomeItem.item),
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {

                }
            }
        }
    }
}

@Composable
fun NoticeSection() {

}

@Composable
fun IntroSection(storeData: StoreDetailData) {
    Text(text = storeData.storeInfo.storeDescription.toString(), style = storeMeTextStyle(FontWeight.Normal, 0))
}

@Composable
fun PhotoSection() {

}

@Composable
fun CouponSection() {

}

@Composable
fun MenuSection() {

}

@Composable
fun StorySection() {


}

@Composable
fun ReviewSection() {


}

@Composable
fun NewsSection(storeData: StoreDetailData) {
    storeData.customLabel!!.labelList!!.forEach {

    }
}

@Composable
fun NewsScreen(navController: NavController) {

}