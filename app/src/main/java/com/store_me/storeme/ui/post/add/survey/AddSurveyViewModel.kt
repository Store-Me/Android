package com.store_me.storeme.ui.post.add.survey

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.SurveyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddSurveyViewModel @Inject constructor(
): ViewModel() {
    private val _surveys = MutableStateFlow<List<SurveyData>>(emptyList())
    val surveys: StateFlow<List<SurveyData>> = _surveys

    fun addSurvey(survey: SurveyData) {
        _surveys.value += survey
    }

    fun deleteSurvey(index: Int) {
        _surveys.value = _surveys.value.toMutableList().apply {
            if (index in indices) removeAt(index)
        }
    }

    fun updateSurveys(surveys: List<SurveyData>) {
        _surveys.value = surveys
    }

    fun updateSurvey(index: Int, survey: SurveyData) {
        _surveys.value = _surveys.value.toMutableList().apply {
            if (index in indices) set(index, survey)
        }
    }

    fun updateSurveyTitle(index: Int, title: String) {
        _surveys.value = _surveys.value.toMutableList().apply {
            if (index in indices) set(index, get(index).copy(title = title))
        }
    }

    fun clearSurveyItems(index: Int) {
        _surveys.value = _surveys.value.toMutableList().apply {
            if (index in indices) set(index, get(index).copy(items = emptyList()))
        }
    }

    fun addSurveyItem(index: Int) {
        _surveys.value = _surveys.value.toMutableList().apply {
            if (index in indices) set(index, get(index).copy(items = get(index).items + ""))
        }
    }

    fun updateSurveyItem(index: Int, itemIndex: Int, item: String) {
        _surveys.value = _surveys.value.toMutableList().apply {
            if (index in indices) set(index, get(index).copy(items = get(index).items.toMutableList().apply {
                if (itemIndex in indices) set(itemIndex, item)
            }))
        }
    }
}