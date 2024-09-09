package com.store_me.storeme.data

enum class MenuPriceType {
    FIXED, RANGE, VARIABLE
}
data class MenuCategory(
    val categoryName: String,
    val order: Int,
    val menuList: List<MenuData>
)

/**
 * 메뉴 정보
 * @param name 메뉴 이름
 * @param price 가격
 * @param description 설명
 * @param isSignature 대표 메뉴 여부
 * @param isPopular 인기메뉴 여부
 * @param isRecommend 사장님 추천 여부
 */
data class MenuData(
    val name: String,
    val price: MenuPrice,
    val description: String,
    val isSignature: Boolean,
    val isPopular: Boolean,
    val isRecommend: Boolean
)

sealed class MenuPrice {
    data class Fixed(val price: Int): MenuPrice()
    data class Range(val minPrice: Int?, val maxPrice: Int?): MenuPrice()

    data object Variable: MenuPrice()
}