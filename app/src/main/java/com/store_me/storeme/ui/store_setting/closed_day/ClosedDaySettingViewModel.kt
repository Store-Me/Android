package com.store_me.storeme.ui.store_setting.closed_day

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.DailyHoursData
import com.store_me.storeme.data.StoreHoursData
import com.store_me.storeme.data.TemporaryOpeningHours
import com.store_me.storeme.ui.store_setting.opening_hours.OpeningHoursSettingViewModel.*
import com.store_me.storeme.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class ClosedDaySettingViewModel: ViewModel() {
    enum class ClosedDayType{
        EXIST, NONE
    }

    private val _selectedType: MutableStateFlow<ClosedDayType?> = MutableStateFlow(null)
    val selectedType: StateFlow<ClosedDayType?> = _selectedType

    fun setSelectedType(closedDayType: ClosedDayType) {
        _selectedType.value = closedDayType
    }

    private val _selectedWeeks = MutableStateFlow<Set<DateTimeUtils.DayOfWeek>>(setOf())
    val selectedWeeks: StateFlow<Set<DateTimeUtils.DayOfWeek>> = _selectedWeeks

    fun setSelectedWeeks(dayOfWeek: DateTimeUtils.DayOfWeek) {
        _selectedWeeks.value = _selectedWeeks.value.toMutableSet().apply {
            if(!add(dayOfWeek)) {
                remove(dayOfWeek)
            }
        }
    }

    private val _selectedStartDate = MutableStateFlow<LocalDate?>(null)
    val selectedStartDate: StateFlow<LocalDate?> = _selectedStartDate

    private val _selectedEndDate = MutableStateFlow<LocalDate?>(null)
    val selectedEndDate: StateFlow<LocalDate?> = _selectedEndDate

    fun updateSelectedStartDate(date: LocalDate) {
        _selectedStartDate.value = date
    }

    fun updateSelectedEndDate(date: LocalDate) {
        _selectedEndDate.value = date
    }

    enum class ClosedTimeType{
        ADJUSTED, CLOSED
    }

    private val _closedTimeType = MutableStateFlow <ClosedTimeType?>(null)
    val closedTimeType: StateFlow<ClosedTimeType?> = _closedTimeType

    fun updateClosedTimeType(type: ClosedTimeType) {
        _closedTimeType.value = type
    }

    //Break Time 존재 여부
    private val _hasBreakTime: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasBreakTime: StateFlow<Boolean> = _hasBreakTime

    //Break Time 존재 여부 변경
    fun changeHasBreakTimeValue(hasBreakTime: Boolean = !_hasBreakTime.value) {
        _hasBreakTime.value = hasBreakTime
    }

    //24시간 영업 설정 여부
    private val _isAlwaysOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAlwaysOpen: StateFlow<Boolean> = _isAlwaysOpen

    //24시간 영업 설정 변경
    fun changeIsAlwaysOpenValue(isAlwaysOpen: Boolean = !_isAlwaysOpen.value) {
        _isAlwaysOpen.value = isAlwaysOpen
    }

    //편집 중인 시간 정보
    private val _nowEditTimeType: MutableStateFlow<EditTimeType?> = MutableStateFlow(null)
    val nowEditTimeType: StateFlow<EditTimeType?> = _nowEditTimeType

    fun setNowEditTimeType(editTimeType: EditTimeType) {
        _nowEditTimeType.value = editTimeType
    }

    //시작 시간 설정 상태
    private val _isStartTime: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isStartTime: StateFlow<Boolean> = _isStartTime

    fun setIsStartTime(isStartTime: Boolean) {
        _isStartTime.value = isStartTime
    }

    //선택 된 시간 값들
    private val _openingStartHours: MutableStateFlow<Int> = MutableStateFlow(-1)
    val openingStartHours: StateFlow<Int> = _openingStartHours

    private val _openingStartMinutes: MutableStateFlow<Int> = MutableStateFlow(-1)
    val openingStartMinutes: StateFlow<Int> = _openingStartMinutes

    private val _openingEndHours: MutableStateFlow<Int> = MutableStateFlow(-1)
    val openingEndHours: StateFlow<Int> = _openingEndHours

    private val _openingEndMinutes: MutableStateFlow<Int> = MutableStateFlow(-1)
    val openingEndMinutes: StateFlow<Int> = _openingEndMinutes

    private val _startBreakHours: MutableStateFlow<Int> = MutableStateFlow(-1)
    val startBreakHours: StateFlow<Int> = _startBreakHours

    private val _startBreakMinutes: MutableStateFlow<Int> = MutableStateFlow(-1)
    val startBreakMinutes: StateFlow<Int> = _startBreakMinutes

    private val _endBreakHours: MutableStateFlow<Int> = MutableStateFlow(-1)
    val endBreakHours: StateFlow<Int> = _endBreakHours

    private val _endBreakMinutes: MutableStateFlow<Int> = MutableStateFlow(-1)
    val endBreakMinutes: StateFlow<Int> = _endBreakMinutes

    fun setSelectedStartTime(type: EditTimeType, hours: Int, minutes: Int) {
        when(type) {
            EditTimeType.OPENING_HOURS -> {
                _openingStartHours.value = hours
                _openingStartMinutes.value = minutes
            }
            EditTimeType.BREAK_TIME -> {
                _startBreakHours.value = hours
                _startBreakMinutes.value = minutes
            }
        }
    }

    fun setSelectedEndTime(type: EditTimeType, hours: Int, minutes: Int) {
        when(type) {
            EditTimeType.OPENING_HOURS -> {
                _openingEndHours.value = hours
                _openingEndMinutes.value = minutes
            }
            EditTimeType.BREAK_TIME -> {
                _endBreakHours.value = hours
                _endBreakMinutes.value = minutes
            }
        }
    }

    //Bottom 진행 상태
    enum class BottomProgress{
        DATE, TIME
    }

    private val _currentBottomProgress = MutableStateFlow(BottomProgress.DATE)
    val currentBottomProgress: StateFlow<BottomProgress> = _currentBottomProgress

    fun updateCurrentBottomProgress(bottomProgress: BottomProgress) {
        _currentBottomProgress.value = bottomProgress
    }

    fun addTemporalOpeningHoursData() {
        val startDate = _selectedStartDate.value
        val endDate = _selectedEndDate.value

        when(closedTimeType.value){
            ClosedTimeType.CLOSED -> {
                Auth.setStoreHoursData(
                    storeHoursData = Auth.storeHoursData.value.copy(
                        temporaryOpeningHours = Auth.storeHoursData.value.temporaryOpeningHours.toMutableList().apply {
                            add(
                                TemporaryOpeningHours.Closed(
                                    startYear = startDate!!.year,
                                    startMonth = startDate.month.value,
                                    startDay = startDate.dayOfMonth,
                                    endYear = endDate!!.year,
                                    endMonth = endDate.month.value,
                                    endDay = endDate.dayOfMonth,
                                )
                            )
                        }
                    )
                )
            }
            ClosedTimeType.ADJUSTED -> {
                Auth.setStoreHoursData(
                    storeHoursData = Auth.storeHoursData.value.copy(
                        temporaryOpeningHours = Auth.storeHoursData.value.temporaryOpeningHours.toMutableList().apply {
                            add(
                                TemporaryOpeningHours.Adjusted(
                                    startYear = startDate!!.year,
                                    startMonth = startDate.month.value,
                                    startDay = startDate.dayOfMonth,
                                    endYear = endDate!!.year,
                                    endMonth = endDate.month.value,
                                    endDay = endDate.dayOfMonth,
                                    dailyHoursData = DailyHoursData(
                                        openHours = _openingStartHours.value,
                                        openMinutes = _openingStartMinutes.value,
                                        closeHours = _openingEndHours.value,
                                        closeMinutes = _openingEndMinutes.value,
                                        startBreakHours = _startBreakHours.value,
                                        startBreakMinutes = _startBreakMinutes.value,
                                        endBreakHours = _endBreakHours.value,
                                        endBreakMinutes = _endBreakMinutes.value,
                                        hasBreakTime = _hasBreakTime.value,
                                        isAlwaysOpen = _isAlwaysOpen.value,
                                    )
                                )
                            )
                        }
                    )
                )
            }
            else -> {

            }
        }
    }

    fun clearBottomData(){
        _selectedStartDate.value = null
        _selectedEndDate.value = null

        _closedTimeType.value = null

        _currentBottomProgress.value = BottomProgress.DATE
    }
}