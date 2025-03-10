package com.store_me.storeme.data.request.menu

data class UpdateStoreMenuCategoryNameRequestDto(
    val storeId: Long,
    val storeMenuCategoryId: Int,
    val storeMenuCategory: String
)
