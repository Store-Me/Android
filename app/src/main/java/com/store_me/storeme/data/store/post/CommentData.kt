package com.store_me.storeme.data.store.post

import com.google.firebase.Timestamp

data class CommentData(
    val id: String,
    val userId: String,
    val content: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)
