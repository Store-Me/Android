package com.store_me.storeme.ui.signup.phone_authentication

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
import com.store_me.storeme.utils.PhoneNumberUtils
import com.store_me.storeme.utils.PhoneNumberVisualTransformation

@Composable
fun PhoneNumberSection(onFinish: () -> Unit) {
    val phoneNumberViewModel = LocalPhoneNumberViewModel.current

    val phoneNumber by phoneNumberViewModel.phoneNumber.collectAsState()
    val smsSentSuccess by phoneNumberViewModel.smsSentSuccess.collectAsState()

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(smsSentSuccess) {
        if(smsSentSuccess) {
            phoneNumberViewModel.clearSmsSentSuccess()
            onFinish()
        }
    }

    LaunchedEffect(phoneNumber) {
        if(isError.value) {
            isError.value = !PhoneNumberUtils().isValidPhoneNumber(phoneNumber)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        item {
            SignupTitleText(title = "본인 인증을 위해\n휴대폰 번호를 입력해주세요.")
        }

        item { Spacer(modifier = Modifier.height(36.dp)) }

        item {
            Text(
                text = "휴대폰 번호",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = Color.Black
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if(it.length < 12) phoneNumberViewModel.updatePhoneNumber(it) },
                textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    if(phoneNumber.isNotEmpty()){
                        IconButton(onClick = { phoneNumberViewModel.updatePhoneNumber("") }) {
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
                        text = "휴대폰 번호를 입력하세요",
                        style = storeMeTextStyle(FontWeight.Normal, 1),
                        color = UndefinedTextColor
                    )
                },
                visualTransformation = PhoneNumberVisualTransformation(),
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
                            text = "휴대폰 번호 형식에 맞지 않습니다.",
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
                buttonText = "인증 요청",
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if(PhoneNumberUtils().isValidPhoneNumber(phoneNumber = phoneNumber)){
                    isError.value = false

                    phoneNumberViewModel.sendSmsMessage(phoneNumber = phoneNumber)
                } else {
                    isError.value = true
                }
            }
        }
    }
}