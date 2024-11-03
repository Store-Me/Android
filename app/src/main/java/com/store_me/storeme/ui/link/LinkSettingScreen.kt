@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.link

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.SocialMediaAccountData
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultOutlineTextField
import com.store_me.storeme.ui.component.EditAddSection
import com.store_me.storeme.ui.component.LargeButton
import com.store_me.storeme.ui.component.SocialMediaIcon
import com.store_me.storeme.ui.component.TextFieldErrorType
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.DeleteTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.ToastMessageUtils
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

val LocalLinkSettingViewModel = staticCompositionLocalOf<LinkSettingViewModel> {
    error("No LinkSettingViewModel provided")
}

@Composable
fun LinkSettingScreen(
    navController: NavController,
    linkSettingViewModel: LinkSettingViewModel = viewModel()
) {
    val linkListData by Auth.linkListData.collectAsState()
    val list = linkListData.urlList

    val editState by linkSettingViewModel.editState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var isError by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLinkSettingViewModel provides linkSettingViewModel) {

        Scaffold(
            containerColor = White,
            topBar = {
                TitleWithDeleteButton(
                    navController = navController,
                    title = "외부링크 관리"
                )
            },
            content = { innerPadding ->
                if(showBottomSheet) {
                    var text by remember { mutableStateOf("") }

                    DefaultBottomSheet(sheetState = sheetState, onDismiss = { showBottomSheet = false }) {
                        Text(text = "링크", style = storeMeTextStyle(FontWeight.ExtraBold, 4), modifier = Modifier.padding(horizontal = 20.dp))

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            if(text.isNotEmpty()){
                                SocialMediaIcon(url = text, size = 24)

                                Spacer(modifier = Modifier.width(10.dp))
                            }

                            DefaultOutlineTextField(
                                text = text,
                                placeholderText = "링크를 입력해주세요.",
                                errorType = TextFieldErrorType.LINK,
                                onErrorChange = { isError = it },
                                onValueChange = { text = it },
                            )
                        }

                        Spacer(modifier = Modifier.height(100.dp))

                        LargeButton(
                            text = "추가",
                            enabled = !isError && text.isNotEmpty(),
                            modifier = Modifier.padding(horizontal = 20.dp),
                            containerColor = Black,
                            contentColor = White
                        ) {
                            Auth.addLinkListData(text)

                            showBottomSheet = false
                        }

                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    LinkEditButtonSection(linkListData) {
                        showBottomSheet = true
                    }

                    when(editState) {
                        true -> {
                            LinkReorderList()
                        }
                        false -> {
                            LazyColumn {
                                items(list) { url ->
                                    LinkItem(url)
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
fun LinkReorderList() {
    val view = LocalView.current
    val linkListData by Auth.linkListData.collectAsState()
    var list = linkListData.urlList

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        list = list.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }

        Auth.setLinkListData(SocialMediaAccountData(list))
        view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(list, key = { _, item -> item }) { index, url ->
            ReorderableItem(reorderableLazyListState, key = url) { isDragging ->
                val interactionSource = remember { MutableInteractionSource() }

                LinkItem(
                    url = url,
                    editable = true,
                    isDragging = isDragging,
                    onDelete = {
                        val newList = list.toMutableList().apply {
                            removeAt(index)
                        }
                        Auth.setLinkListData(SocialMediaAccountData(newList))
                    },
                    columnModifier = Modifier
                        .background(if (isDragging) Black.copy(0.2f) else White)
                        .fillMaxWidth(),
                    handelModifier = Modifier
                        .draggableHandle(
                            interactionSource = interactionSource,
                            onDragStarted = {
                                view.performHapticFeedback(HapticFeedbackConstants.DRAG_START)
                            },
                            onDragStopped = {
                                view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun LinkEditButtonSection(linkListData: SocialMediaAccountData, onAddLink:() -> Unit) {
    val linkSettingViewModel = LocalLinkSettingViewModel.current
    val editState by linkSettingViewModel.editState.collectAsState()

    val context = LocalContext.current

    fun addButtonClick() {
        if(editState) {
            ToastMessageUtils.showToast(context, R.string.alert_edit_already)
            return
        }

        if(linkListData.urlList.size > 6) {
            ToastMessageUtils.showToast(context, R.string.alert_max_link)
            return
        }

        onAddLink()
    }

    EditAddSection(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        dataListSize = linkListData.urlList.size,
        editState = editState,
        addText = "링크 추가",
        editText = "순서 편집/삭제",
        onAdd = { addButtonClick() },
        onChangeEditState = { linkSettingViewModel.setEditState(it) }
    )
}


@Composable
fun LinkItem(url: String, editable: Boolean = false, columnModifier: Modifier = Modifier, handelModifier: Modifier = Modifier, isDragging: Boolean = false, onDelete: () -> Unit = {}) {
    var showDialog by remember { mutableStateOf(false) }

    val lineColor = if(isDragging) Transparent else DefaultDividerColor

    Column(
        modifier = columnModifier
            .fillMaxWidth()
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
                SocialMediaIcon(url)

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = url, style = storeMeTextStyle(FontWeight.ExtraBold, 0))
            }

            if(editable) {
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .background(Transparent)
                            .clickable(
                                onClick = { showDialog = true },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("삭제", style = storeMeTextStyle(FontWeight.ExtraBold, 2), color = DeleteTextColor)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_drag),
                        contentDescription = "드래그 아이콘",
                        modifier = handelModifier
                            .size(24.dp),
                        tint = DeleteTextColor
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
