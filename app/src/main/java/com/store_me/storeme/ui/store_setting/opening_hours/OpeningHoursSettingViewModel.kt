package com.store_me.storeme.ui.store_setting.opening_hours

import android.util.Log
import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.DailyHoursData
import com.store_me.storeme.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OpeningHoursSettingViewModel: ViewModel() {

    // 매일 같음, 요일 마다 설정 처리를 위한 enum, StateFlow
    enum class OpeningHoursType{
        SAME, DIFFERENT
    }

    private val _selectedType: MutableStateFlow<OpeningHoursType?> = MutableStateFlow(null)
    val selectedType: StateFlow<OpeningHoursType?> = _selectedType

    //SelectedType 값 설정 함수
    fun setSelectedType(openingHoursType: OpeningHoursType) {
        _selectedType.value = openingHoursType
    }

    //24시간 영업 설정 여부
    private val _isAlwaysOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAlwaysOpen: StateFlow<Boolean> = _isAlwaysOpen

    //24시간 영업 설정 변경
    fun changeIsAlwaysOpenValue(isAlwaysOpen: Boolean = !_isAlwaysOpen.value) {
        _isAlwaysOpen.value = isAlwaysOpen
    }

    //Break Time 존재 여부
    private val _hasBreakTime: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasBreakTime: StateFlow<Boolean> = _hasBreakTime

    //Break Time 존재 여부 변경
    fun changeHasBreakTimeValue(hasBreakTime: Boolean = !_hasBreakTime.value) {
        _hasBreakTime.value = hasBreakTime
    }

    //시작 시간 설정 상태
    private val _isStartTime: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isStartTime: StateFlow<Boolean> = _isStartTime

    fun setIsStartTime(isStartTime: Boolean) {
        _isStartTime.value = isStartTime
    }

    //편집 중인 시간 정보
    private val _nowEditTimeType: MutableStateFlow<EditTimeType?> = MutableStateFlow(null)
    val nowEditTimeType: StateFlow<EditTimeType?> = _nowEditTimeType

    enum class EditTimeType {
        OPENING_HOURS, BREAK_TIME
    }

    fun setNowEditTimeType(editTimeType: EditTimeType) {
        _nowEditTimeType.value = editTimeType
    }

    //시간 정보
    private val _dailyHoursMap = MutableStateFlow(
        DateTimeUtils.DayOfWeek.entries.associateWith {
            DailyHoursData(
                openHours = -1,
                openMinutes = -1,
                closeHours = -1,
                closeMinutes = -1,
                startBreakHours = -1,
                startBreakMinutes = -1,
                endBreakHours = -1,
                endBreakMinutes = 0,
                hasBreakTime = false,
                isAlwaysOpen = false
            )
        }
    )

    val dailyHoursMap: StateFlow<Map<DateTimeUtils.DayOfWeek, DailyHoursData>> = _dailyHoursMap

    fun setDailyHours(dayOfWeek: DateTimeUtils.DayOfWeek, newHoursData: DailyHoursData) {
        _dailyHoursMap.value = _dailyHoursMap.value.toMutableMap().apply {
            this[dayOfWeek] = newHoursData
        }
    }

    fun setDailyHours(dayOfWeeks: Set<DateTimeUtils.DayOfWeek>, newHoursData: DailyHoursData) {
        _dailyHoursMap.value = _dailyHoursMap.value.toMutableMap().apply {
            dayOfWeeks.forEach {
                this[it] = newHoursData
            }
        }
    }

    //24시간 설정 여부, 브레이크 타임 존재 여부에 따른 필요한 시간 값
    enum class NeedTimeValue {
        NONE, OPENING_HOURS, BREAK_TIME, BOTH
    }

    fun getNeedTimeValue(dayOfWeek: DateTimeUtils.DayOfWeek): NeedTimeValue {
        return when {
            dailyHoursMap.value[dayOfWeek]?.isAlwaysOpen == true && dailyHoursMap.value[dayOfWeek]?.hasBreakTime == true -> {
                //24시 영업 O Break Time O
                NeedTimeValue.BREAK_TIME
            }

            dailyHoursMap.value[dayOfWeek]?.isAlwaysOpen == false && dailyHoursMap.value[dayOfWeek]?.hasBreakTime == true -> {
                //24시 영업 X Break Time O
                NeedTimeValue.BOTH
            }

            dailyHoursMap.value[dayOfWeek]?.isAlwaysOpen == true && dailyHoursMap.value[dayOfWeek]?.hasBreakTime == false -> {
                //24시 영업 O Break Time X
                NeedTimeValue.NONE
            }

            dailyHoursMap.value[dayOfWeek]?.isAlwaysOpen == false && dailyHoursMap.value[dayOfWeek]?.hasBreakTime == false -> {
                //24시 영업 X Break Time X
                NeedTimeValue.OPENING_HOURS
            }

            else -> {
                NeedTimeValue.BOTH
            }
        }
    }

    //입력값 완료 여부
    fun isFinished(dayOfWeek: DateTimeUtils.DayOfWeek): Boolean {
        //입력이 필요한 값에 따른 충족 여부 반환
        return when(getNeedTimeValue(dayOfWeek)) {
            NeedTimeValue.BOTH -> {
                ((dailyHoursMap.value[dayOfWeek]?.openHours != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.openMinutes != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.closeHours != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.closeMinutes != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.startBreakHours != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.startBreakMinutes != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.endBreakHours != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.endBreakMinutes != -1)
                        )
            }

            NeedTimeValue.OPENING_HOURS -> {
                ((dailyHoursMap.value[dayOfWeek]?.openHours != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.openMinutes != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.closeHours != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.closeMinutes != -1))
            }

            NeedTimeValue.BREAK_TIME -> {
                ((dailyHoursMap.value[dayOfWeek]?.startBreakHours != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.startBreakMinutes != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.endBreakHours != -1) &&
                        (dailyHoursMap.value[dayOfWeek]?.endBreakMinutes != -1))
            }

            NeedTimeValue.NONE -> {
                true
            }
        }
    }

    fun isAllFinished(): Boolean {
        return DateTimeUtils.DayOfWeek.entries.all { dayOfWeek ->
            isFinished(dayOfWeek)
        }
    }

    //동일한 영업시간 요일 설정
    private val _selectedWeeks = MutableStateFlow<Set<DateTimeUtils.DayOfWeek>>(setOf())
    val selectedWeeks: StateFlow<Set<DateTimeUtils.DayOfWeek>> = _selectedWeeks

    fun clearSelectedWeeks(standardDayOfWeek: DateTimeUtils.DayOfWeek) {
        _selectedWeeks.value = setOf(standardDayOfWeek)
    }

    fun findSameDailyHoursData(dayOfWeek: DateTimeUtils.DayOfWeek) {
        _selectedWeeks.value = setOf()

        val dailyHoursData = _dailyHoursMap.value[dayOfWeek]

        val sameDayOfWeek = _dailyHoursMap.value.filter { entry ->
            entry.value == dailyHoursData
        }.keys.toList()

        _selectedWeeks.value = _selectedWeeks.value.toMutableSet().apply {
            addAll(sameDayOfWeek)
        }
    }

    fun setSelectedWeeks(standardDayOfWeek: DateTimeUtils.DayOfWeek, dayOfWeek: DateTimeUtils.DayOfWeek) {
        _selectedWeeks.value = _selectedWeeks.value.toMutableSet().apply {
            if(standardDayOfWeek == dayOfWeek)
                return@apply

            if(!add(dayOfWeek)) {
                remove(dayOfWeek)
            }
        }
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

    //영업 시간 관련 기타 정보
}