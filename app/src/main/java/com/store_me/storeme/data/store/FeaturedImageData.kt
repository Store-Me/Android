package com.store_me.storeme.data.store

data class FeaturedImageData(
    val image: String,
    val height: Int,
    val width: Int,
    val description: String? = null
)
