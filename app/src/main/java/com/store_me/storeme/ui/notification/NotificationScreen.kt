@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalPagerApi::class
)

package com.store_me.storeme.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.data.NotificationData
import com.store_me.storeme.ui.component.BackButtonWithTitle
import com.store_me.storeme.ui.theme.DateTimeTextColor
import com.store_me.storeme.ui.theme.DividerLineColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.SampleDataUtils
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(navController: NavController){
    val sampleNotification = SampleDataUtils.sampleNotification()

    Scaffold(
        topBar = { BackButtonWithTitle(navController = navController, title = "알림 목록") },
        content = { innerPadding -> // 컨텐츠 영역
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                NotificationTabLayout(sampleNotification)
            }
        }
    )
}

@Composable
fun NotificationTabLayout(notificationList: MutableList<NotificationData>) {

    val tabTitles = listOf("전체", "예약", "구매")

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(2.dp)
                        .fillMaxWidth(0.9f),
                    color = Color.Black
                )
            },
            divider = {
                Divider(
                    color = DividerLineColor,
                    thickness = 2.dp
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(
                        text = title,
                        style = storeMeTypography.titleSmall
                    ) }
                )
            }
        }

        HorizontalPager(
            count = tabTitles.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.Top
        ) { page ->
            val filteredNotifications = when(tabTitles[page]) {
                "전체" -> notificationList
                "예약" -> notificationList.filter { it.type == "예약" }
                "구매" -> notificationList.filter { it.type == "구매" }
                else -> emptyList()
            }

            NotificationsList(filteredNotifications)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
            // 여기서 추가적인 동작을 수행할 수 있음
        }
    }
}

@Composable
fun NotificationsList(notifications: List<NotificationData>){
    LazyColumn {
        items(notifications) { notifications ->
            NotificationItem(notifications)
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationData) {
    var backgroundColor by remember { mutableStateOf(if (notification.isRead) Color.Transparent else HighlightColor) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .clickable {
                backgroundColor = Color.Transparent
                notification.isRead = true
            },
    ) {
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = notification.storeImage,
                contentDescription = "${notification.content} 사진",
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(

            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = notification.content,
                    fontFamily = appFontFamily,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = notification.datetime,
                    fontFamily = appFontFamily,
                    color = DateTimeTextColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}