package com.store_me.storeme.data.request.store

/**
 * data class for request to create a normal post
 * @param type type of content
 * @param content content of the post
 */
data class CreatePostRequest(
    val type: String,
    val labelId: String,
    val title: String,
    val content: List<ContentData>
)

/**
 * data class for content data
 * @param type type of content
 * @param content content of the content
 */
data class ContentData(
    val type: String,
    val content: String
)
