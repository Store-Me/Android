package com.store_me.storeme.ui.post.add

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.enums.TextStyleOptions
import com.store_me.storeme.data.enums.ToolbarItems
import com.store_me.storeme.utils.preference.SettingPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ToolbarViewModel @Inject constructor(
    private val settingPreferencesHelper: SettingPreferencesHelper,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _keyboardHeight = MutableStateFlow(0)
    val keyboardHeight: StateFlow<Int> = _keyboardHeight

    //Toolbar Items
    private val _selectedToolbarItem = MutableStateFlow<ToolbarItems?>(null)
    val selectedToolbarItem: StateFlow<ToolbarItems?> = _selectedToolbarItem

    private val _selectedTextStyleItem = MutableStateFlow<TextStyleOptions?>(null)
    val selectedTextStyleItem: StateFlow<TextStyleOptions?> = _selectedTextStyleItem

    //이미지
    private val _galleryImages = MutableStateFlow<List<Uri>>(emptyList())
    val galleryImages: StateFlow<List<Uri>> = _galleryImages

    init {
        viewModelScope.launch {
            settingPreferencesHelper.getKeyboardHeight()
                .collect { height ->
                    _keyboardHeight.value = height
                }
        }
    }

    fun updateSelectedToolbarItem(item: ToolbarItems?) {
        _selectedToolbarItem.value = item
    }

    fun updateSelectedTextStyleItem(item: TextStyleOptions?) {
        _selectedTextStyleItem.value = item
    }

    fun fetchGalleryImages() {
        viewModelScope.launch {
            val galleryImages = loadGalleryImages()
            Timber.d("galleryImages: ${galleryImages.size}")
            _galleryImages.value = galleryImages
        }
    }

    private suspend fun loadGalleryImages(): List<Uri> = withContext(Dispatchers.IO) {
        val uris = mutableListOf<Uri>()

        try {
            val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Images.Media._ID)
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = Uri.withAppendedPath(collection, id.toString())
                    uris.add(contentUri)
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        uris
    }
}