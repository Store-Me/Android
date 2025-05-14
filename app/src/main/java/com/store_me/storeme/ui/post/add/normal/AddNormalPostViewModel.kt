package com.store_me.storeme.ui.post.add.normal

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedrejeb.richeditor.model.RichTextState
import com.store_me.storeme.data.PostContentBlock
import com.store_me.storeme.data.PostContentType
import com.store_me.storeme.data.enums.post.PostType
import com.store_me.storeme.data.request.store.ContentData
import com.store_me.storeme.data.request.store.CreatePostRequest
import com.store_me.storeme.data.store.post.NormalPostData
import com.store_me.storeme.repository.storeme.ImageRepository
import com.store_me.storeme.repository.storeme.PostRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.StoragePaths
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNormalPostViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val postRepository: PostRepository
): ViewModel() {
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _content = MutableStateFlow<List<PostContentBlock>>(listOf(PostContentBlock.TextBlock(state = RichTextState())))
    val content: StateFlow<List<PostContentBlock>> = _content

    private val _uploadingCount = MutableStateFlow(0)
    val uploadingCount: StateFlow<Int> = _uploadingCount

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess


    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun removeBlockAt(index: Int, focusedIndex: Int): Int {
        val newList = _content.value.toMutableList()
        if (index in newList.indices) {
            newList.removeAt(index)
            _content.value = newList
        }

        return if(focusedIndex > index)
            focusedIndex - 1
        else
            focusedIndex
    }

    fun insertImage(uri: Uri, focusedIndex: Int): Pair<Int, Int> {
        val list = _content.value.toMutableList()
        val current = list.getOrNull(focusedIndex)


        // 빈 TextBlock 경우
        if (current is PostContentBlock.TextBlock && current.state.annotatedString.text.isBlank()) {
            list.removeAt(focusedIndex)
            list.add(focusedIndex, PostContentBlock.ImageBlock(uri, ""))                      // 이미지 추가
            list.add(focusedIndex + 1, PostContentBlock.TextBlock(RichTextState()))       // 새로운 텍스트 추가
            _content.value = list
            return Pair(focusedIndex, focusedIndex + 1)
        }

        //내용 있는 TextBlock 경우
        var insertIndex = focusedIndex + 1

        // 다음 TextBlock
        for (i in insertIndex..list.lastIndex) {
            val next = list[i]
            if (next is PostContentBlock.TextBlock) {
                insertIndex = i
                if (next.state.annotatedString.text.isBlank()) {
                    list.add(insertIndex, PostContentBlock.ImageBlock(uri, ""))
                    _content.value = list
                    return Pair(insertIndex, insertIndex + 1)
                } else {
                    list.add(insertIndex, PostContentBlock.ImageBlock(uri, ""))
                    list.add(insertIndex + 1, PostContentBlock.TextBlock(RichTextState()))
                    _content.value = list
                    return Pair(insertIndex, insertIndex + 1)
                }
            }
        }

        //이후 텍스트 블럭이 없는 경우 (끝에 추가)
        list.add(insertIndex, PostContentBlock.ImageBlock(uri, ""))
        list.add(insertIndex + 1, PostContentBlock.TextBlock(RichTextState()))
        _content.value = list
        return Pair(insertIndex, insertIndex + 1)
    }

    fun reorderBlock(fromIndex: Int, toIndex: Int) {
        val list = _content.value.toMutableList()
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        _content.value = list
    }

    private fun updateImageBlockProgress(uri: Uri, progress: Float) {
        val updatedList = _content.value.map {
            if (it is PostContentBlock.ImageBlock && it.uri == uri) {
                it.copy(progress = progress)
            } else it
        }
        _content.value = updatedList
    }

    private fun updateImageBlockUrl(uri: Uri, url: String) {
        val updatedList = _content.value.map {
            if (it is PostContentBlock.ImageBlock && it.uri == uri) {
                it.copy(url = url, progress = 1f)
            } else it
        }
        _content.value = updatedList
    }


    private fun removeImageBlockByUri(uri: Uri) {
        _content.value = _content.value.filterNot {
            it is PostContentBlock.ImageBlock && it.uri == uri
        }
    }

    fun uploadImage(
        uri: Uri,
    ) {
        _uploadingCount.value += 1

        viewModelScope.launch {

            val response = imageRepository.uploadImage(
                folderName = StoragePaths.STORE_POST_IMAGES,
                uri = uri,
            ) { progress ->
                updateImageBlockProgress(uri = uri, progress = progress)
            }

            response.onSuccess {
                updateImageBlockUrl(uri = uri, url = it)
            }.onFailure {
                removeImageBlockByUri(uri = uri)

                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit("이미지 업로드에 실패하였습니다.")
                }
            }

            _uploadingCount.value -= 1
        }
    }

    fun createPost(postType: PostType, labelId: String?) {
        viewModelScope.launch {
            if(title.value.isBlank()) {
                ErrorEventBus.errorFlow.emit("제목을 입력해주세요.")
                return@launch
            }

            if(labelId == null) {
                ErrorEventBus.errorFlow.emit("라벨을 선택해주세요.")
                return@launch
            }

            if(content.value.size == 1 && postType == PostType.NORMAL) {
                if(content.value[0] is PostContentBlock.TextBlock) {
                    if((content.value[0] as PostContentBlock.TextBlock).state.annotatedString.isBlank()){
                        ErrorEventBus.errorFlow.emit("내용을 입력해주세요.")
                        return@launch
                    }
                } else if (content.value[0] is PostContentBlock.ImageBlock && (content.value[0] as PostContentBlock.ImageBlock).url.isBlank()){
                    ErrorEventBus.errorFlow.emit("이미지 업로드 중입니다. 잠시 후 시도해주세요.")
                    return@launch
                }
            } else if(postType == PostType.NORMAL && uploadingCount.value != 0) {
                ErrorEventBus.errorFlow.emit("이미지 업로드 중입니다. 잠시 후 시도해주세요.")
                return@launch
            }

            val contentList = mutableListOf<ContentData>()

            content.value.forEach {
                when(it) {
                    is PostContentBlock.TextBlock -> {
                        contentList.add(ContentData(type = it.name, content = it.state.toHtml()))
                    }
                    is PostContentBlock.ImageBlock -> {
                        contentList.add(ContentData(type = it.name, content = it.url))
                        }
                    is PostContentBlock.EmojiBlock -> {
                        contentList.add(ContentData(type = it.name, content = it.url))
                    }
                }
            }

            val response = postRepository.createPost(createPostRequest = CreatePostRequest(
                title = title.value,
                labelId = labelId,
                content = contentList,
                type = postType.name
            ))

            response.onSuccess {
                _isSuccess.value = true
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

    fun syncNormalPost(
        normalPost: NormalPostData
    ) {
        updateTitle(normalPost.title)

        normalPost.content.forEach { content ->
            when(content.type) {
                PostContentType.IMAGE.name -> {
                    val imageBlock = PostContentBlock.ImageBlock(uri = null, url = content.content)
                    _content.value = _content.value.plus(imageBlock)
                }
                PostContentType.TEXT.name -> {
                    val textBlock = PostContentBlock.TextBlock(state = RichTextState())
                    textBlock.state.setHtml(content.content)
                    _content.value = _content.value.plus(textBlock)
                }
                PostContentType.EMOJI.name -> {

                }
            }
        }

        if(normalPost.content.last().type != PostContentType.TEXT.name) {
            val textBlock = PostContentBlock.TextBlock(state = RichTextState())
            _content.value = _content.value.plus(textBlock)
        }
    }

}