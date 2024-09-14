package com.store_me.storeme.ui.store_setting.menu.category

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuCategorySettingViewModel: ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    fun updateName(name: String){
        _name.value = name
    }
}