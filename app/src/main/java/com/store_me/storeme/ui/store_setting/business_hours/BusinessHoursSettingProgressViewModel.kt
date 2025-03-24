package com.store_me.storeme.ui.store_setting.business_hours

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.enums.progress.BusinessHoursSettingProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BusinessHoursSettingProgressViewModel: ViewModel() {
    //진행 상태
    private val _progressState = MutableStateFlow<BusinessHoursSettingProgress>(BusinessHoursSettingProgress.HOLIDAY)
    val progressState: StateFlow<BusinessHoursSettingProgress> = _progressState

    fun moveToNextProgress() {
        val nextProgress = when(_progressState.value) {
            BusinessHoursSettingProgress.HOLIDAY -> BusinessHoursSettingProgress.BUSINESS_HOURS
            BusinessHoursSettingProgress.BUSINESS_HOURS -> return
        }

        _progressState.value = nextProgress
    }

    fun moveToPreviousProgress() {
        val nextProgress = when(_progressState.value) {
            BusinessHoursSettingProgress.HOLIDAY -> return
            BusinessHoursSettingProgress.BUSINESS_HOURS -> BusinessHoursSettingProgress.HOLIDAY
        }

        _progressState.value = nextProgress
    }
}