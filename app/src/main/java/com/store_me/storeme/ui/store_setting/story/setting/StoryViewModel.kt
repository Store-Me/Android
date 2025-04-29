package com.store_me.storeme.ui.store_setting.story.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.store_me.storeme.data.store.story.StoryData
import com.store_me.storeme.repository.storeme.StoryRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository
): ViewModel() {
    private val _stories = MutableStateFlow<List<StoryData>>(emptyList())
    val stories: StateFlow<List<StoryData>> = _stories

    private val _lastCreatedAt = MutableStateFlow<Timestamp?>(null)
    val lastCreatedAt: StateFlow<Timestamp?> = _lastCreatedAt

    private val _hasNextPage = MutableStateFlow(true)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage

    fun updateStories(stories: List<StoryData>) {
        _stories.value = stories
    }

    private fun updateStory(newStoryData: StoryData) {
        _stories.update { storyList ->
            storyList.map { story ->
                if (story.id == newStoryData.id) {
                    newStoryData
                } else {
                    story
                }
            }
        }
    }

    private fun addStories(stories: List<StoryData>) {
        _stories.value += stories
    }

    fun updateLastCreatedAt(lastCreatedAt: Timestamp?) {
        _lastCreatedAt.value = lastCreatedAt
    }

    fun updateHasNextPage(hasNextPage: Boolean) {
        _hasNextPage.value = hasNextPage
    }

    private fun removeStory(storyId: String) {
        _stories.value = _stories.value.filterNot { it.id == storyId }
    }

    fun getStoreStories() {
        if(!hasNextPage.value) return

        viewModelScope.launch {
            val response = storyRepository.getStoreStories(lastCreatedAt = lastCreatedAt.value)

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
            val response = storyRepository.deleteStoreStory(storyId)

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

    fun updateStoryLike(storyData: StoryData) {
        if(storyData.userLiked) {
            deleteStoryLike(storyId = storyData.id)
        } else {
            postStoryLike(storyId = storyData.id)
        }
    }

    private fun postStoryLike(storyId: String) {
        viewModelScope.launch {
            val response = storyRepository.postStoryLike(storyId)

            response.onSuccess {
                updateStory(it.result)
            }.onFailure {
                Timber.d(it)
            }
        }
    }

    private fun deleteStoryLike(storyId: String) {
        viewModelScope.launch {
            val response = storyRepository.deleteStoryLike(storyId)

            response.onSuccess {
                updateStory(it.result)
            }.onFailure {
                Timber.d(it)
            }
        }
    }
}