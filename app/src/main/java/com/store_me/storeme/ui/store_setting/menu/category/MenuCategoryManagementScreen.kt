@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.menu.category

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.data.store.menu.MenuCategoryData
import com.store_me.storeme.data.store.menu.MenuData
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.SelectedCheckBoxColor
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun MenuCategoryManagementScreen(
    navController: NavController,
    selectedCategoryName: String,
    menuCategorySettingViewModel: MenuCategorySettingViewModel,
    menuCategoryManagementViewModel: MenuCategoryManagementViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val originalMenuCategories by menuCategorySettingViewModel.menuCategories.collectAsState()
    val menuCategories by menuCategoryManagementViewModel.menuCategories.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    val hasDifference by remember {
        derivedStateOf {
            if (menuCategories.isEmpty())
                false
            else {
                originalMenuCategories != menuCategories
            }
        }
    }

    fun onClose() {
        if(hasDifference) {
            showDialog = true
        } else {
            navController.popBackStack()
        }
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(originalMenuCategories) {
        menuCategoryManagementViewModel.updateMenuCategories(originalMenuCategories)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TitleWithDeleteButtonAndRow(
                title = "메뉴 카테고리 관리",
                scrollBehavior = scrollBehavior,
                onClose = { onClose() }
            ) {
                DefaultButton(
                    buttonText = "저장",
                    enabled = hasDifference,
                ) {
                    menuCategorySettingViewModel.updateMenuCategories(menuCategories)
                    navController.popBackStack()
                }
            }
        },
        content = { innerPadding ->
            val sortedMenuCategories = remember(menuCategories, selectedCategoryName) {
                menuCategories.sortedWith(compareByDescending { it.categoryName == selectedCategoryName })
            }

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                itemsIndexed(sortedMenuCategories) { index, it ->
                    CategoryWithMenuItem(
                        originalMenuCategories = originalMenuCategories,
                        menuCategory = it,
                        selectedCategoryName = selectedCategoryName,
                        onMove = {
                            menuCategoryManagementViewModel.menuDataMoveToSelectedCategory(it, selectedCategoryName)
                        },
                        onRevert = {
                            menuCategoryManagementViewModel.removeMovedMenuDataFromSelectedCategory(originalMenuCategories = originalMenuCategories, it)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    )

    if(showDialog) {
        BackWarningDialog(
            onDismiss = {
                showDialog = false
            },
            onAction = {
                showDialog = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun CategoryWithMenuItem(
    originalMenuCategories: List<MenuCategoryData>,
    menuCategory: MenuCategoryData,
    selectedCategoryName: String,
    onMove: (MenuData) -> Unit,
    onRevert: (MenuData) -> Unit
) {

    val isSelected = selectedCategoryName == menuCategory.categoryName

    fun isOriginData(menu: MenuData): Boolean {
        val originCategory = originalMenuCategories.find { it.categoryName == menuCategory.categoryName }
        return originCategory?.menus?.any { it.name == menu.name } == true
    }

    fun getCheckBoxColor(menu: MenuData): Color {
        return when {
            isSelected && isOriginData(menu) -> SelectedCheckBoxColor
            isSelected -> HighlightColor
            else -> SelectedCheckBoxColor
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "${menuCategory.categoryName} (${menuCategory.menus.size})",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
        )

        menuCategory.menus.forEachIndexed { index, it ->
            Row(
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Spacer(modifier = Modifier.width(32.dp))

                DefaultCheckButton(
                    isCheckIconOnLeft = true,
                    text = it.name,
                    fontWeight = FontWeight.ExtraBold,
                    isSelected = isSelected,
                    enabled = !(isSelected && isOriginData(it)),
                    selectedColor = getCheckBoxColor(it),
                    diffValue = 2
                ) {
                    if(!isSelected)
                        onMove(it)
                    else
                        onRevert(it)
                }

                Spacer(modifier = Modifier.weight(1f))
            }


            if (index < menuCategory.menus.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}