package com.store_me.storeme.ui.mystore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.store_me.storeme.utils.CategoryUtils
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.StoreCategory

class MyStoreViewModel: ViewModel() {
    //Category 관련
    val categoryList = CategoryUtils.getStoreCategories()

    private val _selectedCategory = MutableLiveData<StoreCategory>()
    val selectedCategory: LiveData<StoreCategory> = _selectedCategory

    init {
        selectCategory(StoreCategory.ALL)
    }

    fun selectCategory(category: StoreCategory) {
        _selectedCategory.value = category
    }

    //Post 관련
    val postList = SampleDataUtils.samplePost()
}