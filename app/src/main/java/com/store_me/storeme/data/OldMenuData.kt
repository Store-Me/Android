package com.store_me.storeme.data

data class OldMenuCategory(
    val categoryName: String,
    val menuList: List<OldMenuData>
)

//메뉴 카테고리에 특정 메뉴가 있는지 확인하는 확장함수
fun OldMenuCategory.hasMenu(menuName: String): Int {
    return menuList.indexOfFirst { it.name == menuName }
}

const val DEFAULT_MENU_CATEGORY = "기본 카테고리"

/**
 * 메뉴 정보
 * @param name 메뉴 이름
 * @param price 가격
 * @param image 이미지 URL
 * @param description 설명
 * @param isSignature 대표 메뉴 여부
 * @param isPopular 인기메뉴 여부
 * @param isRecommend 사장님 추천 여부
 */
data class OldMenuData(
    val name: String,
    val price: MenuPrice,
    val image: String,
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