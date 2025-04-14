package com.store_me.storeme.ui.store_setting.notice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun NoticeSettingScreen(
    navController: NavController,
    noticeSettingViewModel: NoticeSettingViewModel = viewModel()
) {
    val auth = LocalAuth.current
    val loadingViewModel = LocalLoadingViewModel.current
    val storeDataViewModel = LocalStoreDataViewModel.current

    val storeNotice by storeDataViewModel.notice.collectAsState()

    val notice by noticeSettingViewModel.notice.collectAsState()

    LaunchedEffect(storeNotice) {
        noticeSettingViewModel.updateNotice(storeNotice)
    }

    val hasDifference by remember {
        derivedStateOf { storeNotice != notice }
    }

    val showDialog = remember { mutableStateOf(false) }

    fun onClose() {
        if(hasDifference)
            showDialog.value = true
        else
            navController.popBackStack()
    }

    BackHandler {
        onClose()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { TitleWithDeleteButton(title = "공지사항 수정") {
            onClose()
        } },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                item {
                    Text(
                        text = "스토어 공지사항을 입력해주세요.",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(20.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    TextField(
                        value = notice,
                        onValueChange = { noticeSettingViewModel.updateNotice(it) },
                        textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                        placeholder = {
                            Text(
                                text = "손님들에게 안내하고 싶은 내용을 공지사항을 통해 남겨보세요.\n새로운 소식이나, 기본적인 안내사항 등을 표현하기 좋아요",
                                style = storeMeTextStyle(FontWeight.Normal, 1),
                                color = GuideColor
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 350.dp) //최소 높이
                            .padding(horizontal = 4.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                        ),
                        singleLine = false,
                        minLines = 2    //1 -> 2줄 변화시 글자 크기 문제 해결
                    )
                }

                item {
                    DefaultHorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "저장",
                        enabled = hasDifference,
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        loadingViewModel.showLoading()

                        storeDataViewModel.patchStoreNotice(notice = notice)
                    }
                }
            }
        }
    )

    if (showDialog.value) {
        BackWarningDialog(
            onDismiss = {
                showDialog.value = false
            },
            onAction = {
                showDialog.value = false
                navController.popBackStack()
            }
        )
    }

}