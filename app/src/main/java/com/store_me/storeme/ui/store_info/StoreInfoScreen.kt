package com.store_me.storeme.ui.store_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.status_bar.StatusBarPadding
import com.store_me.storeme.ui.theme.MyMenuIconColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

/**
 * 스토어 정보 화면 Composable
 */
@Composable
fun StoreInfoScreen(
    navController: NavController,
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        StatusBarPadding()

        TitleWithSettingNotificationRow(
            stringId = R.string.store_info,
            hasNewNotification = true,
            onSettingClick = {

            },
            onNotificationClick = {

            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))

                storeInfoData?.let {
                    StoreProfile(
                        storeInfoData = it,
                        onSwapClick = {

                        },
                        onStampRankClick = {

                        },
                        onManageSurveyClick = {

                        },
                        onManageVoteClick = {

                        }
                    )
                }
            }

            item {

            }
        }
    }
}

/**
 * Title, 알림, 설정 아이콘 상단 Composable
 * @param stringId 타이틀 문자열 리소스 ID
 * @param hasNewNotification 새로운 알림이 있는지 여부
 * @param onSettingClick 설정 아이콘 클릭 콜백
 * @param onNotificationClick 알림 아이콘 클릭 콜백
 */
@Composable
fun TitleWithSettingNotificationRow(
    stringId: Int,
    hasNewNotification: Boolean,
    onSettingClick: () -> Unit,
    onNotificationClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(stringId),
            style = storeMeTextStyle(FontWeight.ExtraBold, 6)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onNotificationClick() }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id =
                        if(hasNewNotification)
                            R.drawable.ic_notification_on
                        else
                            R.drawable.ic_notification_off
                ),
                contentDescription = "알림",
                modifier = Modifier
                    .size(24.dp),
                tint = Color.Unspecified
            )
        }

        IconButton(
            onClick = { onSettingClick() }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = R.drawable.ic_my_menu_setting
                ),
                contentDescription = "설정",
                modifier = Modifier
                    .size(24.dp),
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * 가게 프로필 Composable
 * @param storeInfoData 가게 정보 데이터
 */
@Composable
fun StoreProfile(
    storeInfoData: StoreInfoData,
    onSwapClick: () -> Unit,
    onStampRankClick: () -> Unit,
    onManageSurveyClick: () -> Unit,
    onManageVoteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black, shape = RoundedCornerShape(topStart = COMPOSABLE_ROUNDING_VALUE, topEnd = COMPOSABLE_ROUNDING_VALUE))
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ProfileImage(
                accountType = AccountType.OWNER,
                url = storeInfoData.storeProfileImage,
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = CircleShape)
            )

            Text(
                text = storeInfoData.storeName,
                style = storeMeTextStyle(fontWeight = FontWeight.ExtraBold, 6, Color.White),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
            )

            Button(
                modifier = Modifier
                    .wrapContentSize(),
                onClick = { onSwapClick },
                shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE),
                border = BorderStroke(2.dp, Color.White),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Black
                ),
                content = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_change),
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "계정 전환",
                        style = storeMeTextStyle(fontWeight = FontWeight.Bold, 0),
                    )
                },
                contentPadding =  PaddingValues(top = 0.dp, bottom = 0.dp, start = 12.dp, end = 12.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black, shape = RoundedCornerShape(bottomStart = COMPOSABLE_ROUNDING_VALUE, bottomEnd = COMPOSABLE_ROUNDING_VALUE)),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileBoxButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = "스탬프 랭킹",
                painter = painterResource(R.drawable.ic_regular_customer),
                onClick = { onStampRankClick() }
            )

            ProfileBoxButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = "설문 관리",
                painter = painterResource(R.drawable.ic_survey),
                onClick = { onManageSurveyClick() }
            )

            ProfileBoxButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = "투표 관리",
                painter = painterResource(R.drawable.ic_vote),
                onClick = { onManageVoteClick() }
            )
        }
    }
}

@Composable
private fun ProfileBoxButton(
    modifier: Modifier,
    buttonText: String,
    painter: Painter,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxSize(),
        onClick = { onClick },
        shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Transparent
        ),
        content = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painter,
                contentDescription = null,
                tint = MyMenuIconColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = buttonText,
                style = storeMeTextStyle(fontWeight = FontWeight.Bold, 0),
            )
        },
        contentPadding =  PaddingValues(top = 0.dp, bottom = 0.dp, start = 12.dp, end = 12.dp)
    )
}