package com.store_me.storeme.data.store

import com.google.firebase.Timestamp

data class FeaturedImageData(
    val image: String,
    val height: Int,
    val width: Int,
    val description: String,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?
)
