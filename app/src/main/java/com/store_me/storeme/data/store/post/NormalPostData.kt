package com.store_me.storeme.data.store.post

import com.google.firebase.Timestamp
import com.store_me.storeme.data.request.store.ContentData

/**
 * data class for normal post
 * @property id Post Id
 * @property labelId Label Id
 * @property title Post Title
 * @property content Post Content
 * @property createdAt Post Created At
 * @property updatedAt Post Updated At
 * @property likesCount Post Likes Count
 * @property userLiked User Liked
 * @property commentsCount Post Comments Count
 * @property views Post Views
 */
data class NormalPostData(
    val id: String,
    val labelId: String,
    val title: String,
    val content: List<ContentData>,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val likesCount: Long,
    val userLiked: Boolean,
    val commentsCount: Long,
    val views: Long
)
