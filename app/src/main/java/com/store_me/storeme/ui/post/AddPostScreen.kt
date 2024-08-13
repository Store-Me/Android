package com.store_me.storeme.ui.post

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.store_me.storeme.data.PostType
import com.store_me.storeme.ui.component.DefaultButton

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
                }
            }
        )
    }


}

@Composable
fun ItemListSection() {
    val addPostViewModel = LocalAddPostViewModel.current

    LazyRow(
        contentPadding = PaddingValues(start = 20.dp),
    ) {
        items(AddPostViewModel.LayoutItem.entries) { item ->
            DefaultButton(text = item.displayName) {
                layoutButtonHandler(item)
            }
        }
    }
}

fun layoutButtonHandler(layoutItem: AddPostViewModel.LayoutItem) {
    when(layoutItem) {
        AddPostViewModel.LayoutItem.TEXT -> {

        }
        AddPostViewModel.LayoutItem.IMAGE -> {

        }
    }
}
