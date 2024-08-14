package com.store_me.storeme.ui.post

import android.content.ContentValues.TAG
import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.store_me.storeme.data.PostType
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.post.AddPostViewModel.*
import com.store_me.storeme.ui.theme.storeMeTypography
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

    CompositionLocalProvider(LocalAddPostViewModel provides addPostViewModel) {
        Scaffold(
            containerColor = Color.White,
            topBar = { /*TODO*/ },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item { ItemListSection() }
                    item { Text(text = "미리보기", style = storeMeTypography.labelLarge) }
                    item { EditSection(currentEditState) }
                }
            }
        )
    }
}

@Composable
fun EditSection(currentEditState: LayoutItem?) {
    Log.d(TAG, currentEditState?.name.toString())

    when (currentEditState) {
        LayoutItem.TEXT -> {
            EditTextSection()
        }

        LayoutItem.IMAGE -> {

        }

        else -> {}
    }
}

@Composable
fun EditTextSection() {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // 텍스트 입력 필드
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text("텍스트를 입력하세요.") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )

        // 마크다운 미리보기
        MarkdownText(
            modifier = Modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = {  }
            ),
            markdown =  """
        # Welcome to StoreMe
        StoreMe는 당신의 물품을 안전하게 보관해줍니다.
        
        ## 기능
        - 물품 관리
        - 안전한 보관
        - 편리한 사용
        
        자세한 정보는 [여기](https://example.com)를 확인하세요.
    """.trimIndent())
    }
}

@Composable
fun ItemListSection() {
    val addPostViewModel = LocalAddPostViewModel.current

    LazyRow(
        contentPadding = PaddingValues(start = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 20.dp)
    ) {
        items(LayoutItem.entries) { item ->
            DefaultButton(text = item.displayName) {
                layoutButtonHandler(item, addPostViewModel)
            }
        }
    }
}

fun layoutButtonHandler(layoutItem: LayoutItem, addPostViewModel: AddPostViewModel) {
    if(addPostViewModel.currentEditState.value == null)
        addPostViewModel.setCurrentEditState(layoutItem)
}