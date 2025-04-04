package com.store_me.storeme.ui.store_setting.story

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.StoryData
import com.store_me.storeme.repository.storeme.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StorySettingViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _uri = MutableStateFlow<Uri?>(null)
    val uri: StateFlow<Uri?> = _uri

    private val _stories = MutableStateFlow<List<StoryData>>(emptyList())
    val stories: StateFlow<List<StoryData>> = _stories

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    fun updateUri(uri: Uri?) {
        _uri.value = uri
    }

    fun updateStories(stories: List<StoryData>) {
        _stories.value = stories
    }

    fun updateDescription(description: String) {
        _description.value = description
    }
}