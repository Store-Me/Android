package com.store_me.storeme.ui.signup.owner

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
import com.store_me.storeme.ui.signup.GuideTextBoxItem
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreSignupDataViewModel
import com.store_me.storeme.utils.StoreCategory

@Composable
fun StoreCustomCategorySection(onFinish: () -> Unit) {
    val storeDataViewModel = LocalStoreSignupDataViewModel.current

    val storeDetailCategory by storeDataViewModel.storeDetailCategory.collectAsState()

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) {
            append("상세 업종을 직접 입력하여")
        }
        append("가게에 대한 정보를 손님들에게 제공할 수 있어요.")
    }

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(storeDetailCategory) {
        isError.value = storeDetailCategory.length > 15
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        item {
            SignupTitleText(title = "상세 업종을 입력해주세요. (선택)")
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

        //상세 업종 입력
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "상세 업종",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                OutlinedTextField(
                    value = storeDetailCategory,
                    onValueChange = {
                        storeDataViewModel.updateStoreDetailCategory(it)
                    },
                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    trailingIcon = {
                        if(storeDetailCategory.isNotEmpty()){
                            IconButton(onClick = { storeDataViewModel.updateStoreDetailCategory("") }) {
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
                            text = "세부 업종을 입력해주세요.",
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
                                text = "세부 업종은 15자 이내로 작성되어야 합니다.",
                                style = storeMeTextStyle(FontWeight.Normal, 0),
                                color = ErrorTextFieldColor
                            )
                        }
                    }
                )
            }

            TextLengthRow(text = storeDetailCategory, limitSize = 15)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            GuideTextBoxItem(
                title = "세부 업종 예시",
                content = "스토어 업종이 \'${StoreCategory.CAFE.displayName}\'인 경우\n세부 업종은 \'케이크 전문점\' 혹은 \'수제 쿠키 전문점\'과 같이 조금 더 자세하게 설명할 수 있어요."
            )
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }

        item {
            DefaultButton(
                buttonText = "다음",
                enabled = !isError.value
            ) {
                onFinish()
            }
        }
    }
}