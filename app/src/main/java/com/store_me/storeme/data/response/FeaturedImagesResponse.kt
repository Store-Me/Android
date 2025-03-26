package com.store_me.storeme.data.response

import com.store_me.storeme.data.store.FeaturedImageData

data class FeaturedImagesResponse(
    val images: List<FeaturedImageData>? = emptyList()
)
