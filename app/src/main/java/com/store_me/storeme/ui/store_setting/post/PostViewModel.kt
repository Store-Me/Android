package com.store_me.storeme.ui.store_setting.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.store.post.LabelData
import com.store_me.storeme.data.store.post.NormalPostData
import com.store_me.storeme.repository.storeme.PostRepository
import com.store_me.storeme.utils.ErrorEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    private val _normalPostByLabel = MutableStateFlow<Map<LabelData?, List<NormalPostData>>>(mapOf())
    val normalPostByLabel: StateFlow<Map<LabelData?, List<NormalPostData>>> = _normalPostByLabel

    private fun updateNormalPost(
        label: LabelData?,
        normalPosts: List<NormalPostData>
    ) {
        _normalPostByLabel.value = _normalPostByLabel.value.toMutableMap().apply {
            this[label] = normalPosts
        }
    }

    fun getNormalPost(label: LabelData?) {
        viewModelScope.launch {
            val response = postRepository.getNormalPostByLabelId(
                labelId = label?.labelId
            )

            response.onSuccess {
                updateNormalPost(label = label, normalPosts = it.posts)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }
}