package com.store_me.storeme.ui.store_setting.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.component.EditAddSection
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner

val LocalMenuSettingViewModel = staticCompositionLocalOf<MenuSettingViewModel> {
    error("No MenuSettingViewModel provided")
}

@Composable
fun MenuSettingScreen(
    navController: NavController,
    menuSettingViewModel: MenuSettingViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val editState by menuSettingViewModel.editState.collectAsState()

    val menuCategoryList by Auth.menuCategoryList.collectAsState()

    CompositionLocalProvider(LocalMenuSettingViewModel provides menuSettingViewModel) {

        menuCategoryList.forEach {
            it.menuList.size
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = White,
            topBar = {
                TitleWithDeleteButton(
                    navController = navController,
                    title = "메뉴 관리"
                )
            },
            content = { innerPadding ->

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    EditAddSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp),
                        dataListSize = menuCategoryList.sumOf { it.menuList.size },
                        editState = editState,
                        onAdd = {  },
                        onChangeEditState = { menuSettingViewModel.setEditState(it) }
                    )
                }
            }
        )
    }

}