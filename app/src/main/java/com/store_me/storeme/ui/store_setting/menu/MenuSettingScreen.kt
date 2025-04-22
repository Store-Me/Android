@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.store_setting.menu

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.ripple
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.MenuData
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.EditAndDeleteRow
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.home.owner.tab.MenuItem
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun MenuSettingScreen(
    navController: NavController,
    selectedMenuName: String = "",
    menuSettingViewModel: MenuSettingViewModel
) {
    val loadingViewModel = LocalLoadingViewModel.current
    val storeDataViewModel = LocalStoreDataViewModel.current

    val originalMenuCategories by storeDataViewModel.menuCategories.collectAsState()
    val menuCategories by menuSettingViewModel.menuCategories.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = rememberLazyListState()

    var showBackWarnDialog by remember { mutableStateOf(false) }
    val hasDifference by remember { derivedStateOf {
        if(menuCategories.isEmpty()) //초기 비교 방지
            false
        else
            originalMenuCategories != menuCategories
    } }

    //후기의 메뉴 항목을 선택 시
    LaunchedEffect(selectedMenuName) {
        if(selectedMenuName.isEmpty())
            return@LaunchedEffect

        var index = 0

        menuSettingViewModel.menuCategories.value.forEach { menuCategory ->
            if(menuCategory.menus.indexOfFirst { it.name == selectedMenuName } == -1) {
                index = menuCategory.menus.size + 1

                if(menuCategory.menus.isNotEmpty()) {
                    index++
                }
            } else {
                index = menuCategory.menus.indexOfFirst { it.name == selectedMenuName } + 1
                return@forEach
            }
        }

        lazyListState.scrollToItem(index)
    }

    LaunchedEffect(originalMenuCategories) {
        if(menuCategories.isEmpty())
            menuSettingViewModel.updateMenuCategories(originalMenuCategories)
    }

    fun onClose() {
        if(hasDifference) {
            showBackWarnDialog = true
        } else {
            navController.popBackStack()
        }
    }

    BackHandler {
        onClose()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TitleWithDeleteButtonAndRow(
                title = "메뉴 관리",
                scrollBehavior = scrollBehavior,
                onClose = { onClose() }
            ) {
                MenuSettingTopBarButtons(
                    menuCategories = menuCategories,
                    hasDifference = hasDifference,
                    onSave = {
                        loadingViewModel.showLoading()

                        storeDataViewModel.patchStoreMenus(menuCategories = menuCategories)
                    },
                    onManageCategory = {
                        navController.navigate(OwnerRoute.MenuCategorySetting.fullRoute)
                    },
                    onAddMenu = {
                        navController.navigate(OwnerRoute.MenuManagement(null).fullRoute)
                    }
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                MenuReorderList(
                    lazyListState = lazyListState,
                    menuCategories = menuCategories,
                    scrollBehavior = scrollBehavior,
                    onMoved = { from, to ->
                        menuSettingViewModel.reorderMenuCategories(fromIndex = from, toIndex = to)
                    },
                    onDelete = {
                        menuSettingViewModel.deleteMenu(menuName = it)
                    },
                    onEdit = {
                        navController.navigate(OwnerRoute.MenuManagement(it).fullRoute)
                    }
                )
            }
        }
    )

    if(showBackWarnDialog) {
        BackWarningDialog(
            onDismiss = { showBackWarnDialog = false },
            onAction = {
                showBackWarnDialog = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun MenuSettingTopBarButtons(menuCategories: List<MenuCategoryData>, hasDifference: Boolean, onSave: () -> Unit, onManageCategory: () -> Unit, onAddMenu: () -> Unit) {
    val showSave = hasDifference
    val showCategory = menuCategories.isNotEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if(showSave) {
            DefaultButton(
                buttonText = "저장",
                modifier = Modifier
                    .animateContentSize()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                onSave()
            }
        }

        if(showCategory) {
            DefaultButton(
                buttonText = "카테고리 관리",
                modifier = Modifier
                    .animateContentSize()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SubHighlightColor,
                    contentColor = Color.Black
                )
            ) {
                onManageCategory()
            }
        }

        DefaultButton(
            buttonText = if(showSave && showCategory) "" else "메뉴 추가",
            leftIconResource = R.drawable.ic_circle_plus,
            colors = ButtonDefaults.buttonColors(
                containerColor = HighlightColor,
                contentColor = Color.White
            ),
            leftIconTint = Color.White,
            modifier = if(showSave && showCategory) {
                Modifier
                    .animateContentSize()
                    .width(60.dp)
            } else {
                Modifier
                    .animateContentSize()
                    .weight(1f)
            }

        ) {
            onAddMenu()
        }
    }
}

@Composable
fun MenuReorderList(
    lazyListState: LazyListState,
    menuCategories: List<MenuCategoryData>,
    scrollBehavior: TopAppBarScrollBehavior,
    onMoved: (Int, Int) -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    val view = LocalView.current
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onMoved(from.index, to.index)
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        menuCategories.forEach { menuCategory ->
            //카테고리 이름 항목
            item(key = "category_${menuCategory.categoryName}") {
                ReorderableItem(state = reorderableLazyListState, key = "category_${menuCategory.categoryName}") {
                    Column {
                        Text(
                            text = menuCategory.categoryName,
                            style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )

                        DefaultHorizontalDivider()
                    }
                }
            }

            //메뉴 항목
            itemsIndexed(menuCategory.menus, key = { _, item -> item.name }) { index, menuData ->
                val interactionSource = remember { MutableInteractionSource() }

                ReorderableItem(state = reorderableLazyListState, key = menuData.name) { isDragging ->
                    Box {
                        Column {
                            DraggableMenuItem(
                                modifier = Modifier
                                    .combinedClickable(
                                        interactionSource = interactionSource,
                                        indication = ripple(bounded = true),
                                        onClick = { },
                                        onLongClick = { }
                                    )
                                    .longPressDraggableHandle(
                                        interactionSource = interactionSource,
                                        onDragStarted = {
                                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                        }
                                    ),
                                menuData = menuData,
                                onEdit = { onEdit(it) },
                                onDelete = { onDelete(it) }
                            )

                            if(!isDragging)
                                DefaultHorizontalDivider()
                        }

                        if(isDragging)
                            Canvas(modifier = Modifier.matchParentSize()) {
                                drawRect(color = Color.White.copy(alpha = 0.7f))
                            }
                    }
                }
            }

            if (menuCategory.menus.isEmpty()) {
                item(key = "empty_${menuCategory.categoryName}") {
                    ReorderableItem(state = reorderableLazyListState, key = "empty_${menuCategory.categoryName}"){
                        Column {
                            Text(
                                text = "${menuCategory.categoryName}에 메뉴를 추가해보세요.",
                                style = storeMeTextStyle(FontWeight.Bold, 2),
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 12.dp),
                                color = UndefinedTextColor
                            )

                            DefaultHorizontalDivider()
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun DraggableMenuItem(modifier: Modifier, menuData: MenuData, onEdit: (String) -> Unit, onDelete: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    EditAndDeleteRow(
        modifier = modifier,
        onEdit = {
            onEdit(menuData.name)
        },
        onDelete = {
            showDialog = true
        }
    ) {
        MenuItem(menuData = menuData)
    }

    if (showDialog) {
        WarningDialog(
            title = "메뉴를 삭제할까요?",
            warningContent = menuData.name,
            content = "위의 메뉴가 삭제되며, 삭제 이후 복구되지않아요.",
            actionText = "삭제",
            onDismiss = {
                showDialog = false
            },
            onAction = {
                onDelete(menuData.name)
                showDialog = false
            }
        )
    }


}