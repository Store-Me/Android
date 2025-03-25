package com.store_me.storeme.ui.store_setting.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.database.location.LocationDataBaseHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSettingViewModel @Inject constructor(
    private val dbHelper: LocationDataBaseHelper,
): ViewModel() {
    private val _hasSameValue = MutableStateFlow(true)
    val hasSameValue:StateFlow<Boolean> = _hasSameValue

    fun hasExactMatch(query: String) {
        viewModelScope.launch {
           _hasSameValue.value = dbHelper.hasExactMatch(query)
        }
    }
}