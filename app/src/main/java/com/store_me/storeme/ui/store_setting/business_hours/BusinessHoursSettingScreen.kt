@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.business_hours

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.TimeData
import com.store_me.storeme.data.enums.BusinessHoursType
import com.store_me.storeme.data.enums.progress.BusinessHoursSettingProgress
import com.store_me.storeme.data.getText
import com.store_me.storeme.data.store.TemporaryBusinessData
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.CircleToggleButton
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.DefaultToggleButton
import com.store_me.storeme.ui.component.StoreMeTimePicker
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.signup.GuideTextBoxItem
import com.store_me.storeme.ui.theme.ErrorColor
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.LighterHighlightColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun BusinessHoursSettingScreen(
    navController: NavController,
    businessHoursSettingViewModel: BusinessHoursSettingViewModel = viewModel(),
    temporaryBusinessSettingViewModel: TemporaryBusinessSettingViewModel = viewModel(),
    businessHoursSettingProgressViewModel: BusinessHoursSettingProgressViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    val storeDataViewModel = LocalStoreDataViewModel.current
    val auth = LocalAuth.current

    val originalBusinessHours by storeDataViewModel.businessHours.collectAsState()

    val progressState by businessHoursSettingProgressViewModel.progressState.collectAsState()
    val hasHoliday by businessHoursSettingViewModel.hasHoliday.collectAsState()

    val showBackWarningDialog = remember { mutableStateOf(false) }

    fun onClose() {
        when(progressState) {
            BusinessHoursSettingProgress.HOLIDAY -> {
                if(hasHoliday == null) {
                    navController.popBackStack()
                } else {
                    showBackWarningDialog.value = true
                }
            }
            BusinessHoursSettingProgress.BUSINESS_HOURS -> {
                businessHoursSettingProgressViewModel.moveToPreviousProgress()
            }
        }
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(originalBusinessHours) {
        if(originalBusinessHours == null || originalBusinessHours?.businessHours.isNullOrEmpty())
            return@LaunchedEffect

        if(originalBusinessHours!!.businessHours?.any { it.isHoliday } == true) {
            //휴일 존재 여부 동기화
            businessHoursSettingViewModel.updateHasHoliday(true)

            //휴일 요일 동기화
            originalBusinessHours!!.businessHours?.forEachIndexed { index, businessHourData ->
                if(businessHourData.isHoliday) {
                    businessHoursSettingViewModel.updateHolidayWeeks(DateTimeUtils.DayOfWeek.entries[index])
                }
            }
        } else {
            //휴일 존재 여부 동기화
            businessHoursSettingViewModel.updateHasHoliday(false)
        }

        businessHoursSettingViewModel.updateExtraInfo(originalBusinessHours!!.extraInfo ?: "")

        //영업 시간 동기화
        originalBusinessHours!!.businessHours?.forEachIndexed { index, businessHourData ->
            if(businessHourData.isHoliday) {

            } else {
                if(businessHoursSettingViewModel.areAllBusinessSettingsSame()) {
                    businessHoursSettingViewModel.updateHasSameBusinessHours(true)
                } else {
                    businessHoursSettingViewModel.updateHasSameBusinessHours(false)
                }

                //휴일 아닌 경우
                businessHoursSettingViewModel.updateStartBusinessTime(index, DateTimeUtils().getTimeData(businessHourData.openingTime))
                businessHoursSettingViewModel.updateEndBusinessTime(index, DateTimeUtils().getTimeData(businessHourData.closingTime))
                businessHoursSettingViewModel.updateStartBreakTime(index, DateTimeUtils().getTimeData(businessHourData.startBreak))
                businessHoursSettingViewModel.updateEndBreakTime(index, DateTimeUtils().getTimeData(businessHourData.endBreak))

                if(businessHourData.openingTime == null && businessHourData.closingTime == null)
                    businessHoursSettingViewModel.updateIsAlwaysOpen(index, true)
                else
                    businessHoursSettingViewModel.updateIsAlwaysOpen(index, false)

                if(businessHourData.startBreak == null && businessHourData.endBreak == null)
                    businessHoursSettingViewModel.updateHasBreakTime(index, false)
                else
                    businessHoursSettingViewModel.updateHasBreakTime(index, true)
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        topBar = { TitleWithDeleteButton(title = "영업 시간 관리") {
            onClose()
        } },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
            ) {
                when(progressState) {
                    BusinessHoursSettingProgress.HOLIDAY -> {
                        HolidaySettingSection(
                            businessHoursSettingViewModel = businessHoursSettingViewModel,
                            temporaryBusinessSettingViewModel = temporaryBusinessSettingViewModel
                        ) {
                            businessHoursSettingProgressViewModel.moveToNextProgress()
                        }
                    }
                    BusinessHoursSettingProgress.BUSINESS_HOURS -> {
                        BusinessHoursSettingSection(
                            businessHoursSettingViewModel = businessHoursSettingViewModel
                        ) {
                            storeDataViewModel.patchBusinessHours(
                                storeId = auth.storeId.value!!,
                                businessHours = businessHoursSettingViewModel.getBusinessHours(),
                                extraInfo = businessHoursSettingViewModel.extraInfo.value
                            )
                        }
                    }
                }
            }
        }
    )

    if(showBackWarningDialog.value) {
        BackWarningDialog(
            onDismiss = { showBackWarningDialog.value = false },
            onAction = {
                showBackWarningDialog.value = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun HolidaySettingSection(
    businessHoursSettingViewModel: BusinessHoursSettingViewModel,
    temporaryBusinessSettingViewModel: TemporaryBusinessSettingViewModel,
    onFinish: () -> Unit
) {
    val hasHoliday by businessHoursSettingViewModel.hasHoliday.collectAsState()
    val holidayWeeks by businessHoursSettingViewModel.holidayWeeks.collectAsState()

    val temporaryBusinesses by temporaryBusinessSettingViewModel.temporaryBusinesses.collectAsState()

    val isFinished by remember {
        derivedStateOf { (hasHoliday == true && holidayWeeks.isNotEmpty()) || (hasHoliday == false) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "휴무일이 있나요?",
            style = storeMeTextStyle(fontWeight = FontWeight.ExtraBold, changeSizeValue = 6),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        //휴무 여부 확인
        CheckHolidayButtonSection(hasHoliday) {
            businessHoursSettingViewModel.updateHasHoliday(it)
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(hasHoliday == true) {
            SelectHolidaySection(holidayWeeks = holidayWeeks) {
                businessHoursSettingViewModel.updateHolidayWeeks(it)
            }
        }

        TemporaryBusinessSection {
            //TODO 추가 바텀시트
        }

        Spacer(modifier = Modifier.height(12.dp))

        when(temporaryBusinesses.isEmpty()) {
            true -> {
                GuideTextBoxItem(
                    title = "임시 영업일이란?",
                    content = "평상시와 다르게 운영하는 날짜가 있다면\n" +
                            "휴무일로 설정하거나 영업 시간을 조정할 수 있어요.",
                )

                Spacer(modifier = Modifier.weight(1f))
            }
            false -> {
                TemporaryBusinessListSection(
                    modifier = Modifier.weight(1f),
                    temporaryBusinesses = temporaryBusinesses
                ) { index ->
                    //TODO 메뉴 클릭
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        DefaultButton(
            buttonText = "다음",
            enabled = isFinished
        ) {
            onFinish()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * 휴무 여부 설정 Composable
 */
@Composable
fun CheckHolidayButtonSection(hasHoliday: Boolean?, onSelected: (Boolean) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultToggleButton(
            buttonText = "있음",
            isSelected = hasHoliday == true,
        ) {
            onSelected(true)
        }

        DefaultToggleButton(
            buttonText = "없음",
            isSelected = hasHoliday == false,
        ) {
            onSelected(false)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * 휴무일 요일 선택 Composable
 */
@Composable
fun SelectHolidaySection(holidayWeeks: Set<DateTimeUtils. DayOfWeek>, onSelected: (DateTimeUtils.DayOfWeek) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "정기 휴무일",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(DateTimeUtils.DayOfWeek.entries) {
                DefaultToggleButton(
                    buttonText = it.displayName,
                    isSelected = it in holidayWeeks
                ) {
                    onSelected(it)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * 임시 영업일 설정 Composable
 */
@Composable
fun TemporaryBusinessSection(onAddTemporaryOpen: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "임시 영업일",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultButton(
            buttonText = "임시 영업일 추가",
            colors = ButtonDefaults.buttonColors(
                containerColor = SubHighlightColor,
                contentColor = Color.Black
            )
        ) {
            onAddTemporaryOpen()
        }
    }
}

/**
 * 임시 영업일 목록
 */
@Composable
fun TemporaryBusinessListSection(modifier: Modifier, temporaryBusinesses: List<TemporaryBusinessData>, onMenuClick: (Int) -> Unit) {
    LazyColumn (
        modifier = modifier
            .fillMaxWidth(),
    ) {
        itemsIndexed(temporaryBusinesses) { index, it ->
            TemporaryBusinessItem(it) {
                onMenuClick(index)
            }
        }
    }
}

@Composable
fun TemporaryBusinessItem(temporaryBusinessData: TemporaryBusinessData, onMenuClick: () -> Unit) {
    val startDatText = remember { DateTimeUtils().getDateString(temporaryBusinessData.startDate, showYear = false, showDay = true) }
    val endDateText = remember { DateTimeUtils().getDateString(temporaryBusinessData.endDate, showYear = false, showDay = true) }

    val businessHoursText = remember {
        when(temporaryBusinessData.businessHour.isHoliday) {
            true -> {
                "휴무"
            }
            false -> {
                //조정
                if(temporaryBusinessData.businessHour.openingTime != null && temporaryBusinessData.businessHour.closingTime != null) {
                    "영업 시간 : " + temporaryBusinessData.businessHour.openingTime + " - " + temporaryBusinessData.businessHour.closingTime
                } else if(temporaryBusinessData.businessHour.openingTime == null && temporaryBusinessData.businessHour.closingTime == null) {
                    "24시간 영업"
                } else {
                    "영업 시간 정보가 올바르지 않습니다."
                }
            }
        }
    }

    val breaktimeText = remember {
        when(temporaryBusinessData.businessHour.isHoliday) {
            true -> {
                null
            }
            false -> {
                if(temporaryBusinessData.businessHour.startBreak != null && temporaryBusinessData.businessHour.endBreak != null) {
                    "브레이크타임 : " + temporaryBusinessData.businessHour.startBreak + " - " + temporaryBusinessData.businessHour.endBreak
                } else {
                    null
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if(startDatText != endDateText) "$startDatText ~ $endDateText" else startDatText,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = Color.Black
            )

            Text(
                text = businessHoursText,
                style = storeMeTextStyle(FontWeight.Normal, 0),
                modifier = Modifier.fillMaxWidth()
            )

            breaktimeText?.let {
                Text(
                    text = it,
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        IconButton(
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = GuideColor
            ),
            interactionSource = remember { MutableInteractionSource() },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .size(16.dp)
                )
            },
            onClick = {
                onMenuClick()
            }
        )
    }
}

/**
 * 영업 시간 설정 화면
 */
@Composable
fun BusinessHoursSettingSection(businessHoursSettingViewModel: BusinessHoursSettingViewModel, onFinish: () -> Unit) {
    val hasSameBusinessHours by businessHoursSettingViewModel.hasSameBusinessHours.collectAsState()

    val startBusinessTimes by businessHoursSettingViewModel.startBusinessTimes.collectAsState()
    val endBusinessTimes by businessHoursSettingViewModel.endBusinessTimes.collectAsState()
    val startBreakTimes by businessHoursSettingViewModel.startBreakTimes.collectAsState()
    val endBreakTimes by businessHoursSettingViewModel.endBreakTimes.collectAsState()

    val isFinished by remember(startBusinessTimes, endBusinessTimes, startBreakTimes, endBreakTimes) {
        derivedStateOf {
            businessHoursSettingViewModel.areAllFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = "영업 시간을 설정해주세요",
                    style = storeMeTextStyle(fontWeight = FontWeight.ExtraBold, changeSizeValue = 6),
                    color = Color.Black
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                //영업 시간 동일 여부 확인
                CheckSameBusinessHours(hasSameBusinessHours) {
                    businessHoursSettingViewModel.updateHasSameBusinessHours(it)
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                AnimatedContent(targetState = hasSameBusinessHours) {
                    when(it) {
                        true -> {
                            SameBusinessHoursSection(
                                businessHoursSettingViewModel = businessHoursSettingViewModel
                            )
                        }
                        false -> {
                            DifferentBusinessHoursSection(
                                businessHoursSettingViewModel = businessHoursSettingViewModel
                            )
                        }
                        else -> {  }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                ExtraDataSection(businessHoursSettingViewModel = businessHoursSettingViewModel)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        DefaultButton(
            buttonText = "저장",
            enabled = if(hasSameBusinessHours == false) isFinished else if(hasSameBusinessHours == true) true else false
        ) {
            onFinish()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CheckSameBusinessHours(hasSameBusinessHours: Boolean?, onSelected: (Boolean) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultToggleButton(
            buttonText = "매일 같음",
            isSelected = hasSameBusinessHours == true,
        ) {
            onSelected(true)
        }

        DefaultToggleButton(
            buttonText = "요일마다 설정",
            isSelected = hasSameBusinessHours == false,
        ) {
            onSelected(false)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SameBusinessHoursSection(
    businessHoursSettingViewModel: BusinessHoursSettingViewModel
) {
    val isAlwaysOpen = remember { mutableStateOf(false) }
    val hasBreakTime = remember { mutableStateOf(false) }
    val startBusinessTime = remember { mutableStateOf(TimeData(9, 0)) }
    val endBusinessTime = remember { mutableStateOf(TimeData(21, 0)) }
    val startBreakTime = remember { mutableStateOf(TimeData(15, 0)) }
    val endBreakTime = remember { mutableStateOf(TimeData(17, 0)) }

    val currentType = remember { mutableStateOf<BusinessHoursType?>(null) }

    LaunchedEffect(Unit) {
        isAlwaysOpen.value = businessHoursSettingViewModel.isAlwaysOpen.value[0]
        hasBreakTime.value = businessHoursSettingViewModel.hasBreakTime.value[0]
        startBusinessTime.value = businessHoursSettingViewModel.startBusinessTimes.value[0] ?: TimeData(9, 0)
        endBusinessTime.value = businessHoursSettingViewModel.endBusinessTimes.value[0] ?: TimeData(21, 0)
        startBreakTime.value = businessHoursSettingViewModel.startBreakTimes.value[0] ?: TimeData(15, 0)
        endBreakTime.value = businessHoursSettingViewModel.endBreakTimes.value[0] ?: TimeData(17, 0)
    }

    LaunchedEffect(isAlwaysOpen.value) {
        businessHoursSettingViewModel.updateIsAlwaysOpen(value = isAlwaysOpen.value)
    }

    LaunchedEffect(hasBreakTime.value) {
        businessHoursSettingViewModel.updateHasBreakTime(value = hasBreakTime.value)
    }

    LaunchedEffect(startBusinessTime.value) {
        businessHoursSettingViewModel.updateStartBusinessTime(newTime = startBusinessTime.value)
    }

    LaunchedEffect(endBusinessTime.value) {
        businessHoursSettingViewModel.updateEndBusinessTime(newTime = endBusinessTime.value)
    }

    LaunchedEffect(startBreakTime.value) {
        businessHoursSettingViewModel.updateStartBreakTime(newTime = startBreakTime.value)
    }

    LaunchedEffect(endBreakTime.value) {
        businessHoursSettingViewModel.updateEndBreakTime(newTime = endBreakTime.value)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "영업 시간",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            )

            Spacer(modifier = Modifier.weight(1f))

            DefaultCheckButton(text = "24시간 영업", isSelected = isAlwaysOpen.value) {
                isAlwaysOpen.value = !isAlwaysOpen.value
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(!isAlwaysOpen.value) {
            //24시간이 아닌 경우 TimePicker
            TimePickerWithStartToEnd(
                startTime = startBusinessTime.value,
                endTime = endBusinessTime.value,
                openType = BusinessHoursType.BUSINESS_HOURS,
                currentType = currentType.value,
                onStartTimeChange = { startBusinessTime.value = it },
                onEndTimeChange = { endBusinessTime.value = it },
                onTypeChange = { currentType.value = it }
            )
        }

        DefaultButton(
            buttonText = if(hasBreakTime.value) "브레이크타임 제거" else "브레이크타임 추가",
            diffValue = 1,
            colors = ButtonDefaults.buttonColors(
                containerColor = SubHighlightColor,
                contentColor = Color.Black
            )
        ) {
            hasBreakTime.value = !hasBreakTime.value
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(hasBreakTime.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "브레이크타임",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                TimePickerWithStartToEnd(
                    startTime = startBreakTime.value,
                    endTime = endBreakTime.value,
                    openType = BusinessHoursType.BREAK_TIME,
                    currentType = currentType.value,
                    onStartTimeChange = { startBreakTime.value = it },
                    onEndTimeChange = { endBreakTime.value = it },
                    onTypeChange = { currentType.value = it }
                )
            }
        }
    }
}

/**
 * 시작 시간과 종료 시간이 포함된 TimePicker
 */
@Composable
fun TimePickerWithStartToEnd(
    startTime: TimeData,
    endTime: TimeData,
    openType: BusinessHoursType,
    currentType: BusinessHoursType?,
    onStartTimeChange: (TimeData) -> Unit,
    onEndTimeChange: (TimeData) -> Unit,
    onTypeChange: (BusinessHoursType) -> Unit
) {
    val isStartTime = remember { mutableStateOf<Boolean?>(null) }

    val startTimeState = remember { mutableStateOf(startTime) }
    val endTimeState = remember { mutableStateOf(endTime) }

    LaunchedEffect(startTimeState.value) {
        onStartTimeChange(startTimeState.value)
    }

    LaunchedEffect(endTimeState.value) {
        onEndTimeChange(endTimeState.value)
    }

    Column(

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //시작 시간 설정 버튼
            DefaultButton(
                modifier = Modifier.weight(1f),
                buttonText = startTimeState.value.getText(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(isStartTime.value == true && openType == currentType) LighterHighlightColor else SubHighlightColor,
                    contentColor = if(isStartTime.value == true && openType == currentType) HighlightColor else Color.Black
                ),
                diffValue = 0
            ) {
                onTypeChange(openType)

                if(isStartTime.value == true) {
                    isStartTime.value = null
                } else {
                    isStartTime.value = true
                }
            }

            Text(
                text = "부터",
                style = storeMeTextStyle(FontWeight.Bold, 0),
                color = Color.Black
            )

            //종료 시간 설정 버튼
            DefaultButton(
                modifier = Modifier.weight(1f),
                buttonText = endTimeState.value.getText(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(isStartTime.value == false && openType == currentType) LighterHighlightColor else SubHighlightColor,
                    contentColor = if(isStartTime.value == false && openType == currentType) HighlightColor else Color.Black
                ),
                diffValue = 0
            ) {
                onTypeChange(openType)

                if(isStartTime.value == false) {
                    isStartTime.value = null
                } else {
                    isStartTime.value = false
                }
            }

            Text(
                text = "까지",
                style = storeMeTextStyle(FontWeight.Bold, 0),
                color = Color.Black
            )
        }

        AnimatedVisibility(visible = isStartTime.value != null && currentType == openType) {
            when(isStartTime.value) {
                true -> {
                    StoreMeTimePicker(
                        initHour = startTimeState.value.hour,
                        initMinute = startTimeState.value.minute,
                        onHourSelected = { startTimeState.value = startTimeState.value.copy(hour = it) },
                        onMinuteSelected = { startTimeState.value = startTimeState.value.copy(minute = it) }
                    )
                }
                false -> {
                    StoreMeTimePicker(
                        initHour = endTimeState.value.hour,
                        initMinute = endTimeState.value.minute,
                        onHourSelected = { endTimeState.value = endTimeState.value.copy(hour = it) },
                        onMinuteSelected = { endTimeState.value = endTimeState.value.copy(minute = it) }
                    )
                }
                else -> {

                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun DifferentBusinessHoursSection(
    businessHoursSettingViewModel: BusinessHoursSettingViewModel
) {
    val holidayWeeks by businessHoursSettingViewModel.holidayWeeks.collectAsState()

    val isAlwaysOpen by businessHoursSettingViewModel.isAlwaysOpen.collectAsState()
    val hasBreakTime by businessHoursSettingViewModel.hasBreakTime.collectAsState()

    val startBusinessTimes by businessHoursSettingViewModel.startBusinessTimes.collectAsState()
    val endBusinessTimes by businessHoursSettingViewModel.endBusinessTimes.collectAsState()

    val startBreakTimes by businessHoursSettingViewModel.startBreakTimes.collectAsState()
    val endBreakTimes by businessHoursSettingViewModel.endBreakTimes.collectAsState()

    val selectedIndex = remember { mutableStateOf(-1) }
    val showBottomSheet = remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(selectedIndex.value) {
        if(selectedIndex.value == -1) {
            showBottomSheet.value = false
            return@LaunchedEffect
        }

        showBottomSheet.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DateTimeUtils.DayOfWeek.entries.forEachIndexed { index, dayOfWeek ->
            Row(
                modifier = Modifier
                    .clickable { if(!holidayWeeks.contains(DateTimeUtils.DayOfWeek.entries[index])) selectedIndex.value = index },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dayOfWeek.displayName + "요일",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when {
                        holidayWeeks.contains(DateTimeUtils.DayOfWeek.entries[index]) -> {
                            Text(
                                text = "휴무",
                                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                                color = GuideColor
                            )
                        }
                        isAlwaysOpen[index] -> {
                            Text(
                                text = "영업시간 : 24시간 영업",
                                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                                color = GuideColor
                            )
                        }
                        startBusinessTimes[index] != null && endBusinessTimes[index] != null -> {
                            Text(
                                text = "영업시간 : " + startBusinessTimes[index]!!.getText() + " ~ " + endBusinessTimes[index]!!.getText(),
                                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                                color = GuideColor
                            )
                        }
                    }

                    when {
                        holidayWeeks.contains(DateTimeUtils.DayOfWeek.entries[index]) -> {

                        }
                        !hasBreakTime[index] -> {

                        }
                        startBreakTimes[index] != null && endBreakTimes[index] != null -> {
                            Text(
                                text = "브레이크타임 : " + startBreakTimes[index]!!.getText() + " ~ " + endBreakTimes[index]!!.getText(),
                                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                                color = GuideColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "더보기 아이콘",
                    modifier = Modifier
                        .size(12.dp),
                    tint = Color.Black
                )
            }

            DefaultHorizontalDivider()
        }
    }

    if(showBottomSheet.value) {
        DefaultBottomSheet(sheetState = sheetState, onDismiss = { selectedIndex.value = -1 }) {
            DifferentBottomSheetContent(
                selectedIndex = selectedIndex.value,
                businessHoursSettingViewModel = businessHoursSettingViewModel
            ) {
                selectedIndex.value = -1
            }
        }
    }
}

@Composable
fun DifferentBottomSheetContent(selectedIndex: Int, businessHoursSettingViewModel: BusinessHoursSettingViewModel, onFinish: () -> Unit) {
    val currentType = remember { mutableStateOf<BusinessHoursType?>(null) }

    val selectedIsAlwaysOpen = remember { mutableStateOf(businessHoursSettingViewModel.isAlwaysOpen.value[selectedIndex]) }
    val selectedHasBreakTime = remember { mutableStateOf(businessHoursSettingViewModel.hasBreakTime.value[selectedIndex]) }
    val selectedStartBusinessTime = remember { mutableStateOf(businessHoursSettingViewModel.startBusinessTimes.value[selectedIndex] ?: TimeData(9, 0)) }
    val selectedEndBusinessTime = remember { mutableStateOf(businessHoursSettingViewModel.endBusinessTimes.value[selectedIndex] ?: TimeData(21, 0)) }
    val selectedStartBreakTime = remember { mutableStateOf(businessHoursSettingViewModel.startBreakTimes.value[selectedIndex] ?: TimeData(15, 0)) }
    val selectedEndBreakTime = remember { mutableStateOf(businessHoursSettingViewModel.endBreakTimes.value[selectedIndex] ?: TimeData(17, 0)) }

    val sameDayIndices = remember { mutableStateOf(businessHoursSettingViewModel.getSameBusinessHourIndices(selectedIndex)) }

    val holidayWeeks by businessHoursSettingViewModel.holidayWeeks.collectAsState()

    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(text = "영업시간 동일하게 설정", style = storeMeTextStyle(FontWeight.ExtraBold, 2))

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            itemsIndexed(DateTimeUtils.DayOfWeek.entries) { index, dayOfWeek ->
                CircleToggleButton(
                    text = dayOfWeek.displayName,
                    enabled = !holidayWeeks.contains(DateTimeUtils.DayOfWeek.entries[index]),
                    isSelected = sameDayIndices.value.contains(index)
                ) {
                    if(index == selectedIndex) {
                        return@CircleToggleButton
                    }

                    if(sameDayIndices.value.contains(index)) {
                        sameDayIndices.value -= index
                    } else {
                        sameDayIndices.value += index
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "영업 시간",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            )

            Spacer(modifier = Modifier.weight(1f))

            DefaultCheckButton(text = "24시간 영업", isSelected = selectedIsAlwaysOpen.value) {
                selectedIsAlwaysOpen.value = !selectedIsAlwaysOpen.value
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if(!selectedIsAlwaysOpen.value) {
            TimePickerWithStartToEnd(
                startTime = selectedStartBusinessTime.value,
                endTime = selectedEndBusinessTime.value,
                openType = BusinessHoursType.BUSINESS_HOURS,
                currentType = currentType.value,
                onStartTimeChange = { selectedStartBusinessTime.value = it },
                onEndTimeChange = { selectedEndBusinessTime.value = it },
                onTypeChange = { currentType.value = it }
            )
        }

        DefaultButton(
            buttonText = if(selectedHasBreakTime.value) "브레이크타임 제거" else "브레이크타임 추가",
            diffValue = 1,
            colors = ButtonDefaults.buttonColors(
                containerColor = SubHighlightColor,
                contentColor = Color.Black
            )
        ) {
            selectedHasBreakTime.value = !selectedHasBreakTime.value
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(selectedHasBreakTime.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "브레이크타임",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                TimePickerWithStartToEnd(
                    startTime = selectedStartBreakTime.value,
                    endTime = selectedEndBreakTime.value,
                    openType = BusinessHoursType.BREAK_TIME,
                    currentType = currentType.value,
                    onStartTimeChange = { selectedStartBreakTime.value = it },
                    onEndTimeChange = { selectedEndBreakTime.value = it },
                    onTypeChange = { currentType.value = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        DefaultButton(buttonText = "저장") {
            sameDayIndices.value.forEach {
                businessHoursSettingViewModel.updateHasBreakTime(index = it, value = selectedHasBreakTime.value)
                businessHoursSettingViewModel.updateIsAlwaysOpen(index = it, value = selectedIsAlwaysOpen.value)
                businessHoursSettingViewModel.updateStartBusinessTime(index = it, newTime = selectedStartBusinessTime.value)
                businessHoursSettingViewModel.updateEndBusinessTime(index = it, newTime = selectedEndBusinessTime.value)
                businessHoursSettingViewModel.updateStartBreakTime(index = it, newTime = selectedStartBreakTime.value)
                businessHoursSettingViewModel.updateEndBreakTime(index = it, newTime = selectedEndBreakTime.value)
            }

            onFinish()
        }
    }
}

@Composable
fun ExtraDataSection(
    businessHoursSettingViewModel: BusinessHoursSettingViewModel
) {
    val extraInfo by businessHoursSettingViewModel.extraInfo.collectAsState()

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(extraInfo) {
        isError.value = extraInfo.length > 50
    }

    Text(
        text = "영업 시간 관련 기타 정보",
        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        color = Color.Black
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = extraInfo,
        onValueChange = {
            businessHoursSettingViewModel.updateExtraInfo(it)
        },
        textStyle = storeMeTextStyle(FontWeight.Normal, 1),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        trailingIcon = {
            if(extraInfo.isNotEmpty()){
                IconButton(onClick = { businessHoursSettingViewModel.updateExtraInfo("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_text_clear),
                        contentDescription = "삭제",
                        modifier = Modifier
                            .size(24.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = "예) 영업 종료 30분 전까지 주문 가능합니다.",
                style = storeMeTextStyle(FontWeight.Normal, 1),
                color = UndefinedTextColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HighlightColor,
            errorBorderColor = ErrorColor,
            errorLabelColor = ErrorColor,
        ),
        isError = isError.value,
        supportingText = {
            if(isError.value){
                Text(
                    text = "100자 이내로 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    color = ErrorTextFieldColor
                )
            }
        }
    )

    TextLengthRow(text = extraInfo, limitSize = 100)
}