package com.store_me.storeme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.data.DateData
import com.store_me.storeme.ui.theme.BeforeMonthDateColor
import com.store_me.storeme.ui.theme.BlockBeforeIconColor
import com.store_me.storeme.ui.theme.DayOfWeekTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.SizeUtils
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.abs

enum class SelectedDateType{
    START_DATE, END_DATE, MIDDLE_DATE, NONE, ONLY_DATE
}

@Composable
fun StoreMeSelectRangeCalendar(
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit
) {
    val nowDate = DateData(year = LocalDateTime.now().year, month = LocalDateTime.now().monthValue, day = LocalDateTime.now().dayOfMonth)

    val currentMonth = remember { mutableStateOf(nowDate.month) }
    val currentYear = remember { mutableStateOf(nowDate.year) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        SelectMonthSection(
            nowDate = nowDate,
            year = currentYear.value,
            month = currentMonth.value,
            onBeforeMonth = {
                if(currentMonth.value == 1){
                    currentMonth.value = 12
                    currentYear.value --
                } else {
                    currentMonth.value--
                }
            },
            onAfterMonth = {
                if(currentMonth.value == 12) {
                    currentMonth.value = 1
                    currentYear.value++
                } else {
                    currentMonth.value++
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        WeekOfDayTextSection()

        Spacer(modifier = Modifier.height(10.dp))

        CalendarSelectRangeSection(
            year = currentYear.value,
            month = currentMonth.value,
            startDate = startDate,
            endDate = endDate,
            onStartDateChange = { onStartDateChange(it) },
            onEndDateChange = { onEndDateChange(it) }
        )
    }
}

@Composable
fun CalendarSelectRangeSection(
    year: Int,
    month: Int,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit
) {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = firstDayOfMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val beforeMonth = firstDayOfMonth.minusMonths(1)
    val beforeDayOfMonth = beforeMonth.lengthOfMonth()

    val afterMonth = firstDayOfMonth.plusMonths(1)

    val selectedStartDate = remember { mutableStateOf(startDate) }
    val selectedEndDate = remember { mutableStateOf(endDate) }

    fun isDateInRange(currentDate: LocalDate): SelectedDateType {
        return when {
            selectedEndDate.value == null || selectedEndDate.value == null -> SelectedDateType.NONE

            currentDate == selectedStartDate.value && selectedStartDate.value == selectedEndDate.value -> SelectedDateType.ONLY_DATE

            currentDate == selectedStartDate.value -> SelectedDateType.START_DATE

            currentDate == selectedEndDate.value -> SelectedDateType.END_DATE

            currentDate in selectedStartDate.value!! .. selectedEndDate.value!! -> SelectedDateType.MIDDLE_DATE

            else -> SelectedDateType.NONE
        }
    }

    fun onDateClick(date: LocalDate){
        when{
            selectedStartDate.value == null || selectedEndDate.value == null -> {
                //처음 선택 시
                selectedStartDate.value = date
                selectedEndDate.value = date

                onStartDateChange(date)
                onEndDateChange(date)
            }

            selectedStartDate.value == selectedEndDate.value -> {
                if(date.isAfter(selectedStartDate.value)) {
                    selectedEndDate.value = date
                    onEndDateChange(date)
                } else {
                    selectedStartDate.value = date
                    onStartDateChange(date)
                }
            }

            else -> {
                selectedEndDate.value = date
                selectedStartDate.value = date

                onStartDateChange(date)
                onEndDateChange(date)
            }

        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var currentDay = 1

        val boxSize = SizeUtils.textSizeToDp(density = LocalDensity.current, diffValue = 2, offset = 20)

        var weekCount = 0
        weekCount += daysInMonth / 7
        if(firstDayOfWeek != 0) {   //1일이 일요일이 아닐 경우
            weekCount += 1
        }

        if(weekCount * 7 < daysInMonth)
            weekCount += 1

        fun getBoxColor(type: SelectedDateType): Color {
            return if (type != SelectedDateType.NONE)
                Black
            else
                Transparent
        }

        fun getBoxShape(type: SelectedDateType): Shape {
            return when(type) {
                SelectedDateType.START_DATE -> { CircleShape }
                SelectedDateType.END_DATE -> { CircleShape }
                SelectedDateType.ONLY_DATE -> { CircleShape }
                SelectedDateType.NONE -> { RectangleShape }
                else -> { RectangleShape }
            }
        }

        fun getTextColor(type: SelectedDateType, color: Color = Black): Color {
            return if (type != SelectedDateType.NONE)
                White
            else
                color
        }

        @Composable
        fun FillColorBox(type: SelectedDateType){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boxSize)
            ) {
                Box(
                    modifier = Modifier
                        .height(boxSize)
                        .weight(1f)
                        .background(
                            color = if(type == SelectedDateType.END_DATE || type == SelectedDateType.MIDDLE_DATE) Black else Transparent
                        )
                ) {

                }

                Box(
                    modifier = Modifier
                        .height(boxSize)
                        .weight(1f)
                        .background(
                            color = if(type == SelectedDateType.START_DATE || type == SelectedDateType.MIDDLE_DATE) Black else Transparent
                        )
                ) {

                }
            }
        }

        for(week in 0 until weekCount) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for(day in 0 until 7) {
                    when {
                        week == 0 && day < firstDayOfWeek -> {
                            //이전 달 영역
                            val beforeMonthDay = beforeDayOfMonth - (abs(day - firstDayOfWeek) - 1)
                            val date = LocalDate.of(beforeMonth.year, beforeMonth.month, beforeMonthDay)
                            val type = isDateInRange(date)

                            Box(
                                modifier = Modifier
                                    .height(boxSize)
                                    .weight(1f)
                                    .clickable(
                                        onClick = { onDateClick(date) },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false),
                                    ),
                            ) {
                                FillColorBox(type)

                                Box(
                                    modifier = Modifier
                                        .size(boxSize)
                                        .background(
                                            color = getBoxColor(type),
                                            shape = getBoxShape(type)
                                        )
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = beforeMonthDay.toString(),
                                        style = storeMeTextStyle(FontWeight.Bold, 2),
                                        color = getTextColor(type, BeforeMonthDateColor)
                                    )
                                }

                            }
                        }

                        currentDay > daysInMonth -> {

                            val afterMonthDay = currentDay - daysInMonth
                            val date = LocalDate.of(afterMonth.year, afterMonth.month, afterMonthDay)
                            val type = isDateInRange(date)

                            Box(
                                modifier = Modifier
                                    .height(boxSize)
                                    .weight(1f)
                                    .clickable(
                                        onClick = { onDateClick(date) },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false),
                                    ),
                            ) {
                                FillColorBox(type)

                                Box(
                                    modifier = Modifier
                                        .size(boxSize)
                                        .background(
                                            color = getBoxColor(type),
                                            shape = getBoxShape(type)
                                        )
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (currentDay - daysInMonth).toString(),
                                        style = storeMeTextStyle(FontWeight.Bold, 2),
                                        color = getTextColor(type, BeforeMonthDateColor)
                                    )
                                }
                            }

                            currentDay++
                        }

                        else -> {
                            val date = LocalDate.of(year, month, currentDay)
                            val type = isDateInRange(date)

                            Box(
                                modifier = Modifier
                                    .height(boxSize)
                                    .weight(1f)
                                    .clickable(
                                        onClick = { onDateClick(date) },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false),
                                    ),
                            ) {
                                FillColorBox(type)

                                Box(
                                    modifier = Modifier
                                        .size(boxSize)
                                        .background(
                                            color = getBoxColor(type),
                                            shape = getBoxShape(type)
                                        )
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = currentDay.toString(),
                                        style = storeMeTextStyle(FontWeight.Bold, 2),
                                        color = getTextColor(type)
                                    )
                                }
                            }

                            currentDay++
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun SelectMonthSection(nowDate: DateData, year: Int, month: Int, onBeforeMonth: () -> Unit, onAfterMonth: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = "이전 달",
            modifier = Modifier
                .size(14.dp)
                .clickable(
                    onClick = onBeforeMonth,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false),
                    enabled = !(nowDate.year == year && nowDate.month == month)
                ),
            tint = if(nowDate.year == year && nowDate.month == month) BlockBeforeIconColor else Black
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(text = "${year}년 ${month}월", style = storeMeTextStyle(FontWeight.ExtraBold, 6))

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "다음 달",
            modifier = Modifier
                .size(14.dp)
                .clickable(
                    onClick = onAfterMonth,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false),
                ),
            tint = Black
        )
    }
}

@Composable
fun StoreMeSelectDateCalendar(
    date: LocalDate? = null,
    onDateChange: (LocalDate) -> Unit
) {
    val nowDate = DateData(year = LocalDateTime.now().year, month = LocalDateTime.now().monthValue, day = LocalDateTime.now().dayOfMonth)

    val currentMonth = remember { mutableStateOf(nowDate.month) }
    val currentYear = remember { mutableStateOf(nowDate.year) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        SelectMonthSection(
            nowDate = nowDate,
            year = currentYear.value,
            month = currentMonth.value,
            onBeforeMonth = {
                if(currentMonth.value == 1){
                    currentMonth.value = 12
                    currentYear.value --
                } else {
                    currentMonth.value--
                }
            },
            onAfterMonth = {
                if(currentMonth.value == 12) {
                    currentMonth.value = 1
                    currentYear.value++
                } else {
                    currentMonth.value++
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        WeekOfDayTextSection()

        Spacer(modifier = Modifier.height(10.dp))

        CalendarSelectDateSection(
            year = currentYear.value,
            month = currentMonth.value,
            date = date,
            onDateChange = { onDateChange(it) }
        )
    }
}

@Composable
fun CalendarSelectDateSection(
    year: Int,
    month: Int,
    date: LocalDate? = null,
    onDateChange: (LocalDate) -> Unit
) {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = firstDayOfMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val beforeMonth = firstDayOfMonth.minusMonths(1)
    val beforeDayOfMonth = beforeMonth.lengthOfMonth()

    val afterMonth = firstDayOfMonth.plusMonths(1)

    val selectedDate = remember { mutableStateOf(date) }

    fun onDateClick(date: LocalDate){
        selectedDate.value = date
        onDateChange(date)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var currentDay = 1

        val boxSize = SizeUtils.textSizeToDp(density = LocalDensity.current, diffValue = 2, offset = 20)

        var weekCount = 0
        weekCount += daysInMonth / 7
        if(firstDayOfWeek != 0) {   //1일이 일요일이 아닐 경우
            weekCount += 1
        }

        if(weekCount * 7 < daysInMonth)
            weekCount += 1

        for(week in 0 until weekCount) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for(day in 0 until 7) {
                    when {
                        week == 0 && day < firstDayOfWeek -> {
                            //이전 달 영역
                            val beforeMonthDay = beforeDayOfMonth - (abs(day - firstDayOfWeek) - 1)
                            val dateValue = LocalDate.of(beforeMonth.year, beforeMonth.month, beforeMonthDay)
                            val isSelected = dateValue == selectedDate.value

                            Box(
                                modifier = Modifier
                                    .height(boxSize)
                                    .weight(1f)
                                    .clickable(
                                        onClick = { onDateClick(dateValue) },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false),
                                    ),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(boxSize)
                                        .background(
                                            color = if(isSelected) Black else Transparent,
                                            shape = if(isSelected) CircleShape else RectangleShape
                                        )
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = beforeMonthDay.toString(),
                                        style = storeMeTextStyle(FontWeight.Bold, 2),
                                        color = if(isSelected) White else BeforeMonthDateColor
                                    )
                                }

                            }
                        }

                        currentDay > daysInMonth -> {

                            val afterMonthDay = currentDay - daysInMonth
                            val dateValue = LocalDate.of(afterMonth.year, afterMonth.month, afterMonthDay)
                            val isSelected = dateValue == selectedDate.value

                            Box(
                                modifier = Modifier
                                    .height(boxSize)
                                    .weight(1f)
                                    .clickable(
                                        onClick = { onDateClick(dateValue) },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false),
                                    ),
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(boxSize)
                                        .background(
                                            color = if(isSelected) Black else Transparent,
                                            shape = if(isSelected) CircleShape else RectangleShape
                                        )
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (currentDay - daysInMonth).toString(),
                                        style = storeMeTextStyle(FontWeight.Bold, 2),
                                        color = if(isSelected) White else BeforeMonthDateColor
                                    )
                                }
                            }

                            currentDay++
                        }

                        else -> {
                            val dateValue = LocalDate.of(year, month, currentDay)
                            val isSelected = dateValue == selectedDate.value

                            Box(
                                modifier = Modifier
                                    .height(boxSize)
                                    .weight(1f)
                                    .clickable(
                                        onClick = { onDateClick(dateValue) },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false),
                                    ),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(boxSize)
                                        .background(
                                            color = if(isSelected) Black else Transparent,
                                            shape = if(isSelected) CircleShape else RectangleShape
                                        )
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = currentDay.toString(),
                                        style = storeMeTextStyle(FontWeight.Bold, 2),
                                        color = if(isSelected) White else Black
                                    )
                                }
                            }

                            currentDay++
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun WeekOfDayTextSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val boxSize = SizeUtils.textSizeToDp(density = LocalDensity.current, diffValue = 2, offset = 20)

        DateTimeUtils.DayOfWeek.entries.forEach {
            Box(
                modifier = Modifier
                    .height(boxSize)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = it.displayName, style = storeMeTextStyle(FontWeight.Bold, 2), color = DayOfWeekTextColor)
            }
        }
    }
}
