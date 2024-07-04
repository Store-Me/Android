package com.store_me.storeme.data

data class DefaultPostData(
    val storeImage: String,
    val storeName: String,
    val location: String,
    val datetime: String,

    val imageList: List<String>,
    val title: String,
    val content: String,
)
