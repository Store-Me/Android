package com.store_me.storeme.ui.store_setting.story.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.StoryData
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorySettingViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository
): ViewModel() {
    private val _stories = MutableStateFlow<List<StoryData>>(emptyList())
    val stories: StateFlow<List<StoryData>> = _stories

    private val _lastCreatedAt = MutableStateFlow<String?>(null)
    val lastCreatedAt: StateFlow<String?> = _lastCreatedAt

    private val _hasNextPage = MutableStateFlow(true)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage

    fun updateStories(stories: List<StoryData>) {
        _stories.value = stories
    }

    fun addStories(stories: List<StoryData>) {
        _stories.value += stories
    }

    fun updateLastCreatedAt(lastCreatedAt: String?) {
        _lastCreatedAt.value = lastCreatedAt
    }

    fun updateHasNextPage(hasNextPage: Boolean) {
        _hasNextPage.value = hasNextPage
    }

    private fun removeStory(storyId: String) {
        _stories.value = _stories.value.filterNot { it.id == storyId }
    }

    fun getStoreStories(lastCreatedAt: String?) {
        if(!hasNextPage.value) return

        viewModelScope.launch {
            val response = ownerRepository.getStoreStories(lastCreatedAt = lastCreatedAt)

            response.onSuccess {
                addStories(it.result)
                updateLastCreatedAt(it.pagination.lastCreatedAt)
                updateHasNextPage(it.pagination.hasNextPage)
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun deleteStoreStories(storyId: String) {

        viewModelScope.launch {
            val response = ownerRepository.deleteStoreStory(storyId)

            response.onSuccess {
                removeStory(storyId)
                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }
}