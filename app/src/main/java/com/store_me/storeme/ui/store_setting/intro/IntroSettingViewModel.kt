package com.store_me.storeme.ui.store_setting.intro

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IntroSettingViewModel(): ViewModel() {
    private val _intro = MutableStateFlow("")
    val intro: StateFlow<String> = _intro

    fun updateStoreIntro(intro: String) {
        _intro.value = intro
    }
}