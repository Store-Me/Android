package com.store_me.storeme.data.request.store

data class CreatePostRequest(
    val type: String,
    val labelId: String,
    val title: String,
    val content: List<ContentData>
)

data class ContentData(
    val type: String,
    val content: String
)
