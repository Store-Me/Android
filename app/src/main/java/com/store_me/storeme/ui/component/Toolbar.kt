package com.store_me.storeme.ui.component

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mohamedrejeb.richeditor.model.RichTextState
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.TextColors
import com.store_me.storeme.data.enums.TextSizeOptions
import com.store_me.storeme.data.enums.TextStyleOptions
import com.store_me.storeme.data.enums.ToolbarItems
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.LighterHighlightColor
import com.store_me.storeme.ui.theme.SecondaryToolbarBackgroundColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.CropUtils
import com.yalantis.ucrop.UCrop

@Composable
fun KeyboardToolbar(
    state: RichTextState,
    keyboardHeight: Dp,
    selectedToolbarItem: ToolbarItems?,
    selectedTextStyleItem: TextStyleOptions?,
    isKeyboardOpen: Boolean,
    images: List<Uri>,
    onToolbarItemClick: (ToolbarItems?) -> Unit,
    onTextStyleItemClick: (TextStyleOptions?) -> Unit,
    onImagePick: (Uri) -> Unit
) {
    //Crop 관련
    val context = LocalContext.current
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let { uri ->
                onImagePick(uri)
            }
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { sourceUri ->
            val cropIntent = CropUtils.getCropIntent(context = context, sourceUri = sourceUri, aspectRatio = null)
            cropLauncher.launch(cropIntent)
        }
    }

    Column(
        modifier = Modifier
    ) {
        //Toolbar 상단의 텍스트 크기 Toolbar
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(),
            visible = selectedToolbarItem == ToolbarItems.TEXT_STYLE && selectedTextStyleItem == TextStyleOptions.SIZE
        ) {
            TextSizeToolbar(
                spSize = state.currentSpanStyle.fontSize,
                onSizeChange = {
                    state.toggleSpanStyle(
                        spanStyle = SpanStyle(fontSize = it)
                    )
                }
            )
        }

        //Toolbar 상단의 텍스트 색상 선택 Toolbar
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(),
            visible = selectedToolbarItem == ToolbarItems.TEXT_STYLE && selectedTextStyleItem == TextStyleOptions.COLOR
        ) {
            TextColorToolbar(
                onColorSelect = {
                    state.toggleSpanStyle(
                        spanStyle = SpanStyle(color = it.color)
                    )
                }
            )
        }

        DefaultHorizontalDivider()

        //기본 Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            //글자 정렬 Toolbar
            AnimatedVisibility(
                visible = selectedToolbarItem == ToolbarItems.ALIGN
            ) {
                TextAlignItems(state = state) {
                    state.toggleParagraphStyle(paragraphStyle = ParagraphStyle(textAlign = it))
                }
            }

            //글자 스타일 Toolbar
            AnimatedVisibility(
                visible = selectedToolbarItem == ToolbarItems.TEXT_STYLE
            ) {
                TextStyleItems(
                    state = state,
                    selectedTextStyle = selectedTextStyleItem,
                    onSizeClick = { onTextStyleItemClick(
                        TextStyleOptions.SIZE
                    ) },
                    onColorClick = { onTextStyleItemClick(
                        TextStyleOptions.COLOR
                    ) }
                )
            }

            //기본 Toolbar Item
            AnimatedVisibility(
                visible = selectedToolbarItem == null || selectedToolbarItem == ToolbarItems.EMOJI || selectedToolbarItem == ToolbarItems.IMAGE,
            ) {
                ToolbarItems(
                    state = state,
                    selectedToolbarItem = selectedToolbarItem,
                    onClick = { onToolbarItemClick(it) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            //Align, TextStyle 일때 뒤로가기
            AnimatedVisibility(
                enter = fadeIn(),
                exit = fadeOut(),
                visible = selectedToolbarItem == ToolbarItems.ALIGN || selectedToolbarItem == ToolbarItems.TEXT_STYLE
            ) {
                IconButton(
                    onClick = {
                        onToolbarItemClick(null)
                        onTextStyleItemClick(null)
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
                    SelectEmojiSection(
                        keyboardHeight = keyboardHeight
                    )
                }
                ToolbarItems.IMAGE -> {
                    SelectImageSection(
                        keyboardHeight = keyboardHeight,
                        images = images
                    ) {
                        galleryLauncher.launch("image/*")
                    }
                }
                else -> {
                    if (isKeyboardOpen) {
                        Spacer(modifier = Modifier.height(keyboardHeight))
                    }
                }
            }
        }
    }
}

@Composable
fun ToolbarItems(
    state: RichTextState,
    selectedToolbarItem: ToolbarItems?,
    onClick: (ToolbarItems) -> Unit
) {
    LazyRow {
        items(ToolbarItems.entries) { toolbarItem ->
            val isSelected = toolbarItem == selectedToolbarItem

            when(toolbarItem) {
                ToolbarItems.IMAGE -> { ToolbarImage(isSelected = isSelected) {
                    onClick(it)
                } }
                ToolbarItems.ALIGN -> { ToolbarAlign(
                    isSelected = isSelected,
                    currentAlign = state.currentParagraphStyle.textAlign,
                ) {
                    onClick(it)
                } }
                ToolbarItems.TEXT_STYLE -> { ToolbarTextStyle(isSelected = isSelected) {
                    onClick(it)
                } }
                ToolbarItems.EMOJI -> { ToolbarEmoji(isSelected = isSelected) {
                    onClick(it)
                } }
            }
        }
    }
}

@Composable
fun ToolbarImage(isSelected: Boolean, onClick: (ToolbarItems) -> Unit) {
    IconButton(
        onClick = {
            onClick(ToolbarItems.IMAGE)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_image),
            contentDescription = "이미지",
            tint = if(isSelected) HighlightColor else Color.Black,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ToolbarAlign(isSelected: Boolean, currentAlign: TextAlign, onClick: (ToolbarItems) -> Unit) {
    val icon = when(currentAlign) {
        TextAlign.Left -> R.drawable.ic_left_align
        TextAlign.Center -> R.drawable.ic_center_align
        TextAlign.Right -> R.drawable.ic_right_align
        else -> R.drawable.ic_left_align
    }

    IconButton(
        onClick = {
            onClick(ToolbarItems.ALIGN)
        }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "정렬",
            tint = if(isSelected) HighlightColor else Color.Black,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ToolbarTextStyle(isSelected: Boolean, onClick: (ToolbarItems) -> Unit) {
    IconButton(
        onClick = {
            onClick(ToolbarItems.TEXT_STYLE)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_text),
            contentDescription = "글자 스타일",
            tint = if(isSelected) HighlightColor else Color.Black,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ToolbarEmoji(isSelected: Boolean, onClick: (ToolbarItems) -> Unit) {
    IconButton(
        onClick = {
            onClick(ToolbarItems.EMOJI)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_emoji),
            contentDescription = "이모티콘",
            tint = if(isSelected) HighlightColor else Color.Black,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun TextStyleItems(
    state: RichTextState,
    selectedTextStyle: TextStyleOptions?,
    onSizeClick: () -> Unit,
    onColorClick: () -> Unit
) {
    Row {
        TextStyleOptions.entries.forEach {
            when(it) {
                TextStyleOptions.SIZE -> TextStyleSize(isSelecting = selectedTextStyle == TextStyleOptions.SIZE, onClick = {
                    onSizeClick()
                })
                TextStyleOptions.WEIGHT -> TextStyleWeight(isActive = state.currentSpanStyle.fontWeight == FontWeight.Bold, onClick = {
                    state.toggleSpanStyle(
                        spanStyle = SpanStyle(fontWeight = it)
                    )
                })
                TextStyleOptions.ITALICS -> TextStyleItalic(isActive = state.currentSpanStyle.fontStyle == FontStyle.Italic, onClick = {
                    state.toggleSpanStyle(
                        spanStyle = SpanStyle(fontStyle = it)
                    )
                })
                TextStyleOptions.UNDERLINE -> TextStyleUnderline(isActive = state.currentSpanStyle.textDecoration == TextDecoration.Underline, onClick = {
                    state.toggleSpanStyle(
                        spanStyle = SpanStyle(textDecoration = it)
                    )
                })
                TextStyleOptions.COLOR -> TextStyleColor(color = state.currentSpanStyle.color, onClick = {
                    onColorClick()
                })
            }
        }
    }
}

@Composable
fun TextStyleSize(isSelecting: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .background(
                color =
                    if (isSelecting)
                        LighterHighlightColor
                    else
                        Color.Transparent,
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_text_size),
            contentDescription = "크기",
            tint = HighlightColor
        )
    }
}

@Composable
fun TextStyleWeight(isActive: Boolean, onClick: (FontWeight) -> Unit) {
    IconButton(
        onClick = {
            if(isActive)
                onClick(FontWeight.Normal)
            else
                onClick(FontWeight.Bold)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_text_weight),
            contentDescription = "두께",
            tint = if(isActive) HighlightColor else Color.Black,
        )
    }
}

@Composable
fun TextStyleItalic(isActive: Boolean, onClick: (FontStyle) -> Unit) {
    IconButton(
        onClick = {
            if(isActive)
                onClick(FontStyle.Normal)
            else
                onClick(FontStyle.Italic)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_text_italic),
            contentDescription = "기울기",
            tint = if(isActive) HighlightColor else Color.Black,
        )
    }
}

@Composable
fun TextStyleUnderline(isActive: Boolean, onClick: (TextDecoration) -> Unit) {
    IconButton(
        onClick = {
            if(isActive)
                onClick(TextDecoration.None)
            else
                onClick(TextDecoration.Underline)
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_text_line),
            contentDescription = "밑줄",
            tint = if(isActive) HighlightColor else Color.Black,
        )
    }
}

@Composable
fun TextStyleColor(color: Color, onClick: () -> Unit) {
    IconButton(
        onClick = {
            onClick()
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_text_color),
            contentDescription = "색상",
            tint = color,
        )
    }
}

@Composable
fun TextAlignItems(state: RichTextState, onClick: (TextAlign) -> Unit) {
    val currentAlign = state.currentParagraphStyle.textAlign

    Row {
        IconButton(
            onClick = { onClick(TextAlign.Left) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_align),
                contentDescription = "왼쪽 정렬",
                modifier = Modifier.size(20.dp),
                tint = if (currentAlign == TextAlign.Left || currentAlign == TextAlign.Unspecified) HighlightColor else Color.Black
            )
        }

        IconButton(
            onClick = { onClick(TextAlign.Center) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_center_align),
                contentDescription = "중앙 정렬",
                modifier = Modifier.size(20.dp),
                tint = if (currentAlign == TextAlign.Center) HighlightColor else Color.Black
            )
        }

        IconButton(
            onClick = { onClick(TextAlign.Right) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_right_align),
                contentDescription = "오른쪽 정렬",
                modifier = Modifier.size(20.dp),
                tint = if (currentAlign == TextAlign.Right) HighlightColor else Color.Black
            )
        }
    }
}

@Composable
fun TextSizeToolbar(
    spSize: TextUnit,
    onSizeChange: (TextUnit) -> Unit
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
                    onClick = { onSizeChange(TextSizeOptions.SMALL.spSize) }
                ) {
                    Text (
                        text = "작게",
                        style = storeMeTextStyle(FontWeight.Normal, -2),
                        color = if(spSize == TextSizeOptions.SMALL.spSize) HighlightColor else Color.Black
                    )
                }
            }

            item {
                TextButton(
                    onClick = { onSizeChange(TextSizeOptions.REGULAR.spSize) }
                ) {
                    Text (
                        text = "보통",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = if(spSize == TextSizeOptions.REGULAR.spSize) HighlightColor else Color.Black
                    )
                }
            }

            item {
                TextButton(
                    onClick = { onSizeChange(TextSizeOptions.BIGGER.spSize) }
                ) {
                    Text (
                        text = "크게",
                        style = storeMeTextStyle(FontWeight.Normal, 2),
                        color = if(spSize == TextSizeOptions.BIGGER.spSize) HighlightColor else Color.Black
                    )
                }
            }

            item {
                TextButton(
                    onClick = { onSizeChange(TextSizeOptions.BIGGEST.spSize) }
                ) {
                    Text (
                        text = "더 크게",
                        style = storeMeTextStyle(FontWeight.Normal, 4),
                        color = if(spSize == TextSizeOptions.BIGGEST.spSize) HighlightColor else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun TextColorToolbar(
    onColorSelect: (TextColors) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 8.dp, start = 12.dp, end = 12.dp)
            .background(color = SecondaryToolbarBackgroundColor, shape = CircleShape)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
        ) {
            items(TextColors.entries) {
                IconButton(
                    onClick = {
                        onColorSelect(it)
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

@Composable
fun SelectEmojiSection(
    keyboardHeight: Dp
) {
    Column(
        modifier = Modifier
            .height(keyboardHeight)
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
fun SelectImageSection(
    keyboardHeight: Dp,
    images: List<Uri>,
    onImagePick: (Uri) -> Unit
) {
    if(images.isEmpty()) {
        Column(
            modifier = Modifier
                .height(keyboardHeight)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "표시할 이미지가 없습니다.",
                style = storeMeTextStyle(FontWeight.Bold, 4),
                color = UndefinedTextColor
            )
        }
    }

    LazyVerticalGrid (
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .height(keyboardHeight)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) {
            AsyncImage(
                model = it,
                contentDescription = "이미지",
                modifier = Modifier
                    .clip(RoundedCornerShape(10))
                    .aspectRatio(1f)
                    .clickable { onImagePick(it) },
                contentScale = ContentScale.Crop
            )
        }
    }
}