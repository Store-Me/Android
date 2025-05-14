package com.store_me.storeme.ui.store_setting.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.store.post.LabelData
import com.store_me.storeme.data.store.post.NormalPostData
import com.store_me.storeme.repository.storeme.PostRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
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

    private val _selectedNormalPost = MutableStateFlow<NormalPostData?>(null)
    val selectedNormalPost: StateFlow<NormalPostData?> = _selectedNormalPost

    fun updateSelectedNormalPost(normalPost: NormalPostData?) {
        _selectedNormalPost.value = normalPost
    }

    private fun updateNormalPost(
        label: LabelData?,
        normalPosts: List<NormalPostData>
    ) {
        _normalPostByLabel.value = _normalPostByLabel.value.toMutableMap().apply {
            this[label] = normalPosts
        }
    }

    private fun updateNormalPost(
        normalPost: NormalPostData
    ) {
        _normalPostByLabel.value = _normalPostByLabel.value.mapValues { (_, postList) ->
            postList.map { post ->
                if (post.id == normalPost.id) normalPost else post
            }
        }
    }

    private fun deleteNormalPost(postId: String) {
        _normalPostByLabel.value = _normalPostByLabel.value.mapValues { (_, postList) ->
            postList.filterNot { it.id == postId }
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

    fun likeNormalPost(normalPost: NormalPostData) {
        viewModelScope.launch {
            val response = if(normalPost.userLiked)
                postRepository.deleteNormalPostLike(normalPost.id)
            else
                postRepository.postNormalPostLike(normalPost.id)

            response.onSuccess {
                updateNormalPost(it.result)
            }.onFailure {

            }
        }
    }

    fun postNormalPostViews(normalPost: NormalPostData) {
        updateNormalPost(normalPost.copy(views = normalPost.views + 1))

        viewModelScope.launch {
            val response = postRepository.postPostViews(normalPost.id)
        }
    }

    fun deletePost(postId: String){
        viewModelScope.launch {
            val response = postRepository.deletePost(postId)

            response.onSuccess {
                deleteNormalPost(postId)
                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }
}