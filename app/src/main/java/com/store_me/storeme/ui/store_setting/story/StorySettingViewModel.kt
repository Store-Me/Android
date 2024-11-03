package com.store_me.storeme.ui.store_setting.story

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StorySettingViewModel: ViewModel() {
    private val _videoUris = MutableStateFlow((emptyList<Uri>()))
    val videoUris: StateFlow<List<Uri>> = _videoUris

    fun addVideo(videoUri: Uri) {
        val videoUris = _videoUris.value.toMutableList().apply { add(videoUri) }
        _videoUris.value = videoUris
    }
}