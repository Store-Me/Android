package com.store_me.storeme.ui.store_setting.menu.category

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.MenuCategory
import com.store_me.storeme.data.MenuData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditMenuCategoryViewModel: ViewModel() {
    private val _categoryList = MutableStateFlow(Auth.menuCategoryList.value)
    val categoryList:StateFlow<List<MenuCategory>> = _categoryList

    fun menuDataMoveToSelectedCategory(menuData: MenuData, selectedCategoryName: String) {
        //바뀔 메뉴 카테고리 인덱스, 카테고리 내부 메뉴 인덱스
        val selectedMenuCategoryIndex = _categoryList.value.indexOfFirst { it.menuList.contains(menuData) }
        val selectedMenuIndex = _categoryList.value[selectedMenuCategoryIndex].menuList.indexOfFirst { it == menuData }

        val targetMenuCategoryIndex = _categoryList.value.indexOfFirst { it.categoryName == selectedCategoryName }

        val deletedList = _categoryList.value[selectedMenuCategoryIndex].menuList.toMutableList().apply {
            removeAt(selectedMenuIndex)
        }
        val addedList = _categoryList.value[targetMenuCategoryIndex].menuList.toMutableList().apply {
            add(menuData)
        }

        val updatedList = _categoryList.value.toMutableList().apply {
            this[selectedMenuCategoryIndex] = this[selectedMenuCategoryIndex].copy(menuList = deletedList)
            this[targetMenuCategoryIndex] = this[targetMenuCategoryIndex].copy(menuList = addedList)
        }

        _categoryList.value = updatedList
    }

    fun removeMovedMenuDataFromSelectedCategory(menuData: MenuData) {
        val selectedMenuCategoryIndex = _categoryList.value.indexOfFirst { it.menuList.contains(menuData) }
        val selectedMenuIndex = _categoryList.value[selectedMenuCategoryIndex].menuList.indexOfFirst { it == menuData }

        //옮겨지기 전 원래 Index
        val targetMenuCategoryIndex = Auth.menuCategoryList.value.indexOfFirst { it.menuList.contains(menuData) }

        val deletedList = _categoryList.value[selectedMenuCategoryIndex].menuList.toMutableList().apply {
            removeAt(selectedMenuIndex)
        }
        val addedList = _categoryList.value[targetMenuCategoryIndex].menuList.toMutableList().apply {
            add(menuData)
        }

        val updatedList = _categoryList.value.toMutableList().apply {
            this[selectedMenuCategoryIndex] = this[selectedMenuCategoryIndex].copy(menuList = deletedList)
            this[targetMenuCategoryIndex] = this[targetMenuCategoryIndex].copy(menuList = addedList)
        }

        _categoryList.value = updatedList
    }

    fun updateCategory(){
        Auth.updateCategory(categoryList.value)
    }
}