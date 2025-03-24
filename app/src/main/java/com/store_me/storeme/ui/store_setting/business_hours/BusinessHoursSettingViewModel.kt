package com.store_me.storeme.ui.store_setting.business_hours

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.TimeData
import com.store_me.storeme.data.getText
import com.store_me.storeme.data.store.BusinessHourData
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.ErrorEventBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessHoursSettingViewModel: ViewModel() {
    private val _hasHoliday = MutableStateFlow<Boolean?>(null)
    val hasHoliday: StateFlow<Boolean?> = _hasHoliday

    private val _selectedWeeks = MutableStateFlow<Set<DateTimeUtils.DayOfWeek>>(emptySet())
    val selectedWeeks: StateFlow<Set<DateTimeUtils.DayOfWeek>> = _selectedWeeks

    private val _hasSameBusinessHours = MutableStateFlow<Boolean?>(null)
    val hasSameBusinessHours: StateFlow<Boolean?> = _hasSameBusinessHours

    private val _isAlwaysOpen = MutableStateFlow(List(7) { false })
    val isAlwaysOpen: StateFlow<List<Boolean>> = _isAlwaysOpen

    private val _hasBreakTime = MutableStateFlow(List(7) { false })
    val hasBreakTime: StateFlow<List<Boolean>> = _hasBreakTime

    //시간 정보
    private val _startBusinessTimes = MutableStateFlow(List<TimeData?>(7) { null })
    val startBusinessTimes: StateFlow<List<TimeData?>> = _startBusinessTimes

    private val _endBusinessTimes = MutableStateFlow(List<TimeData?>(7) { null })
    val endBusinessTimes: StateFlow<List<TimeData?>> = _endBusinessTimes

    private val _startBreakTimes = MutableStateFlow(List<TimeData?>(7) { null })
    val startBreakTimes: StateFlow<List<TimeData?>> = _startBreakTimes

    private val _endBreakTimes = MutableStateFlow(List<TimeData?>(7) { null })
    val endBreakTimes: StateFlow<List<TimeData?>> = _endBreakTimes

    //추가 정보
    private val _extraInfo = MutableStateFlow<String>("")
    val extraInfo: StateFlow<String> = _extraInfo


    fun updateHasHoliday(value: Boolean) {
        _hasHoliday.value = value
    }

    fun updateSelectedWeeks(selectedWeek: DateTimeUtils.DayOfWeek) {
        _selectedWeeks.value = _selectedWeeks.value.toMutableSet().apply {
            if(!add(selectedWeek)) {
                remove(selectedWeek)
            }
        }
    }

    fun updateHasSameBusinessHours(hasSameBusinessHours: Boolean) {
        _hasSameBusinessHours.value = hasSameBusinessHours
    }

    fun updateIsAlwaysOpen(index: Int? = null, value: Boolean) {
        _isAlwaysOpen.value = _isAlwaysOpen.value.mapIndexed { i, oldValue ->
            if (index == null || i == index) value else oldValue
        }
    }

    fun updateHasBreakTime(index: Int? = null, value: Boolean) {
        _hasBreakTime.value = _hasBreakTime.value.mapIndexed { i, oldValue ->
            if (index == null || i == index) value else oldValue
        }
    }

    //시간 업데이트
    private fun updateTimeAt(
        flow: MutableStateFlow<List<TimeData?>>,
        index: Int? = null,
        newTime: TimeData?
    ) {
        flow.value = flow.value.mapIndexed { i, time ->
            if (index == null || i == index) newTime else time
        }
    }

    //시간 업데이트 함수
    fun updateStartBusinessTime(index: Int? = null, newTime: TimeData?) {
        updateTimeAt(_startBusinessTimes, index, newTime)
    }

    fun updateEndBusinessTime(index: Int? = null, newTime: TimeData?) {
        updateTimeAt(_endBusinessTimes, index, newTime)
    }

    fun updateStartBreakTime(index: Int? = null, newTime: TimeData?) {
        updateTimeAt(_startBreakTimes, index, newTime)
    }

    fun updateEndBreakTime(index: Int? = null, newTime: TimeData?) {
        updateTimeAt(_endBreakTimes, index, newTime)
    }

    fun getSameBusinessHourIndices(selectedIndex: Int): Set<Int> {
        if (selectedIndex !in 0..6) return emptySet()

        val isInitialState =
            !isAlwaysOpen.value[selectedIndex] &&
                    !hasBreakTime.value[selectedIndex] &&
                    startBusinessTimes.value[selectedIndex] == null &&
                    endBusinessTimes.value[selectedIndex] == null &&
                    startBreakTimes.value[selectedIndex] == null &&
                    endBreakTimes.value[selectedIndex] == null

        if (isInitialState) {
            return setOf(selectedIndex) // 자기 자신만
        }

        return DateTimeUtils.DayOfWeek.entries.mapIndexedNotNull { index, _ ->
            if (
                isAlwaysOpen.value[index] == isAlwaysOpen.value[selectedIndex] &&
                hasBreakTime.value[index] == hasBreakTime.value[selectedIndex] &&
                startBusinessTimes.value[index] == startBusinessTimes.value[selectedIndex] &&
                endBusinessTimes.value[index] == endBusinessTimes.value[selectedIndex] &&
                startBreakTimes.value[index] == startBreakTimes.value[selectedIndex] &&
                endBreakTimes.value[index] == endBreakTimes.value[selectedIndex]
            ) {
                index
            } else null
        }.toSet()
    }

    private fun <T> allItemsEqual(
        list: List<T?>,
        selected: Set<DateTimeUtils.DayOfWeek>
    ): Boolean {
        val selectedIndices = selected.map { it.ordinal }
        val filtered = list.filterIndexed { index, _ -> index in selectedIndices }
        if (filtered.any { it == null }) return false
        return filtered.distinct().size <= 1
    }

    fun areAllBusinessSettingsSame(): Boolean {
        val selected = selectedWeeks.value

        return allItemsEqual(isAlwaysOpen.value, selected) &&
                allItemsEqual(hasBreakTime.value, selected) &&
                allItemsEqual(startBusinessTimes.value, selected) &&
                allItemsEqual(endBusinessTimes.value, selected) &&
                allItemsEqual(startBreakTimes.value, selected) &&
                allItemsEqual(endBreakTimes.value, selected)
    }

    private fun isFinished(
        isAlwaysOpen: Boolean,
        hasBreakTime: Boolean,
        startBusiness: TimeData?,
        endBusiness: TimeData?,
        startBreak: TimeData?,
        endBreak: TimeData?
    ): Boolean {
        return when {
            isAlwaysOpen && !hasBreakTime -> true

            isAlwaysOpen && hasBreakTime ->
                startBreak != null && endBreak != null

            !isAlwaysOpen && !hasBreakTime ->
                startBusiness != null && endBusiness != null

            else -> // !isAlwaysOpen && hasBreakTime
                startBusiness != null && endBusiness != null &&
                        startBreak != null && endBreak != null
        }
    }

    fun areAllFinished(): Boolean {
        val selected = selectedWeeks.value

        for (i in 0 until 7) {
            if (DateTimeUtils.DayOfWeek.entries[i] in selected) continue // 제외

            val valid = isFinished(
                isAlwaysOpen = isAlwaysOpen.value[i],
                hasBreakTime = hasBreakTime.value[i],
                startBusiness = startBusinessTimes.value[i],
                endBusiness = endBusinessTimes.value[i],
                startBreak = startBreakTimes.value[i],
                endBreak = endBreakTimes.value[i]
            )

            if (!valid) return false
        }
        return true
    }

    fun updateExtraInfo(value: String) {
        _extraInfo.value = value
    }

    fun getBusinessHours(): List<BusinessHourData> {
        val result = mutableListOf<BusinessHourData>()

        try {
            DateTimeUtils.DayOfWeek.entries.forEachIndexed { index, dayOfWeek ->
                when {
                    selectedWeeks.value.contains(dayOfWeek) -> {
                        result.add(BusinessHourData(openingTime = null, closingTime = null, startBreak = null, endBreak = null, isHoliday = true))
                    }
                    isAlwaysOpen.value[index] -> {
                        //24시
                        if(hasBreakTime.value[index]) {
                            result.add(BusinessHourData(
                                openingTime = null,
                                closingTime = null,
                                startBreak = startBreakTimes.value[index]!!.getText(),
                                endBreak = endBreakTimes.value[index]!!.getText(),
                                isHoliday = false,

                                ))
                        } else {
                            result.add(BusinessHourData(
                                openingTime = null,
                                closingTime = null,
                                startBreak = null,
                                endBreak = null,
                                isHoliday = false,
                            ))
                        }
                    }
                    else -> {
                        if(hasBreakTime.value[index]) {
                            result.add(BusinessHourData(
                                openingTime = startBusinessTimes.value[index]!!.getText(),
                                closingTime = endBusinessTimes.value[index]!!.getText(),
                                startBreak = startBreakTimes.value[index]!!.getText(),
                                endBreak = endBreakTimes.value[index]!!.getText(),
                                isHoliday = false,

                                ))
                        } else {
                            result.add(BusinessHourData(
                                openingTime = startBusinessTimes.value[index]!!.getText(),
                                closingTime = endBusinessTimes.value[index]!!.getText(),
                                startBreak = null,
                                endBreak = null,
                                isHoliday = false,
                            ))
                        }
                    }
                }

            }
        } catch (e: Exception) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("일부 항목이 비어있습니다. 다시 시도해주세요.")
            }
        }

        return result
    }
}