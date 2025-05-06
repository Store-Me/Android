package com.store_me.storeme.data.response

import com.store_me.storeme.data.store.post.CommentData

data class CommentResponse(
    val comments: List<CommentData>
)
