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
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.signup.GuidTextBoxItem
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreDataViewModel

@Composable
fun StoreNameSection(onFinish: () -> Unit) {
    val storeDataViewModel = LocalStoreDataViewModel.current

    val storeName by storeDataViewModel.storeName.collectAsState()

    val isError = remember { mutableStateOf(false) }

    val annotatedString = buildAnnotatedString {
        append("스토어미 안에서 손님이 검색하거나\n주변 가게로 노출되는 ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) {
            append("대표 이름")
        }
        append("이에요.")
    }

    LaunchedEffect(storeName) {
        isError.value = storeName.length > 30
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item {
            SignupTitleText(title = "스토어 이름을 입력해주세요.")
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

        //스토어 이름 입력
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "스토어 이름",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                OutlinedTextField(
                    value = storeName,
                    onValueChange = {
                        storeDataViewModel.updateStoreName(it)
                    },
                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        if(storeName.isNotEmpty()){
                            IconButton(onClick = { storeDataViewModel.updateStoreName("") }) {
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
                            text = "스토어 이름을 입력해주세요.",
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
                                text = "스토어 이름은 30자 이내로 작성되어야 합니다.",
                                style = storeMeTextStyle(FontWeight.Normal, 0),
                                color = ErrorTextFieldColor
                            )
                        }
                    }
                )
            }

            TextLengthRow(text = storeName, limitSize = 30)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            GuidTextBoxItem(
                title = "스토어 이름 가이드",
                content = "업체명 또는 간판명으로 입력하거나\n스토어를 대표할 수 있는 이름으로 입력해주세요.\n\n스토어 이름은 운영정책에 따라 임의로 수정되거나 제재될 수 있어요."
            )
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }

        item {
            DefaultButton(
                buttonText = "다음",
                enabled = !isError.value && storeName.isNotEmpty()
            ) {
                onFinish()
            }
        }
    }
}