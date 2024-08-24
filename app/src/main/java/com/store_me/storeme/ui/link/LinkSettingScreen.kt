@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.link

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.SocialMediaAccountData
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultFinishButton
import com.store_me.storeme.ui.component.DefaultOutlineTextField
import com.store_me.storeme.ui.component.SocialMediaIcon
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.DeleteTextColor
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.SaveButtonColor
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
    val editState by linkSettingViewModel.editState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

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
                    val focusManager = LocalFocusManager.current

                    DefaultBottomSheet(sheetState = sheetState, onDismiss = { showBottomSheet = false }) {
                        Text(text = "링크", style = storeMeTextStyle(FontWeight.ExtraBold, 2), modifier = Modifier.padding(horizontal = 20.dp))

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
                                focusManager = focusManager,
                                onValueChange = { text = it },
                            )
                        }



                        Spacer(modifier = Modifier.height(100.dp))

                        DefaultFinishButton(text = "추가", modifier = Modifier.padding(horizontal = 20.dp)) {
                            //TODO ADD
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

                    if (linkListData != null) {
                        when(editState) {
                            true -> {
                                LinkReorderList()
                            }
                            false -> {
                                LazyColumn {
                                    itemsIndexed(Auth.linkListData.value!!.urlList) { index, url ->

                                        LinkItem(url) {  }
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
fun LinkReorderList() {
    val view = LocalView.current
    val linkListData by Auth.linkListData.collectAsState()
    var list = linkListData?.urlList ?: emptyList()

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
                    modifier = Modifier
                        .background(if (isDragging) Black.copy(0.2f) else White)
                        .fillMaxWidth()
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

    /*var items by remember { mutableStateOf(Auth.linkListData.value?.urlList ?: listOf()) }

    val state = rememberReorderableLazyListState(onMove = { from, to ->
        items = items.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }

        Auth.setLinkListData(socialMediaAccountData = SocialMediaAccountData(items))
    })

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        itemsIndexed(items) { index, item ->
            ReorderableItem(reorderableState = state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                val color = if(isDragging) Black.copy(0.3f) else White

                Column(
                    Modifier
                        .shadow(elevation.value)
                        .background(color)
                ) {
                    if (index == 0) {
                        HorizontalDivider(
                            color = DefaultDividerColor,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                    }

                    LinkItem(url = item, editable = true) {  }

                    HorizontalDivider(
                        color = DefaultDividerColor,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            }
        }
    }*/
}

@Composable
fun LinkEditButtonSection(linkListData: SocialMediaAccountData?, onAddLink:() -> Unit) {
    val linkSettingViewModel = LocalLinkSettingViewModel.current
    val editState by linkSettingViewModel.editState.collectAsState()

    val context = LocalContext.current

    @Composable
    fun SaveButton(modifier: Modifier = Modifier,onClick: () -> Unit) {
        Button(
            modifier = modifier
                .height(50.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SaveButtonColor,
                contentColor = White
            ),
            onClick = onClick,
        ) {
            Text(text = "저장", style = storeMeTextStyle(FontWeight.ExtraBold, 2))
        }
    }

    @Composable
    fun EditLinkButton(modifier: Modifier = Modifier,onClick: () -> Unit) {
        Button(
            modifier = modifier
                .height(50.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EditButtonColor,
                contentColor = Black
            ),
            onClick = onClick,
        ) {
            Text(text = "순서 편집/삭제", style = storeMeTextStyle(FontWeight.ExtraBold, 2))
        }
    }

    fun addButtonClick() {
        if(editState) {
            ToastMessageUtils.showToast(context, R.string.alert_edit_already)
            return
        }

        if((linkListData?.urlList?.size ?: 0) > 6) {
            ToastMessageUtils.showToast(context, R.string.alert_max_link)
            return
        }

        onAddLink()
    }

    @Composable
    fun AddLinkButton(modifier: Modifier = Modifier) {
        Button(
            modifier = modifier
                .height(50.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Black,
                contentColor = White
            ),
            onClick = { addButtonClick() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_circle_plus),
                contentDescription = "링크 추가 아이콘",
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape),
                tint = White
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(text = "링크 추가", style = storeMeTextStyle(FontWeight.ExtraBold, 2))
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
    ) {

        when(linkListData) {
            null -> {
                AddLinkButton(modifier = Modifier.weight(1f))
            }
            else -> {
                when(linkListData.urlList.size) {
                    0 -> {
                        AddLinkButton(modifier = Modifier.weight(1f))
                    }
                    else -> {
                        when(editState) {
                            true -> {
                                SaveButton(modifier = Modifier.weight(1f)) {
                                    linkSettingViewModel.setEditState(false)
                                }
                            }
                            false -> {
                                EditLinkButton(modifier = Modifier.weight(1f)) {
                                    linkSettingViewModel.setEditState(true)
                                }
                            }
                        }


                        Spacer(modifier = Modifier.width(10.dp))

                        AddLinkButton(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}


@Composable
fun LinkItem(url: String, editable: Boolean = false, modifier: Modifier = Modifier, isDragging: Boolean = false, onDelete: (Int) -> Unit) {
    val lineColor = if(isDragging) Transparent else DefaultDividerColor

    Column(
        modifier = modifier
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
                                onClick = { },
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
                        modifier = Modifier
                            .size(24.dp)
                            .draggableHandle(),
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
}

@Composable
fun Modifier.draggableHandle(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onDragStarted: (() -> Unit)? = null,
    onDragStopped: (() -> Unit)? = null
): Modifier = this.then(
    Modifier.semantics {
        clearAndSetSemantics { }
    }
)