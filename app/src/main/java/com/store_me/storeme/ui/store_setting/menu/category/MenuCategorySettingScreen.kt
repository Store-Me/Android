@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.store_setting.menu.category

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.data.store.menu.MenuCategoryData
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.EditAndDeleteRow
import com.store_me.storeme.ui.component.SaveAndAddButton
import com.store_me.storeme.ui.component.SimpleOutLinedTextField
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.store_setting.menu.MenuSettingViewModel
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun MenuCategorySettingScreen(
    navController: NavController,
    menuSettingViewModel: MenuSettingViewModel,
    menuCategorySettingViewModel: MenuCategorySettingViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current

    val originalMenuCategories by menuSettingViewModel.menuCategories.collectAsState()
    val menuCategories by menuCategorySettingViewModel.menuCategories.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var showEditBottomSheet by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val hasDifference by remember { derivedStateOf {
        if(menuCategories.isEmpty())
            false
        else {
            originalMenuCategories != menuCategories
        }
    }}

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
        //데이터 동기화
        if(menuCategories.isEmpty())
            menuCategorySettingViewModel.updateMenuCategories(originalMenuCategories)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        topBar = {
            TitleWithDeleteButtonAndRow(
                title = "메뉴 카테고리 관리",
                scrollBehavior = scrollBehavior,
                onClose = { onClose() }
            ) {
                SaveAndAddButton(
                    addButtonText = "카테고리 추가",
                    hasDifference = hasDifference,
                    onAddClick = {
                        showAddBottomSheet = true
                    },
                    onSaveClick = {
                        menuSettingViewModel.updateMenuCategories(menuCategories)
                        navController.popBackStack()
                    }
                )
            }
        },
        content = { innerPadding ->
            MenuCategoryReorderList(
                modifier = Modifier.padding(innerPadding),
                menuCategories = menuCategories,
                scrollBehavior = scrollBehavior,
                onMoved = { from, to ->
                    menuCategorySettingViewModel.reorderMenuCategories(fromIndex = from, toIndex = to)
                },
                onClick = {
                    navController.navigate(OwnerRoute.MenuCategoryManagement(it).fullRoute)
                },
                onDelete = {
                    menuCategorySettingViewModel.deleteMenuCategory(it)
                },
                onEdit = {
                    showEditBottomSheet = it
                }
            )
        }
    )

    if(showAddBottomSheet) {
        DefaultBottomSheet(
            sheetState = sheetState,
            onDismiss = { showAddBottomSheet = false }
        ) {
            EditMenuCategorySection(menuCategories = menuCategories) {
                //추가
                menuCategorySettingViewModel.addMenuCategory(it)
                showAddBottomSheet = false
            }
        }
    }

    if(showEditBottomSheet != null) {
        DefaultBottomSheet(
            sheetState = sheetState,
            onDismiss = { showEditBottomSheet = null }
        ) {
            EditMenuCategorySection(initValue = showEditBottomSheet ?: "", menuCategories = menuCategories) {
                //수정
                menuCategorySettingViewModel.editMenuCategory(categoryName = showEditBottomSheet ?: "", newCategoryName = it)
                showEditBottomSheet = null
            }
        }
    }

    if(showDialog) {
        BackWarningDialog(
            onDismiss = { showDialog = false },
            onAction = {
                showDialog = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun MenuCategoryReorderList(
    modifier: Modifier,
    menuCategories: List<MenuCategoryData>,
    scrollBehavior: TopAppBarScrollBehavior,
    onMoved: (Int, Int) -> Unit,
    onClick: (String) -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    val view = LocalView.current

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onMoved(from.index, to.index)
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        if(menuCategories.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .clickable { onClick(menuCategories[0].categoryName) }
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${menuCategories[0].categoryName} (${menuCategories[0].menus.size})",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    )
                }
            }
        }

        if(menuCategories.size > 1) {
            itemsIndexed(menuCategories.drop(1), key = { _, item -> item.categoryName }) { index, menuCategory ->
                ReorderableItem(state = reorderableLazyListState, key = menuCategory.categoryName) { isDragging ->
                    val interactionSource = remember { MutableInteractionSource() }

                    Box {
                        MenuCategoryItem(
                            modifier = Modifier
                                .combinedClickable(
                                    interactionSource = interactionSource,
                                    indication = ripple(bounded = true),
                                    onClick = { onClick(menuCategory.categoryName) },
                                    onLongClick = { }
                                )
                                .longPressDraggableHandle(
                                    interactionSource = interactionSource,
                                    onDragStarted = {
                                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                    }
                                ),
                            menuCategory = menuCategory,
                            onDelete = { onDelete(it) },
                            onEdit = { onEdit(it) },
                        )

                        if(isDragging)
                            Canvas(modifier = Modifier.matchParentSize()) {
                                drawRect(color = Color.White.copy(alpha = 0.7f))
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCategoryItem(
    modifier: Modifier,
    menuCategory: MenuCategoryData,
    onDelete: (String) -> Unit,
    onEdit: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    EditAndDeleteRow(
        modifier = modifier,
        onEdit = { onEdit(menuCategory.categoryName) },
        onDelete = { showDialog = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${menuCategory.categoryName} (${menuCategory.menus.size})",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            )
        }
    }

    if(showDialog) {
        WarningDialog(
            title = "카테고리를 삭제할까요?",
            warningContent = menuCategory.categoryName,
            content = "위의 카테고리와 카테고리에 포함된 ${menuCategory.menus.size}개의 메뉴가 함께 삭제되며, 삭제 이후 복구되지않아요.",
            actionText = "삭제",
            onDismiss = {
                showDialog = false
            },
            onAction = {
                onDelete(menuCategory.categoryName)
                showDialog = false
            }
        )
    }
}

@Composable
fun EditMenuCategorySection(initValue: String = "", menuCategories: List<MenuCategoryData>, onAdd: (String) -> Unit) {
    var categoryName by remember { mutableStateOf(initValue) }

    val lengthCondition by remember(categoryName) { derivedStateOf { categoryName.length > 20 } }
    //수정일 경우 자기 자신 제외
    val duplicateCondition by remember(categoryName, menuCategories) { derivedStateOf { menuCategories.any { it.categoryName == categoryName && it.categoryName != initValue } } }

    val isError by remember(categoryName, menuCategories) { derivedStateOf { lengthCondition || duplicateCondition } }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "카테고리" + if(initValue.isEmpty()) "" else " 수정",
            style = storeMeTextStyle(FontWeight.ExtraBold, 4)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SimpleOutLinedTextField(
            text = categoryName,
            onValueChange = { categoryName = it },
            placeholderText = "카테고리 이름을 입력해주세요.",
            isError = isError,
            errorText = if(lengthCondition) "카테고리 이름은 20자 이내로 입력해주세요." else "이미 존재하는 카테고리 이름 입니다."
        )

        TextLengthRow(text = categoryName, limitSize = 20)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "가게 메뉴 카테고리는 기본 카테고리를 포함하여 20개 까지 생성할 수 있어요.",
            style = storeMeTextStyle(FontWeight.Bold, 0),
            color = GuideColor
        )

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton(
            buttonText = "추가",
            enabled = menuCategories.size < 20 && !isError && initValue != categoryName && categoryName.isNotEmpty()
        ) {
            onAdd(categoryName)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}