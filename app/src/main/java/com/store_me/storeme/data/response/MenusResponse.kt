package com.store_me.storeme.data.response

import com.store_me.storeme.data.store.menu.MenuCategoryData

data class MenusResponse(
    val categories: List<MenuCategoryData>
)
