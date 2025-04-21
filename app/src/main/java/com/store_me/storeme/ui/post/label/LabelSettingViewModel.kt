package com.store_me.storeme.ui.post.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.LabelData
import com.store_me.storeme.data.request.store.PatchLabelData
import com.store_me.storeme.data.request.store.PatchLabelRequest
import com.store_me.storeme.repository.storeme.PostRepository
import com.store_me.storeme.utils.DEFAULT_LABEL_ID
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelSettingViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    private val _labels = MutableStateFlow<List<LabelData>>(emptyList())
    val labels: StateFlow<List<LabelData>> = _labels

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun updateLabels(labels: List<LabelData>) {
        _labels.value = labels
    }

    fun reorderLabels(fromIndex: Int, toIndex: Int) {
        val currentLabels = _labels.value.toMutableList()
        val movedItem = currentLabels.removeAt(fromIndex)
        currentLabels.add(toIndex, movedItem)
        _labels.value = currentLabels.toList()
    }

    fun addLabel(labelName: String) {
        val currentLabels = _labels.value.toMutableList()
        currentLabels.add(LabelData(labelId = "", name = labelName, order = currentLabels.size, postCount = 0))
        _labels.value = currentLabels.toList()
    }

    fun deleteLabel(label: LabelData) {
        val currentLabels = _labels.value.toMutableList()
        currentLabels.remove(label)
        _labels.value = currentLabels.toList()
    }

    fun editLabel(label: LabelData, newLabelName: String) {
        val currentLabels = _labels.value.toMutableList()

        val index = currentLabels.indexOfFirst { it == label }
        if (index != -1) {
            val updatedLabels = currentLabels[index].copy(name = newLabelName)
            currentLabels[index] = updatedLabels
            _labels.value = currentLabels.toList()
        }
    }

    private fun syncLabelsOrder() {
        val currentLabels = _labels.value.mapIndexed { index, label ->
            label.copy(order = index)
        }
        _labels.value = currentLabels
    }

    fun patchLabels() {
        syncLabelsOrder()

        val patchLabelsRequest = _labels.value.map { label ->
            PatchLabelData(
                labelId = label.labelId.ifEmpty { null },
                name = label.name,
                order = label.order
            )
        }



        viewModelScope.launch {
            if(_labels.value.isEmpty() || _labels.value[0].labelId != DEFAULT_LABEL_ID) {
                ErrorEventBus.errorFlow.emit("기본 라벨은 수정이 불가능해요.")
                return@launch
            }

            val response = postRepository.patchLabel(
                patchLabelRequest = PatchLabelRequest(
                    labels = patchLabelsRequest
                )
            )

            response.onSuccess {
                _isSuccess.value = true
                updateLabels(it.result)
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