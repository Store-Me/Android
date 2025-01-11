package com.store_me.storeme.ui.post.normal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils

@Composable
fun AddNormalPostSection(
    navController: NavController,
    addNormalPostViewModel: AddNormalPostViewModel = viewModel()
) {
    val selectedLabel by addNormalPostViewModel.selectedLabel.collectAsState()

    val title by addNormalPostViewModel.title.collectAsState()
    val content by addNormalPostViewModel.content.collectAsState()

    Column {
        PostLabelSection(selectedLabel)

        EditNormalPostTitle(
            title = title,
            onValueChange = { addNormalPostViewModel.updateTitle(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        EditNormalPostContent(
            content = content,
            onValueChange = { addNormalPostViewModel.updateContent(it) }
        )
    }
}

@Composable
fun EditNormalPostTitle(
    title: String,
    onValueChange: (String) -> Unit,
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
                .padding(horizontal = 20.dp),
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
    content: String,
    onValueChange: (String) -> Unit
) {
    val imeInsets = WindowInsets.ime // 키보드 Insets
    val imeHeight = with(LocalDensity.current) {
        imeInsets.getBottom(LocalDensity.current).toDp()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                value = content,
                onValueChange = { onValueChange(it) },
                textStyle = storeMeTextStyle(FontWeight.Normal, 0),
                placeholder = {
                    Text(
                        text = "손님들에게 알리고 싶은 소식을 작성해보세요.",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
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
                singleLine = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }

        KeyboardToolbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = imeHeight) // 키보드 높이만큼 툴바를 올림
        )
    }
}

@Composable
fun KeyboardToolbar(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_image),
                    contentDescription = "이미지 추가",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        item {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_left_align),
                    contentDescription = "정렬 변경",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        item {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_line),
                    contentDescription = "구분선 추가",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
