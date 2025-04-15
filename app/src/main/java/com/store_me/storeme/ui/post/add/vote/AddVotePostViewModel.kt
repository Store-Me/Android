package com.store_me.storeme.ui.post.add.vote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.TimeData
import com.store_me.storeme.data.request.store.CreateVotePostRequest
import com.store_me.storeme.repository.storeme.PostRepository
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddVotePostViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _options = MutableStateFlow(listOf("", ""))
    val options: StateFlow<List<String>> = _options

    private val _startLocalDate = MutableStateFlow<LocalDate?>(null)
    val startLocalDate: StateFlow<LocalDate?> = _startLocalDate

    private val _startTime = MutableStateFlow<TimeData>(TimeData(9, 0))
    val startTime: StateFlow<TimeData> = _startTime

    private val _endLocalDate = MutableStateFlow<LocalDate?>(null)
    val endLocalDate: StateFlow<LocalDate?> = _endLocalDate

    private val _endTime = MutableStateFlow<TimeData>(TimeData(9, 0))
    val endTime: StateFlow<TimeData> = _endTime

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun updateTitle(title: String) {
        _title.value = title
    }

    fun addOption() {
        if(options.value.size >= 5) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("항목은 최대 5개까지 추가할 수 있습니다.")
            }

            return
        }

        _options.value += ""
    }

    fun deleteOption(index: Int) {
        if(options.value.size <= 2) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("항목은 최소 2개 이상이어야 합니다.")
            }

            return
        }

        val current = _options.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _options.value = current
        }
    }

    fun updateOption(index: Int, value: String) {
        val current = _options.value.toMutableList()
        if (index in current.indices) {
            current[index] = value
            _options.value = current
        }
    }

    fun updateStartDateTime(localDate: LocalDate?, timeData: TimeData) {
        _startLocalDate.value = localDate
        _startTime.value = timeData
    }

    fun updateEndDateTime(localDate: LocalDate?, timeData: TimeData) {
        _endLocalDate.value = localDate
        _endTime.value = timeData
    }

    fun createVotePost() {
        viewModelScope.launch {
            if(title.value.isEmpty()) {
                ErrorEventBus.errorFlow.emit("제목을 입력해주세요.")
                return@launch
            }

            if(options.value.size < 2) {
                ErrorEventBus.errorFlow.emit("항목은 최소 2개 이상이어야 합니다.")
                return@launch
            }

            options.value.forEach {
                if (it.isEmpty()) {
                    ErrorEventBus.errorFlow.emit("빈 항목을 모두 작성해주세요.")
                    return@launch
                }
            }

            if(startLocalDate.value == null) {
                ErrorEventBus.errorFlow.emit("시작 날짜를 선택해주세요.")
                return@launch
            }

            if(endLocalDate.value == null) {
                ErrorEventBus.errorFlow.emit("종료 날짜를 선택해주세요.")
                return@launch
            }

            val startDateTime = DateTimeUtils.toIsoDateTimeString(startLocalDate.value!!, startTime.value, second = 0)
            val endDateTime = DateTimeUtils.toIsoDateTimeString(endLocalDate.value!!, endTime.value, second = 59)

            if(startDateTime >= endDateTime) {
                ErrorEventBus.errorFlow.emit("시작 날짜가 종료 날짜보다 이전이어야 합니다.")
                return@launch
            }

            val response = postRepository.createVotePost(createVotePostRequest = CreateVotePostRequest(
                title = title.value,
                options = options.value,
                startDateTime = startDateTime,
                endDateTime = endDateTime
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
}