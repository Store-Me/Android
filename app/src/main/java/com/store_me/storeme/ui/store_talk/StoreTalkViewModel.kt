package com.store_me.storeme.ui.store_talk

import androidx.lifecycle.ViewModel
import com.store_me.storeme.utils.SampleDataUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StoreTalkViewModel : ViewModel() {
    val sampleChatRoomData = SampleDataUtils.sampleChatRooms()

    private val _searchState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchState: StateFlow<Boolean> = _searchState

    fun setSearchState(value: Boolean){
        _searchState.value = value
    }
}