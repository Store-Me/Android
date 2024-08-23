package com.store_me.storeme.ui.link

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LinkSettingViewModel: ViewModel() {
    private val _editState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val editState: StateFlow<Boolean> = _editState

    fun setEditState(state: Boolean) {
        _editState.value = state
    }
}