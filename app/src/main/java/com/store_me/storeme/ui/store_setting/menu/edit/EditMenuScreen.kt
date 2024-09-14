@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.menu.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.store_setting.menu.add.AddMenuTopLayout
import com.store_me.storeme.ui.store_setting.menu.add.AddMenuViewModel
import com.store_me.storeme.ui.store_setting.menu.add.LocalAddMenuViewModel
import com.store_me.storeme.ui.store_setting.menu.add.MenuCategorySection
import com.store_me.storeme.ui.store_setting.menu.add.MenuDescriptionSection
import com.store_me.storeme.ui.store_setting.menu.add.MenuHighlightSection
import com.store_me.storeme.ui.store_setting.menu.add.MenuImageSection
import com.store_me.storeme.ui.store_setting.menu.add.MenuNameSection
import com.store_me.storeme.ui.store_setting.menu.add.MenuPriceSection

@Composable
fun EditMenuScreen(
    navController: NavController,
    selectedMenuName: String,
    addMenuViewModel: AddMenuViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(selectedMenuName) {
        addMenuViewModel.getMenuData(selectedMenuName)
    }

    CompositionLocalProvider(LocalAddMenuViewModel provides addMenuViewModel) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = Color.White,
            topBar = { AddMenuTopLayout(
                navController = navController,
                scrollBehavior = scrollBehavior,
                isEdit = true
            ){
                addMenuViewModel.updateMenuData()
                navController.popBackStack()
            } },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxWidth()
                ) {
                    item { MenuCategorySection() }

                    item { MenuNameSection(isEdit = true) }

                    item { MenuPriceSection() }

                    item { MenuImageSection() }

                    item { MenuDescriptionSection() }

                    item { MenuHighlightSection() }
                }
            }
        )
    }
}