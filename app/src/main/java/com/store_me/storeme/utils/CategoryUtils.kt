package com.store_me.storeme.utils

enum class StoreCategory(val displayName: String){
    ALL("전체"),
    RESTAURANT("식당"),
    CAFE("카페·디저트"),
    BEAUTY("뷰티샵"),
    MEDICAL("의료"),
    EXERCISE("운동"),
    SALON("미용실")
}

class CategoryUtils {
    companion object {
        fun getStoreCategories(): List<StoreCategory> {

            return StoreCategory.values().toList()
        }
    }
}
