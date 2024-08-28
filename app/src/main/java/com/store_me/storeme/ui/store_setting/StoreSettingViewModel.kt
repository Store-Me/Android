package com.store_me.storeme.ui.store_setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.StoreHomeItemData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoreSettingViewModel: ViewModel() {
    /*
     * 수정 상태를 관리를 위한 editState
     */
    private val _editState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val editState: StateFlow<Boolean> = _editState

    fun setEditState(state: Boolean) {
        _editState.value = state
    }

    /*
     * Auth 의 데이터를 통해 수정 작업을 진행
     * storeHomeItemList 의 변경을 감지하여 업데이트 진행
     */
    private val _visibleItems = MutableStateFlow<List<StoreHomeItemData>>(emptyList())
    val visibleItems: StateFlow<List<StoreHomeItemData>> = _visibleItems

    private val _hiddenItems = MutableStateFlow<List<StoreHomeItemData>>(emptyList())
    val hiddenItems: StateFlow<List<StoreHomeItemData>> = _hiddenItems

    init {
        viewModelScope.launch {
            Auth.storeHomeItemList.collectLatest {
                sortStoreHomeItems(it)
            }
        }
    }

    private fun sortStoreHomeItems(items: List<StoreHomeItemData>) {
        _visibleItems.value = items
            .filter { !it.isHidden }
            .sortedBy { it.order }

        _hiddenItems.value = items
            .filter { it.isHidden }
    }
}