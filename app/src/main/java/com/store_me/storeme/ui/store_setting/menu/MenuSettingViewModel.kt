package com.store_me.storeme.ui.store_setting.menu

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.MenuData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MenuSettingViewModel @Inject constructor(
): ViewModel() {
    private val _menuCategories = MutableStateFlow<List<MenuCategoryData>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategoryData>> = _menuCategories

    fun addMenu(menu: MenuData, targetCategoryName: String) {
        val updatedCategories = _menuCategories.value.map { menuCategory ->
            if(menuCategory.categoryName == targetCategoryName) {
                val updatedMenus = menuCategory.menus.toMutableList().apply {
                    add(menu)
                }
                menuCategory.copy(menus = updatedMenus)
            } else {
                menuCategory
            }
        }.toMutableList()

        if(updatedCategories.none { it.categoryName == targetCategoryName }) {
            updatedCategories.add(
                MenuCategoryData(
                    categoryName = targetCategoryName,
                    menus = listOf(menu)
                )
            )
        }

        _menuCategories.value = updatedCategories
    }

    fun editMenu(originalMenuName: String, newMenu: MenuData, newCategoryName: String) {
        val currentCategories = _menuCategories.value.toMutableList()
        val updatedCategories = mutableListOf<MenuCategoryData>()

        val originalCategory = currentCategories.find { it.menus.any { it.name == originalMenuName } }?.categoryName ?: return

        for(category in currentCategories) {
            if(category.categoryName == originalCategory) {
                //기존 카테고리와 동일한 경우
                if(category.categoryName == newCategoryName) {
                    //수정된 카테고리와 기존 카테고리가 동일한 경우, 기존 데이터 수정
                    val updatedMenus = category.menus.map {
                        if(it.name == originalMenuName) newMenu else it
                    }
                    updatedCategories.add(category.copy(menus = updatedMenus))
                } else {
                    //다른 카테고리로 이전한 경우, 기존 데이터 삭제
                    val filteredMenus = category.menus.filterNot { it.name == originalMenuName }
                    updatedCategories.add(category.copy(menus = filteredMenus))
                }
            } else if(category.categoryName == newCategoryName) {
                //새로 이동한 카테고리인 경우, 데이터 추가
                val updatedMenus = category.menus.toMutableList().apply { add(newMenu) }
                updatedCategories.add(category.copy(menus = updatedMenus))
            } else {
                //유지
                updatedCategories.add(category)
            }
        }

        _menuCategories.value = updatedCategories
    }

    fun deleteMenu(menuName: String) {
        val updatedCategories = _menuCategories.value.map { category ->
            category.copy(
                menus = category.menus.filter { it.name != menuName }
            )
        }

        _menuCategories.value = updatedCategories
    }

    fun reorderMenuCategories(fromIndex: Int, toIndex: Int) {
        var totalIndex = 0

        var totalIndexForFrom = 0
        var totalIndexForTo = 0

        var selectedCategoryIndexForFrom = 0
        var selectedCategoryIndexForTo = 0

        if(toIndex == 0)
            return

        for(menuCategory in menuCategories.value.withIndex()) {
            totalIndex += if(menuCategory.value.menus.isEmpty()) 1 + 1 else menuCategory.value.menus.size + 1

            when {
                toIndex == totalIndex -> {
                    when {
                        toIndex < fromIndex -> {
                            var removedItem: MenuData

                            val reorderedFromMenu = menuCategories.value[menuCategory.index + 1].menus.toMutableList().apply{
                                removedItem = removeAt(0)
                            }

                            val reorderedToMenu = menuCategories.value[menuCategory.index].menus.toMutableList().apply {
                                add(removedItem)
                            }


                            updateMenus(menus = reorderedFromMenu, categoryIndex = menuCategory.index + 1)
                            updateMenus(menus = reorderedToMenu, categoryIndex = menuCategory.index)
                        }
                        toIndex > fromIndex -> {
                            var removedItem: MenuData

                            val reorderedFromMenu = _menuCategories.value[menuCategory.index].menus.toMutableList().apply{
                                removedItem = removeAt(this.lastIndex)
                            }

                            val reorderedToMenu = menuCategories.value[menuCategory.index + 1].menus.toMutableList().apply {
                                add(0, removedItem)
                            }

                            updateMenus(menus = reorderedFromMenu, categoryIndex = menuCategory.index)
                            updateMenus(menus = reorderedToMenu, categoryIndex = menuCategory.index + 1)
                        }
                    }

                    return
                }
                totalIndex > fromIndex && totalIndex > toIndex -> {
                    if(totalIndexForFrom == 0) {
                        totalIndexForFrom = totalIndex
                        selectedCategoryIndexForFrom = menuCategory.index
                    }

                    if(totalIndexForTo == 0) {
                        totalIndexForTo = totalIndex
                        selectedCategoryIndexForTo = menuCategory.index
                    }

                    break
                }

                totalIndex > fromIndex -> {
                    totalIndexForFrom = totalIndex
                    selectedCategoryIndexForFrom = menuCategory.index
                }

                totalIndex > toIndex -> {
                    totalIndexForTo = totalIndex
                    selectedCategoryIndexForTo = menuCategory.index
                }
            }
        }

        val correctedFromIndex = fromIndex - totalIndexForFrom + menuCategories.value[selectedCategoryIndexForFrom].menus.size
        val correctedToIndex = toIndex - totalIndexForTo + menuCategories.value[selectedCategoryIndexForTo].menus.size

        if(menuCategories.value[selectedCategoryIndexForTo].menus.isEmpty()) {
            var removedItem: MenuData

            val reorderedFromMenu = menuCategories.value[selectedCategoryIndexForFrom].menus.toMutableList().apply{
                removedItem = removeAt(correctedFromIndex)
            }

            val reorderedToMenu = menuCategories.value[selectedCategoryIndexForTo].menus.toMutableList().apply {
                add(0, removedItem)
            }

            updateMenus(menus = reorderedFromMenu, categoryIndex = selectedCategoryIndexForFrom)
            updateMenus(menus = reorderedToMenu, categoryIndex = selectedCategoryIndexForTo)
        } else if(selectedCategoryIndexForFrom != selectedCategoryIndexForTo){
            var removedItem: MenuData

            val reorderedFromMenu = menuCategories.value[selectedCategoryIndexForFrom].menus.toMutableList().apply{
                removedItem = removeAt(correctedFromIndex)
            }

            val reorderedToMenu = menuCategories.value[selectedCategoryIndexForTo].menus.toMutableList().apply {
                add(correctedToIndex, removedItem)
            }

            updateMenus(menus = reorderedFromMenu, categoryIndex = selectedCategoryIndexForFrom)
            updateMenus(menus = reorderedToMenu, categoryIndex = selectedCategoryIndexForTo)
        } else {
            val reorderedMenu = menuCategories.value[selectedCategoryIndexForFrom].menus.toMutableList().apply {
                add(correctedToIndex, removeAt(correctedFromIndex))
            }

            updateMenus(reorderedMenu, selectedCategoryIndexForFrom)
        }
    }

    private fun updateMenus(menus: List<MenuData>, categoryIndex: Int) {
        val currentList = _menuCategories.value.toMutableList()

        val updatedMenuCategory = _menuCategories.value[categoryIndex].copy(menus = menus)
        currentList[categoryIndex] = updatedMenuCategory

        _menuCategories.value = currentList
    }
}