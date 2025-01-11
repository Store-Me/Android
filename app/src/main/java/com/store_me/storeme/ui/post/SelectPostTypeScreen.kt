@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.post

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.PostType
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.post.SelectPostTypeViewModel.*
import com.store_me.storeme.ui.post.normal.AddNormalPostSection
import com.store_me.storeme.ui.theme.AddPostCouponIconColor
import com.store_me.storeme.ui.theme.AddPostEditIconColor
import com.store_me.storeme.ui.theme.AddPostEventIconColor
import com.store_me.storeme.ui.theme.AddPostNoticeIconColor
import com.store_me.storeme.ui.theme.AddPostStoryIconColor
import com.store_me.storeme.ui.theme.AddPostSurveyIconColor
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils

val LocalSelectPostTypeViewModel = staticCompositionLocalOf<SelectPostTypeViewModel> {
    error("No AddPostViewModel provided")
}

@Composable
fun SelectPostTypeScreen(
    navController: NavController,
    selectPostTypeViewModel: SelectPostTypeViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    val postType by selectPostTypeViewModel.postType.collectAsState()
    
    BackHandler {
        if(postType == null){
            navController.popBackStack()
        } else {
            selectPostTypeViewModel.updatePostType(null)
        }
    }

    CompositionLocalProvider(LocalSelectPostTypeViewModel provides selectPostTypeViewModel) {
        Scaffold(
            modifier = Modifier
                .addFocusCleaner(focusManager),
            containerColor = Color.White,
            topBar = {
                if(postType != null) {
                    AddPostTopBar(
                        canFinish = false,
                        onClose = { selectPostTypeViewModel.updatePostType(null) },
                        onFinish = { }
                    )
                } },
            bottomBar = {},
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    when(postType) {
                        PostType.NORMAL -> { navController.navigate("add_post") }
                        PostType.COUPON -> {  }
                        PostType.EVENT -> {  }
                        PostType.SURVEY -> {  }
                        PostType.NOTICE -> {  }
                        PostType.STORY -> {  }
                        null -> { SelectPostTypeSection() }
                    }
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
fun SelectPostTypeSection() {
    val addPostViewModel = LocalSelectPostTypeViewModel.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "추가하고 싶은 항목을 선택하세요.",
            style = storeMeTextStyle(FontWeight.ExtraBold, 8)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PostTypeLargeItem(
                postType = PostType.NORMAL,
                iconResource = R.drawable.ic_edit,
                iconTint = AddPostEditIconColor
            ) {
                addPostViewModel.updatePostType(PostType.NORMAL)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PostTypeSmallItem(
                    modifier = Modifier
                        .weight(1f),
                    postType = PostType.COUPON,
                    iconResource = R.drawable.ic_post_coupon,
                    iconTint = AddPostCouponIconColor
                ) {
                    addPostViewModel.updatePostType(PostType.COUPON)
                }

                PostTypeSmallItem(
                    modifier = Modifier
                        .weight(1f),
                    postType = PostType.EVENT,
                    iconResource = R.drawable.ic_event,
                    iconTint = AddPostEventIconColor
                ) {
                    addPostViewModel.updatePostType(PostType.EVENT)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PostTypeSmallItem(
                    modifier = Modifier
                        .weight(1f),
                    postType = PostType.SURVEY,
                    iconResource = R.drawable.ic_survey,
                    iconTint = AddPostSurveyIconColor
                ) {
                    addPostViewModel.updatePostType(PostType.SURVEY)
                }

                PostTypeSmallItem(
                    modifier = Modifier
                        .weight(1f),
                    postType = PostType.NOTICE,
                    iconResource = R.drawable.ic_notice,
                    iconTint = AddPostNoticeIconColor
                ) {
                    addPostViewModel.updatePostType(PostType.NOTICE)
                }
            }

            PostTypeLargeItem(
                postType = PostType.STORY,
                iconResource = R.drawable.ic_story,
                iconTint = AddPostStoryIconColor
            ) {
                addPostViewModel.updatePostType(PostType.STORY)
            }
        }
    }
}

@Composable
fun PostTypeLargeItem(postType: PostType, iconResource: Int, iconTint: Color, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            containerColor = EditButtonColor
        )
    ) {
        val density = LocalDensity.current

        val iconSize = SizeUtils.textSizeToDp(density, 2, 12)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = postType.displayName,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2)
            )

            postType.description?.let {
                Text(
                    text = it,
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = UndefinedTextColor
                )
            }
        }

        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            modifier = Modifier
                .size(iconSize),
            tint = iconTint
        )
    }
}

@Composable
fun PostTypeSmallItem(modifier: Modifier, postType: PostType, iconResource: Int, iconTint: Color, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            containerColor = EditButtonColor
        )
    ) {
        val density = LocalDensity.current

        val iconSize = SizeUtils.textSizeToDp(density, 2, 12)

        Column(
            modifier = modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconResource),
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize),
                tint = iconTint
            )

            Text(
                text = postType.displayName,
                style = storeMeTextStyle(FontWeight.ExtraBold, 0)
            )
        }
    }
}
