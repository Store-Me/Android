package com.store_me.storeme.ui.post.add

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.enums.TextColors
import com.store_me.storeme.data.enums.TextSizeOptions
import com.store_me.storeme.data.enums.TextStyleOptions
import com.store_me.storeme.data.enums.ToolbarItems
import com.store_me.storeme.utils.preference.SettingPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val settingPreferencesHelper: SettingPreferencesHelper
): ViewModel() {
    private val _keyboardHeight = MutableStateFlow(0)
    val keyboardHeight: StateFlow<Int> = _keyboardHeight

    private val _selectedLabel = MutableStateFlow("기본 라벨")
    val selectedLabel: StateFlow<String> = _selectedLabel

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    //Toolbar Items
    private val _selectedToolbarItem = MutableStateFlow<ToolbarItems?>(null)
    val selectedToolbarItem: StateFlow<ToolbarItems?> = _selectedToolbarItem

    private val _textAlign = MutableStateFlow(TextAlign.Left)
    val textAlign: StateFlow<TextAlign> = _textAlign

    private val _selectedTextStyleItem = MutableStateFlow<TextStyleOptions?>(null)
    val selectedTextStyleItem: StateFlow<TextStyleOptions?> = _selectedTextStyleItem

    private val _textSize = MutableStateFlow(TextSizeOptions.REGULAR)
    val textSize: StateFlow<TextSizeOptions> = _textSize

    private val _textIsBold = MutableStateFlow(false)
    val textIsBold: StateFlow<Boolean> = _textIsBold

    private val _textIsItalic = MutableStateFlow(false)
    val textIsItalic: StateFlow<Boolean> = _textIsItalic

    private val _textHasUnderLine = MutableStateFlow(false)
    val textHasUnderLine: StateFlow<Boolean> = _textHasUnderLine

    private val _textColor = MutableStateFlow(TextColors.Black)
    val textColor: StateFlow<TextColors> = _textColor

    init {
        viewModelScope.launch {
            settingPreferencesHelper.getKeyboardHeight()
                .collect { height ->
                    _keyboardHeight.value = height
                }
        }
    }

    fun updateSelectedLabel(newSelectedLabel: String) {
        _selectedLabel.value = newSelectedLabel
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateSelectedToolbarItem(item: ToolbarItems?) {
        _selectedToolbarItem.value = item
    }

    fun updateTextAlign(textAlign: TextAlign) {
        _textAlign.value = textAlign
    }

    fun updateSelectedTextStyleItem(item: TextStyleOptions?) {
        _selectedTextStyleItem.value = item

        when(item) {
            TextStyleOptions.WEIGHT -> {
                _textIsBold.value = !textIsBold.value
            }
            TextStyleOptions.ITALICS -> {
                _textIsItalic.value = !textIsItalic.value
            }
            TextStyleOptions.UNDERLINE -> {
                _textHasUnderLine.value = !textHasUnderLine.value
            }
            else -> {

            }
        }
    }

    fun updateTextSize(textSize: TextSizeOptions) {
        _textSize.value = textSize
        updateSelectedTextStyleItem(null)
    }

    fun updateTextBold(textIsBold: Boolean) {
        _textIsBold.value = textIsBold
    }

    fun updateTextItalic(textIsItalic: Boolean) {
        _textIsItalic.value = textIsItalic
    }

    fun updateTextUnderLine(textHasUnderLine: Boolean) {
        _textHasUnderLine.value = textHasUnderLine
    }

    fun updateTextColor(textColor: TextColors) {
        _textColor.value = textColor
        updateSelectedTextStyleItem(null)
    }

    fun updateTextColor(color: Color) {
        Timber.d("color: ${color}")
        val textColor = TextColors.entries.filter { it.color == color }

        if(textColor.isNotEmpty())
            _textColor.value = textColor.first()
    }
}