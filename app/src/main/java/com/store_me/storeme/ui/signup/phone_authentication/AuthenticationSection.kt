package com.store_me.storeme.ui.signup.phone_authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.ui.signup.NextButton
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalPhoneNumberViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalSignupViewModel
import com.store_me.storeme.utils.PhoneNumberUtils

@Composable
fun AuthenticationSection(onFinish: () -> Unit) {
    val phoneNumberViewModel = LocalPhoneNumberViewModel.current
    val signupViewModel = LocalSignupViewModel.current

    val phoneNumber by phoneNumberViewModel.phoneNumber.collectAsState()
    val verificationCode by phoneNumberViewModel.verificationCode.collectAsState()

    val verificationSuccess by phoneNumberViewModel.verificationSuccess.collectAsState()

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(verificationSuccess) {
        when(verificationSuccess){
            true -> {
                phoneNumberViewModel.clearVerificationSuccess()
                onFinish()
            }
            false -> {
                isError.value = true
            }
            null -> {

            }
        }
    }

    LaunchedEffect(verificationCode) {
        if(isError.value) {
            isError.value = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        item {
            SignupTitleText(title = "휴대폰으로 발송된\n인증 번호를 입력해주세요.")
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                ) {
                    Text(
                        text = PhoneNumberUtils().phoneNumberAddDashes(phoneNumber),
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                    )

                    Text(
                        text = "로 인증번호를 보냈어요.",
                        style = storeMeTextStyle(FontWeight.Normal, 2)
                    )
                }
                Text(
                    text = "확인 후 인증번호를 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 2)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(
                modifier = Modifier
                    .clickable {
                        signupViewModel.moveToPreviousProgress()
                    }
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "휴대폰 번호 변경 / 다시 보내기",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = HighlightTextFieldColor
                )

                Icon(
                    painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "화살표",
                    modifier = Modifier
                        .size(16.dp),
                    tint = HighlightTextFieldColor
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "인증 번호",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = Color.Black,
                modifier = Modifier
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            OutlinedTextField(
                value = verificationCode,
                onValueChange = { phoneNumberViewModel.updateVerificationCode(it) },
                textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    if(verificationCode.isNotEmpty()){
                        IconButton(onClick = { phoneNumberViewModel.updateVerificationCode("") }) {
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
                        text = "인증코드를 입력해주세요.",
                        style = storeMeTextStyle(FontWeight.Normal, 1),
                        color = UndefinedTextColor
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HighlightTextFieldColor,
                    errorBorderColor = ErrorTextFieldColor,
                    errorLabelColor = ErrorTextFieldColor,
                ),
                isError = isError.value,
                supportingText = {
                    if(isError.value){
                        Text(
                            text = "인증번호가 일치하지 않습니다.",
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = ErrorTextFieldColor
                        )
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }

        item {
            NextButton(
                buttonText = "확인",
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                phoneNumberViewModel.confirmCode()
            }
        }
    }
}