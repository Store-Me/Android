package com.store_me.storeme.ui.post

import android.util.Log
import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.enums.PostType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SelectPostTypeViewModel: ViewModel() {
    private val _postType = MutableStateFlow<PostType?>(null)
    val postType: StateFlow<PostType?> = _postType

    fun updatePostType(newPostType: PostType?) {
        _postType.value = newPostType
    }







    var fontSize = 14

    enum class LayoutItem(val displayName: String) {
        TEXT("텍스트"), IMAGE("이미지"), DIVIDER("구분선")
    }

    enum class TextStyleItem(val displayName: String) {
        BOLD("굵게"), ITALIC("기울기"), SIZE_UP("크게"), SIZE_DOWN("작게"), COLOR("색깔"),
    }

    enum class TextColor(val displayName: String) {
        BLACK("검정")
    }

    sealed class BlockContent {
        data class TextContent(val text: String, val hasMultipleLine: Boolean, val style: Set<TextStyleItem>): BlockContent()
        data class ImageContent(val imageUrl: String): BlockContent()
        data object DividerContent : BlockContent()
    }

    private val _previewVisible = MutableStateFlow(false)
    val previewVisible: StateFlow<Boolean> = _previewVisible

    fun togglePreview() {
        _previewVisible.value = !_previewVisible.value
    }

    private val _currentEditState = MutableStateFlow<LayoutItem?>(null)
    val currentEditState: StateFlow<LayoutItem?> = _currentEditState

    fun setCurrentEditState(type: LayoutItem) {
        _currentEditState.value = type
    }

    private fun clearCurrentEditState() {
        _currentEditState.value = null
    }

    private val _selectedTextStyleStates = MutableStateFlow<Set<TextStyleItem>>(setOf())
    val selectedTextStyleItems: StateFlow<Set<TextStyleItem>> = _selectedTextStyleStates

    fun setTextStyle(textStyleItem: TextStyleItem) {
        _selectedTextStyleStates.value = _selectedTextStyleStates.value.toMutableSet().apply {
            if(!add(textStyleItem)) {
                remove(textStyleItem)
            }
        }
    }

    private fun clearTextStyle() {
        _selectedTextStyleStates.value = setOf()
    }

    private val _totalContent = MutableStateFlow("")
    val totalContent: StateFlow<String> = _totalContent

    fun addContent(text: String = "") {
        when(currentEditState.value) {
            LayoutItem.TEXT -> {
                _totalContent.value += currentText(text)
                addTextBlock(text)
                clearCurrentEditState()
                clearTextStyle()
            }
            LayoutItem.IMAGE -> {

            }
            LayoutItem.DIVIDER -> {
                _totalContent.value += currentText("\n *** \n")
                addDividerBlock()
                clearCurrentEditState()
            }
            null -> {

            }
        }
    }

    fun currentText(text: String): String {
        val selectedStyles = _selectedTextStyleStates.value
        var formattedText = text

        if (TextStyleItem.BOLD in selectedStyles) {
            formattedText = "<strong>$formattedText</strong>"
        }
        if (TextStyleItem.ITALIC in selectedStyles) {
            formattedText = "<em>$formattedText</em>"
        }
        if (TextStyleItem.SIZE_UP in selectedStyles) {
            formattedText = "<span style=\"font-size: 1.5em;\">$formattedText</span>" // 텍스트 크기 증가
        }
        if (TextStyleItem.SIZE_DOWN in selectedStyles) {
            formattedText = "<span style=\"font-size: 0.8em;\">$formattedText</span>" // 텍스트 크기 감소
        }
        if (TextStyleItem.COLOR in selectedStyles) {
            formattedText = "<span style=\"color: green;\">$formattedText</span>" // 텍스트 색상 변경
        }

        Log.d("formattedText", formattedText)

        return formattedText
    }

    private val _blockContents = MutableStateFlow<List<BlockContent>>(emptyList())
    val blockContents: StateFlow<List<BlockContent>> = _blockContents

    private fun addTextBlock(text: String) {
        val updatedList = _blockContents.value.toMutableList()
        updatedList.add(BlockContent.TextContent(text, hasMultipleLine(text), selectedTextStyleItems.value))
        _blockContents.value = updatedList
    }

    private fun addDividerBlock() {
        val updatedList = _blockContents.value.toMutableList()
        updatedList.add(BlockContent.DividerContent)
        _blockContents.value = updatedList
    }


    private fun hasMultipleLine(text: String): Boolean {
        return text.contains("\n")
    }
}