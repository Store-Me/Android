@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.closed_day

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.ripple
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.TemporaryOpeningHours
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.SmallButton
import com.store_me.storeme.ui.component.DefaultToggleButton
import com.store_me.storeme.ui.component.LargeButton
import com.store_me.storeme.ui.component.StoreMeSelectRangeCalendar
import com.store_me.storeme.ui.component.StoreMeTimePicker
import com.store_me.storeme.ui.component.SubTitleSection
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.store_setting.closed_day.ClosedDaySettingViewModel.*
import com.store_me.storeme.ui.store_setting.opening_hours.OpeningHoursSettingViewModel.*
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextColor
import com.store_me.storeme.ui.theme.NextButtonColor
import com.store_me.storeme.ui.theme.SaveButtonColor
import com.store_me.storeme.ui.theme.TimePickerSelectLineColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.SizeUtils

val LocalClosedDaySettingViewModel = staticCompositionLocalOf<ClosedDaySettingViewModel> {
    error("No LocalClosedDaySettingViewModel provided")
}

@Composable
fun ClosedDaySettingScreen(
    navController: NavController,
    closedDaySettingViewModel: ClosedDaySettingViewModel = viewModel()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    var showEditBottomSheet by remember { mutableStateOf(false) }

    val selectedType by remember { closedDaySettingViewModel.selectedType }.collectAsState()
    val selectedWeeks by remember { closedDaySettingViewModel.selectedWeeks }.collectAsState()

    val openingHours by remember { Auth.storeHoursData }.collectAsState()

    CompositionLocalProvider(LocalClosedDaySettingViewModel provides closedDaySettingViewModel) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = White,
            topBar = { TitleWithDeleteButton(navController = navController, title = "휴무일 설정") },
            content = { innerPadding ->
                if(showBottomSheet) {

                    DefaultBottomSheet(onDismiss = { showBottomSheet = false }, sheetState = sheetState) {
                        BottomSheetContent{
                            showBottomSheet = false
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                if(showEditBottomSheet) {
                    DefaultBottomSheet(onDismiss = { showEditBottomSheet = false }, sheetState = sheetState, hasDeleteButton = false) {
                        EditBottomSheetContent(
                            onEditClick = {},
                            onDeleteClick = {}
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        item {
                            SubTitleSection("휴무일이 있나요?", modifier = Modifier.padding(horizontal = 20.dp))

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        item {
                            SetTypeSection()

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        item{
                            AnimatedVisibility(visible = selectedType == ClosedDayType.EXIST) {
                                Column {
                                    SelectWeeksSection()

                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                        }

                        item{
                            AnimatedVisibility(visible = (selectedType == ClosedDayType.NONE) ||
                                    (selectedType == ClosedDayType.EXIST && selectedWeeks.isNotEmpty())) {
                                Column {
                                    LargeButton(
                                        text = "저장",
                                        modifier = Modifier.padding(horizontal = 20.dp),
                                        containerColor = Black,
                                        contentColor = White
                                    ) {

                                    }

                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                        }

                        item {
                            TemporaryOpeningSection {
                                showBottomSheet = true
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        items(openingHours.temporaryOpeningHours) {
                            when(it) {
                                is TemporaryOpeningHours.Adjusted -> {
                                    TemporaryAdjustedItem(it) {
                                        showEditBottomSheet = true
                                    }

                                }
                                is TemporaryOpeningHours.Closed -> {
                                    TemporaryClosedItem(it){
                                        showEditBottomSheet = true
                                    }
                                }
                            }

                            HorizontalDivider(color = DefaultDividerColor, thickness = 1.dp)

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }

            }
        )

    }
}

@Composable
fun EditBottomSheetContent(onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "수정하기",
            style = storeMeTextStyle(FontWeight.Bold, 2),
            color = Black,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onEditClick()
                }
                .padding(vertical = 30.dp),
            textAlign = TextAlign.Center
        )

        HorizontalDivider(color = DefaultDividerColor, thickness = 1.dp)

        Text(
            text = "삭제하기",
            style = storeMeTextStyle(FontWeight.Bold, 2),
            color = ErrorTextFieldColor,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onDeleteClick()
                }
                .padding(vertical = 30.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SetTypeSection() {
    val closedDaySettingViewModel = LocalClosedDaySettingViewModel.current

    val selectType by remember { closedDaySettingViewModel.selectedType }.collectAsState()

    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DefaultToggleButton(text = "있음", isSelected = selectType == ClosedDayType.EXIST) {
            closedDaySettingViewModel.setSelectedType(ClosedDayType.EXIST)
        }

        DefaultToggleButton(text = "없음", isSelected = selectType == ClosedDayType.NONE) {
            closedDaySettingViewModel.setSelectedType(ClosedDayType.NONE)
        }
    }
}

@Composable
fun SelectWeeksSection() {
    val closedDaySettingViewModel = LocalClosedDaySettingViewModel.current

    val selectedWeeks by remember { closedDaySettingViewModel.selectedWeeks }.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "정기 휴무일",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(start = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(DateTimeUtils.DayOfWeek.entries) {
                DefaultToggleButton(text = it.displayName, isSelected = it in selectedWeeks) {
                    closedDaySettingViewModel.setSelectedWeeks(it)
                }
            }
        }
    }
}

@Composable
fun TemporaryOpeningSection(onSetTemporaryOpen: () -> Unit) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "임시 영업일",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Text(
            text = "평상시와 다르게 운영하는 날짜가 있다면\n" +
                    "휴무일로 설정하거나 영업 시간을 조정할 수 있어요.",
            style = storeMeTextStyle(FontWeight.Normal, 0),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        SmallButton(
            text = "임시 영업 설정하기",
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(40.dp)
                .fillMaxWidth()) {
            onSetTemporaryOpen()
        }
    }
}

@Composable
fun TemporaryAdjustedItem(item: TemporaryOpeningHours.Adjusted, onMenuClick: () -> Unit) {
    val openingHours = item.dailyHoursData

    val openingHoursText = "영업시간 : " +
            if(item.dailyHoursData.isAlwaysOpen)
                "24시간 영업"
            else {
                DateTimeUtils().getSelectTimeText(openingHours.openHours, openingHours.openMinutes) +
                        " ~ " +
                        DateTimeUtils().getSelectTimeText(openingHours.closeHours, openingHours.closeMinutes)
            }

    val breakTimeText = "브레이크타임 : " +
            if(!item.dailyHoursData.hasBreakTime)
                ""
            else {
                DateTimeUtils().getSelectTimeText(openingHours.startBreakHours, openingHours.startBreakMinutes) +
                        " ~ " +
                        DateTimeUtils().getSelectTimeText(openingHours.endBreakHours, openingHours.endBreakMinutes)
            }

    Row(
        modifier = Modifier.padding(start = 20.dp, bottom = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${item.startMonth}월 ${item.startDay}일 (${DateTimeUtils().getDayOfWeek(item.startYear, item.startMonth, item.startDay)}) ~ " +
                    "${item.endMonth}월 ${item.endDay}일 (${DateTimeUtils().getDayOfWeek(item.endYear, item.endMonth, item.endDay)})",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = openingHoursText,
                style = storeMeTextStyle(FontWeight.Normal, 0),
                modifier = Modifier.fillMaxWidth())

            if(item.dailyHoursData.hasBreakTime)
                Text(
                    text = breakTimeText,
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    modifier = Modifier.fillMaxWidth()
                )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = "메뉴 아이콘",
            modifier = Modifier
                .size(12.dp)
                .clickable(
                    onClick = { onMenuClick() },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false),
                )
        )
    }
}

@Composable
fun TemporaryClosedItem(item: TemporaryOpeningHours.Closed, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(start = 20.dp, bottom = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${item.startMonth}월 ${item.startDay}일 (${DateTimeUtils().getDayOfWeek(item.startYear, item.startMonth, item.startDay)}) ~ " +
                    "${item.endMonth}월 ${item.endDay}일 (${DateTimeUtils().getDayOfWeek(item.endYear, item.endMonth, item.endDay)})",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "휴무",
                style = storeMeTextStyle(FontWeight.Normal, 0),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = "메뉴 아이콘",
            modifier = Modifier
                .size(12.dp)
                .clickable(
                    onClick = { onMenuClick() },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false),
                )
        )
    }
}

@Composable
fun BottomSheetContent(onFinished: () -> Unit) {
    val closedDaySettingViewModel = LocalClosedDaySettingViewModel.current

    val currentBottomProgress by remember { closedDaySettingViewModel.currentBottomProgress }.collectAsState()

    AnimatedVisibility(visible = currentBottomProgress == BottomProgress.DATE) {
        SelectRangeDateSection()
    }

    AnimatedVisibility(visible = currentBottomProgress == BottomProgress.TIME) {
        SelectTemporalTimeSection {
            onFinished()
        }
    }

}

@Composable
fun SelectRangeDateSection() {
    val closedDaySettingViewModel = LocalClosedDaySettingViewModel.current

    val selectedStartDate by remember { closedDaySettingViewModel.selectedStartDate }.collectAsState()
    val selectedEndDate by remember { closedDaySettingViewModel.selectedEndDate }.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        StoreMeSelectRangeCalendar(
            startDate = selectedStartDate,
            endDate = selectedEndDate,
            onStartDateChange = { closedDaySettingViewModel.updateSelectedStartDate(it) },
            onEndDateChange = { closedDaySettingViewModel.updateSelectedEndDate(it) }
        )

        LargeButton(
            text = "다음",
            containerColor = NextButtonColor,
            contentColor = White,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedStartDate != null && selectedEndDate != null
        ) {
            closedDaySettingViewModel.updateCurrentBottomProgress(BottomProgress.TIME)
        }
    }
}

@Composable
fun SelectTemporalTimeSection(onFinished: () -> Unit) {
    val closedDaySettingViewModel = LocalClosedDaySettingViewModel.current

    val closedTimeType by remember { closedDaySettingViewModel.closedTimeType }.collectAsState()

    val startDate = closedDaySettingViewModel.selectedStartDate.value!!
    val endDate = closedDaySettingViewModel.selectedEndDate.value!!

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row {
            Text(
                text = "${startDate.month.value}월 ${startDate.dayOfMonth}일 (${DateTimeUtils.DayOfWeek.entries[startDate.dayOfWeek.value % 7].displayName}) ~ " +
                        "${endDate.month.value}월 ${endDate.dayOfMonth}일 (${DateTimeUtils.DayOfWeek.entries[endDate.dayOfWeek.value % 7].displayName})",
                style = storeMeTextStyle(FontWeight.Bold, 6)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DefaultToggleButton(text = "영업시간 조정", isSelected = closedTimeType == ClosedTimeType.ADJUSTED) {
                closedDaySettingViewModel.updateClosedTimeType(ClosedTimeType.ADJUSTED)
            }

            DefaultToggleButton(text = "휴무일로 설정", isSelected = closedTimeType == ClosedTimeType.CLOSED) {
                closedDaySettingViewModel.updateClosedTimeType(ClosedTimeType.CLOSED)
            }
        }

        AnimatedVisibility(visible = closedTimeType == ClosedTimeType.ADJUSTED) {
            AdjustTimeSection()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LargeButton(
                text = "이전",
                modifier = Modifier.weight(1f),
                containerColor = EditButtonColor,
                contentColor = Black
            ) {
                closedDaySettingViewModel.updateCurrentBottomProgress(BottomProgress.DATE)
            }

            LargeButton(text = "다음",
                modifier = Modifier.weight(1f),
                containerColor = SaveButtonColor,
                contentColor = White,
                enabled = closedDaySettingViewModel.closedTimeType.value != null
            ) {
                closedDaySettingViewModel.addTemporalOpeningHoursData()

                closedDaySettingViewModel.clearBottomData()

                onFinished()
            }
        }
    }
}

@Composable
fun AdjustTimeSection() {
    val closedDaySettingViewModel = LocalClosedDaySettingViewModel.current
    val isAlwaysOpen by remember { closedDaySettingViewModel.isAlwaysOpen }.collectAsState()

    Column(

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

            DefaultCheckButton(text = "24시간 영업", isSelected = isAlwaysOpen) {
                closedDaySettingViewModel.changeIsAlwaysOpenValue()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(visible = !isAlwaysOpen){
            Column {
                SelectTimeSection(EditTimeType.OPENING_HOURS)

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        BreakTimeSection()
    }
}
@Composable
fun SelectTimeSection(thisType: EditTimeType) {
    val closedDaySettingViewModel = LocalClosedDaySettingViewModel.current

    val isStartTime by remember { closedDaySettingViewModel.isStartTime }.collectAsState()
    val nowEditTimeType by remember { closedDaySettingViewModel.nowEditTimeType }.collectAsState()

    val selectedStartHour = remember { mutableStateOf(9) }
    val selectedStartMinute = remember { mutableStateOf(0) }

    val selectedEndHour = remember { mutableStateOf(21) }
    val selectedEndMinute = remember { mutableStateOf(0) }

    val typeSelected = thisType ==  nowEditTimeType

    var isInitialized by remember { mutableStateOf(false) }


    if(!isInitialized){
        when(thisType) {
            EditTimeType.OPENING_HOURS -> {
                selectedStartHour.value = if(closedDaySettingViewModel.openingStartHours.value != -1) closedDaySettingViewModel.openingStartHours.value else 9
                selectedStartMinute.value = if(closedDaySettingViewModel.openingStartMinutes.value != -1) closedDaySettingViewModel.openingStartMinutes.value else 0

                selectedEndHour.value = if(closedDaySettingViewModel.openingEndHours.value != -1) closedDaySettingViewModel.openingEndHours.value else 21
                selectedEndMinute.value = if(closedDaySettingViewModel.openingEndMinutes.value != -1) closedDaySettingViewModel.openingEndMinutes.value else 0
            }
            EditTimeType.BREAK_TIME -> {
                selectedStartHour.value = if(closedDaySettingViewModel.startBreakHours.value != -1) closedDaySettingViewModel.startBreakHours.value else 9
                selectedStartMinute.value = if(closedDaySettingViewModel.startBreakMinutes.value != -1) closedDaySettingViewModel.startBreakMinutes.value else 0

                selectedEndHour.value = if(closedDaySettingViewModel.endBreakHours.value != -1) closedDaySettingViewModel.endBreakHours.value else 21
                selectedEndMinute.value = if(closedDaySettingViewModel.endBreakMinutes.value != -1) closedDaySettingViewModel.endBreakMinutes.value else 0
            }
        }

        isInitialized = true
    }


    closedDaySettingViewModel.setSelectedStartTime(thisType, selectedStartHour.value, selectedStartMinute.value)
    closedDaySettingViewModel.setSelectedEndTime(thisType, selectedEndHour.value, selectedEndMinute.value)

    LaunchedEffect(selectedStartHour.value, selectedStartMinute.value) {
        if (typeSelected) {
            closedDaySettingViewModel.setSelectedStartTime(thisType, selectedStartHour.value, selectedStartMinute.value)
        }
    }

    LaunchedEffect(selectedEndHour.value, selectedEndMinute.value) {
        if (typeSelected) {
            closedDaySettingViewModel.setSelectedEndTime(thisType, selectedEndHour.value, selectedEndMinute.value)
        }
    }

    Column(
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
                        .height(SizeUtils().textSizeToDp(LocalDensity.current, 0, 20))
                ) {
                    closedDaySettingViewModel.setIsStartTime(true)
                    closedDaySettingViewModel.setNowEditTimeType(thisType)
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
                        .height(SizeUtils().textSizeToDp(LocalDensity.current, 0, 20))
                ) {
                    closedDaySettingViewModel.setIsStartTime(false)
                    closedDaySettingViewModel.setNowEditTimeType(thisType)
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
fun BreakTimeSection(modifier: Modifier = Modifier) {
    val closedDaySettingViewModel = LocalClosedDaySettingViewModel.current
    val hasBreakTime by remember { closedDaySettingViewModel.hasBreakTime }.collectAsState()

    Column(
        modifier = modifier
    ) {
        SmallButton(text = if(hasBreakTime) "브레이크타임 제거" else "브레이크타임 추가") {
            closedDaySettingViewModel.changeHasBreakTimeValue()
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

                Spacer(modifier = Modifier.height(20.dp))

                SelectTimeSection(EditTimeType.BREAK_TIME)
            }

        }
    }

}