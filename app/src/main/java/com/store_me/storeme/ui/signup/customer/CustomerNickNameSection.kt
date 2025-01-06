package com.store_me.storeme.ui.signup.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.signup.NextButton
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalCustomerDataViewModel

@Composable
fun CustomerNickNameSection(onFinish: () -> Unit) {
    val customerDataViewModel = LocalCustomerDataViewModel.current

    val nickname by customerDataViewModel.nickName.collectAsState()
    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(nickname) {
        isError.value = nickname.length > 10
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        SignupTitleText(title = "스토어미에서 사용할\n닉네임을 설정해 주세요.")

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "닉네임",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2, true),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = {
                customerDataViewModel.updateNickName(it)
            },
            textStyle = storeMeTextStyle(FontWeight.Normal, 1, isFixedSize = true),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = {
                if(nickname.isNotEmpty()){
                    IconButton(onClick = { customerDataViewModel.updateNickName("") }) {
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
                    text = "닉네임을 입력해 주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 1, isFixedSize = true),
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
                        text = "닉네임은 10자 이내로 작성되어야 합니다.",
                        style = storeMeTextStyle(FontWeight.Normal, 0, isFixedSize = true),
                        color = ErrorTextFieldColor
                    )
                }
            }
        )

        TextLengthRow(text = nickname, limitSize = 10)

        Spacer(modifier = Modifier.height(48.dp))

        NextButton(
            buttonText = "다음",
            enabled = !isError.value && nickname.isNotEmpty()
        ) {
            onFinish()
        }
    }
}