package com.store_me.storeme.ui.mystore

import android.util.Log
import androidx.lifecycle.ViewModel
import com.store_me.storeme.utils.CategoryUtils
import com.store_me.storeme.utils.StoreCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(

): ViewModel() {

    //Category 관련
    val categoryList = CategoryUtils.getStoreCategories()

    private val _selectedCategory = MutableStateFlow(StoreCategory.ALL)
    val selectedCategory: StateFlow<StoreCategory> = _selectedCategory

    init {
        selectCategory(StoreCategory.ALL)
    }

    fun selectCategory(category: StoreCategory) {
        Log.d("CategoryViewModel", "현재 카테고리는 ${category.displayName}")
        _selectedCategory.update { category }
    }
}