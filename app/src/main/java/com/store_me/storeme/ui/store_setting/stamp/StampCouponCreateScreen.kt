@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.stamp

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.DefaultToggleButton
import com.store_me.storeme.ui.component.SimpleNumberOutLinedTextField
import com.store_me.storeme.ui.component.SimpleOutLinedTextField
import com.store_me.storeme.ui.component.StoreMeSelectDateCalendar
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.signup.GuideTextBoxItem
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import java.time.LocalDate

@Composable
fun StampCouponCreateScreen(
    navController: NavController,
    stampCouponCreateViewModel: StampCouponCreateViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val loadingViewModel = LocalLoadingViewModel.current
    val storeDataViewModel = LocalStoreDataViewModel.current

    val name by stampCouponCreateViewModel.name.collectAsState()
    val rewardInterval by stampCouponCreateViewModel.rewardInterval.collectAsState()
    val rewardFor5 by stampCouponCreateViewModel.rewardFor5.collectAsState()
    val rewardFor10 by stampCouponCreateViewModel.rewardFor10.collectAsState()
    val dueDate by stampCouponCreateViewModel.dueDate.collectAsState()
    val password by stampCouponCreateViewModel.password.collectAsState()
    val description by stampCouponCreateViewModel.description.collectAsState()

    val createdStampCoupon by stampCouponCreateViewModel.createdStampCoupon.collectAsState()

    var showBackWarningDialog by remember { mutableStateOf(false) }

    fun onClose() {
        showBackWarningDialog = true
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(createdStampCoupon) {
        if(createdStampCoupon != null) {
            storeDataViewModel.updateStampCoupon(createdStampCoupon!!)

            navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier
            .addFocusCleaner(focusManager),
        topBar = {  TitleWithDeleteButton (
            title = "스탬프 만들기",
            onClose = { onClose() }
        ) },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item {
                    StampCouponNameSection(
                        name = name,
                        onNameChange = {
                            stampCouponCreateViewModel.updateName(it)
                        }
                    )
                }

                item {
                    StampCouponRewardSection(
                        rewardInterval = rewardInterval,
                        rewardFor5 = rewardFor5,
                        rewardFor10 = rewardFor10,
                        onRewardIntervalChange = { stampCouponCreateViewModel.updateRewardInterval(it) },
                        onRewardFor5Change = { stampCouponCreateViewModel.updateRewardFor5(it) },
                        onRewardFor10Change = { stampCouponCreateViewModel.updateRewardFor10(it) }
                    )
                }

                item {
                    StampCouponDueDateSection(
                        dueDate = dueDate,
                        onDueDateChang = { stampCouponCreateViewModel.updateDueDate(it) }
                    )
                }

                item {
                    StampCouponPasswordSection(
                        password = password,
                        onPasswordChange = { stampCouponCreateViewModel.updatePassword(it) }
                    )
                }

                item {
                    StampCouponDescriptionSection(
                        description = description,
                        onDescriptionChange = { stampCouponCreateViewModel.updateDescription(it) },
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "스탬프 추가",
                        diffValue = 4,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {
                        loadingViewModel.showLoading()

                        stampCouponCreateViewModel.postStampCoupon()
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    )

    if(showBackWarningDialog) {
        BackWarningDialog(
            onDismiss = { showBackWarningDialog = false },
            onAction = {
                showBackWarningDialog = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun StampCouponNameSection(
    name: String,
    onNameChange: (String) -> Unit,
) {
    val isError by remember(name) { derivedStateOf {
        name.length > 20
    } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "스탬프 쿠폰 이름",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SimpleOutLinedTextField(
            text = name,
            onValueChange = { onNameChange(it) },
            placeholderText = "스탬프 쿠폰 이름을 입력하세요.",
            isError = isError,
            errorText = "20자 이내로 작성해주세요."
        )

        TextLengthRow(text = name, limitSize = 20)
    }
}

@Composable
fun StampCouponRewardSection(
    rewardInterval: Int,
    rewardFor5: String?,
    rewardFor10: String,
    onRewardIntervalChange: (Int) -> Unit,
    onRewardFor5Change: (String?) -> Unit,
    onRewardFor10Change: (String) -> Unit
) {
    val isRewardFor5Error by remember(rewardFor5) { derivedStateOf {
        (rewardFor5?.length ?: 0) > 20
    } }

    val isRewardFor10Error by remember(rewardFor10) { derivedStateOf {
        rewardFor10.length > 20
    } }

    DefaultHorizontalDivider()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "스탬프 보상",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultToggleButton(
                buttonText = "5개 보상",
                isSelected = rewardInterval == 5,
            ) {
                when(rewardInterval) {
                    5 -> onRewardIntervalChange(10)
                    else -> onRewardIntervalChange(5)
                }
            }

            DefaultToggleButton(
                buttonText = "10개 보상",
                isSelected = true,
            ) {  }

            Spacer(modifier = Modifier.weight(1f))
        }

        GuideTextBoxItem(
            title = "스탬프 보상 설정 가이드",
            content = "스탬프 보상은 10개가 기본 값이에요.\n\n5개 보상도 추가할 수 있어요.\n\n아래의 보상 입력을 통해 손님들에게 쿠폰의 보상을 안내할 수 있어요."
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(visible = rewardInterval == 5) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "스탬프 5개 보상",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleOutLinedTextField(
                    text = rewardFor5 ?: "",
                    onValueChange = { onRewardFor5Change(it) },
                    placeholderText = "스탬프 5개 보상을 입력해주세요.",
                    isError = isRewardFor5Error,
                    errorText = "20자 이내로 작성해주세요."
                )

                TextLengthRow(text = rewardFor5 ?: "", limitSize = 20)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "스탬프 10개 보상",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2)
            )

            Spacer(modifier = Modifier.height(12.dp))

            SimpleOutLinedTextField(
                text = rewardFor10,
                onValueChange = { onRewardFor10Change(it) },
                placeholderText = "스탬프 10개 보상을 입력해주세요.",
                isError = isRewardFor10Error,
                errorText = "20자 이내로 작성해주세요."
            )

            TextLengthRow(text = rewardFor10, limitSize = 20)
        }
    }
}

@Composable
fun StampCouponDueDateSection(
    dueDate: String?,
    onDueDateChang: (LocalDate?) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    DefaultHorizontalDivider()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "스탬프 유효기한",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SimpleOutLinedTextField(
            modifier = Modifier
                .clickable(
                    onClick = { showBottomSheet = true },
                    indication = null,
                    interactionSource = null
                ),
            text = DateTimeUtils.convertExpiredDateToKorean(dueDate),
            placeholderText = "유효 기한을 선택해주세요.",
            onValueChange = {},
            trailingIconResource = R.drawable.ic_calendar,
            isError = false,
            errorText = "",
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Black,
                disabledTextColor = Color.Black
            )
        )
    }

    if(showBottomSheet) {
        DefaultBottomSheet(onDismiss = { showBottomSheet = false }, sheetState = sheetState) {
            var selectedDueDate by remember { mutableStateOf<LocalDate?>(null) }

            StoreMeSelectDateCalendar(onDateChange = {
                selectedDueDate = it
            })

            Spacer(modifier = Modifier.height(20.dp))

            DefaultButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                buttonText = "확인",
                enabled = selectedDueDate != null
            ) {
                onDueDateChang(selectedDueDate)
                showBottomSheet = false
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StampCouponPasswordSection(
    password: String,
    onPasswordChange: (String) -> Unit
) {
    var passwordConfirm by remember { mutableStateOf("") }

    val passwordError by remember(password) { derivedStateOf {
        password.isNotEmpty() && password.length != 4
    } }
    val passwordConfirmError by remember(passwordConfirm) { derivedStateOf {
        passwordConfirm.isNotEmpty() && passwordConfirm != password
    } }

    DefaultHorizontalDivider()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "스탬프 비밀번호 설정 (숫자 4자리)",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SimpleNumberOutLinedTextField(
            text = password,
            isPassword = true,
            onValueChange = { onPasswordChange(it) },
            placeholderText = "스탬프 쿠폰 비밀번호를 입력해주세요.",
            isError = passwordError,
            errorText = "4자리 숫자로 이루어진 비밀번호를 입력해주세요."
        )

        Spacer(modifier = Modifier.height(8.dp))

        SimpleNumberOutLinedTextField(
            text = passwordConfirm,
            isPassword = true,
            onValueChange = { passwordConfirm = it },
            placeholderText = "동일한 비밀번호를 입력해주세요.",
            isError = passwordConfirmError,
            errorText = "동일한 비밀번호를 입력해주세요."
        )
    }
}

@Composable
fun StampCouponDescriptionSection(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    DefaultHorizontalDivider()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "스탬프 쿠폰 설명",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = description,
            onValueChange = { onDescriptionChange(it) },
            textStyle = storeMeTextStyle(FontWeight.Normal, 1),
            placeholder = {
                Text(
                    text = "스탬프 쿠폰 설명을 입력하세요.\n",
                    style = storeMeTextStyle(FontWeight.Normal, 1),
                    color = GuideColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp) //최소 높이
                .padding(horizontal = 4.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
            singleLine = false,
            minLines = 2    //1 -> 2줄 변화시 글자 크기 문제 해결
        )
    }
}