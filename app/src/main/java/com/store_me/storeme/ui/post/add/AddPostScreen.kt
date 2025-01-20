@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.store_me.storeme.ui.post.add

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.PostType
import com.store_me.storeme.data.enums.TextColors
import com.store_me.storeme.data.enums.TextSizeOptions
import com.store_me.storeme.data.enums.TextStyleOptions
import com.store_me.storeme.data.enums.ToolbarItems
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.theme.SecondaryToolbarBackgroundColor
import com.store_me.storeme.ui.theme.SelectedToolbarItemColor
import com.store_me.storeme.ui.theme.SelectedToolbarTextStyleBackgroundColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils
import com.store_me.storeme.utils.composition_locals.post.LocalAddPostViewModel
import timber.log.Timber

@Composable
fun AddPostScreen(
    postType: PostType,
    addPostViewModel: AddPostViewModel = hiltViewModel()
) {
    val focusManage = LocalFocusManager.current

    val imeVisible = WindowInsets.isImeVisible

    val selectedLabel by addPostViewModel.selectedLabel.collectAsState()

    val title by addPostViewModel.title.collectAsState()

    val richTextState = rememberRichTextState()

    CompositionLocalProvider(
        LocalAddPostViewModel provides addPostViewModel
    ) {
        Scaffold(
            modifier = Modifier
                .addFocusCleaner(focusManage),
            containerColor = Color.White,
            topBar = {
                AddPostTopBar(
                    canFinish = false,
                    onClose = {  },
                    onFinish = {  }
                ) },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    PostLabelSection(selectedLabel)

                    EditNormalPostTitle(
                        title = title,
                        onValueChange = { addPostViewModel.updateTitle(it) }
                    )

                    DefaultHorizontalDivider()

                    EditNormalPostContent(
                        state = richTextState,
                        modifier = Modifier.weight(1f)
                    )

                    KeyboardToolbar(state = richTextState, isKeyboardOpen = imeVisible)
                }
            }
        )
    }
}

@Composable
fun AddPostTopBar(canFinish: Boolean, onClose: () -> Unit, onFinish: () -> Unit) {
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
            enabled = canFinish,
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
fun PostLabelSection(label: String) {
    val density = LocalDensity.current

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultHorizontalDivider()

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "라벨(선택)",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = label,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = UndefinedTextColor
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "화살표 아이콘",
                modifier = Modifier
                    .size(SizeUtils.textSizeToDp(density, diffValue = 2)),
                tint = UndefinedTextColor
            )
        }

        DefaultHorizontalDivider()
    }
}

@Composable
fun EditNormalPostContent(
    state: RichTextState,
    modifier: Modifier
) {
    val addPostViewModel = LocalAddPostViewModel.current
    val textAlign by addPostViewModel.textAlign.collectAsState()

    Column(
        modifier = modifier
    ) {
        RichTextEditor(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
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
                    text = "손님들에게 알리고 싶은 소식을 작성해보세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    color = UndefinedTextColor,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

}

@Composable
fun KeyboardToolbar(
    state: RichTextState,
    isKeyboardOpen: Boolean,
) {
    val addPostViewModel = LocalAddPostViewModel.current

    val keyboardHeight by addPostViewModel.keyboardHeight.collectAsState()

    val selectedToolbarItem by addPostViewModel.selectedToolbarItem.collectAsState()

    val textAlign by addPostViewModel.textAlign.collectAsState()

    val textAlignPainterResource = when(textAlign) {
        TextAlign.Left -> { R.drawable.ic_left_align }
        TextAlign.Center -> { R.drawable.ic_center_align }
        TextAlign.Right -> { R.drawable.ic_right_align }
        else -> { R.drawable.ic_left_align }
    }

    val textColor by addPostViewModel.textColor.collectAsState()

    LaunchedEffect(state.currentParagraphStyle.textAlign) {
        val newTextAlign = state.currentParagraphStyle.textAlign
        if (newTextAlign != textAlign) {
            addPostViewModel.updateTextAlign(newTextAlign)
        }
    }

    LaunchedEffect(textAlign) {
        if (state.currentParagraphStyle.textAlign != textAlign) {
            val paragraphStyle = ParagraphStyle(textAlign = textAlign)
            state.toggleParagraphStyle(paragraphStyle = paragraphStyle)
        }
    }

    LaunchedEffect(textColor) {
        state.toggleSpanStyle(
            spanStyle = SpanStyle(color = textColor.color)
        )
    }

    val items = listOf(
        ToolbarItems.IMAGE to R.drawable.ic_image,
        ToolbarItems.ALIGN to textAlignPainterResource,
        ToolbarItems.DIVIDER to R.drawable.ic_line,
        ToolbarItems.TEXT_STYLE to R.drawable.ic_text,
        ToolbarItems.EMOJI to R.drawable.ic_emoji
    )

    Column {
        SecondaryToolbar()

        TextColorToolbar()

        DefaultHorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            AnimatedVisibility(
                visible = selectedToolbarItem == ToolbarItems.IMAGE
            ) {
                ToolbarTitleText("이미지")
            }

            AnimatedVisibility(
                visible = selectedToolbarItem == ToolbarItems.ALIGN
            ) {
                TextAlignItems(textAlign = textAlign) {
                    addPostViewModel.updateTextAlign(it)
                }
            }

            AnimatedVisibility(
                visible = selectedToolbarItem == ToolbarItems.TEXT_STYLE
            ) {
                TextStyleItems(state = state)
            }

            AnimatedVisibility(
                visible = selectedToolbarItem == ToolbarItems.EMOJI
            ) {
                ToolbarTitleText("이모티콘")
            }

            LazyRow(
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(items) { index, (item, painterResource) ->
                    val isSelected = selectedToolbarItem == item
                    val isVisible = selectedToolbarItem == null

                    val offsetX by animateDpAsState(
                        targetValue = when {
                            isSelected -> 0.dp
                            selectedToolbarItem != null -> {
                                if (index < items.indexOfFirst { it.first == selectedToolbarItem }) (-200).dp
                                else 200.dp
                            }
                            else -> 0.dp
                        },
                        label = ""
                    )

                    val alpha by animateFloatAsState(
                        targetValue = if (isVisible) 1f else 0f,
                        label = "AlphaAnimation"
                    )


                    IconButton(
                        onClick = {
                            addPostViewModel.updateSelectedToolbarItem(item)
                        },
                        enabled = selectedToolbarItem == null,
                        modifier = Modifier
                            .offset { IntOffset(offsetX.value.toInt(), 0) }
                            .alpha(alpha)
                    ) {
                        Icon(
                            painter = painterResource(id = painterResource),
                            contentDescription = item.name,
                            modifier = Modifier.size(20.dp),
                            tint = if (isSelected) SelectedToolbarItemColor else Color.Black
                        )
                    }

                }
            }

            AnimatedVisibility(
                enter = fadeIn(),
                exit = fadeOut(),
                visible = selectedToolbarItem != null
            ) {
                IconButton(
                    onClick = {
                        addPostViewModel.updateSelectedToolbarItem(null)
                        addPostViewModel.updateSelectedTextStyleItem(null)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "닫기",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }



        AnimatedVisibility(
            visible = selectedToolbarItem != null || isKeyboardOpen,
        ) {
            when (selectedToolbarItem) {
                ToolbarItems.EMOJI -> {
                    SelectEmojiSection()
                }
                ToolbarItems.IMAGE -> {
                    SelectImageSection()
                }
                else -> {
                    if (isKeyboardOpen) {
                        Spacer(modifier = Modifier.height(with(LocalDensity.current) { keyboardHeight.toDp() }))
                    }
                }
            }
        }

    }
}

@Composable
fun SelectEmojiSection() {
    val addPostViewModel = LocalAddPostViewModel.current
    val keyboardHeight by addPostViewModel.keyboardHeight.collectAsState()

    Column(
        modifier = Modifier
            .height(with(LocalDensity.current) { keyboardHeight.toDp() })
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "준비중인 기능입니다.",
            style = storeMeTextStyle(FontWeight.Bold, 4),
            color = UndefinedTextColor
        )
    }
}

@Composable
fun SelectImageSection() {
    val addPostViewModel = LocalAddPostViewModel.current
    val keyboardHeight by addPostViewModel.keyboardHeight.collectAsState()

    Column(
        modifier = Modifier
            .height(with(LocalDensity.current) { keyboardHeight.toDp() })
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "준비중인 기능입니다.",
            style = storeMeTextStyle(FontWeight.Bold, 4),
            color = UndefinedTextColor
        )
    }
}

@Composable
fun TextAlignItems(textAlign: TextAlign, onClick: (TextAlign) -> Unit) {
    Row {
        IconButton(
            onClick = { onClick(TextAlign.Left) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_align),
                contentDescription = "왼쪽 정렬",
                modifier = Modifier.size(20.dp),
                tint = if (textAlign == TextAlign.Left) SelectedToolbarItemColor else Color.Black
            )
        }

        IconButton(
            onClick = { onClick(TextAlign.Center) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_center_align),
                contentDescription = "중앙 정렬",
                modifier = Modifier.size(20.dp),
                tint = if (textAlign == TextAlign.Center) SelectedToolbarItemColor else Color.Black
            )
        }

        IconButton(
            onClick = { onClick(TextAlign.Right) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_right_align),
                contentDescription = "오른쪽 정렬",
                modifier = Modifier.size(20.dp),
                tint = if (textAlign == TextAlign.Right) SelectedToolbarItemColor else Color.Black
            )
        }

    }
}

@Composable
fun ToolbarTitleText(text: String) {
    Column(
        modifier = Modifier
            .height(48.dp)
            .padding(start = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = storeMeTextStyle(FontWeight.Bold, 2),
            color = Color.Black,
        )
    }
}

@Composable
fun TextStyleItems(state: RichTextState) {
    val addPostViewModel = LocalAddPostViewModel.current

    val selectedTextStyleItem by addPostViewModel.selectedTextStyleItem.collectAsState()

    val textSize by addPostViewModel.textSize.collectAsState()
    val textIsBold by addPostViewModel.textIsBold.collectAsState()
    val textIsItalic by addPostViewModel.textIsItalic.collectAsState()
    val textHasUnderLine by addPostViewModel.textHasUnderLine.collectAsState()
    val textColor by addPostViewModel.textColor.collectAsState()

    LaunchedEffect(textSize) {
        state.toggleSpanStyle(
            spanStyle = SpanStyle(fontSize = textSize.spSize)
        )
    }

    LaunchedEffect(textIsBold) {
        state.toggleSpanStyle(
            spanStyle = SpanStyle(fontWeight = if (textIsBold) FontWeight.Bold else FontWeight.Normal)
        )
    }

    LaunchedEffect(textIsItalic) {
        state.toggleSpanStyle(
            spanStyle = SpanStyle(fontStyle = if (textIsItalic) FontStyle.Italic else FontStyle.Normal)
        )
    }

    LaunchedEffect(textHasUnderLine) {
        if (textHasUnderLine) {
            state.toggleSpanStyle(
                spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)
            )
        } else {
            state.removeSpanStyle(
                spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }

    LaunchedEffect(state.currentSpanStyle.fontSize) {
        when(state.currentSpanStyle.fontSize) {
            TextSizeOptions.SMALL.spSize -> {
                if(textSize != TextSizeOptions.SMALL) {
                    addPostViewModel.updateTextSize(TextSizeOptions.SMALL)
                }
            }
            TextSizeOptions.REGULAR.spSize -> {
                if(textSize != TextSizeOptions.REGULAR) {
                    addPostViewModel.updateTextSize(TextSizeOptions.REGULAR)
                }
            }
            TextSizeOptions.BIGGER.spSize -> {
                if(textSize != TextSizeOptions.BIGGER) {
                    addPostViewModel.updateTextSize(TextSizeOptions.BIGGER)
                }
            }
            TextSizeOptions.BIGGEST.spSize -> {
                if(textSize != TextSizeOptions.BIGGEST) {
                    addPostViewModel.updateTextSize(TextSizeOptions.BIGGEST)
                }
            }
        }
    }

    LaunchedEffect(state.currentSpanStyle.fontWeight) {
        when(state.currentSpanStyle.fontWeight) {
            FontWeight.Bold -> {
                if(!textIsBold) {
                    addPostViewModel.updateTextBold(true)
                }
            }
            else -> {
                if(textIsBold) {
                    addPostViewModel.updateTextBold(false)
                }
            }
        }
    }

    LaunchedEffect(state.currentSpanStyle.fontStyle) {
        when(state.currentSpanStyle.fontStyle) {
            FontStyle.Italic -> {
                if(!textIsItalic) {
                    addPostViewModel.updateTextItalic(true)
                }
            }
            else -> {
                if(textIsItalic) {
                    addPostViewModel.updateTextItalic(false)
                }
            }
        }
    }

    LaunchedEffect(state.currentSpanStyle.textDecoration ) {
        when(state.currentSpanStyle.textDecoration) {
            TextDecoration.Underline -> {
                if(!textHasUnderLine){
                    addPostViewModel.updateTextUnderLine(true)
                }
            }
            else -> {
                if(textHasUnderLine){
                    addPostViewModel.updateTextUnderLine(false)
                }
            }
        }
    }

    LaunchedEffect(state.currentSpanStyle.color != textColor.color) {
        addPostViewModel.updateTextColor(state.currentSpanStyle.color)
    }

    val items = listOf(
        TextStyleOptions.SIZE to R.drawable.ic_text_size,
        TextStyleOptions.WEIGHT to R.drawable.ic_text_weight,
        TextStyleOptions.ITALICS to R.drawable.ic_text_italic,
        TextStyleOptions.UNDERLINE to R.drawable.ic_text_line,
        TextStyleOptions.COLOR to R.drawable.ic_text_color
    )

    Row {
        items.forEach { (item, painterResource) ->
            val isSelected = selectedTextStyleItem == item

            //변경 사항 존재, 아이콘 기본색 변경 여부 값
            val isActivate = when(item) {
                TextStyleOptions.SIZE -> {
                    textSize != TextSizeOptions.REGULAR
                }
                TextStyleOptions.WEIGHT -> {
                    textIsBold
                }
                TextStyleOptions.ITALICS -> {
                    textIsItalic
                }
                TextStyleOptions.UNDERLINE -> {
                    textHasUnderLine
                }
                else -> {
                    false
                }
            }

            val iconTint = when {
                isActivate -> {
                    SelectedToolbarItemColor
                }
                item == TextStyleOptions.COLOR -> {
                    textColor.color
                }
                isSelected -> {
                    if(item == TextStyleOptions.SIZE)
                        SelectedToolbarItemColor
                    else
                        Color.Black
                }
                else -> {
                    Color.Black
                }
            }

            Box(
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(32.dp)
                        .background(
                            color =
                            if (isSelected && (item == TextStyleOptions.SIZE || item == TextStyleOptions.COLOR))
                                SelectedToolbarTextStyleBackgroundColor
                            else
                                Color.Transparent,
                            shape = RoundedCornerShape(50)
                        )
                ) {

                }

                IconButton(
                    onClick = {
                        addPostViewModel.updateSelectedTextStyleItem(item)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = painterResource),
                        contentDescription = item.name,
                        tint = iconTint
                    )
                }
            }
        }
    }
}

@Composable
fun SecondaryToolbar() {
    val addPostViewModel = LocalAddPostViewModel.current

    val selectedToolbarItem by addPostViewModel.selectedToolbarItem.collectAsState()
    val selectedTextStyleItem by addPostViewModel.selectedTextStyleItem.collectAsState()

    val textSize by addPostViewModel.textSize.collectAsState()

    //글자 크기 선택
    AnimatedVisibility(
        enter = fadeIn(),
        exit = fadeOut(),
        visible = selectedToolbarItem == ToolbarItems.TEXT_STYLE && selectedTextStyleItem == TextStyleOptions.SIZE
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = SecondaryToolbarBackgroundColor)
        ) {
            DefaultHorizontalDivider()

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    TextButton(
                        onClick = { addPostViewModel.updateTextSize(TextSizeOptions.SMALL) }
                    ) {
                        Text (
                            text = "작게",
                            style = storeMeTextStyle(FontWeight.Normal, -2),
                            color = if(textSize == TextSizeOptions.SMALL) SelectedToolbarItemColor else Color.Black
                        )
                    }
                }

                item {
                    TextButton(
                        onClick = { addPostViewModel.updateTextSize(TextSizeOptions.REGULAR) }
                    ) {
                        Text (
                            text = "보통",
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = if(textSize == TextSizeOptions.REGULAR) SelectedToolbarItemColor else Color.Black
                        )
                    }
                }

                item {
                    TextButton(
                        onClick = { addPostViewModel.updateTextSize(TextSizeOptions.BIGGER) }
                    ) {
                        Text (
                            text = "크게",
                            style = storeMeTextStyle(FontWeight.Normal, 2),
                            color = if(textSize == TextSizeOptions.BIGGER) SelectedToolbarItemColor else Color.Black
                        )
                    }
                }

                item {
                    TextButton(
                        onClick = { addPostViewModel.updateTextSize(TextSizeOptions.BIGGEST) }
                    ) {
                        Text (
                            text = "더 크게",
                            style = storeMeTextStyle(FontWeight.Normal, 4),
                            color = if(textSize == TextSizeOptions.BIGGEST) SelectedToolbarItemColor else Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextColorToolbar() {
    val addPostViewModel = LocalAddPostViewModel.current
    val selectedToolbarItem by addPostViewModel.selectedToolbarItem.collectAsState()
    val selectedTextStyleItem by addPostViewModel.selectedTextStyleItem.collectAsState()

    AnimatedVisibility(
        enter = fadeIn(),
        exit = fadeOut(),
        visible = selectedToolbarItem == ToolbarItems.TEXT_STYLE && selectedTextStyleItem == TextStyleOptions.COLOR
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 8.dp, start = 12.dp, end = 12.dp)
                .background(color = SecondaryToolbarBackgroundColor, shape = CircleShape)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(TextColors.entries) {
                    IconButton(
                        onClick = {
                            addPostViewModel.updateTextColor(it)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_text_color),
                            contentDescription = it.name,
                            tint = it.color,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}