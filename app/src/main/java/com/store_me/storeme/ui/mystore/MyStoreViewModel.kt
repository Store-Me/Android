package com.store_me.storeme.ui.mystore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.store_me.storeme.utils.CategoryUtils
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.StoreCategory

class MyStoreViewModel: ViewModel() {


    //Post 관련
    val postList = SampleDataUtils.samplePost()
}