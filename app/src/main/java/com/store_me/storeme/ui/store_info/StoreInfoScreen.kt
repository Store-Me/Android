package com.store_me.storeme.ui.store_info

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.status_bar.StatusBarPadding
import com.store_me.storeme.ui.theme.MyMenuIconColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

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
                .fillMaxSize()
        ) {
            item {
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
            style = storeMeTextStyle(FontWeight.W900, 6)
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
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black, shape = RoundedCornerShape(topStart = COMPOSABLE_ROUNDING_VALUE, topEnd = COMPOSABLE_ROUNDING_VALUE))
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ProfileImage(
                accountType = AccountType.OWNER,
                url = storeInfoData.storeProfileImage,
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = CircleShape)
            )

            Text(
                text = storeInfoData.storeName,
                style = storeMeTextStyle(fontWeight = FontWeight.ExtraBold, 6, Color.White),
                maxLines = 1,
                overflow = TextOverflow.Visible,
                modifier = Modifier
                    .weight(1f)
            )

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                DefaultButton(
                    modifier = Modifier
                        .border(1.dp, color = Color.White, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)),
                    buttonText = "계정 전환",
                    diffValue = 0,
                    leftIconResource = R.drawable.ic_change,
                    leftIconTint = Color.White
                ) { onSwapClick() }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black, shape = RoundedCornerShape(bottomStart = COMPOSABLE_ROUNDING_VALUE, bottomEnd = COMPOSABLE_ROUNDING_VALUE)),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = "스탬프 랭킹",
                diffValue = 0,
                fontWeight = FontWeight.Bold,
                leftIconResource = R.drawable.ic_regular_customer,
                leftIconTint = MyMenuIconColor
            ) { onStampRankClick() }

            DefaultButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = "설문 관리",
                diffValue = 0,
                fontWeight = FontWeight.Bold,
                leftIconResource = R.drawable.ic_survey,
                leftIconTint = MyMenuIconColor
            ) { onManageSurveyClick() }

            DefaultButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = "투표 관리",
                diffValue = 0,
                fontWeight = FontWeight.Bold,
                leftIconResource = R.drawable.ic_vote,
                leftIconTint = MyMenuIconColor
            ) { onManageVoteClick() }
        }
    }

}