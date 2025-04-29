package com.store_me.storeme.ui.store_setting.menu.category

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.store.menu.MenuCategoryData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MenuCategorySettingViewModel @Inject constructor() : ViewModel() {
    private val _menuCategories = MutableStateFlow<List<MenuCategoryData>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategoryData>> = _menuCategories

    fun updateMenuCategories(newMenuCategories: List<MenuCategoryData>) {
        _menuCategories.value = newMenuCategories
    }

    fun reorderMenuCategories(fromIndex: Int, toIndex: Int) {
        val currentMenuCategories = _menuCategories.value.toMutableList()
        val movedItem = currentMenuCategories.removeAt(fromIndex)
        currentMenuCategories.add(toIndex, movedItem)
        _menuCategories.value = currentMenuCategories.toList()
    }

    fun addMenuCategory(categoryName: String) {
        if (_menuCategories.value.any { it.categoryName == categoryName }) return

        val updatedList = _menuCategories.value + MenuCategoryData(
            categoryName = categoryName,
            menus = emptyList() // 기본은 빈 메뉴 리스트로 추가
        )

        _menuCategories.value = updatedList
    }

    fun deleteMenuCategory(categoryName: String) {
        val currentMenuCategories = _menuCategories.value.toMutableList()
        currentMenuCategories.removeIf { it.categoryName == categoryName }
        _menuCategories.value = currentMenuCategories.toList()
    }

    fun editMenuCategory(categoryName: String, newCategoryName: String) {
        val currentMenuCategories = _menuCategories.value.toMutableList()

        if (currentMenuCategories.any { it.categoryName == newCategoryName }) return

        val index = currentMenuCategories.indexOfFirst { it.categoryName == categoryName }
        if (index != -1) {
            val updatedCategory = currentMenuCategories[index].copy(categoryName = newCategoryName)
            currentMenuCategories[index] = updatedCategory
            _menuCategories.value = currentMenuCategories.toList()
        }
    }
}