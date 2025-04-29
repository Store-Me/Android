package com.store_me.storeme.ui.home.owner.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.data.store.menu.MenuCategoryData
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.theme.storeMeTextStyle

/**
 * 홈 화면 메뉴 탭
 */
@Composable
fun MenuTab(menuCategories: List<MenuCategoryData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        menuCategories.forEach { menuCategory ->
            if(menuCategory.menus.isNotEmpty()) {
                Text(
                    text = menuCategory.categoryName,
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )

                DefaultHorizontalDivider()

                menuCategory.menus.forEach { menu ->
                    MenuItem(menu)

                    DefaultHorizontalDivider()
                }
            }
        }
    }
}