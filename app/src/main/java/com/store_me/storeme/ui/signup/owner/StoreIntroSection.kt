package com.store_me.storeme.ui.signup.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreSignupDataViewModel

@Composable
fun StoreIntroSection(onFinish: () -> Unit) {
    val storeDataViewModel = LocalStoreSignupDataViewModel.current

    val storeIntro by storeDataViewModel.storeIntro.collectAsState()

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(storeIntro) {
        isError.value = storeIntro.length > 100
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            SignupTitleText(
                title = "스토어 소개글을 입력해 주세요. (선택)",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "지금 입력 후 나중에도 자유롭게 변경할 수 있어요.",
                style = storeMeTextStyle(FontWeight.Normal, 2),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }

        item {
            TextField(
                value = storeIntro,
                onValueChange = { storeDataViewModel.updateStoreIntro(it) },
                textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                placeholder = {
                    Text(
                        text = "손님들에게 알리고 싶은 내용을 남겨보세요.\n우리 스토어만의 차별점과 특별한 서비스를 안내하면 좋아요.",
                        style = storeMeTextStyle(FontWeight.Normal, 1),
                        color = UndefinedTextColor
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
                    errorTextColor = ErrorColor
                ),
                singleLine = false,
                minLines = 2,    //1 -> 2줄 변화시 글자 크기 문제 해결
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
            DefaultHorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                TextLengthRow(text = storeIntro, limitSize = 100)
            }
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                DefaultButton(
                    buttonText = "다음",
                    enabled = !isError.value
                ) {
                    onFinish()
                }
            }
        }
    }
}