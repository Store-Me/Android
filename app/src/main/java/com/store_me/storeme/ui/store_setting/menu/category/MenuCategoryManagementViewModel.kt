package com.store_me.storeme.ui.store_setting.menu.category

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.MenuData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuCategoryManagementViewModel : ViewModel() {
    private val _menuCategories = MutableStateFlow<List<MenuCategoryData>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategoryData>> = _menuCategories

    fun updateMenuCategories(menuCategories: List<MenuCategoryData>) {
        _menuCategories.value = menuCategories
    }

    fun menuDataMoveToSelectedCategory(menuData: MenuData, selectedCategoryName: String) {
        val current = _menuCategories.value.toMutableList()

        val fromIndex = current.indexOfFirst { it.menus.any { menu -> menu.name == menuData.name } }
        val toIndex = current.indexOfFirst { it.categoryName == selectedCategoryName }

        if (fromIndex == -1 || toIndex == -1) return

        val fromMenus = current[fromIndex].menus.toMutableList().apply {
            removeIf { it.name == menuData.name }
        }
        val toMenus = current[toIndex].menus.toMutableList().apply {
            add(menuData)
        }

        current[fromIndex] = current[fromIndex].copy(menus = fromMenus)
        current[toIndex] = current[toIndex].copy(menus = toMenus)

        _menuCategories.value = current
    }

    fun removeMovedMenuDataFromSelectedCategory(
        originalMenuCategories: List<MenuCategoryData>,
        menuData: MenuData
    ) {
        val current = _menuCategories.value.toMutableList()

        val fromIndex = current.indexOfFirst { it.menus.any { menu -> menu.name == menuData.name } }
        val toIndex = originalMenuCategories.indexOfFirst { it.menus.any { menu -> menu.name == menuData.name } }

        if (fromIndex == -1 || toIndex == -1) return

        val fromMenus = current[fromIndex].menus.toMutableList().apply {
            removeIf { it.name == menuData.name }
        }
        val toMenus = current[toIndex].menus.toMutableList().apply {
            add(menuData)
        }

        current[fromIndex] = current[fromIndex].copy(menus = fromMenus)
        current[toIndex] = current[toIndex].copy(menus = toMenus)

        _menuCategories.value = current
    }
}