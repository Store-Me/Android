@file:OptIn(ExperimentalNaverMapApi::class)

package com.store_me.storeme.ui.home.owner

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.DailyHoursData
import com.store_me.storeme.data.StoreDetailData
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.StoreHomeItemData
import com.store_me.storeme.data.StoreNormalItem
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.theme.CopyButtonColor
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.StoreDetailIconColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.NavigationUtils

@Composable
fun OwnerStoreHomeSection(navController: NavController) {

    val storeHomeItems by Auth.storeHomeItemList.collectAsState()

    Column {
        InfoSection(navController)

        storeHomeItems
            .sortedBy { it.order }
            .forEach { item ->
                if(!item.isHidden) {
                    Spacer(modifier = Modifier.padding(top = 20.dp))

                    StoreHomeItemSection(item, navController)

                    DefaultHorizontalDivider(modifier = Modifier.padding(top = 20.dp))
                }
            }

        Spacer(modifier = Modifier.height(200.dp))
    }
}

@Composable
fun InfoSection(navController: NavController) {
    val context = LocalContext.current
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current

    Column {

        OpeningHoursSection {
            NavigationUtils().navigateOwnerNav(navController = navController, screenName = StoreNormalItem.OPENING_HOURS)
        }

        ClosedDaySection {
            NavigationUtils().navigateOwnerNav(navController = navController, screenName = StoreNormalItem.CLOSED_DAY)
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

        DefaultHorizontalDivider(modifier = Modifier.padding(top = 20.dp))
    }
}

/**
 * 영업 시간 정보
 */
@Composable
fun OpeningHoursSection(onClick: () -> Unit) {
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current

    val openingHours = ownerHomeViewModel.storeData.storeHours.openingHours
    val closedDay = ownerHomeViewModel.storeData.storeHours.closedDay

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = 20.dp),
    ) {
        when {
            openingHours.isEmpty() -> {
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

/**
 * 요일 별 영업 시간 정보
 */
@Composable
fun OpeningHoursForDay(index: Int, hoursData: DailyHoursData, isClosed: Boolean) {
    val dayOfWeek = DateTimeUtils.DayOfWeek.entries[index].displayName

    val timeText = if(isClosed) "정기 휴무" else DateTimeUtils().getSelectTimeText(
        hours = hoursData.openHours,
        minutes = hoursData.openMinutes
    ) + " - " + DateTimeUtils().getSelectTimeText(
        hours = hoursData.closeHours,
        minutes = hoursData.closeMinutes
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(index == 0) {
            Spacer(modifier = Modifier.width(20.dp))

            StoreHomeIcon(id = R.drawable.ic_clock)

            Spacer(modifier = Modifier.width(10.dp))
        } else {
            Spacer(modifier = Modifier.width(48.dp))
        }


        Text(text = dayOfWeek, style = storeMeTextStyle(FontWeight.Bold, 1), modifier = Modifier.padding(vertical = 5.dp))

        Spacer(modifier = Modifier.width(15.dp))

        Text(text = timeText, style = storeMeTextStyle(FontWeight.Bold, 1), modifier = Modifier.padding(vertical = 5.dp))
    }
}

/**
 * 휴무일 정보
 */
@Composable
fun ClosedDaySection(onClick: () -> Unit) {
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current

    val closedDay = ownerHomeViewModel.storeData.storeHours.closedDay

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
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current

    val phoneNumber = ownerHomeViewModel.storeData.storePhoneNumber

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
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current
    val locationInfo = ownerHomeViewModel.storeData.locationInfo

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
                text = locationInfo.locationDetail.ifEmpty { "위치 정보를 입력해주세요." },
                style = storeMeTextStyle(FontWeight.Bold, 1),
                color = if(locationInfo.locationDetail.isEmpty()) UndefinedTextColor else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }

    ownerHomeViewModel.storeData.locationInfo.latLng.let { OwnerHomeNaverMapSection() }
}

@Composable
fun OwnerHomeNaverMapSection() {
    val ownerHomeViewModel = LocalOwnerHomeViewModel.current
    val cameraPosition = CameraPosition(ownerHomeViewModel.storeData.locationInfo.latLng!!, 15.0)

    NaverMap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .aspectRatio(2f / 1f),
        cameraPositionState = CameraPositionState(position = cameraPosition),
        uiSettings = MapUiSettings(
            isScrollGesturesEnabled = false,
            isZoomGesturesEnabled = false,
            isTiltGesturesEnabled = false,
            isRotateGesturesEnabled = false,
            isStopGesturesEnabled = false,
            isZoomControlEnabled = false,
            isIndoorLevelPickerEnabled = false,
            isLogoClickEnabled = false,
        ),
        properties = MapProperties(
            isIndoorEnabled = false
        )
    ) {

        Marker(
            state = MarkerState(position = ownerHomeViewModel.storeData.locationInfo.latLng)
        )
    }
}

@Composable
fun StoreHomeItemSection(storeHomeItem: StoreHomeItemData, navController: NavController) {
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
                    }

                    else -> {
                        NoticeSection()
                    }
                }
            }
            StoreHomeItem.INTRO -> {
                when {
                    storeData.storeInfo.storeDescription.isEmpty() -> {
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                    }
                    else -> {
                        IntroSection(storeData)
                    }
                }

            }
            StoreHomeItem.PHOTO -> {
                when {
                    storeData.representPhoto.isEmpty() -> {
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
            }
            StoreHomeItem.COUPON -> {
                when {
                    storeData.couponList.isEmpty() -> {
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
            }
            StoreHomeItem.MENU -> {
                when {
                    storeData.storeMenu.menus.isEmpty() -> {
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
            }
            StoreHomeItem.NEWS -> {
                when {
                    storeData.customLabel.labelList.isEmpty() -> {
                        //커스텀 라벨이 없는 경우
                        Text(
                            text = ownerHomeViewModel.getEmptySectionText(storeHomeItem.item),
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = UndefinedTextColor
                        )
                    }
                    else -> {
                        when {
                            storeData.labelWithPostData.isEmpty() -> {
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
            }
        }

        DefaultButton(
            buttonText = ownerHomeViewModel.getEditButtonText(storeHomeItem.item),
            modifier = Modifier
                .padding(top = 12.dp),
            diffValue = 1,
            colors = ButtonDefaults.buttonColors(
                containerColor = EditButtonColor,
                contentColor = Color.Black
            )
        ) {
            NavigationUtils().navigateOwnerNav(navController, storeHomeItem.item)
        }
    }
}

@Composable
fun NoticeSection() {

}

@Composable
fun IntroSection(storeData: StoreDetailData) {
    Text(text = storeData.storeInfo.storeDescription, style = storeMeTextStyle(FontWeight.Normal, 0))
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
    storeData.customLabel.labelList.forEach {

    }
}

/**
 * 아이콘 Composable
 */
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

/**
 * 복사 Text Button
 */
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
            contentDescription = "복사 아이콘",
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