package com.store_me.storeme.data.request.store

import com.store_me.storeme.data.store.FeaturedImageData

data class PatchStoreFeaturedImagesRequest(
    val images: List<FeaturedImageData>
)
