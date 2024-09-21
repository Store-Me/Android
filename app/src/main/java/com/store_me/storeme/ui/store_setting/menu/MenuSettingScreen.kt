@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)

package com.store_me.storeme.ui.store_setting.menu

import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.MenuData
import com.store_me.storeme.data.MenuPrice
import com.store_me.storeme.data.hasMenu
import com.store_me.storeme.ui.component.AddButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.LargeButton
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.store_setting.menu.add.AddMenuViewModel.MenuHighLightType.*
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.MenuPriceDescriptionColor
import com.store_me.storeme.ui.theme.PopularBoxColor
import com.store_me.storeme.ui.theme.PopularTextColor
import com.store_me.storeme.ui.theme.RecommendBoxColor
import com.store_me.storeme.ui.theme.RecommendTextColor
import com.store_me.storeme.ui.theme.SignatureBoxColor
import com.store_me.storeme.ui.theme.SignatureTextColor
import com.store_me.storeme.ui.theme.SwipeDeleteColor
import com.store_me.storeme.ui.theme.SwipeEditColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.PriceUtils
import com.store_me.storeme.utils.SizeUtils
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.roundToInt

val LocalMenuSettingViewModel = staticCompositionLocalOf<MenuSettingViewModel> {
    error("No MenuSettingViewModel provided")
}

enum class DragValue {
    Start,
    Center,
    End,
}

@Composable
fun MenuSettingScreen(
    navController: NavController,
    selectedMenuName: String = "",
    menuSettingViewModel: MenuSettingViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val menuCategoryList by Auth.menuCategoryList.collectAsState()

    val view = LocalView.current

    //Reorderable
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState = lazyListState) { from, to ->
        var totalIndex = 0

        var totalIndexForFrom = 0
        var totalIndexForTo = 0

        var selectedCategoryIndexForFrom = 0
        var selectedCategoryIndexForTo = 0

        if(to.index == 0)
            return@rememberReorderableLazyListState

        for(category in menuCategoryList.withIndex()) {
            totalIndex += if(category.value.menuList.isEmpty()) 1 + 1 else category.value.menuList.size + 1

            when {
                to.index == totalIndex -> {
                    when {
                        to.index < from.index -> {
                            var removedItem: MenuData

                            val reorderedFromMenu = menuCategoryList[category.index + 1].menuList.toMutableList().apply{
                                removedItem = removeAt(0)
                            }

                            val reorderedToMenu = menuCategoryList[category.index].menuList.toMutableList().apply {
                                add(removedItem)
                            }

                            Auth.updateMenuData(menuList = reorderedFromMenu, categoryIndex = category.index + 1)
                            Auth.updateMenuData(menuList = reorderedToMenu, categoryIndex = category.index)
                        }
                        to.index > from.index -> {
                            var removedItem: MenuData

                            val reorderedFromMenu = menuCategoryList[category.index].menuList.toMutableList().apply{
                                removedItem = removeAt(this.lastIndex)
                            }

                            val reorderedToMenu = menuCategoryList[category.index + 1].menuList.toMutableList().apply {
                                add(0, removedItem)
                            }

                            Auth.updateMenuData(menuList = reorderedFromMenu, categoryIndex = category.index)
                            Auth.updateMenuData(menuList = reorderedToMenu, categoryIndex = category.index + 1)
                        }
                    }

                    return@rememberReorderableLazyListState
                }
                totalIndex > from.index && totalIndex > to.index -> {
                    if(totalIndexForFrom == 0) {
                        totalIndexForFrom = totalIndex
                        selectedCategoryIndexForFrom = category.index
                    }

                    if(totalIndexForTo == 0) {
                        totalIndexForTo = totalIndex
                        selectedCategoryIndexForTo = category.index
                    }

                    break
                }

                totalIndex > from.index -> {
                    totalIndexForFrom = totalIndex
                    selectedCategoryIndexForFrom = category.index
                }

                totalIndex > to.index -> {
                    totalIndexForTo = totalIndex
                    selectedCategoryIndexForTo = category.index
                }
            }
        }

        val correctedFromIndex = from.index - totalIndexForFrom + menuCategoryList[selectedCategoryIndexForFrom].menuList.size
        val correctedToIndex = to.index - totalIndexForTo + menuCategoryList[selectedCategoryIndexForTo].menuList.size

        Log.d("Index", "from.index : ${from.index}, to.index : ${to.index}")
        Log.d("Index", "correctedFromIndex : $correctedFromIndex, correctedToIndex : $correctedToIndex")

        if(menuCategoryList[selectedCategoryIndexForTo].menuList.isEmpty()) {
            var removedItem: MenuData

            val reorderedFromMenu = menuCategoryList[selectedCategoryIndexForFrom].menuList.toMutableList().apply{
                removedItem = removeAt(correctedFromIndex)
            }

            val reorderedToMenu = menuCategoryList[selectedCategoryIndexForTo].menuList.toMutableList().apply {
                add(0, removedItem)
            }

            Auth.updateMenuData(menuList = reorderedFromMenu, categoryIndex = selectedCategoryIndexForFrom)
            Auth.updateMenuData(menuList = reorderedToMenu, categoryIndex = selectedCategoryIndexForTo)
        } else if(selectedCategoryIndexForFrom != selectedCategoryIndexForTo){
            var removedItem: MenuData

            val reorderedFromMenu = menuCategoryList[selectedCategoryIndexForFrom].menuList.toMutableList().apply{
                removedItem = removeAt(correctedFromIndex)
            }

            val reorderedToMenu = menuCategoryList[selectedCategoryIndexForTo].menuList.toMutableList().apply {
                add(correctedToIndex, removedItem)
            }

            Auth.updateMenuData(menuList = reorderedFromMenu, categoryIndex = selectedCategoryIndexForFrom)
            Auth.updateMenuData(menuList = reorderedToMenu, categoryIndex = selectedCategoryIndexForTo)
        } else {
            val reorderedMenu = menuCategoryList[selectedCategoryIndexForFrom].menuList.toMutableList().apply {
                add(correctedToIndex, removeAt(correctedFromIndex))
            }

            Auth.updateMenuData(reorderedMenu, categoryIndex = selectedCategoryIndexForFrom)
        }

        view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)

    }

    LaunchedEffect(selectedMenuName) {
        if(selectedMenuName.isEmpty())
            return@LaunchedEffect

        var index: Int = 0

        Auth.menuCategoryList.value.forEach {
            if(it.hasMenu(selectedMenuName) == -1){
                index = it.menuList.size + 1

                if(it.menuList.isEmpty())
                    index++
            } else {
                index = it.hasMenu(selectedMenuName) + 1
                return@forEach
            }
        }

        lazyListState.scrollToItem(index)
    }

    CompositionLocalProvider(LocalMenuSettingViewModel provides menuSettingViewModel) {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = White,
            topBar = {
                MenuSettingTopLayout(navController = navController, scrollBehavior = scrollBehavior)
            },
            content = { innerPadding ->

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxSize()
                ) {

                    menuCategoryList.forEach { category ->
                        item(key = "category_${category.categoryName}") {
                            ReorderableItem(state = reorderableLazyListState, key = "category_${category.categoryName}") {
                                Column {
                                    Text(
                                        text = category.categoryName,
                                        style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                                        modifier = Modifier.padding(20.dp)
                                    )

                                    DefaultHorizontalDivider()
                                }
                            }
                        }

                        itemsIndexed(category.menuList, key = { _, item -> item.name }) { index, menuData ->
                            val interactionSource = remember { MutableInteractionSource() }

                            ReorderableItem(state = reorderableLazyListState, key = menuData.name) { isDragging ->
                                Box {
                                    MenuItemSection(
                                        navController = navController,
                                        menuData = menuData,
                                        modifier = Modifier
                                            .combinedClickable(
                                                interactionSource = interactionSource,
                                                indication = ripple(bounded = true),
                                                onClick = { },
                                                onLongClick = { }
                                            )
                                            .longPressDraggableHandle(
                                                onDragStarted = {
                                                    val press = PressInteraction.Press(it)
                                                    interactionSource.tryEmit(press)

                                                    interactionSource.tryEmit(
                                                        PressInteraction.Cancel(
                                                            press
                                                        )
                                                    )
                                                    view.performHapticFeedback(
                                                        HapticFeedbackConstants.LONG_PRESS
                                                    )
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

                        if (category.menuList.isEmpty()) {
                            item(key = "empty_${category.categoryName}") {
                                ReorderableItem(state = reorderableLazyListState, key = "empty_${category.categoryName}"){
                                    Column {
                                        Text(
                                            text = "${category.categoryName}에 메뉴를 추가해보세요.",
                                            style = storeMeTextStyle(FontWeight.Bold, 2),
                                            modifier = Modifier
                                                .padding(horizontal = 20.dp, vertical = 10.dp),
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
        )
    }
}

@Composable
fun MenuItemSection(navController: NavController, menuData: MenuData, modifier: Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    val priceText = when(menuData.price) {
        is MenuPrice.Fixed -> {
            PriceUtils().numberToPrice(menuData.price.price)
        }
        is MenuPrice.Range -> {
            if(menuData.price.minPrice == null && menuData.price.maxPrice == null)
                "${menuData.price.minPrice}~${menuData.price.maxPrice}"
            else if(menuData.price.maxPrice == null)
                "최소 ${menuData.price.minPrice} 부터"
            else if(menuData.price.minPrice == null)
                "최대 ${menuData.price.maxPrice} 까지"
            else
                ""
        }
        is MenuPrice.Variable -> {
            "변동"
        }
    }

    val haptic = LocalHapticFeedback.current

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidth = with(density) { (configuration.screenWidthDp.dp).toPx() }
    val halfScreen = with(density) { (configuration.screenWidthDp.dp / 2).toPx() } // 절반 크기 계산

    var oldValue = DragValue.Center

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Center,
            anchors = DraggableAnchors {
                DragValue.Start at -screenWidth //Delete
                DragValue.Center at 0f
                DragValue.End at screenWidth
            },
            positionalThreshold = { halfScreen },
            velocityThreshold = { with(density) { 1000.dp.toPx() } },
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
                NavigationUtils().navigateOwnerNav(navController = navController, MainActivity.OwnerNavItem.EDIT_MENU, additionalData = menuData.name)
                state.snapTo(DragValue.Center)
            }
            DragValue.Start -> {
                showDialog = true
                state.snapTo(DragValue.Center)
            }
            else -> {}
        }
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
                Auth.deleteMenuData(menuData)
                showDialog = false
            }
        )
    }

    val editAlpha = (state.progress(DragValue.Center, DragValue.End) * 2).coerceIn(0f, 1f)
    val deleteAlpha = (state.progress(DragValue.Center, DragValue.Start) * 2).coerceIn(0f, 1f)
    val rowContentAlpha =  1 - (deleteAlpha + editAlpha)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                drawRect(
                    color = SwipeEditColor.copy(alpha = editAlpha)
                )

                drawRect(
                    color = SwipeDeleteColor.copy(alpha = deleteAlpha)
                )

                // 기존 콘텐츠 그리기
                drawContent()
            }
    ) {

        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "수정 아이콘",
                tint = White.copy(alpha = editAlpha),
                modifier = Modifier.size(SizeUtils().textSizeToDp(density, 6))
            )

            Text(
                text = "수정",
                style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                color = White.copy(alpha = editAlpha)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "삭제",
                style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                color = White.copy(alpha = deleteAlpha)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_delete_trashcan),
                contentDescription = "삭제 아이콘",
                tint = White.copy(alpha = deleteAlpha),
                modifier = Modifier.size(SizeUtils().textSizeToDp(density, 6))
            )
        }

        Column {
            Row(
                modifier = Modifier
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
                    //.clickable { onMenuClick() }
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = menuData.name,
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                        color = Black.copy(
                            alpha = rowContentAlpha
                        )
                    )

                    if(menuData.description.isNotEmpty()){
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = menuData.description,
                            style = storeMeTextStyle(FontWeight.Bold, 0),
                            color = MenuPriceDescriptionColor.copy(
                                alpha = rowContentAlpha
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = priceText,
                        style = storeMeTextStyle(FontWeight.ExtraBold, 0),
                        color = Black.copy(
                            alpha = rowContentAlpha
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        if(menuData.isSignature){
                            Text(
                                text = SIGNATURE.displayName,
                                style = storeMeTextStyle(FontWeight.Bold, -2),
                                modifier = Modifier
                                    .background(
                                        color = SignatureBoxColor.copy(
                                            alpha = rowContentAlpha
                                        ),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(5.dp),
                                color = SignatureTextColor.copy(
                                    alpha = rowContentAlpha
                                )
                            )
                        }

                        if(menuData.isPopular){
                            Text(
                                text = POPULAR.displayName,
                                style = storeMeTextStyle(FontWeight.Bold, -2),
                                modifier = Modifier
                                    .background(
                                        color = PopularBoxColor.copy(
                                            alpha = rowContentAlpha
                                        ),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(5.dp),
                                color = PopularTextColor.copy(
                                    alpha = rowContentAlpha
                                )
                            )
                        }

                        if(menuData.isRecommend){
                            Text(
                                text = RECOMMEND.displayName,
                                style = storeMeTextStyle(FontWeight.Bold, -2),
                                modifier = Modifier
                                    .background(
                                        color = RecommendBoxColor.copy(
                                            alpha = rowContentAlpha
                                        ),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(5.dp),
                                color = RecommendTextColor.copy(
                                    alpha = rowContentAlpha
                                )
                            )
                        }
                    }

                }

                AsyncImage(
                    model = menuData.imageUrl,
                    contentDescription = "메뉴 이미지",
                    error = painterResource(id = R.drawable.store_null_image),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .alpha(alpha = rowContentAlpha)
                )
            }

            DefaultHorizontalDivider()
        }
    }
}

@Composable
fun MenuSettingTopLayout(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val menuSettingViewModel = LocalMenuSettingViewModel.current

    val menuCategoryList by Auth.menuCategoryList.collectAsState()
    val editState by menuSettingViewModel.editState.collectAsState()

    Column {
        TopAppBar(title = {
            TitleWithDeleteButton(
                navController = navController,
                title = "메뉴 관리",
                isInTopAppBar = true
            ) },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White,
                scrolledContainerColor = White
            )
        )

        EditButtonsSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            addText = "메뉴 추가",
            onAdd = { NavigationUtils().navigateOwnerNav(navController, MainActivity.OwnerNavItem.ADD_MENU) },
            onEditCategory = { NavigationUtils().navigateOwnerNav(navController, MainActivity.OwnerNavItem.MENU_CATEGORY_SETTING) },
        )
    }
}

@Composable
fun EditButtonsSection(
    modifier: Modifier = Modifier,
    addText: String,
    onAdd: () -> Unit,
    onEditCategory: () -> Unit
) {

    Row(
        modifier = modifier,
    ) {
        LargeButton(
            text = "카테고리 관리",
            containerColor = EditButtonColor,
            contentColor = Black,
            modifier = Modifier.weight(1f)
        ) {
            onEditCategory()
        }

        Spacer(modifier = Modifier.width(10.dp))

        AddButton(
            text = addText,
            containerColor = Black,
            contentColor = White,
            modifier = Modifier.weight(1f)
        ) {
            onAdd()
        }
    }
}