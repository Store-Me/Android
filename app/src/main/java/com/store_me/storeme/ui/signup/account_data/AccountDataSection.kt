package com.store_me.storeme.ui.signup.account_data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.ValidIconColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.ui.theme.TextClearIconColor
import com.store_me.storeme.utils.composition_locals.signup.LocalAccountDataViewModel

@Composable
fun AccountDataSection(onFinish: () -> Unit) {
    val accountDataViewModel = LocalAccountDataViewModel.current

    val accountId by accountDataViewModel.accountId.collectAsState()
    val accountPw by accountDataViewModel.accountPw.collectAsState()
    val accountPwConfirm by accountDataViewModel.accountPwConfirm.collectAsState()

    val isIdError = remember { mutableStateOf(false) }
    val isPwError = remember { mutableStateOf(false) }
    val isPwConfirmError = remember { mutableStateOf(false) }

    val isHidePw = remember { mutableStateOf(true) }
    val isHidePwConfirm = remember { mutableStateOf(true) }

    val accountIdDuplicate by accountDataViewModel.accountIdDuplicate.collectAsState()
    val isPasswordMatching by accountDataViewModel.isPasswordMatching.collectAsState()

    LaunchedEffect(accountPwConfirm, accountPw) {
        if(accountPw.isNotEmpty() && accountPwConfirm.isNotEmpty()) {
            isPwConfirmError.value = !isPasswordMatching
            isPwError.value = !accountDataViewModel.isValidPw()
        } else if (accountPw.isNotEmpty()) {
            isPwError.value = !accountDataViewModel.isValidPw()
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        item {
            SignupTitleText(title = "아이디와 비밀번호를\n 설정해주세요.")
        }

        item { Spacer(modifier = Modifier.height(36.dp)) }

        //아이디 입력
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "아이디",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                OutlinedTextField(
                    value = accountId,
                    onValueChange = {
                        accountDataViewModel.updateAccountId(it)
                        accountDataViewModel.clearAccountIdDuplicate()
                    },
                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        Button(
                            onClick = {
                                if(accountDataViewModel.isValidId()){
                                    isIdError.value = false
                                    accountDataViewModel.checkDuplicate()
                                } else {
                                    isIdError.value = true
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(accountIdDuplicate == null || accountIdDuplicate == true) Color.Black else ValidIconColor,
                                contentColor = Color.White
                            ),
                            enabled = accountIdDuplicate == null || accountIdDuplicate == true,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = if(accountIdDuplicate == null || accountIdDuplicate == true) "중복 확인" else "확인 완료",
                                style = storeMeTextStyle(FontWeight.ExtraBold, -1),
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "아이디를 입력해주세요.",
                            style = storeMeTextStyle(FontWeight.Normal, 1),
                            color = UndefinedTextColor
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HighlightTextFieldColor,
                        errorBorderColor = ErrorTextFieldColor,
                        errorLabelColor = ErrorTextFieldColor,
                    ),
                    isError = isIdError.value,
                    supportingText = {
                        if(isIdError.value){
                            Text(
                                text = "4 ~ 20 글자 영어 혹은 숫자로 구성되어야 합니다.",
                                style = storeMeTextStyle(FontWeight.Normal, 0),
                                color = ErrorTextFieldColor
                            )
                        }
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        //비밀번호 입력
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "비밀번호",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                OutlinedTextField(
                    value = accountPw,
                    onValueChange = {
                        accountDataViewModel.updateAccountPw(it)
                        if(it.isNotEmpty()) {
                            isPwError.value = !accountDataViewModel.isValidPw()
                        }
                    },
                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    visualTransformation = if (isHidePw.value) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(onClick = { isHidePw.value = !isHidePw.value }) {
                            Icon(
                                painter = painterResource(id = if(isHidePw.value) R.drawable.ic_hide else R.drawable.ic_show),
                                contentDescription = "보이기/숨기기",
                                modifier = Modifier
                                    .size(24.dp),
                                tint = TextClearIconColor
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "비밀번호를 입력해주세요.",
                            style = storeMeTextStyle(FontWeight.Normal, 1),
                            color = UndefinedTextColor
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HighlightTextFieldColor,
                        errorBorderColor = ErrorTextFieldColor,
                        errorLabelColor = ErrorTextFieldColor,
                    ),
                    isError = isPwError.value,
                    supportingText = {
                        if(isPwError.value){
                            Text(
                                text = "4 ~ 20 글자로 구성되어야 합니다.",
                                style = storeMeTextStyle(FontWeight.Normal, 0),
                                color = ErrorTextFieldColor
                            )
                        }
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        //비밀번호 확인
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "비밀번호 확인",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                OutlinedTextField(
                    value = accountPwConfirm,
                    onValueChange = {
                        accountDataViewModel.updateAccountPwConfirm(it)
                    },
                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    visualTransformation = if (isHidePwConfirm.value) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(onClick = { isHidePwConfirm.value = !isHidePwConfirm.value }) {
                            Icon(
                                painter = painterResource(id = if(isHidePwConfirm.value) R.drawable.ic_hide else R.drawable.ic_show),
                                contentDescription = "보이기/숨기기",
                                modifier = Modifier
                                    .size(24.dp),
                                tint = TextClearIconColor
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "동일한 비밀번호를 입력해주세요.",
                            style = storeMeTextStyle(FontWeight.Normal, 1),
                            color = UndefinedTextColor
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HighlightTextFieldColor,
                        errorBorderColor = ErrorTextFieldColor,
                        errorLabelColor = ErrorTextFieldColor,
                    ),
                    isError = isPwConfirmError.value,
                    supportingText = {
                        if(isPwConfirmError.value){
                            Text(
                                text = "비밀번호가 일치하지 않습니다.",
                                style = storeMeTextStyle(FontWeight.Normal, 0),
                                color = ErrorTextFieldColor
                            )
                        }
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(48.dp)) }

        item {
            DefaultButton(
                buttonText = "다음",
                enabled = accountIdDuplicate == false && isPasswordMatching && !isPwError.value && accountPw.isNotEmpty()
            ) {
                onFinish()
            }
        }

    }
}