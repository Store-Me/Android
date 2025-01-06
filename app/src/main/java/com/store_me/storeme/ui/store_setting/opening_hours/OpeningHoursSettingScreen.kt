@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.opening_hours

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.DailyHoursData
import com.store_me.storeme.ui.component.CircleToggleButton
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.SmallButton
import com.store_me.storeme.ui.component.DefaultOutlineTextField
import com.store_me.storeme.ui.component.DefaultToggleButton
import com.store_me.storeme.ui.component.LargeButton
import com.store_me.storeme.ui.component.StoreMeTimePicker
import com.store_me.storeme.ui.component.SubTitleSection
import com.store_me.storeme.ui.component.TextFieldErrorType
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.store_setting.opening_hours.OpeningHoursSettingViewModel.*
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.HighlightTextColor
import com.store_me.storeme.ui.theme.TimePickerSelectLineColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.SizeUtils

val LocalOpeningHoursSettingViewModel = staticCompositionLocalOf<OpeningHoursSettingViewModel> {
    error("No LocalOpeningHoursSettingViewModel provided")
}
@Composable
fun OpeningHoursSettingScreen(
    navController: NavController,
    openingHoursSettingViewModel: OpeningHoursSettingViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    val selectType by remember { openingHoursSettingViewModel.selectedType }.collectAsState()
    val isAlwaysOpen by remember { openingHoursSettingViewModel.isAlwaysOpen }.collectAsState()

    var isError by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    var selectedWeek by remember { mutableStateOf(DateTimeUtils.DayOfWeek.SUNDAY) }

    CompositionLocalProvider(LocalOpeningHoursSettingViewModel provides openingHoursSettingViewModel) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = White,
            topBar = { TitleWithDeleteButton(navController = navController, title = "영업 시간 설정") },
            content = { innerPadding ->
                if(showBottomSheet) {

                    DefaultBottomSheet(onDismiss = { showBottomSheet = false }, sheetState = sheetState) {
                        BottomSheetContent(selectedWeek) {
                            showBottomSheet = false
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        item { SubTitleSection("영업 시간을 설정해주세요.", modifier = Modifier.padding(horizontal = 20.dp)) }

                        item { SetTypeSection() }

                        item {
                            AnimatedVisibility(visible = selectType == OpeningHoursType.SAME) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                ) {
                                    CheckAlwaysSection()

                                    AnimatedVisibility(visible = !isAlwaysOpen) {
                                        SelectTimeSection(EditTimeType.OPENING_HOURS)
                                    }
                                }
                            }

                            AnimatedVisibility(visible = selectType == OpeningHoursType.DIFFERENT) {
                                WeekListSection {
                                    showBottomSheet = true
                                    selectedWeek = it
                                }
                            }
                        }

                        item {
                            AnimatedVisibility(visible = selectType == OpeningHoursType.SAME) {
                                BreakTimeSection(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                )
                            }
                        }

                        item {
                            AnimatedVisibility(visible = selectType != null) {
                                ExtraTextSection {
                                    isError = it
                                }
                            }
                        }

                        item {
                            AnimatedVisibility(visible = selectType != null) {
                                LargeButton(
                                    text = "저장",
                                    enabled = !isError && (selectType != null)
                                            && ((openingHoursSettingViewModel.isAllFinished() && selectType == OpeningHoursType.DIFFERENT) || selectType == OpeningHoursType.SAME),
                                    modifier = Modifier.padding(vertical = 100.dp, horizontal = 20.dp),
                                    containerColor = Black,
                                    contentColor = White
                                ) {
                                    openingHoursSettingViewModel.updateOpeningHoursData()
                                }
                            }
                        }
                    }
                }
             }
        )
    }
}

@Composable
fun BottomSheetContent(selectedWeek: DateTimeUtils.DayOfWeek, onFinishButtonClick: () -> Unit) {
    val openingHoursSettingViewModel = LocalOpeningHoursSettingViewModel.current

    val selectType by remember { openingHoursSettingViewModel.selectedType }.collectAsState()

    val isAlwaysOpen by remember { openingHoursSettingViewModel.isAlwaysOpen }.collectAsState()
    val hasBreakTime by remember { openingHoursSettingViewModel.hasBreakTime }.collectAsState()

    val selectedWeeks by remember { openingHoursSettingViewModel.selectedWeeks }.collectAsState()

    var isInitialized by remember { mutableStateOf(false) }



    LaunchedEffect(selectedWeek) {
        isInitialized = false
    }

    if(!isInitialized){
        if(openingHoursSettingViewModel.isFinished(selectedWeek)) { //이미 완성된 요일인 경우
            openingHoursSettingViewModel.findSameDailyHoursData(selectedWeek)

            openingHoursSettingViewModel.changeIsAlwaysOpenValue(openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.isAlwaysOpen ?: false)
            openingHoursSettingViewModel.changeHasBreakTimeValue(openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.hasBreakTime ?: false)
        } else {
            openingHoursSettingViewModel.clearSelectedWeeks(selectedWeek)

            openingHoursSettingViewModel.changeIsAlwaysOpenValue(false)
            openingHoursSettingViewModel.changeHasBreakTimeValue(false)
        }

        isInitialized = true
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
    ) {
        Text(text = "영업시간 동일하게 설정", style = storeMeTextStyle(FontWeight.ExtraBold, 2))

        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(DateTimeUtils.DayOfWeek.entries) {
                CircleToggleButton(text = it.displayName, isSelected = it in selectedWeeks) {
                    openingHoursSettingViewModel.setSelectedWeeks(selectedWeek, it)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        CheckAlwaysSection()

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(visible = !isAlwaysOpen) {
            Column {
                SelectTimeSection(EditTimeType.OPENING_HOURS, selectedWeek)

                Spacer(modifier = Modifier.height(20.dp))
            }

        }

        AnimatedVisibility(visible = selectType == OpeningHoursType.DIFFERENT) {
            Column {
                BreakTimeSection()

                Spacer(modifier = Modifier.height(20.dp))
            }

        }

        LargeButton(text = "저장", containerColor = Black, contentColor = White) {
            openingHoursSettingViewModel.setDailyHours(
                dayOfWeeks = selectedWeeks,
                newHoursData = DailyHoursData(
                    openHours = openingHoursSettingViewModel.openingStartHours.value,
                    openMinutes = openingHoursSettingViewModel.openingStartMinutes.value,
                    closeHours = openingHoursSettingViewModel.openingEndHours.value,
                    closeMinutes = openingHoursSettingViewModel.openingEndMinutes.value,
                    startBreakHours = openingHoursSettingViewModel.startBreakHours.value,
                    startBreakMinutes = openingHoursSettingViewModel.startBreakMinutes.value,
                    endBreakHours = openingHoursSettingViewModel.endBreakHours.value,
                    endBreakMinutes = openingHoursSettingViewModel.endBreakMinutes.value,
                    hasBreakTime = hasBreakTime,
                    isAlwaysOpen = isAlwaysOpen,
                )
            )

            onFinishButtonClick()
        }
    }
}

@Composable
fun WeekListSection(onClickWeek: (DateTimeUtils.DayOfWeek) -> Unit) {
    val openingHoursSettingViewModel = LocalOpeningHoursSettingViewModel.current
    val dailyHoursMap by remember { openingHoursSettingViewModel.dailyHoursMap }.collectAsState()

    val isFinished = remember(dailyHoursMap) {
        DateTimeUtils.DayOfWeek.entries.associateWith { dayOfWeek ->
            openingHoursSettingViewModel.isFinished(dayOfWeek)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        DateTimeUtils.DayOfWeek.entries.forEach {
            openingHoursSettingViewModel.isFinished(it)

            Row(
                modifier = Modifier
                    .clickable { onClickWeek(it) }
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = it.displayName + "요일",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                if(isFinished[it] == true) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        if(dailyHoursMap[it]?.isAlwaysOpen == true)
                            Text(
                                text = "24시간 영업",
                                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                                color = UndefinedTextColor
                            )
                        else
                            Text(
                                text = "영업시간 : ${DateTimeUtils().getSelectTimeText(
                                    hours = dailyHoursMap[it]?.openHours ?: 0, 
                                    minutes = dailyHoursMap[it]?.openMinutes ?: 0
                                )}~${DateTimeUtils().getSelectTimeText(
                                    hours = dailyHoursMap[it]?.closeHours ?: 0,
                                    minutes = dailyHoursMap[it]?.closeMinutes ?: 0
                                )}",
                                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                                color = UndefinedTextColor
                            )

                        if(dailyHoursMap[it]?.hasBreakTime == true)
                            Text(text = "브레이크타임 : ${DateTimeUtils().getSelectTimeText(
                                hours = dailyHoursMap[it]?.startBreakHours ?: 0,
                                minutes = dailyHoursMap[it]?.startBreakMinutes ?: 0
                            )}~${DateTimeUtils().getSelectTimeText(
                                hours = dailyHoursMap[it]?.endBreakHours ?: 0,
                                minutes = dailyHoursMap[it]?.endBreakMinutes ?: 0
                            )}",
                                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                                color = UndefinedTextColor
                            )

                        Spacer(modifier = Modifier.weight(1f))
                    }

                }

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "더보기 아이콘",
                    modifier = Modifier
                        .size(12.dp),
                    tint = Black
                )
            }

            HorizontalDivider(color = DefaultDividerColor, thickness = 1.dp)
        }
    }
}

@Composable
fun SelectTimeSection(thisType: EditTimeType, selectedWeek: DateTimeUtils.DayOfWeek? = null) {
    val openingHoursSettingViewModel = LocalOpeningHoursSettingViewModel.current

    val isStartTime by remember { openingHoursSettingViewModel.isStartTime }.collectAsState()
    val nowEditTimeType by remember { openingHoursSettingViewModel.nowEditTimeType }.collectAsState()

    val selectedStartHour = remember { mutableStateOf(9) }
    val selectedStartMinute = remember { mutableStateOf(0) }

    val selectedEndHour = remember { mutableStateOf(21) }
    val selectedEndMinute = remember { mutableStateOf(0) }

    val typeSelected = thisType ==  nowEditTimeType

    var isInitialized by remember { mutableStateOf(false) }
    //초기화 변수를 사용하는게 아닌, 초기화 값을 파라미터로 받아서 사용하면 좋을듯..
    //fun SelectTimeSection(thisType: EditTimeType, selectedWeek: DateTimeUtils.DayOfWeek? = null, initHour: Int = 9, ... , ) {
    //위와 같은 방법으로 받으면 더 나은 해결


    if(selectedWeek != null && openingHoursSettingViewModel.isFinished(selectedWeek)) {

        if(!isInitialized){
            when(thisType) {
                EditTimeType.OPENING_HOURS -> {
                    selectedStartHour.value = openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.openHours ?: 9
                    selectedStartMinute.value = openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.openMinutes ?: 0

                    selectedEndHour.value = openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.closeHours ?: 21
                    selectedEndMinute.value = openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.closeMinutes ?: 0
                }
                EditTimeType.BREAK_TIME -> {
                    selectedStartHour.value = openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.startBreakHours ?: 9
                    selectedStartMinute.value = openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.startBreakMinutes ?: 0

                    selectedEndHour.value = openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.endBreakHours ?: 21
                    selectedEndMinute.value = openingHoursSettingViewModel.dailyHoursMap.value[selectedWeek]?.endBreakMinutes ?: 0
                }
            }

            isInitialized = true
        }
    }

    openingHoursSettingViewModel.setSelectedStartTime(thisType, selectedStartHour.value, selectedStartMinute.value)
    openingHoursSettingViewModel.setSelectedEndTime(thisType, selectedEndHour.value, selectedEndMinute.value)


    LaunchedEffect(selectedWeek) {
        isInitialized = false
    }

    LaunchedEffect(selectedStartHour.value, selectedStartMinute.value) {
        if (typeSelected) {
            openingHoursSettingViewModel.setSelectedStartTime(thisType, selectedStartHour.value, selectedStartMinute.value)
        }
    }

    LaunchedEffect(selectedEndHour.value, selectedEndMinute.value) {
        if (typeSelected) {
            openingHoursSettingViewModel.setSelectedEndTime(thisType, selectedEndHour.value, selectedEndMinute.value)
        }
    }

    Column(
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val selected = isStartTime && typeSelected

                SmallButton(
                    text = DateTimeUtils().getSelectTimeText(
                        hours = selectedStartHour.value,
                        minutes = selectedStartMinute.value
                    ),
                    containerColor = if (selected) TimePickerSelectLineColor else EditButtonColor,
                    contentColor = if(selected) HighlightTextColor else Black,
                    modifier = Modifier
                        .weight(1f)
                        .height(SizeUtils.textSizeToDp(LocalDensity.current, 0, 20))
                ) {
                    openingHoursSettingViewModel.setIsStartTime(true)
                    openingHoursSettingViewModel.setNowEditTimeType(thisType)
                }

                Spacer(modifier = Modifier.width(5.dp))

                Text("부터", style = storeMeTextStyle(FontWeight.Bold, 0))
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val selected = !isStartTime && typeSelected

                SmallButton(
                    text = DateTimeUtils().getSelectTimeText(
                        hours = selectedEndHour.value,
                        minutes = selectedEndMinute.value
                    ),
                    containerColor = if (selected) TimePickerSelectLineColor else EditButtonColor,
                    contentColor = if(selected) HighlightTextColor else Black,
                    modifier = Modifier
                        .weight(1f)
                        .height(SizeUtils.textSizeToDp(LocalDensity.current, 0, 20))
                ) {
                    openingHoursSettingViewModel.setIsStartTime(false)
                    openingHoursSettingViewModel.setNowEditTimeType(thisType)
                }

                Spacer(modifier = Modifier.width(5.dp))

                Text("까지", style = storeMeTextStyle(FontWeight.Bold, 0))
            }
        }

        AnimatedVisibility(visible = typeSelected){
            when(isStartTime) {
                true -> {
                    StoreMeTimePicker(
                        initHour = selectedStartHour.value,
                        initMinute = selectedStartMinute.value,
                        onHourSelected = { selectedStartHour.value = it },
                        onMinuteSelected = { selectedStartMinute.value = it }
                    )
                }

                false -> {
                    StoreMeTimePicker(
                        initHour = selectedEndHour.value,
                        initMinute = selectedEndMinute.value,
                        onHourSelected = { selectedEndHour.value = it },
                        onMinuteSelected = { selectedEndMinute.value = it }
                    )
                }
            }
        }
    }
}


@Composable
fun SetTypeSection() {
    val openingHoursSettingViewModel = LocalOpeningHoursSettingViewModel.current

    val selectType by remember { openingHoursSettingViewModel.selectedType }.collectAsState()

    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DefaultToggleButton(text = "매일 같음", isSelected = selectType == OpeningHoursType.SAME) {
            openingHoursSettingViewModel.setSelectedType(OpeningHoursType.SAME)
        }

        DefaultToggleButton(text = "요일마다 설정", isSelected = selectType == OpeningHoursType.DIFFERENT) {
            openingHoursSettingViewModel.setSelectedType(OpeningHoursType.DIFFERENT)

        }
    }
}

@Composable
fun CheckAlwaysSection() {
    val openingHoursSettingViewModel = LocalOpeningHoursSettingViewModel.current
    val isAlwaysOpen by remember { openingHoursSettingViewModel.isAlwaysOpen }.collectAsState()

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

        DefaultCheckButton(text = "24시간 영업", isSelected = isAlwaysOpen) {
            openingHoursSettingViewModel.changeIsAlwaysOpenValue()
        }
    }
}

@Composable
fun BreakTimeSection(modifier: Modifier = Modifier) {
    val openingHoursSettingViewModel = LocalOpeningHoursSettingViewModel.current
    val hasBreakTime by remember { openingHoursSettingViewModel.hasBreakTime }.collectAsState()

    Column(
        modifier = modifier
    ) {
        SmallButton(text = if(hasBreakTime) "브레이크타임 제거" else "브레이크타임 추가") {
            openingHoursSettingViewModel.changeHasBreakTimeValue()
        }

        AnimatedVisibility(visible = hasBreakTime) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "브레이크타임",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                )

                SelectTimeSection(EditTimeType.BREAK_TIME)
            }

        }
    }

}

@Composable
fun ExtraTextSection(onErrorChange: (Boolean) -> Unit) {
    val openingHoursSettingViewModel = LocalOpeningHoursSettingViewModel.current
    val extraDescription by remember { openingHoursSettingViewModel.extraDescription }.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "영업 시간 관련 기타 정보",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.height(10.dp))

        DefaultOutlineTextField(
            text = extraDescription,
            placeholderText = "예) 부재시 매장 앞 번호로 연락주세요.",
            errorType = TextFieldErrorType.DESCRIPTION,
            onErrorChange = { onErrorChange(it) },
            onValueChange = { openingHoursSettingViewModel.updateExtraDescription(it) },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextLengthRow(text = extraDescription, limitSize = 100)
    }
}