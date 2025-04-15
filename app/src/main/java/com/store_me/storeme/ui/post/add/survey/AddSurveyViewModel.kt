package com.store_me.storeme.ui.post.add.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.Question
import com.store_me.storeme.data.SurveyData
import com.store_me.storeme.data.TimeData
import com.store_me.storeme.data.request.store.CreateSurveyPostRequest
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
class AddSurveyViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _questions = MutableStateFlow(listOf(Question(title = "", options = null)))
    val questions: StateFlow<List<Question>> = _questions

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

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun addQuestion() {
        if(questions.value.size >= 20) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("질문은 최대 20개까지 설정할 수 있습니다.")
            }

            return
        }

        _questions.value += Question(title = "", options = null)
    }

    fun deleteQuestion(index: Int) {
        if (index !in _questions.value.indices) return

        if(questions.value.size <= 1) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("질문은 최소 1개 이상이어야 합니다.")
            }

            return
        }

        _questions.value = _questions.value.toMutableList().apply {
            removeAt(index)
        }
    }


    fun updateQuestionTitle(index: Int, title: String) {
        _questions.value = _questions.value.toMutableList().apply {
            if (index in indices) {
                val updated = this[index].copy(title = title)
                this[index] = updated
            }
        }
    }

    fun updateQuestionType(index: Int, changeToSubjective: Boolean) {
        _questions.value = _questions.value.toMutableList().apply {
            if (index in indices) {
                val updated = this[index].copy(options = if(changeToSubjective) null else listOf("", ""))
                this[index] = updated
            }
        }
    }

    fun addQuestionOption(index: Int) {
        if((_questions.value[index].options?.size ?: 0) >= 10) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("항목은 최대 10개까지 설정할 수 있습니다.")
            }

            return
        }

        _questions.value = _questions.value.toMutableList().apply {
            if (index in indices) {
                val updated = this[index].copy(options = this[index].options?.plus(listOf("")))
                this[index] = updated
            }
        }
    }

    fun deleteQuestionOption(questionIndex: Int, optionIndex: Int) {
        val currentOptions = _questions.value.getOrNull(questionIndex)?.options ?: return

        if (currentOptions.size <= 2) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("항목은 최소 2개 이상 설정해야 합니다.")
            }
            return
        }

        val updatedOptions = currentOptions.toMutableList().apply {
            removeAt(optionIndex)
        }

        _questions.value = _questions.value.toMutableList().apply {
            this[questionIndex] = this[questionIndex].copy(options = updatedOptions)
        }
    }

    fun updateQuestionOption(questionIndex: Int, optionIndex: Int, option: String) {
        val currentOptions = _questions.value.getOrNull(questionIndex)?.options ?: return

        if (optionIndex !in currentOptions.indices) return

        val updatedOptions = currentOptions.toMutableList().apply {
            this[optionIndex] = option
        }

        _questions.value = _questions.value.toMutableList().apply {
            this[questionIndex] = this[questionIndex].copy(options = updatedOptions)
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

    fun createSurveyPost() {
        viewModelScope.launch {
            if(title.value.isEmpty()) {
                ErrorEventBus.errorFlow.emit("제목을 입력해주세요.")
                return@launch
            }

            questions.value.forEach {
                if(it.title.isEmpty()) {
                    ErrorEventBus.errorFlow.emit("질문을 모두 작성해주세요.")
                    return@launch
                } else if(it.options != null) {
                    if(it.options.size < 2) {
                        ErrorEventBus.errorFlow.emit("항목은 최소 2개 이상이어야 합니다.")
                        return@launch
                    } else {
                        it.options.forEach { option ->
                            if (option.isEmpty()) {
                                ErrorEventBus.errorFlow.emit("객관식 질문 항목을 모두 작성해주세요.")
                                return@launch
                            }
                        }
                    }
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

            if(questions.value.size <= 1) {
                ErrorEventBus.errorFlow.emit("질문은 최소 1개 이상이어야 합니다.")
                return@launch
            }

            val startDateTime = DateTimeUtils.toIsoDateTimeString(startLocalDate.value!!, startTime.value, second = 0)
            val endDateTime = DateTimeUtils.toIsoDateTimeString(endLocalDate.value!!, endTime.value, second = 59)

            if(startDateTime >= endDateTime) {
                ErrorEventBus.errorFlow.emit("시작 날짜가 종료 날짜보다 이전이어야 합니다.")
                return@launch
            }

            val response = postRepository.createSurveyPost(createSurveyPostRequest = CreateSurveyPostRequest(
                title = title.value,
                description = description.value,
                questions = questions.value,
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