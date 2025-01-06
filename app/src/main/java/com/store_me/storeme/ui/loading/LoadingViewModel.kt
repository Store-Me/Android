package com.store_me.storeme.ui.loading

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 로딩 상태 관리를 위한 ViewModel
 */
class LoadingViewModel: ViewModel(){
    //로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * 로딩 활성화
     */
    fun showLoading() {
        _isLoading.value = true
    }

    /**
     * 로딩 비 활성화
     */
    fun hideLoading() {
        _isLoading.value = false
    }
}