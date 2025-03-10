package com.store_me.storeme.data.request.menu

data class MenuCategoryRequest(
    val storeId: Long,
    val storeMenuCategory: String,
    val storeMenuCategoryOrder: Int
)