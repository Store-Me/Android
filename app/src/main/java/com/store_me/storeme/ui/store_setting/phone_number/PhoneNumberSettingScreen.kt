package com.store_me.storeme.ui.store_setting.phone_number

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.signup.GuideTextBoxItem
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.PhoneNumberUtils
import com.store_me.storeme.utils.StoreNumberVisualTransformation
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun PhoneNumberSettingScreen(
    navController: NavController,
    phoneNumberSettingViewModel: PhoneNumberSettingViewModel = viewModel()
) {
    val auth = LocalAuth.current
    val storeDataViewModel = LocalStoreDataViewModel.current
    val loadingViewModel = LocalLoadingViewModel.current

    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val phoneNumber by phoneNumberSettingViewModel.phoneNumber.collectAsState()

    val hasDifference by remember {
        derivedStateOf { (storeInfoData?.storePhoneNumber ?: "") != phoneNumber }
    }

    val isError = remember { mutableStateOf(false) }

    val showDialog = remember { mutableStateOf(false) }

    fun onClose() {
        if(hasDifference)
            showDialog.value = true
        else
            navController.popBackStack()
    }

    LaunchedEffect(storeInfoData) {
        phoneNumberSettingViewModel.updatePhoneNumber(storeInfoData?.storePhoneNumber ?: "")
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(phoneNumber) {
        if(phoneNumber.isNotEmpty())
            isError.value = !PhoneNumberUtils().isValidStoreNumber(phoneNumber)
        else
            isError.value = false
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { TitleWithDeleteButton(title = "전화번호 수정") {
            onClose()
        } },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
            ) {
                item {
                    Text(
                        text = "스토어 전화번호를 입력해주세요.",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if(it.length < 13)
                                phoneNumberSettingViewModel.updatePhoneNumber(it)
                        },
                        textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = {
                            if(phoneNumber.isNotEmpty()){
                                IconButton(onClick = { phoneNumberSettingViewModel.updatePhoneNumber("") }) {
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
                                text = "스토어 전화번호를 입력해주세요.",
                                style = storeMeTextStyle(FontWeight.Normal, 1),
                                color = UndefinedTextColor
                            )
                        },
                        visualTransformation = StoreNumberVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HighlightTextFieldColor,
                            errorBorderColor = ErrorTextFieldColor,
                            errorLabelColor = ErrorTextFieldColor,
                        ),
                        isError = isError.value,
                        supportingText = {
                            if(isError.value){
                                Text(
                                    text = "전화번호 형식이 올바르지 않습니다.",
                                    style = storeMeTextStyle(FontWeight.Normal, 0),
                                    color = ErrorTextFieldColor
                                )
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    GuideTextBoxItem(
                        title = "스토어 전화번호 가이드",
                        content = "다른 사람들에게 노출되는 번호이기 때문에, 개인 핸드폰 번호가 아닌 안심번호, 가게 유선 전화번호로 등록하는 것이 권장돼요."
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "저장",
                        enabled = hasDifference && !isError.value,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                    ) {
                        loadingViewModel.showLoading()

                        storeDataViewModel.patchStorePhoneNumber(storePhoneNumber = phoneNumber)
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