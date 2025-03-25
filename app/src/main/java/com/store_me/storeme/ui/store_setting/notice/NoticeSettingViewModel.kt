package com.store_me.storeme.ui.store_setting.notice

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NoticeSettingViewModel: ViewModel(

) {
    private val _notice = MutableStateFlow("")
    val notice: StateFlow<String> = _notice

    fun updateNotice(notice: String) {
        _notice.value = notice
    }
}