package com.store_me.storeme.ui.post.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.data.ContentType
import com.store_me.storeme.data.PostContentType
import com.store_me.storeme.ui.post.add.ToolbarViewModel
import com.store_me.storeme.ui.post.add.normal.AddNormalPostScreen
import com.store_me.storeme.ui.post.add.normal.AddNormalPostViewModel
import com.store_me.storeme.ui.post.add.normal.LabelViewModel
import com.store_me.storeme.ui.store_setting.post.PostViewModel

@Composable
fun EditNormalPostScreen(
    navController: NavController,
    postViewModel: PostViewModel,
    addNormalPostViewModel: AddNormalPostViewModel = hiltViewModel(),
    labelViewModel: LabelViewModel = hiltViewModel(),
    toolbarViewModel: ToolbarViewModel = hiltViewModel(),
) {
    val selectedNormalPost by postViewModel.selectedNormalPost.collectAsState()
    val labels by labelViewModel.labels.collectAsState()

    LaunchedEffect(Unit) {
        selectedNormalPost?.let {
            labelViewModel.updateSelectedLabel(labels.find { label -> label.labelId == it.labelId })
            addNormalPostViewModel.syncNormalPost(it)
        }
    }

    AddNormalPostScreen(
        labelViewModel = labelViewModel,
        addNormalPostViewModel = addNormalPostViewModel,
        toolbarViewModel = toolbarViewModel,
    )
}