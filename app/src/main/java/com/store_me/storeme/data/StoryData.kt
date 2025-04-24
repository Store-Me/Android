package com.store_me.storeme.data

import com.google.firebase.Timestamp

data class StoryData(
    val id: String,
    val video: String,
    val thumbNail: String,
    val description: String,
    val createdAt: Timestamp,
    val likesCount: Long,
    val userLiked: Boolean
)
