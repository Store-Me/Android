package com.store_me.storeme.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CustomerHomeViewModel () : ViewModel() {
    private val _searchState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchState: StateFlow<Boolean> = _searchState

    fun setSearchState(value: Boolean){
        _searchState.value = value
    }
}