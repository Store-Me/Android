package com.store_me.storeme.ui.store_setting.business_hours

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.store.TemporaryBusinessData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TemporaryBusinessSettingViewModel: ViewModel() {
    private val _temporaryBusinesses = MutableStateFlow<List<TemporaryBusinessData>>(emptyList())
    val temporaryBusinesses: StateFlow<List<TemporaryBusinessData>> = _temporaryBusinesses

    fun updateTemporaryBusinesses(temporaryBusinessData: TemporaryBusinessData) {

    }
}