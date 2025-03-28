package com.store_me.storeme.data

data class MenuData(
    val name: String,
    val priceType: String,
    val price: Int?,
    val minPrice: Int?,
    val maxPrice: Int?,
    val image: String?,
    val description: String?,
    val isSignature: Boolean,
    val isPopular: Boolean,
    val isRecommend: Boolean
)
