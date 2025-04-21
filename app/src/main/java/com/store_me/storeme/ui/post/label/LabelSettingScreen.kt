@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.post.label

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.data.LabelData
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
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun LabelSettingScreen(
    navController: NavController,
    labelSettingViewModel: LabelSettingViewModel = hiltViewModel()
) {
    val storeDateViewModel = LocalStoreDataViewModel.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current
    val loadingViewModel = LocalLoadingViewModel.current

    val originalLabels by storeDateViewModel.labels.collectAsState()
    val labels by labelSettingViewModel.labels.collectAsState()
    val isSuccess by labelSettingViewModel.isSuccess.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var showEditBottomSheet by remember { mutableStateOf<LabelData?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val hasDifference by remember {
        derivedStateOf {
            if (labels.isEmpty())
                false
            else {
                originalLabels != labels
            }
        }
    }

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

    LaunchedEffect(originalLabels) {
        //데이터 동기화
        if(labels.isEmpty())
            labelSettingViewModel.updateLabels(originalLabels)
    }

    LaunchedEffect(isSuccess) {
        if(isSuccess) {
            storeDateViewModel.updateLabels(labels)
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        topBar = {
            TitleWithDeleteButtonAndRow(
                title = "소식 라벨 관리",
                scrollBehavior = scrollBehavior,
                onClose = { onClose() }
            ) {
                SaveAndAddButton(
                    addButtonText = "라벨 추가",
                    hasDifference = hasDifference,
                    onAddClick = {
                        showAddBottomSheet = true
                    },
                    onSaveClick = {
                        loadingViewModel.showLoading()
                        labelSettingViewModel.patchLabels()
                    }
                )
            }
        },
        content = { innerPadding ->
            LabelReorderList(
                modifier = Modifier.padding(innerPadding),
                labels = labels,
                scrollBehavior = scrollBehavior,
                onMoved = { from, to ->
                    labelSettingViewModel.reorderLabels(fromIndex = from, toIndex = to)
                },
                onClick = {

                },
                onDelete = {
                    labelSettingViewModel.deleteLabel(it)
                },
                onEdit = {
                    showEditBottomSheet = it
                }
            )
        }
    )

    if(showAddBottomSheet) {
        DefaultBottomSheet (
            sheetState = sheetState,
            onDismiss = { showAddBottomSheet = false }
        ) {
            LabelManagementSection(labels = labels) {
                //추가
                labelSettingViewModel.addLabel(it)
                showAddBottomSheet = false
            }
        }
    }

    if(showEditBottomSheet != null) {
        DefaultBottomSheet(
            sheetState = sheetState,
            onDismiss = { showEditBottomSheet = null }
        ) {
            LabelManagementSection(initValue = showEditBottomSheet?.name ?: "", labels = labels) {
                //수정
                labelSettingViewModel.editLabel(label = showEditBottomSheet!!, newLabelName = it)
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
fun LabelReorderList(
    modifier: Modifier,
    labels: List<LabelData>,
    scrollBehavior: TopAppBarScrollBehavior,
    onMoved: (Int, Int) -> Unit,
    onClick: (LabelData) -> Unit,
    onDelete: (LabelData) -> Unit,
    onEdit: (LabelData) -> Unit
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
        if(labels.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .clickable {  }
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${labels[0].name} (${labels[0].postCount})",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    )
                }
            }
        }

        if(labels.size > 1) {
            itemsIndexed(labels.drop(1), key = { _, item -> item.name }) { _, label ->
                ReorderableItem(state = reorderableLazyListState, key = label.name) { isDragging ->
                    val interactionSource = remember { MutableInteractionSource() }

                    Box {
                        LabelItem(
                            modifier = Modifier
                                .combinedClickable(
                                    interactionSource = interactionSource,
                                    indication = ripple(bounded = true),
                                    onClick = { onClick(label) },
                                    onLongClick = { }
                                )
                                .longPressDraggableHandle(
                                    interactionSource = interactionSource,
                                    onDragStarted = {
                                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                    }
                                ),
                            label = label,
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
fun LabelItem(
    modifier: Modifier,
    label: LabelData,
    onDelete: (LabelData) -> Unit,
    onEdit: (LabelData) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    EditAndDeleteRow (
        modifier = modifier,
        onEdit = { onEdit(label) },
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
                text = "${label.name} (${label.postCount})",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            )
        }
    }

    if(showDialog) {
        WarningDialog (
            title = "라벨을 삭제할까요?",
            warningContent = label.name,
            content = "위의 라벨와 라벨에 포함된 ${label.postCount}개의 소식이 함께 삭제되며, 삭제 이후 복구되지않아요.",
            actionText = "삭제",
            onDismiss = {
                showDialog = false
            },
            onAction = {
                onDelete(label)
                showDialog = false
            }
        )
    }
}

@Composable
fun LabelManagementSection(initValue: String = "", labels: List<LabelData>, onFinish: (String) -> Unit) {
    var labelName by remember { mutableStateOf(initValue) }

    val lengthCondition by remember(labelName) { derivedStateOf { labelName.length > 10 } }
    //수정일 경우 자기 자신 제외
    val duplicateCondition by remember(labelName, labels) { derivedStateOf { labels.any { it.name == labelName && it.name != initValue } } }

    val isError by remember(labelName, labels) { derivedStateOf { lengthCondition || duplicateCondition } }

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "라벨" + if(initValue.isEmpty()) "" else " 수정",
            style = storeMeTextStyle(FontWeight.ExtraBold, 4)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SimpleOutLinedTextField(
            text = labelName,
            onValueChange = { labelName = it },
            placeholderText = "라벨 이름을 입력해주세요.",
            isError = isError,
            errorText = if(lengthCondition) "라벨 이름은 10자 이내로 입력해주세요." else "이미 존재하는 라벨 이름 입니다."
        )

        TextLengthRow(text = labelName, limitSize = 10)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "라벨은 기본 라벨을 포함하여 10개 까지 생성할 수 있어요.",
            style = storeMeTextStyle(FontWeight.Bold, 0),
            color = GuideColor
        )

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton (
            buttonText = "추가",
            enabled = labels.size < 10 && !isError && initValue != labelName && labelName.isNotEmpty()
        ) {
            onFinish(labelName)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}