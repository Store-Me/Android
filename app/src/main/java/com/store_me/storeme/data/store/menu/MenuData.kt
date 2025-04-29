package com.store_me.storeme.data.store.menu

data class MenuData(
    val name: String,
    val priceType: String,
    val price: Long?,
    val minPrice: Long?,
    val maxPrice: Long?,
    val image: String?,
    val description: String?,
    val isSignature: Boolean,
    val isPopular: Boolean,
    val isRecommend: Boolean
)
