@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.menu.edit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.ui.store_setting.menu.add.MenuManagementViewModel

@Composable
fun EditMenuScreen(
    navController: NavController,
    selectedMenuName: String,
    menuManagementViewModel: MenuManagementViewModel = viewModel()
) {

}