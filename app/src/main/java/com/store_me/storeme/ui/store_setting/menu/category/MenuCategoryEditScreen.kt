@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.menu.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.OldMenuCategory
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.TitleWithSaveButton
import com.store_me.storeme.ui.theme.SelectedCheckBoxColor
import com.store_me.storeme.ui.theme.SelectedCheckBoxColorPink
import com.store_me.storeme.ui.theme.storeMeTextStyle

val LocalEditMenuCategoryViewModel = staticCompositionLocalOf<EditMenuCategoryViewModel> {
    error("No EditMenuCategoryViewModel provided")
}

@Composable
fun MenuCategoryEditScreen(
    navController: NavController,
    selectedCategoryName: String,
    editMenuCategoryViewModel: EditMenuCategoryViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val originCategoryList by remember { Auth.menuCategoryList }.collectAsState()
    val categoryList by remember { editMenuCategoryViewModel.categoryList }.collectAsState()


    CompositionLocalProvider(LocalEditMenuCategoryViewModel provides editMenuCategoryViewModel) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = White,
            topBar = {
                EditMenuCategoryTopLayout(
                    navController = navController,
                    scrollBehavior = scrollBehavior
                )
            },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxSize()
                ) {
                    itemsIndexed(categoryList) {index, it ->
                        CategoryWithMenuItem(it, index ,selectedCategoryName)
                    }
                }
            }
        )
    }
}

@Composable
fun CategoryWithMenuItem(menuCategory: OldMenuCategory, currentIndex: Int, selectedCategoryName: String) {
    val editMenuCategoryViewModel = LocalEditMenuCategoryViewModel.current

    val originCategoryList by Auth.menuCategoryList.collectAsState()
    val categoryList by editMenuCategoryViewModel.categoryList.collectAsState()

    val isSelected = selectedCategoryName == menuCategory.categoryName

    fun isOriginData(menuName: String): Boolean {
        return when (originCategoryList[currentIndex].menuList.find { it.name == menuName } ) {
            null -> false
            else -> true
        }
    }

    fun getCheckBoxColor(menuName: String): Color {
        return when {
            isSelected && isOriginData(menuName) -> {
                SelectedCheckBoxColor
            }
            isSelected -> {
                SelectedCheckBoxColorPink
            }
            else -> {
                SelectedCheckBoxColor
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "${menuCategory.categoryName} (${menuCategory.menuList.size})",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            modifier = Modifier.padding(vertical = 20.dp)
        )

        menuCategory.menuList.forEachIndexed { index, it ->
            DefaultCheckButton(
                isCheckIconOnLeft = true,
                text = it.name,
                fontWeight = FontWeight.ExtraBold,
                isSelected = isSelected,
                enabled = !(isSelected && isOriginData(it.name)),
                selectedColor = getCheckBoxColor(it.name),
                diffValue = 2,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if(!isSelected)
                    editMenuCategoryViewModel.menuDataMoveToSelectedCategory(it, selectedCategoryName)
                else
                    editMenuCategoryViewModel.removeMovedMenuDataFromSelectedCategory(it)
            }

            if (index < menuCategory.menuList.size - 1) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun EditMenuCategoryTopLayout(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    val editMenuCategoryViewModel = LocalEditMenuCategoryViewModel.current

    TitleWithSaveButton(navController = navController, title = "카테고리 관리", scrollBehavior = scrollBehavior) {
        editMenuCategoryViewModel.updateCategory()
        navController.popBackStack()
    }
}