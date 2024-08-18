package com.store_me.storeme.ui.post

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.store_me.storeme.R
import com.store_me.storeme.data.PostType
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.post.AddPostViewModel.*
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.ToastMessageUtils
import dev.jeziellago.compose.markdowntext.MarkdownText

val LocalAddPostViewModel = staticCompositionLocalOf<AddPostViewModel> {
    error("No AddPostViewModel provided")
}

@Composable
fun AddPostScreen(postType: PostType) {
    when(postType) {
        PostType.NORMAL -> { AddNormalPostScreen() }
        else -> { AddNormalPostScreen() }
    }
}

@Composable
fun AddNormalPostScreen(addPostViewModel: AddPostViewModel = viewModel()) {
    val currentEditState by addPostViewModel.currentEditState.collectAsState()
    val previewVisible by addPostViewModel.previewVisible.collectAsState()

    CompositionLocalProvider(LocalAddPostViewModel provides addPostViewModel) {
        Scaffold(
            containerColor = Color.White,
            topBar = { /*TODO*/ },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item { PreviewButtonSection() }
                    if(previewVisible) {
                        item{ PreviewSection() }
                    } else {
                        item { ItemListSection() }
                        item { AllBlockSection() }
                        item { EditSection(currentEditState) }
                    }
                }
            }
        )
    }
}

@Composable
fun PreviewSection() {
    val addPostViewModel = LocalAddPostViewModel.current

    MarkdownText(
        modifier = Modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = { }
            )
            .fillMaxWidth(),
        markdown = addPostViewModel.totalContent.value.ifEmpty { "내용이 없습니다." }
    )
}

@Composable
fun PreviewButtonSection() {
    val addPostViewModel = LocalAddPostViewModel.current

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(text = "미리보기") {
            addPostViewModel.togglePreview()
        }
    }
}

@Composable
fun AllBlockSection() {
    val addPostViewModel = LocalAddPostViewModel.current
    val blockContents by addPostViewModel.blockContents.collectAsState()

    val groupedBlocks = remember(blockContents) {
        mutableListOf<List<BlockContent>>().apply {
            val currentRowBlocks = mutableListOf<BlockContent>()

            // 블록들 그룹화
            blockContents.forEach { blockContent ->
                when {
                    blockContent is BlockContent.TextContent && !blockContent.hasMultipleLine -> {
                        currentRowBlocks.add(blockContent)
                    }
                    blockContent is BlockContent.TextContent && blockContent.hasMultipleLine -> {
                        currentRowBlocks.add(blockContent)

                        add(currentRowBlocks.toList())
                        currentRowBlocks.clear()
                    }
                    else -> {
                        if(currentRowBlocks.isNotEmpty()) {
                            add(currentRowBlocks.toList())
                            currentRowBlocks.clear()
                        }
                        add(listOf(blockContent))
                    }
                }
            }

            // 마지막 그룹 추가
            if (currentRowBlocks.isNotEmpty()) {
                add(currentRowBlocks.toList())
            }
        }
    }

    Column {
        groupedBlocks.forEach { group ->
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                group.forEach { blockContent ->
                    BlockItem(blockContent, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun EditSection(currentEditState: LayoutItem?) {
    val addPostViewModel = LocalAddPostViewModel.current

    when (currentEditState) {
        LayoutItem.TEXT -> {
            Text(text = "미리보기", style = storeMeTypography.labelLarge)

            TextStyleSection()
            EditTextSection()
        }

        LayoutItem.IMAGE -> {

        }

        LayoutItem.DIVIDER -> {
            addPostViewModel.addContent()
        }

        null -> {

        }
    }
}

@Composable
fun TextStyleSection() {
    val addPostViewModel = LocalAddPostViewModel.current
    val selectedTextStyleItems by addPostViewModel.selectedTextStyleItems.collectAsState()

    LazyRow(
        contentPadding = PaddingValues(start = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 20.dp)
    ) {

        items(TextStyleItem.entries) { item ->
            val text = if(item in selectedTextStyleItems) {
                item.displayName + "선택됨"
            } else {
                item.displayName
            }

            DefaultButton(text = text) {
                addPostViewModel.setTextStyle(item)
            }
        }
    }
}

@Composable
fun EditTextSection() {
    val focusManager = LocalFocusManager.current
    val addPostViewModel = LocalAddPostViewModel.current

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                focusManager.clearFocus()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // 마크다운 미리보기
        MarkdownText(
            modifier = Modifier
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { }
                )
                .fillMaxWidth(),
            markdown = if(text.isNotEmpty()) addPostViewModel.currentText(text) else "내용이 없습니다."
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 텍스트 입력 필드
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = {  },
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaultButton(text = "추가") {
                    addPostViewModel.addContent(text)
                }

                Spacer(modifier = Modifier.height(10.dp))

                DefaultButton(text = "삭제") {

                }
            }
        }

    }
}

@Composable
fun ItemListSection() {
    val addPostViewModel = LocalAddPostViewModel.current
    val context = LocalContext.current

    LazyRow(
        contentPadding = PaddingValues(start = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 20.dp)
    ) {
        items(LayoutItem.entries) { item ->
            DefaultButton(text = item.displayName) {
                layoutButtonHandler(item, addPostViewModel, context)
            }
        }
    }

}

fun layoutButtonHandler(layoutItem: LayoutItem, addPostViewModel: AddPostViewModel, context: Context) {
    if(addPostViewModel.currentEditState.value == null) {
        addPostViewModel.setCurrentEditState(layoutItem)
    } else {
        ToastMessageUtils.showToast(context, R.string.fail_change_edit_state)
    }
}

@Composable
fun BlockItem(blockContent: BlockContent, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .height(30.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.CenterStart,
    ) {

        when(blockContent) {
            is BlockContent.TextContent -> TextContentBox(text = blockContent.text)
            is BlockContent.ImageContent -> ImageContentBox(imageUrl = blockContent.imageUrl)
            is BlockContent.DividerContent -> DividerContentBox()
        }
    }
}

@Composable
fun DividerContentBox() {
    Text(text = "구분선")
}

@Composable
fun TextContentBox(text: String) {
    Text(text = "T $text", maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
fun ImageContentBox(imageUrl: String) {

}