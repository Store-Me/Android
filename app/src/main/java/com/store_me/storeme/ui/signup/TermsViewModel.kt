package com.store_me.storeme.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class RequiredTerms {
    USE, PRIVACY
}

enum class OptionalTerms {
    MARKETING
}

class TermsViewModel: ViewModel() {
    //필수 약관 동의 상태
    private val _requiredTermsState = MutableStateFlow(
        RequiredTerms.values().associateWith { false }
    )
    val requiredTermsState: StateFlow<Map<RequiredTerms, Boolean>> = _requiredTermsState

    //선택 약관 동의 상태
    private val _optionalTermsState = MutableStateFlow(
        OptionalTerms.values().associateWith { false }
    )
    val optionalTermsState: StateFlow<Map<OptionalTerms, Boolean>> = _optionalTermsState

    //모든 약관 동의 상태
    private val _isAllTermsAgreed = MutableStateFlow(false)
    val isAllTermsAgreed: StateFlow<Boolean> = _isAllTermsAgreed

    init {
        viewModelScope.launch {
            combine(
                _requiredTermsState,
                _optionalTermsState
            ) { requiredTerms, optionalTerms ->
                requiredTerms.all { it.value } && optionalTerms.all { it.value }
            }.collect { allTermsAgreed ->
                _isAllTermsAgreed.value = allTermsAgreed
            }
        }
    }

    /**
     * 모든 약관 동의 여부 변경
     */
    fun updateAllTerms(isAgreed: Boolean) {
        _requiredTermsState.value = _requiredTermsState.value.mapValues { isAgreed }
        _optionalTermsState.value = _optionalTermsState.value.mapValues { isAgreed }
    }

    /**
     * 필수 약관 동의 변경
     * @param term RequiredTerms
     */
    fun updateRequiredTerms(term: RequiredTerms) {
        _requiredTermsState.value = _requiredTermsState.value.toMutableMap().apply {
            this[term] = !(this[term] ?: false)
        }
    }

    /**
     * 선택 약관 동의 변경
     * @param term OptionalTerms
     */
    fun updateOptionalTerms(term: OptionalTerms) {
        _optionalTermsState.value = _optionalTermsState.value.toMutableMap().apply {
            this[term] = !(this[term] ?: false)
        }
    }

    /**
     * 모든 필수 약관 동의 여부
     * @return Boolean
     */
    fun isAllRequiredTermsAgreed(): Boolean {
        return _requiredTermsState.value.all { it.value }
    }

    /**
     * 모든 선택 약관 동의 여부
     * @return Boolean
     */
    fun isOptionalTermsAgreed(): Boolean {
        return _optionalTermsState.value.all { it.value }
    }
}