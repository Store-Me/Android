@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.link

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.DragValue
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.LinkIcon
import com.store_me.storeme.ui.component.SaveAndAddButton
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.ErrorColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.SwipeDeleteColor
import com.store_me.storeme.ui.theme.SwipeEditColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.roundToInt

@Composable
fun LinkSettingScreen(
    navController: NavController,
    linkSettingViewModel: LinkSettingViewModel = viewModel()
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val auth = LocalAuth.current
    val loadingViewModel = LocalLoadingViewModel.current

    val originalLink by storeDataViewModel.links.collectAsState()
    val links by linkSettingViewModel.links.collectAsState()

    val focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }
    val showEditBottomSheet = remember { mutableStateOf(-1) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val hasDifference = remember { mutableStateOf(false) }

    val showBackWarningDialog = remember { mutableStateOf(false) }

    LaunchedEffect(originalLink, links) {
        if (originalLink != null) {
            hasDifference.value = originalLink != links
        }
    }

    fun onClose() {
        if(hasDifference.value)
            showBackWarningDialog.value = true
        else
            navController.popBackStack()
    }

    LaunchedEffect(originalLink) {
        linkSettingViewModel.updateLinks(originalLink ?: emptyList())
    }

    BackHandler {
        onClose()
    }

    Scaffold(
        containerColor = White,
        modifier = Modifier
            .addFocusCleaner(focusManager),
        topBar = {
            TitleWithDeleteButtonAndRow(
                title = "외부링크 관리",
                scrollBehavior = scrollBehavior,
                onClose = { onClose() }
            ) {
                SaveAndAddButton(
                    addButtonText = "링크 추가",
                    hasDifference = hasDifference.value,
                    onAddClick = {
                        //링크 추가
                        showBottomSheet.value = true
                    },
                    onSaveClick = {
                        loadingViewModel.showLoading()

                        //변경 저장
                        storeDataViewModel.patchStoreLinks(storeId = auth.storeId.value!!, links = links)
                    }
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                LinkReorderList(
                    links = links,
                    scrollBehavior = scrollBehavior,
                    onMoved = { fromIndex, toIndex ->
                        linkSettingViewModel.reorderLinks(fromIndex, toIndex)
                    },
                    onDelete = { index ->
                        linkSettingViewModel.deleteLink(index)
                    },
                   onEdit = { index ->
                       showEditBottomSheet.value = index
                   }
                )
            }
        }
    )

    if(showBottomSheet.value) {
        DefaultBottomSheet(sheetState = sheetState, onDismiss = { showBottomSheet.value = false }) {
            LinkSettingBottomSheetContent(links = links) {
                linkSettingViewModel.addLink(it)
                showBottomSheet.value = false
            }
        }
    }

    if(showEditBottomSheet.value >= 0) {
        DefaultBottomSheet(sheetState = sheetState, onDismiss = { showEditBottomSheet.value = -1 }) {
            LinkSettingBottomSheetContent(links = links, editIndex = showEditBottomSheet.value) {
                linkSettingViewModel.editLink(showEditBottomSheet.value, it)
                showEditBottomSheet.value = -1
            }
        }
    }

    if(showBackWarningDialog.value) {
        BackWarningDialog(
            onDismiss = { showBackWarningDialog.value = false },
            onAction = {
                showBackWarningDialog.value = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun LinkSettingBottomSheetContent(links: List<String>, editIndex: Int = -1, onAdd: (String) -> Unit) {
    val text = remember { mutableStateOf(if(editIndex != -1) links[editIndex] else "") }
    val errorText = remember { mutableStateOf<String?>(null) }
    val isError by remember { derivedStateOf { errorText.value != null } }

    LaunchedEffect(text.value) {
        errorText.value = when {
            links.contains(text.value) -> {
                if(editIndex == -1 || links[editIndex] != text.value)
                    "이미 존재하는 링크입니다."
                else
                    "변경사항이 없습니다."
            }
            text.value.isNotEmpty() && !text.value.startsWith("http://") && !text.value.startsWith("https://") -> {
                "링크는 https:// 혹은 http:// 로 시작해야 합니다."
            }
            else -> {
                null
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(text = "링크", style = storeMeTextStyle(FontWeight.ExtraBold, 4))

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            maxLines = 1,
            textStyle = storeMeTextStyle(FontWeight.Normal, 1),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            leadingIcon = {
                if(text.value.isNotEmpty() && !isError){
                    LinkIcon(
                        modifier = Modifier
                            .size(24.dp),
                        url = text.value
                    )
                }
            },
            trailingIcon = {
                if(text.value.isNotEmpty()) {
                    IconButton(onClick = { text.value = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_text_clear),
                            contentDescription = "삭제",
                            modifier = Modifier
                                .size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = "링크를 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 1),
                    color = GuideColor
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HighlightColor,
                errorBorderColor = ErrorColor,
                errorLabelColor = ErrorColor,
            ),
            isError = isError,
            supportingText = {
                if(isError){
                    Text(
                        text = errorText.value ?: "",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = ErrorColor
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(100.dp))

        DefaultButton(
            buttonText = "추가",
            enabled = !isError && text.value.isNotEmpty()
        ) {
            onAdd(text.value)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
fun LinkReorderList(
    links: List<String>,
    scrollBehavior: TopAppBarScrollBehavior,
    onMoved: (Int, Int) -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit
) {
    val view = LocalView.current

    val lazyListState = rememberLazyListState()
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
        itemsIndexed(links, key = { _, item -> item }) { index, url ->
            ReorderableItem(state = reorderableLazyListState, key = url) { isDragging ->
                val interactionSource = remember { MutableInteractionSource() }

                Box {
                    LinkItem(
                        url = url,
                        isDragging = isDragging,
                        onDelete = { onDelete(index) },
                        onEdit = { onEdit(index) },
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
                            )
                    )

                    if(isDragging)
                        Canvas(modifier = Modifier.matchParentSize()) {
                            drawRect(color = White.copy(alpha = 0.7f))
                        }
                }
            }
        }
    }
}

@Composable
fun LinkItem(
    modifier: Modifier = Modifier,
    url: String,
    isDragging: Boolean = false,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidth = with(density) { (configuration.screenWidthDp.dp).toPx() }
    val halfScreen = with(density) { (configuration.screenWidthDp.dp / 2).toPx() } // 절반 크기 계산

    val oldValue = remember { mutableStateOf(DragValue.Center) }

    var showDialog by remember { mutableStateOf(false) }

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
                if(newValue != oldValue.value) {
                    oldValue.value = newValue

                    when(newValue) {
                        DragValue.End -> {  }
                        DragValue.Start -> {  }
                        DragValue.Center -> {  }
                    }
                }
                true
            }
        )
    }

    LaunchedEffect(state.settledValue) {
        when (state.currentValue) {
            DragValue.End -> {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                onEdit()
                state.snapTo(DragValue.Center)
            }
            DragValue.Start -> {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                showDialog = true
                state.snapTo(DragValue.Center)
            }
            else -> {  }
        }
    }

    val editAlpha = (state.progress(DragValue.Center, DragValue.End) * 2).coerceIn(0f, 1f)
    val deleteAlpha = (state.progress(DragValue.Center, DragValue.Start) * 2).coerceIn(0f, 1f)

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
                modifier = Modifier.size(SizeUtils.textSizeToDp(density, 6))
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                modifier = Modifier.size(SizeUtils.textSizeToDp(density, 6))
            )
        }

        Column(
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
                .background(White)
        ) {
            Spacer(modifier= Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinkIcon(
                        modifier = Modifier
                            .size(40.dp),
                        url
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = url, style = storeMeTextStyle(FontWeight.ExtraBold, 0))
                }
            }

            if(!isDragging) {
                HorizontalDivider(
                    color = DefaultDividerColor,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 20.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (showDialog) {
        WarningDialog(
            title = "링크를 삭제할까요?",
            warningContent = url,
            content = "위의 링크가 삭제되며, 삭제 이후 복구되지않아요.",
            actionText = "삭제",
            onDismiss = {
                showDialog = false
            },
            onAction = {
                onDelete()
                showDialog = false
            }
        )
    }
}
