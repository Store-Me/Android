package com.store_me.storeme.ui.post.normal

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddNormalPostViewModel: ViewModel() {
    private val _selectedLabel = MutableStateFlow("기본 라벨")
    val selectedLabel: StateFlow<String> = _selectedLabel

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    fun updateSelectedLabel(newSelectedLabel: String) {
        _selectedLabel.value = newSelectedLabel
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }
}