package com.store_me.storeme.ui.mystore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.store_me.storeme.ui.main.ALL
import com.store_me.storeme.utils.CategoryUtils
import com.store_me.storeme.utils.SampleDataUtils

class MyStoreViewModel: ViewModel() {
    //Category 관련
    val categoryList = CategoryUtils.getCategories()

    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String> = _selectedCategory

    init {
        selectCategory(ALL)
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    //Post 관련
    val postList = SampleDataUtils.samplePost()


}