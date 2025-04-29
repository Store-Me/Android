package com.store_me.storeme.data.request.store

data class ReviewRequest(
    val selectedReviews: List<String>,
    val purchasedMenus: List<String>,
    val images: List<String>,
    val comment: String
)
