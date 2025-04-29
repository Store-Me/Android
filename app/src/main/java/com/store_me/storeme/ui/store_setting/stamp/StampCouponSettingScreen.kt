@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.stamp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.store.coupon.StampCouponData
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.SimpleNumberOutLinedTextField
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.theme.CouponDueDateIconColor
import com.store_me.storeme.ui.theme.FinishedColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun StampCouponSettingScreen(
    navController: NavController,
    stampCouponSettingViewModel: StampCouponSettingViewModel = hiltViewModel()
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val focusManager = LocalFocusManager.current

    val stampCoupon by storeDataViewModel.stampCoupon.collectAsState()
    val stampCouponPassword by stampCouponSettingViewModel.stampPassword.collectAsState()

    fun onClose() {
        navController.popBackStack()
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(stampCoupon) {
        if(stampCoupon != null) {
            stampCouponSettingViewModel.getStampPassword()
        }
    }

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier
            .addFocusCleaner(focusManager),
        topBar = { Column {
            TitleWithDeleteButton (
                title = "스탬프 관리",
                onClose = { onClose() }
            )
            if(stampCoupon == null) {
                DefaultButton(
                    buttonText = "스탬프 쿠폰 생성",
                    leftIconResource = R.drawable.ic_circle_plus,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HighlightColor,
                        contentColor = Color.White
                    ),
                    leftIconTint = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    diffValue = 4
                ) {
                    navController.navigate(OwnerRoute.StampCouponCreate.fullRoute)
                }
            }
        }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                when(stampCoupon) {
                    null -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.image_stamp),
                                modifier = Modifier
                                    .padding(top = 40.dp)
                                    .size(350.dp),
                                contentDescription = "스탬프 쿠폰 없음",
                                alignment = Alignment.Center
                            )
                        }
                    }
                    else -> {
                        StampCouponItem(
                            stampCoupon = stampCoupon!!
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        when(stampCoupon!!.rewardInterval) {
                            5 -> {
                                RewardItem(indexText = "보상 1",rewardText = stampCoupon!!.rewardFor5 ?: "올바르지 않은 값입니다.")
                                Spacer(modifier = Modifier.height(8.dp))
                                RewardItem(indexText = "보상 2",rewardText = stampCoupon!!.rewardFor10)
                            }
                            10 -> {
                                RewardItem(indexText = "보상 2",rewardText = stampCoupon!!.rewardFor10)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        StampPassword(stampCouponPassword = stampCouponPassword) {
                            stampCouponSettingViewModel.patchStampCouponPassword(it)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun StampCouponItem(
    currentStampCount: Int = 0,
    stampCoupon: StampCouponData
) {
    var iconSize by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = SubHighlightColor, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            StampCouponIcon(
                modifier = Modifier
                    .size(iconSize),
                isActive = currentStampCount >= 1,
                isReward = false
            )

            StampCouponIcon(
                modifier = Modifier
                    .size(iconSize),
                isActive = currentStampCount >= 2,
                isReward = false
            )

            StampCouponIcon(
                modifier = Modifier
                    .size(iconSize),
                isActive = currentStampCount >= 3,
                isReward = false
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            StampCouponIcon(
                modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned { iconSize = with(localDensity) { it.size.width.toDp() } },
                isActive = currentStampCount >= 4,
                isReward = false
            )

            StampCouponIcon(
                modifier = Modifier.weight(1f),
                isActive = currentStampCount >= 5,
                isReward = stampCoupon.rewardInterval == 5
            )

            StampCouponIcon(
                modifier = Modifier.weight(1f),
                isActive = currentStampCount >= 6,
                isReward = false
            )

            StampCouponIcon(
                modifier = Modifier.weight(1f),
                isActive = currentStampCount >= 7,
                isReward = false
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            StampCouponIcon(
                modifier = Modifier
                    .size(iconSize),
                isActive = currentStampCount >= 8,
                isReward = false
            )

            StampCouponIcon(
                modifier = Modifier
                    .size(iconSize),
                isActive = currentStampCount >= 9,
                isReward = false
            )

            StampCouponIcon(
                modifier = Modifier
                    .size(iconSize),
                isActive = currentStampCount >= 10,
                isReward = true
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(thickness = 2.dp, color = Color.White)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "스탬프 유효 기간",
                style = storeMeTextStyle(FontWeight.ExtraBold, 0),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = DateTimeUtils.formatToDate(dueDateText = stampCoupon.dueDate),
                style = storeMeTextStyle(FontWeight.ExtraBold, 0),
            )
        }
    }
}

@Composable
fun StampCouponIcon(
    modifier: Modifier,
    isActive: Boolean,
    isReward: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            modifier = Modifier
                .aspectRatio(1f),
            painter = painterResource(if(isActive) R.drawable.ic_stamp_on else R.drawable.ic_stamp_off),
            contentDescription = "스탬프 쿠폰 아이콘"
        )

        if(isReward) {
            Text(
                text = "보상",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = Color.White,
                modifier = Modifier
                    .background(color = CouponDueDateIconColor, shape = CircleShape)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun RewardItem(indexText: String, rewardText: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = CouponDueDateIconColor, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = indexText,
            style = storeMeTextStyle(FontWeight.ExtraBold, 0),
            color = CouponDueDateIconColor,
            modifier = Modifier
                .background(color = Color.White, shape = CircleShape)
                .padding(8.dp)
        )

        Text(
            text = rewardText,
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StampPassword(stampCouponPassword: String?, onStampPasswordChange: (String) -> Unit) {
    var isHide by remember { mutableStateOf(true) }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    val passwordError by remember { derivedStateOf {
        password.isNotEmpty() && password.length != 4
    } }
    val passwordConfirmError by remember { derivedStateOf {
        passwordConfirm.isNotEmpty() && passwordConfirm != password
    } }

    Text(
        text = "스탬프 비밀번호",
        style = storeMeTextStyle(FontWeight.ExtraBold, 6)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = SubHighlightColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if(isHide) "****" else stampCouponPassword ?: "다시 시도해주세요.",
            style = storeMeTextStyle(FontWeight.ExtraBold, 12)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { isHide = !isHide }
        ) {
            Icon(
                painter = painterResource(if(isHide) R.drawable.ic_hide else R.drawable.ic_show),
                contentDescription = "보이기/숨기기",
                modifier = Modifier
                    .size(24.dp),
                tint = FinishedColor
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.width(IntrinsicSize.Max)) {
            DefaultButton(
                buttonText = "비밀번호 변경",
                diffValue = 0,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                showBottomSheet = true
            }
        }
    }

    if(showBottomSheet) {
        DefaultBottomSheet(sheetState = sheetState, onDismiss = { showBottomSheet = false }) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "새로운 비밀번호",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleNumberOutLinedTextField(
                    text = password,
                    placeholderText = "4자리 숫자로 이루어진 비밀번호를 입력해주세요.",
                    onValueChange = { password = it },
                    isError = passwordError,
                    errorText = "4자리 숫자로 이루어진 비밀번호를 입력해주세요.",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "비밀번호 확인",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleNumberOutLinedTextField(
                    text = passwordConfirm,
                    placeholderText = "동일한 비밀번호를 입력해주세요.",
                    onValueChange = { passwordConfirm = it },
                    isError = passwordConfirmError,
                    errorText = "동일한 비밀번호를 입력해주세요.",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                DefaultButton(
                    buttonText = "저장",
                    diffValue = 2,
                    enabled = !passwordError && !passwordConfirmError && password.isNotEmpty() && passwordConfirm.isNotEmpty()
                ) {
                    showBottomSheet = false
                    onStampPasswordChange(password)
                    password = ""
                    passwordConfirm = ""
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}