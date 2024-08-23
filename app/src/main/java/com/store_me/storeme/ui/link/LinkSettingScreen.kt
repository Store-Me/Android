package com.store_me.storeme.ui.link

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.SocialMediaAccountData
import com.store_me.storeme.ui.component.SocialMediaIcon
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.ManagementButtonColor
import com.store_me.storeme.ui.theme.SaveButtonColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SocialMediaAccountUtils
import com.store_me.storeme.utils.ToastMessageUtils

val LocalLinkSettingViewModel = staticCompositionLocalOf<LinkSettingViewModel> {
    error("No LinkSettingViewModel provided")
}

@Composable
fun LinkSettingScreen(navController: NavController, linkSettingViewModel: LinkSettingViewModel = viewModel()){
    val linkListData by Auth.linkListData.collectAsState()
    val editState by linkSettingViewModel.editState.collectAsState()

    CompositionLocalProvider(LocalLinkSettingViewModel provides linkSettingViewModel) {
        Scaffold(
            containerColor = White,
            topBar = { TitleWithDeleteButton(navController = navController, title = "외부링크 관리") },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ){
                    LinkEditButtonSection(linkListData)

                    if(linkListData != null) {
                        LazyColumn(

                        ) {
                            itemsIndexed(Auth.linkListData.value!!.urlList) { index, url ->
                                if(index == 0)
                                    HorizontalDivider(
                                        color = DefaultDividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 20.dp)
                                    )

                                LinkItem(url)

                                HorizontalDivider(
                                    color = DefaultDividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 20.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }

}

@Composable
fun LinkEditButtonSection(linkListData: SocialMediaAccountData?) {
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
fun LinkItem(url: String) {

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialMediaIcon(url)

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = url, style = storeMeTextStyle(FontWeight.ExtraBold, 0))
    }


}
