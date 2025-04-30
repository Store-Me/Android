package com.store_me.storeme.data.store.post

/**
 * data class for label data
 * @param labelId label id
 * @param name label name
 * @param order label order
 * @param postCount label post count
 */
data class LabelData(
    val labelId: String,
    val name: String,
    val order: Int,
    val postCount: Int
)