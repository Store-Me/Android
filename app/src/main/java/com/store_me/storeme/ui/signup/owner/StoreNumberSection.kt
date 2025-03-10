package com.store_me.storeme.ui.signup.owner

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.signup.GuidTextBoxItem
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.PhoneNumberUtils
import com.store_me.storeme.utils.StoreNumberVisualTransformation
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreSignupDataViewModel

@Composable
fun StoreNumberSection(onFinish: () -> Unit) {
    val storeDataViewModel = LocalStoreSignupDataViewModel.current

    val storeNumber by storeDataViewModel.storeNumber.collectAsState()

    val annotatedString = buildAnnotatedString {
        append("스토어미 안에서 ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) {
            append("손님들이 확인하고 연락할 수 있는 ")
        }
        append("전화번호에요.")
    }

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(storeNumber) {
        if(isError.value) {
            isError.value = !PhoneNumberUtils().isValidStoreNumber(storeNumber)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item {
            SignupTitleText(title = "스토어 전화번호를 입력해주세요. (선택)")
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = annotatedString,
                style = storeMeTextStyle(FontWeight.Normal, 2)
            )
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }

        //스토어 전화번호 입력
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "전화번호",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )
                
                OutlinedTextField(
                    value = storeNumber,
                    onValueChange = {
                        if(it.length < 13)
                            storeDataViewModel.updateStoreNumber(it)
                    },
                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        if(storeNumber.isNotEmpty()){
                            IconButton(onClick = { storeDataViewModel.updateStoreNumber("") }) {
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
            
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            GuidTextBoxItem(
                title = "스토어 전화번호 가이드",
                content = "다른 사람들에게 노출되는 번호이기 때문에, 개인 핸드폰 번호가 아닌 안심번호, 가게 유선 전화번호로 등록하는 것이 권장돼요."
            )
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }
        
        item {
            DefaultButton(buttonText = "다음") {
                if(storeNumber.isEmpty() || PhoneNumberUtils().isValidStoreNumber(storeNumber))
                    onFinish()
                else
                    isError.value = true
            }
        }
    }
}