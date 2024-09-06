@file:OptIn( ExperimentalPagerApi::class,
    ExperimentalPagerApi::class
)

package com.store_me.storeme.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.data.NotificationType
import com.store_me.storeme.data.NotificationWithStoreInfoData
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.DateTimeTextColor
import com.store_me.storeme.ui.theme.TabDividerLineColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.SampleDataUtils
import kotlinx.coroutines.launch

val LocalNotificationViewModel = staticCompositionLocalOf<NotificationViewModel> {
    error("No NotificationViewModel provided")
}

@Composable
fun NotificationScreen(navController: NavController, notificationViewModel: NotificationViewModel = hiltViewModel()){
    val sampleNotification = SampleDataUtils.sampleNotification()

    CompositionLocalProvider(LocalNotificationViewModel provides notificationViewModel) {

        Scaffold(
            containerColor = White,
            topBar = { TitleWithDeleteButton(navController = navController, title = "알림 목록") },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    NotificationTabLayout(sampleNotification)
                }
            }
        )

    }



}

@Composable
fun NotificationTabLayout(notificationList: MutableList<NotificationWithStoreInfoData>) {

    val tabTitles = listOf(NotificationType.ALL.displayName, NotificationType.NORMAL.displayName, NotificationType.RESERVATION.displayName)

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Transparent,
            contentColor = Black,
            indicator = { tabPositions ->
                SecondaryIndicator(
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
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
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
            count = tabTitles.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page ->
            val filteredNotifications = when(tabTitles[page]) {
                NotificationType.ALL.displayName -> notificationList
                NotificationType.NORMAL.displayName -> notificationList.filter { it.notificationType == NotificationType.NORMAL }
                NotificationType.RESERVATION.displayName -> notificationList.filter { it.notificationType == NotificationType.RESERVATION }
                else -> emptyList()
            }

            NotificationsList(filteredNotifications)
        }
    }
}

@Composable
fun NotificationsList(notifications: List<NotificationWithStoreInfoData>){
    LazyColumn {
        items(notifications) { notification ->
            NotificationItem(notification)
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationWithStoreInfoData) {
    val notificationViewModel = LocalNotificationViewModel.current

    var backgroundColor by remember { mutableStateOf(if (notification.isRead) Transparent else HighlightColor) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .clickable {
                backgroundColor = Transparent
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
                model = notification.storeInfoData.storeImage,
                contentDescription = "${notification.storeInfoData.storeName} 사진",
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = notificationViewModel.getNotificationText(notification),
                    fontFamily = appFontFamily,
                    color = Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.7.sp,
                    lineHeight = 17.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = DateTimeUtils().datetimeAgo(notification.datetime),
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