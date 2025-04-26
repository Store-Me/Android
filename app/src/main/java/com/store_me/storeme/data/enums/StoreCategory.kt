package com.store_me.storeme.data.enums

enum class StoreCategory(val key: String, val displayName: String){
    ALL(key = "all", displayName = "전체"),
    RESTAURANT(key = "restaurant", displayName = "식당"),
    CAFE(key = "cafe", displayName = "카페·디저트"),
    BEAUTY(key = "beauty", displayName = "뷰티샵"),
    MEDICAL(key = "medical", displayName = "의료"),
    EXERCISE(key = "exercise", displayName = "운동"),
    SALON(key = "salon", displayName = "미용실")
}