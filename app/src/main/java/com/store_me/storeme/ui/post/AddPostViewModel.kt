package com.store_me.storeme.ui.post

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddPostViewModel: ViewModel() {

    enum class LayoutItem(val displayName: String) {
        TEXT("텍스트"), IMAGE("이미지"),
    }

    enum class TextStyleItem(val displayName: String){
        BOLD("굵게"), ITALIC("기울기"), SIZE("크기"), COLOR("색깔")
    }

    sealed class PostContent {
        data class TextContent(val text: String): PostContent()
        data class ImageContent(val imageUrl: String): PostContent()
    }

    private val _currentEditState = MutableStateFlow<LayoutItem?>(null)
    val currentEditState: StateFlow<LayoutItem?> = _currentEditState

    fun setCurrentEditState(type: LayoutItem) {
        _currentEditState.value = type
    }

    fun clearCurrentEditState() {
        _currentEditState.value = null
    }

}