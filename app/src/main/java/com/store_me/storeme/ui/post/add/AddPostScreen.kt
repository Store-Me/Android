@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)

package com.store_me.storeme.ui.post.add

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.store_me.storeme.R
import com.store_me.storeme.data.LabelData
import com.store_me.storeme.data.PostContentBlock
import com.store_me.storeme.data.enums.ToolbarItems
import com.store_me.storeme.data.enums.post.PostType
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.KeyboardToolbar
import com.store_me.storeme.ui.component.LoadingProgress
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.post.AddPostViewModel
import com.store_me.storeme.ui.post.LabelViewModel
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.PermissionUtils
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState

private const val LazyListHeaderCount = 3

@Composable
fun AddPostScreen(
    labelViewModel: LabelViewModel = hiltViewModel(),
    addPostViewModel: AddPostViewModel = hiltViewModel(),
    toolbarViewModel: ToolbarViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val view = LocalView.current

    val snackbarHostState = LocalSnackbarHostState.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            toolbarViewModel.fetchGalleryImages()
        } else {
            coroutine.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "권한이 허용되지 않아 이미지를 불러오지 못했습니다.",
                    actionLabel = "설정으로"
                )
                if (result == SnackbarResult.ActionPerformed) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.isImeVisible

    val title by addPostViewModel.title.collectAsState()

    val labels by labelViewModel.labels.collectAsState()
    val selectedLabel by labelViewModel.selectedLabel.collectAsState()

    val keyboardHeight by toolbarViewModel.keyboardHeight.collectAsState()
    val selectedToolbarItem by toolbarViewModel.selectedToolbarItem.collectAsState()
    val selectedTextStyleItem by toolbarViewModel.selectedTextStyleItem.collectAsState()
    val galleryImages by toolbarViewModel.galleryImages.collectAsState()

    val content by addPostViewModel.content.collectAsState()
    var focusedIndex by remember { mutableIntStateOf(0) }
    var addedIndex by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current

    val listState = rememberLazyListState()

    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState = listState) { from, to ->
        // 헤더 개수만큼 인덱스 보정
        val fromIndex = from.index - LazyListHeaderCount
        val toIndex = to.index - LazyListHeaderCount

        addPostViewModel.reorderBlock(fromIndex, toIndex)

        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    val keyboardHeightDp = with(density) { keyboardHeight.toDp() }
    var lastSelectedToolbarItems by remember { mutableStateOf<ToolbarItems?>(null) }

    val isSuccess by addPostViewModel.isSuccess.collectAsState()

    BackHandler {
        if(selectedToolbarItem != null) {
            toolbarViewModel.updateSelectedToolbarItem(null)
        } else if(selectedTextStyleItem != null) {
            toolbarViewModel.updateSelectedTextStyleItem(null)
        } else {
            (context as Activity).finish()
        }
    }

    LaunchedEffect(Unit) {
        labelViewModel.getLabels()
    }

    LaunchedEffect(isSuccess) {
        if(isSuccess) {
            (context as Activity).finish()
        }
    }

    LaunchedEffect(imeVisible) {
        if ((selectedToolbarItem == ToolbarItems.IMAGE || selectedToolbarItem == ToolbarItems.EMOJI) && imeVisible) {
            toolbarViewModel.updateSelectedToolbarItem(null)
        }
    }

    LaunchedEffect(selectedToolbarItem) {
        if (
            (lastSelectedToolbarItems == ToolbarItems.IMAGE || lastSelectedToolbarItems == ToolbarItems.EMOJI)
            && (selectedToolbarItem == ToolbarItems.TEXT_STYLE || selectedToolbarItem == ToolbarItems.ALIGN)
        ) {
            keyboardController?.show()
        }

        lastSelectedToolbarItems = selectedToolbarItem
    }

    LaunchedEffect(addedIndex) {
        delay(100)
        listState.animateScrollToItem(addedIndex + LazyListHeaderCount, scrollOffset = - keyboardHeight)
    }

    Scaffold(
        modifier = Modifier
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { StoreMeSnackbar(snackbarData = it) }
        ) },
        topBar = {
            AddPostTopBar(
                onClose = {  },
                onFinish = { addPostViewModel.createPost(postType = PostType.NORMAL, labelId = selectedLabel?.labelId) }
            ) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    state = listState
                ) {
                    item {
                        PostLabelSection(
                            labels = labels,
                            selectedLabel = selectedLabel,
                            onSelected = { labelViewModel.updateSelectedLabel(it) }
                        )
                    }

                    item {
                        EditNormalPostTitle(
                            title = title,
                            onValueChange = { addPostViewModel.updateTitle(it) }
                        )
                    }

                    item {
                        DefaultHorizontalDivider()
                    }

                    itemsIndexed(content, key = { _, item -> item.id }) { index, item ->
                        Spacer(modifier = Modifier.height(20.dp))

                        when(item) {
                            is PostContentBlock.TextBlock -> {
                                TextBlockContent(
                                    state = item.state,
                                    modifier = Modifier
                                        .wrapContentHeight()
                                ) {
                                    //포커스 변경 시 인덱스 변경
                                    focusedIndex = index
                                }
                            }
                            is PostContentBlock.ImageBlock -> {
                                ReorderableImageBlockContent(
                                    lazyItemScope = this,
                                    item = item,
                                    reorderableLazyListState = reorderableLazyListState,
                                    onDelete = {
                                        focusedIndex = addPostViewModel.removeBlockAt(index, focusedIndex = focusedIndex)
                                    }
                                )
                            }
                            is PostContentBlock.EmojiBlock -> {

                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                KeyboardToolbar(
                    state = (content.getOrNull(focusedIndex) as? PostContentBlock.TextBlock)?.state ?: rememberRichTextState(),
                    keyboardHeight = keyboardHeightDp,
                    selectedToolbarItem = selectedToolbarItem,
                    selectedTextStyleItem = selectedTextStyleItem,
                    isKeyboardOpen = imeVisible,
                    images = galleryImages,
                    onToolbarItemClick = {
                        toolbarViewModel.updateSelectedToolbarItem(item = it)

                        when {
                            it == ToolbarItems.IMAGE -> {
                                val imagePermission = PermissionUtils.imagePermission()

                                val hasPermission = ContextCompat.checkSelfPermission(context, imagePermission) ==
                                        PackageManager.PERMISSION_GRANTED

                                if (hasPermission) {
                                    toolbarViewModel.fetchGalleryImages()
                                } else {
                                    permissionLauncher.launch(imagePermission)
                                }

                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                            it == ToolbarItems.EMOJI -> {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        }
                    },
                    onTextStyleItemClick = { toolbarViewModel.updateSelectedTextStyleItem(item = it) },
                    onImagePick = {
                        val insertResult = addPostViewModel.insertImage(uri = it, focusedIndex = focusedIndex)
                        addPostViewModel.uploadImage(uri = it)

                        focusedIndex = insertResult.first
                        addedIndex = insertResult.second
                    }
                )
            }
        }
    )
}

@Composable
fun AddPostTopBar(onClose: () -> Unit, onFinish: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(
            text = "추가하기",
            style = storeMeTextStyle(FontWeight.ExtraBold, 6)
        ) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        navigationIcon = { IconButton(onClick = { onClose() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "닫기",
                modifier = Modifier
                    .size(24.dp)
            )
        } },
        actions = { TextButton(
            onClick = { onFinish() },
            interactionSource = remember { MutableInteractionSource() },
            content = { Text(
                text = "완료",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            ) }
        ) },
    )
}

@Composable
fun EditNormalPostTitle(
    title: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = title,
        onValueChange = { onValueChange(it) },
        textStyle = storeMeTextStyle(FontWeight.Bold, 4),
        placeholder = {
            Text(
                text = "소식의 제목을 입력하세요.",
                style = storeMeTextStyle(FontWeight.Bold, 4),
                color = UndefinedTextColor
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}

@Composable
fun PostLabelSection(
    labels: List<LabelData>,
    selectedLabel: LabelData?,
    onSelected: (LabelData) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultHorizontalDivider()

        Row(
            modifier = Modifier
                .clickable { showBottomSheet = true }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "라벨",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = selectedLabel?.name ?: "라벨을 선택해주세요.",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = GuideColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "화살표 아이콘",
                modifier = Modifier
                    .size(18.dp),
                tint = GuideColor
            )
        }

        DefaultHorizontalDivider()
    }

    if(showBottomSheet) {
        DefaultBottomSheet(onDismiss = { showBottomSheet = false }, sheetState = sheetState) {
            SelectLabelBottomSheetContent(
                labels = labels,
                selectedLabel = selectedLabel
            ) {
                onSelected(it)
                showBottomSheet = false
            }
        }
    }
}

@Composable
fun SelectLabelBottomSheetContent(labels: List<LabelData>, selectedLabel: LabelData?, onSelected: (LabelData) -> Unit) {
    var selected by remember { mutableStateOf(selectedLabel) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(labels) {
                val isSelected by remember { derivedStateOf {
                    selected == it
                } }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selected = it }
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${it.name} (${it.postCount})",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = if(!isSelected) R.drawable.ic_check_off else R.drawable.ic_check_on),
                        contentDescription = "체크 아이콘",
                        modifier = Modifier
                            .size(24.dp),
                        tint = if(!isSelected) GuideColor else HighlightColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton(
            buttonText = "저장",
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            selected?.let { onSelected(it) }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun TextBlockContent(
    state: RichTextState,
    modifier: Modifier,
    isFocused: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        RichTextEditor(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 16.dp)
                .onFocusChanged { isFocused(it.isFocused) },
            contentPadding = PaddingValues(horizontal = 20.dp),
            state = state,
            textStyle = storeMeTextStyle(FontWeight.Normal, 0),
            singleLine = false,
            colors = RichTextEditorDefaults.richTextEditorColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
            placeholder = {
                Text(
                    text = "내용을 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    color = GuideColor,
                    textAlign = state.currentParagraphStyle.textAlign,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

@Composable
fun ReorderableImageBlockContent(
    lazyItemScope: LazyItemScope,
    reorderableLazyListState: ReorderableLazyListState,
    onDelete: () -> Unit,
    item: PostContentBlock.ImageBlock
) {
    val view = LocalView.current

    var showDialog by remember { mutableStateOf(false) }

    with(lazyItemScope) {
        ReorderableItem(
            state = reorderableLazyListState,
            key = item.id,
        ) { isDragging ->
            val interactionSource = remember { MutableInteractionSource() }
            val indication = if (isDragging) null else ripple(bounded = true)


            ImageBlockContent(
                modifier = Modifier
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = indication,
                        onClick = { showDialog = true },
                        onLongClick = { }
                    )
                    .longPressDraggableHandle(
                        interactionSource = interactionSource,
                        onDragStarted = {
                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        }
                    ),
                isDragging = isDragging,
                item = item,
            )
        }
    }

    if (showDialog) {
        WarningDialog(
            title = "이미지를 삭제할까요?",
            content = "이미지가 삭제되며, 삭제 이후 복구되지않아요.",
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

@Composable
fun ImageBlockContent(
    modifier: Modifier,
    isDragging: Boolean,
    item: PostContentBlock.ImageBlock
) {
    val scale by animateFloatAsState(if (isDragging) 0.5f else 1f)

    Box(
        modifier = modifier
            .scale(scale)
    ) {
        AsyncImage(
            model = item.uri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        if(item.url.isEmpty()) {
            LoadingProgress(
                progress = item.progress,
                modifier = Modifier
                    .matchParentSize()
            )
        }

        if(isDragging)
            Canvas(modifier = Modifier
                .matchParentSize()
            ) {
                drawRect(color = Color.White.copy(alpha = 0.7f))
            }
    }
}