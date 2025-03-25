@file:OptIn(ExperimentalNaverMapApi::class)

package com.store_me.storeme.ui.home.owner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.StoreProfileItems
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.component.CopyButton
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.theme.ManagementButtonColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.UndefinedColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.PhoneNumberUtils
import com.store_me.storeme.utils.StoreCategory
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

/**
 * Owner Home 화면의 StoreProfile Composable
 */
@Composable
fun StoreHomeInfoSection(
    storeInfoData: StoreInfoData,
    businessHours: BusinessHoursResponse,
    onClick: (StoreProfileItems) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        /**
         * 가게 이름, 카테고리, 설명, 수정 및 관리 버튼
         */
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            /**
             * 가게 이름, 카테고리, 설명
             */
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = storeInfoData.storeName,
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                    color = Color.Black
                )

                Text(
                    text = StoreCategory.valueOf(storeInfoData.storeCategory).displayName + " · " + storeInfoData.storeDetailCategory,
                    style = storeMeTextStyle(FontWeight.Bold, -1)
                )
            }

            /**
             * 수정 및 관리 버튼
             */
            StoreHomeInfoButtonsSection(
                onEditClick = {
                    onClick(StoreProfileItems.EDIT_PROFILE)
                },
                onManagementClick = {
                    onClick(StoreProfileItems.MANAGEMENT)
                }
            )
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //가게 설명
            StoreIntroSection(storeInfoData.storeIntro) {
                onClick(StoreProfileItems.INTRO)
            }

            DefaultHorizontalDivider()

            //영업시간
            StoreBusinessHoursSection(businessHours) {
                onClick(StoreProfileItems.BUSINESS_HOURS)
            }

            //휴무일
            StoreHolidaySection(businessHours) {
                onClick(StoreProfileItems.BUSINESS_HOURS)
            }

            //가게 전화번호
            StorePhoneNumberSection(
                phoneNumber = storeInfoData.storePhoneNumber,
                onCopy = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(storeInfoData.storeName, storeInfoData.storePhoneNumber)
                    clipboard.setPrimaryClip(clip)
                },
                onClick = {
                    onClick(StoreProfileItems.PHONE_NUMBER)
                }
            )

            //가게 위치 정보
            StoreLocationSection(
                storeInfoData.storeLocationAddress,
                storeInfoData.storeLocationDetail,
            ) {
                onClick(StoreProfileItems.LOCATION)
            }
        }
    }
}

@Composable
fun StoreHomeInfoButtonsSection(
    onEditClick: () -> Unit,
    onManagementClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        DefaultButton(
            buttonText = "가게정보 관리",
            colors = ButtonDefaults.buttonColors(
                containerColor = ManagementButtonColor,
                contentColor = Color.White
            ),
            diffValue = 0
        ) {
            onManagementClick()
        }

        DefaultButton(
            buttonText = "프로필 수정",
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = SubHighlightColor
            ),
            diffValue = 0
        ) {
            onEditClick()
        }
    }
}

@Composable
fun StoreIntroSection(intro: String?, onClick: () -> Unit) {
    when {
        intro.isNullOrEmpty() -> {
            Text(
                text = "가게에 대한 소개를 작성해주세요.",
                style = storeMeTextStyle(FontWeight.Normal, -1),
                color = UndefinedColor,
                modifier = Modifier
                    .clickable(
                        onClick = onClick,
                        indication = null,
                        interactionSource = null
                    )
            )
        }
        else -> {
            Text(
                text = intro,
                style = storeMeTextStyle(FontWeight.Normal, -1),
                color = Color.Black,
                modifier = Modifier
                    .clickable(
                        onClick = onClick,
                        indication = null,
                        interactionSource = null
                    )
            )
        }
    }
}

/**
 * Store Info Icon Composable
 */
@Composable
fun StoreHomeIcon(id: Int) {
    Icon(
        imageVector = ImageVector.vectorResource(id = id),
        contentDescription = "스토어 홈 아이콘",
        modifier = Modifier
            .size(20.dp),
        tint = Color.Black
    )

    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
fun StoreBusinessHoursSection(businessHours: BusinessHoursResponse, onClick: () -> Unit) {
    val showAll = remember { mutableStateOf(false) }
    val todayWeekDay = remember { mutableStateOf(DateTimeUtils().getTodayWeekday()) }
    val currentTime = remember { mutableStateOf(DateTimeUtils().getCurrentTime()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        StoreHomeIcon(R.drawable.ic_clock)

        when {
            businessHours.businessHours.isNullOrEmpty() -> {
                Text(
                    text = "영업 시간을 설정해주세요.",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = UndefinedColor,
                )
            }
            else -> {
                Text(
                    text = DateTimeUtils().getBusinessHoursText(currentTime = currentTime.value, businessHourData = businessHours.businessHours[todayWeekDay.value]),
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(id = if(!showAll.value) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up),
                    contentDescription = "영업시간 확대/축소",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(
                            onClick = { showAll.value = !showAll.value },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false)
                        ),
                    tint = Color.Black
                )
            }
        }
    }

    AnimatedVisibility(showAll.value && !businessHours.businessHours.isNullOrEmpty()) {
        Column(
            modifier = Modifier.padding(start = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            businessHours.businessHours?.forEachIndexed { index, it ->
                val isToday = index == todayWeekDay.value
                val fontWeight = if(isToday) FontWeight.ExtraBold else FontWeight.Bold

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = DateTimeUtils.DayOfWeek.entries[index].displayName,
                        style = storeMeTextStyle(fontWeight, 0),
                        color = Color.Black
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if(it.isHoliday) {
                            Text(
                                text = "휴무일",
                                style = storeMeTextStyle(fontWeight, 0),
                                color = Color.Black
                            )
                        }

                        if(it.openingTime != null && it.closingTime != null) {
                            Text(
                                text = it.openingTime + " - " + it.closingTime,
                                style = storeMeTextStyle(fontWeight, 0),
                                color = Color.Black
                            )
                        }

                        if(it.startBreak != null && it.endBreak != null) {
                            Text(
                                text = it.startBreak + " - " + it.endBreak + " 브레이크 타임",
                                style = storeMeTextStyle(fontWeight, 0),
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            if(!businessHours.extraInfo.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = businessHours.extraInfo,
                    style = storeMeTextStyle(FontWeight.Bold, 1),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun StoreHolidaySection(businessHours: BusinessHoursResponse, onClick: () -> Unit) {
    val holidays = remember {
        businessHours.businessHours
            ?.mapIndexedNotNull { index, it -> if (it.isHoliday) DateTimeUtils.DayOfWeek.entries[index].displayName else null }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        StoreHomeIcon(R.drawable.ic_calendar)

        when {
            businessHours.businessHours.isNullOrEmpty() -> {
                Text(
                    text = "휴무일을 설정해주세요.",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = UndefinedColor,
                )
            }
            else -> {
                Text(
                    text = if(holidays?.isEmpty() == true) "휴무일 없음" else "매주 " + holidays?.joinToString(", ") + " 휴무",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun StorePhoneNumberSection(phoneNumber: String?, onClick: () -> Unit, onCopy: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        StoreHomeIcon(R.drawable.ic_call)

        when {
            phoneNumber.isNullOrEmpty() -> {
                Text(
                    text = "가게 전화번호를 설정해주세요.",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = UndefinedColor,
                )
            }
            else -> {
                Text(
                    text = PhoneNumberUtils().getStorePhoneNumberAddDashes(phoneNumber),
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.width(4.dp))

                CopyButton {
                    onCopy()
                }
            }
        }
    }
}

@Composable
fun StoreLocationSection(
    storeLocationAddress: String,
    storeLocationDetail: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StoreHomeIcon(R.drawable.ic_marker)

        Text(
            text = storeLocationAddress + "\n" + (storeLocationDetail ?: ""),
            style = storeMeTextStyle(FontWeight.Bold, 0),
            color = Color.Black,
        )
    }

    OwnerHomeNaverMapSection()
}

@Composable
fun OwnerHomeNaverMapSection() {
    val storeDataViewModel = LocalStoreDataViewModel.current

    val storeMapImage by storeDataViewModel.storeMapImage.collectAsState()

    if(storeMapImage != null) {
        AsyncImage(
            model = storeMapImage,
            contentDescription = "가게 지도",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 1f),
        )
    }

}