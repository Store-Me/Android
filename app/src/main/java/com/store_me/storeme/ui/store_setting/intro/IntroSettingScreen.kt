package com.store_me.storeme.ui.store_setting.intro

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.signup.GuideTextBoxItem
import com.store_me.storeme.ui.status_bar.StatusBarPadding
import com.store_me.storeme.ui.theme.ErrorColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun IntroSettingScreen(
    navController: NavController,
    introSettingViewModel: IntroSettingViewModel = viewModel()
) {
    val loadingViewModel = LocalLoadingViewModel.current
    val storeDataViewModel = LocalStoreDataViewModel.current

    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val intro by introSettingViewModel.intro.collectAsState()

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(intro) {
        isError.value = intro.length > 100
    }

    LaunchedEffect(storeInfoData) {
        introSettingViewModel.updateStoreIntro(storeInfoData?.storeIntro ?: "")
    }

    val hasDifference by remember {
        derivedStateOf { storeInfoData?.storeIntro != intro}
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

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            StatusBarPadding()

            TitleWithDeleteButton(title = "소개 수정") {
                onClose()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = intro,
                        onValueChange = {
                            introSettingViewModel.updateStoreIntro(it)
                        },
                        textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE),
                        trailingIcon = {
                            if(intro.isNotEmpty()){
                                IconButton(onClick = { introSettingViewModel.updateStoreIntro("") }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_text_clear),
                                        contentDescription = "삭제",
                                        modifier = Modifier
                                            .size(24.dp),
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.owner_home_intro_none),
                                style = storeMeTextStyle(FontWeight.Normal, 1),
                                color = GuideColor
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HighlightColor,
                            errorBorderColor = ErrorColor,
                            errorLabelColor = ErrorColor,
                        ),
                        isError = isError.value,
                        supportingText = {
                            if(isError.value){
                                Text(
                                    text = "스토어 소개글은 100자 이내로 작성되어야 합니다.",
                                    style = storeMeTextStyle(FontWeight.Normal, 0),
                                    color = ErrorColor
                                )
                            }
                        }
                    )
                }

                item {
                    TextLengthRow(text = intro, limitSize = 100)
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    GuideTextBoxItem(
                        title = "스토어 소개 가이드",
                        content = "스토어 프로필에 노출되는 짧은 소개글이에요. 손님들에게 간단하게 가게에 대한 소개를 해주세요."
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(400.dp))
                }
            }
        }

        DefaultButton(
            buttonText = "저장",
            enabled = hasDifference && !isError.value,
            modifier = Modifier
                .padding(20.dp)
                .shadow(
                    elevation = 4.dp, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)
                )
                .align(alignment = Alignment.BottomCenter)
        ) {
            loadingViewModel.showLoading()

            storeDataViewModel.patchStoreIntro(storeIntro = intro)
        }
    }

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