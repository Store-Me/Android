package com.store_me.storeme.ui.store_setting.menu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuSettingViewModel: ViewModel() {
    private val _editState = MutableStateFlow(false)
    val editState: StateFlow<Boolean> = _editState

    fun setEditState(editState: Boolean){
        _editState.value = editState
    }


}