package com.store_me.storeme.ui.mystore

import androidx.lifecycle.ViewModel
import com.store_me.storeme.utils.SampleDataUtils

class MyStoreViewModel: ViewModel() {


    //Post 관련
    val postList = SampleDataUtils.samplePost()
}