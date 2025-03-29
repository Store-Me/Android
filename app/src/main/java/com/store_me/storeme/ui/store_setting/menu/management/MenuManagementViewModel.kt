package com.store_me.storeme.ui.store_setting.menu.management

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.DEFAULT_MENU_CATEGORY
import com.store_me.storeme.data.MenuData
import com.store_me.storeme.data.enums.MenuPriceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuManagementViewModel: ViewModel() {
    enum class MenuHighLightType(val displayName: String) {
        SIGNATURE("대표 메뉴"), POPULAR("인기 메뉴"), RECOMMEND("사장님 추천")
    }

    private val _selectedCategory = MutableStateFlow(DEFAULT_MENU_CATEGORY)
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _menuData = MutableStateFlow(MenuData(
        name = "",
        priceType = "",
        price = 0,
        minPrice = 0,
        maxPrice = 0,
        image = null,
        description = null,
        isSignature = false,
        isPopular = false,
        isRecommend = false
    ))
    val menuData: StateFlow<MenuData> = _menuData

    fun updateSelectedCategory(selectedCategory: String) {
        _selectedCategory.value = selectedCategory
    }

    fun updateMenuName(name: String) {
        _menuData.value = _menuData.value.copy(name = name)
    }

    fun updateMenuPriceType(menuPriceType: MenuPriceType) {
        _menuData.value = _menuData.value.copy(priceType = menuPriceType.name)
    }

    fun updateMenuPrice(price: Int?, minPrice: Int?, maxPrice: Int?) {
        _menuData.value = _menuData.value.copy(price = price, minPrice = minPrice, maxPrice = maxPrice)
    }

}