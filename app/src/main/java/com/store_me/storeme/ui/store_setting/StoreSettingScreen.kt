@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.ui.component.LargeButton
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.DeleteTextColor
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.SaveButtonColor
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.NavigationUtils

val LocalStoreSettingViewModel = staticCompositionLocalOf<StoreSettingViewModel> {
    error("No LocalStoreSettingViewModel provided")
}
@Composable
fun StoreSettingScreen(
    navController: NavController,
    storeSettingViewModel: StoreSettingViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    CompositionLocalProvider(LocalStoreSettingViewModel provides storeSettingViewModel) {
        Scaffold(
            containerColor = White,
            topBar = { StoreSettingTopLayout(navController = navController, scrollBehavior = scrollBehavior) },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxWidth()
                ) {
                    StoreItemsSection(navController)
                }
            }
        )
    }
}

@Composable
fun StoreSettingTopLayout(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    val storeSettingViewModel = LocalStoreSettingViewModel.current
    val editState by storeSettingViewModel.editState.collectAsState()

    Column {
        TopAppBar(
            title ={
                TitleWithDeleteButton(navController = navController, title = "가게정보 관리", isInTopAppBar = true)
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White,
                scrolledContainerColor = White
            )
        )

        LargeButton(
            text = if(editState) "저장" else "순서 편집/숨기기",
            containerColor = if(editState) SaveButtonColor else EditButtonColor,
            contentColor = if(editState) White else Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            storeSettingViewModel.setEditState(!editState)
        }
    }


}

@Composable
fun StoreItemsSection(navController: NavController) {
    val storeSettingViewModel = LocalStoreSettingViewModel.current

    val editState by storeSettingViewModel.editState.collectAsState()

    LazyColumn {
        items(StoreHomeItem.entries) {
            StoreItemDefault(it.displayName, editState = false, columnModifier = Modifier.clickable {
                NavigationUtils().navigateOwnerNav(navController = navController, screenName = it)
            })
        }
    }
}

@Composable
fun BasicTextSection(text: String) {
    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = text,
        style = storeMeTextStyle(FontWeight.ExtraBold, 6),
        modifier = Modifier
            .padding(20.dp)
    )

    HorizontalDivider(
        color = DefaultDividerColor,
        thickness = 1.dp,
    )
}

@Composable
fun StoreItemDefault(
    title: String,
    isVisible: Boolean? = null,
    editState: Boolean,
    isDragging: Boolean = false,
    columnModifier: Modifier = Modifier,
    handelModifier: Modifier = Modifier,
    onVisibilityChange: () -> Unit = {}
) {
    val lineColor = if(isDragging) Color.Transparent else DefaultDividerColor

    Column(
        modifier = columnModifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            )

            Spacer(modifier = Modifier.weight(1f))

            when(editState) {
                true -> {
                    if(isVisible != null) {
                        Box(
                            Modifier
                                .background(Color.Transparent)
                                .clickable(
                                    onClick = onVisibilityChange,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = false)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = if(isVisible) "숨김" else "보임", style = storeMeTextStyle(FontWeight.ExtraBold, 2), color = DeleteTextColor)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_drag),
                            contentDescription = "드래그 아이콘",
                            modifier = handelModifier
                                .size(18.dp),
                            tint = DeleteTextColor
                        )
                    } else {
                        Box(
                            Modifier
                                .background(Color.Transparent)
                                .size(18.dp)
                        )
                    }
                }

                false -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "이동 아이콘",
                        modifier = Modifier
                            .size(18.dp)
                            .padding(3.dp),
                        tint = Black
                    )
                }
            }

        }

        HorizontalDivider(
            color = lineColor,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Composable
fun EmptyAlertText(text: String) {
    Text(
        text = text,
        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        color = UnselectedItemColor,
        modifier = Modifier.padding(20.dp)
    )

    HorizontalDivider(
        color = DefaultDividerColor,
        thickness = 1.dp,
    )
}