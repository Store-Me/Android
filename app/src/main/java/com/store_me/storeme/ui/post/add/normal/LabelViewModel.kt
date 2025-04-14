package com.store_me.storeme.ui.post.add.normal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.LabelData
import com.store_me.storeme.repository.storeme.PostRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    private val _labels = MutableStateFlow<List<LabelData>>(emptyList())
    val labels: StateFlow<List<LabelData>> = _labels

    private val _selectedLabel = MutableStateFlow<LabelData?>(null)
    val selectedLabel: StateFlow<LabelData?> = _selectedLabel

    fun updateLabels(labels: List<LabelData>) {
        _labels.value = labels
    }

    fun updateSelectedLabel(label: LabelData?) {
        _selectedLabel.value = label
    }

    fun getLabels() {
        viewModelScope.launch {
            val response = postRepository.getLabels()

            response.onSuccess {
                val sortedLabels = it.sortedBy { label -> label.order }

                updateLabels(sortedLabels)
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