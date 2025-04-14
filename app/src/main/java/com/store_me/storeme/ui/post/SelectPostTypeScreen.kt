@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.post

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.post.PostType
import com.store_me.storeme.ui.post.add.AddPostActivity
import com.store_me.storeme.ui.theme.AddPostCouponIconColor
import com.store_me.storeme.ui.theme.AddPostEditIconColor
import com.store_me.storeme.ui.theme.AddPostEventIconColor
import com.store_me.storeme.ui.theme.AddPostNoticeIconColor
import com.store_me.storeme.ui.theme.AddPostStoryIconColor
import com.store_me.storeme.ui.theme.AddPostSurveyIconColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

val LocalSelectPostTypeViewModel = staticCompositionLocalOf<SelectPostTypeViewModel> {
    error("No AddPostViewModel provided")
}

@Composable
fun SelectPostTypeScreen(
    navController: NavController,
    selectPostTypeViewModel: SelectPostTypeViewModel = viewModel()
) {
    val context = LocalContext.current
    val storeDataViewModel = LocalStoreDataViewModel.current
    val auth = LocalAuth.current

    CompositionLocalProvider(LocalSelectPostTypeViewModel provides selectPostTypeViewModel) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = Color.White,
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    SelectPostTypeSection {
                        navigateToAddPostActivity(context = context, postType = it, storeName = storeDataViewModel.storeInfoData.value!!.storeName, storeId = auth.storeId.value!!)
                    }
                }
            }
        )
    }
}

fun navigateToAddPostActivity(context: Context, postType: PostType, storeName: String, storeId: String) {

    val intent = Intent(context, AddPostActivity::class.java).apply {
        putExtra("postType", postType.name)
        putExtra("storeName", storeName)
        putExtra("storeId", storeId)
    }

    context.startActivity(intent)
}

@Composable
fun SelectPostTypeSection(onSelectType: (PostType) -> Unit) {
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
                onSelectType(PostType.NORMAL)
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
                    onSelectType(PostType.COUPON)
                }

                PostTypeSmallItem(
                    modifier = Modifier
                        .weight(1f),
                    postType = PostType.SURVEY,
                    iconResource = R.drawable.ic_survey,
                    iconTint = AddPostEventIconColor
                ) {
                    onSelectType(PostType.SURVEY)
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
                    postType = PostType.VOTE,
                    iconResource = R.drawable.ic_vote,
                    iconTint = AddPostSurveyIconColor
                ) {
                    onSelectType(PostType.VOTE)
                }

                PostTypeSmallItem(
                    modifier = Modifier
                        .weight(1f),
                    postType = PostType.NOTICE,
                    iconResource = R.drawable.ic_notice,
                    iconTint = AddPostNoticeIconColor
                ) {
                    onSelectType(PostType.NOTICE)
                }
            }

            PostTypeLargeItem(
                postType = PostType.STORY,
                iconResource = R.drawable.ic_story,
                iconTint = AddPostStoryIconColor
            ) {
                onSelectType(PostType.STORY)
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
            containerColor = SubHighlightColor
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
            containerColor = SubHighlightColor
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
