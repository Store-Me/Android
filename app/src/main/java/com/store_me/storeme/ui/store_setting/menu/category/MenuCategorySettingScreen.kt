@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.store_setting.menu.category

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.DEFAULT_MENU_CATEGORY
import com.store_me.storeme.data.MenuCategory
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.DefaultOutlineTextField
import com.store_me.storeme.ui.component.EditAndDeleteRow
import com.store_me.storeme.ui.component.LargeButton
import com.store_me.storeme.ui.component.TextFieldErrorType
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithSaveButton
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.store_setting.menu.DragValue
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.SwipeDeleteColor
import com.store_me.storeme.ui.theme.SwipeEditColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.NavigationUtils
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.roundToInt

val LocalMenuCategorySettingViewModel = staticCompositionLocalOf<MenuCategorySettingViewModel> {
    error("No MenuCategorySettingViewModel provided")
}

@Composable
fun MenuCategorySettingScreen(
    navController: NavController,
    menuCategorySettingViewModel: MenuCategorySettingViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val categoryList by Auth.menuCategoryList.collectAsState()

    val haptic = LocalHapticFeedback.current

    //Reorderable
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState = lazyListState) { from, to ->
        Auth.updateCategory(
            categoryList.toMutableList().apply {
                add(to.index - 1, removeAt(from.index - 1))
            }
        )
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    CompositionLocalProvider(LocalMenuCategorySettingViewModel provides menuCategorySettingViewModel) {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = White,
            topBar = {
                MenuCategoryTopLayout(navController = navController, scrollBehavior = scrollBehavior)
            },
            content = { innerPadding ->
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxSize()
                ) {
                    item { AddCategorySection() }

                    itemsIndexed(categoryList.dropLast(1), key = { _ , item -> item.categoryName}) { index, item ->
                        val interactionSource = remember { MutableInteractionSource() }

                        ReorderableItem(state = reorderableLazyListState, key = item.categoryName) { isDragging ->
                            Box {
                                MenuCategoryItem(
                                    menuCategory = item,
                                    modifier = Modifier
                                        .combinedClickable(
                                            interactionSource = interactionSource,
                                            indication = ripple(bounded = true),
                                            onClick = {
                                                NavigationUtils().navigateOwnerNav(
                                                    navController,
                                                    MainActivity.OwnerNavItem.EDIT_MENU_CATEGORY,
                                                    additionalData = item.categoryName
                                                ) },
                                            onLongClick = {  }
                                        )
                                        .longPressDraggableHandle (
                                            onDragStarted = {
                                                val press = PressInteraction.Press(it)
                                                interactionSource.tryEmit(press)

                                                interactionSource.tryEmit(PressInteraction.Cancel(press))
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            }
                                        )
                                )

                                if(isDragging)
                                    Canvas(modifier = Modifier.matchParentSize()) {
                                        drawRect(color = White.copy(alpha = 0.7f))
                                    }

                            }

                        }
                    }

                    item { MenuCategoryItem(menuCategory = categoryList.last(), modifier = Modifier) }
                }
            }
        )

    }
}

@Composable
fun MenuCategoryItem(menuCategory: MenuCategory, modifier: Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val halfScreen = with(density) { (configuration.screenWidthDp.dp / 2).toPx() }

    var oldValue = DragValue.Center

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Center,
            anchors = DraggableAnchors {
                DragValue.Start at -screenWidth
                DragValue.Center at 0f
                DragValue.End at screenWidth
            },
            positionalThreshold = { halfScreen },
            velocityThreshold = { with(density) { 1000.dp.toPx()} },
            snapAnimationSpec = tween(),
            decayAnimationSpec = exponentialDecay(),
            confirmValueChange = { newValue ->
                if(newValue != oldValue) {
                    oldValue = newValue

                    when(newValue) {
                        DragValue.End -> {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        DragValue.Start -> {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        DragValue.Center -> {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    }
                }

                true
            }
        )
    }

    LaunchedEffect(state.settledValue) {
        when (state.currentValue) {
            DragValue.End -> {
                state.snapTo(DragValue.Center)
            }
            DragValue.Start -> {
                showDialog = true
                state.snapTo(DragValue.Center)
            }
            else -> {  }
        }
    }

    val editAlpha = (state.progress(DragValue.Center, DragValue.End) * 2).coerceIn(0f, 1f)
    val deleteAlpha = (state.progress(DragValue.Center, DragValue.Start) * 2).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawRect(
                    color = SwipeEditColor.copy(alpha = editAlpha)
                )

                drawRect(
                    color = SwipeDeleteColor.copy(alpha = deleteAlpha)
                )

                drawContent()
            }
    ) {
        EditAndDeleteRow(
            diffValue = 2,
            editModifier = Modifier.align(Alignment.CenterStart),
            editAlpha = editAlpha,
            deleteModifier = Modifier.align(Alignment.CenterEnd),
            deleteAlpha = deleteAlpha
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal
                )
                .offset {
                    IntOffset(
                        state
                            .requireOffset()
                            .roundToInt(), 0
                    )
                }
                .background(color = White)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${menuCategory.categoryName} (${menuCategory.menuList.size})",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )
        }
    }

    if(showDialog) {
        WarningDialog(
            title = "카테고리를 삭제할까요?",
            warningContent = menuCategory.categoryName,
            content = "위의 카테고리와 카테고리에 포함된 ${menuCategory.menuList.size}개의 메뉴가 함께 삭제되며, 삭제 이후 복구되지않아요.",
            actionText = "삭제",
            onDismiss = {
                showDialog = false
            },
            onAction = {
                Auth.deleteCategory(categoryName = menuCategory.categoryName)
                showDialog = false
            }
        )
    }
}

@Composable
fun AddCategorySection() {
    val menuCategorySettingViewModel = LocalMenuCategorySettingViewModel.current
    val name by remember { menuCategorySettingViewModel.name }.collectAsState()

    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "카테고리 추가",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2)
            )

            TextLengthRow(text = name, limitSize = 20)
        }

        DefaultOutlineTextField(
            text = name,
            onValueChange = { menuCategorySettingViewModel.updateName(it) },
            placeholderText = "카테고리 이름을 입력해주세요.",
            errorType = TextFieldErrorType.MENU_CATEGORY_NAME,
            onErrorChange = { isError = it }
        )

        Text(
            text = "가게 메뉴 카테고리는 20개 까지 생성할 수 있어요.",
            style = storeMeTextStyle(FontWeight.Bold, 0),
            color = UndefinedTextColor,
            modifier = Modifier
                .padding(vertical = 10.dp)
        )
        LargeButton(text = "추가하기",
            containerColor = EditButtonColor,
            contentColor = Black,
            enabled = !isError && name.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Auth.addCategory(name)
            menuCategorySettingViewModel.updateName("")
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    DefaultHorizontalDivider()
}

@Composable
fun MenuCategoryTopLayout(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    TitleWithSaveButton(navController = navController, title = "카테고리 관리", scrollBehavior = scrollBehavior) {
        navController.popBackStack()
    }
}
