@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import java.time.LocalDate

enum class TextFieldErrorType {
    STORE_NAME,
    LINK, DESCRIPTION, DATE, NAME, COUPON_CONTENT, MENU_DESCRIPTION, MENU_NAME, MENU_CATEGORY_NAME,
    COUPON_QUANTITY, COUPON_RATE, PRICE
}

@Composable
fun DefaultOutlineTextField(
    text: String,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    errorType: TextFieldErrorType? = null,
    onValueChange: (String) -> Unit,
    onErrorChange: (Boolean) -> Unit = {},
    singleLine: Boolean = true,
    enabled: Boolean = true,
    exceptText: String = ""
) {
    val urlList = Auth.linkListData.value.urlList

    val errorMessage = when (errorType) {
        TextFieldErrorType.LINK -> {
            if (urlList.contains(text))
                "이미 존재하는 링크입니다."
            else if (text.isNotEmpty() && !text.startsWith("http://") && !text.startsWith("https://"))
                "링크는 https:// 혹은 http:// 로 시작해야 합니다."
            else
                null
        }
        TextFieldErrorType.DESCRIPTION -> {
            if(text.length > 100) {
                "100자 이내로 작성해야 합니다."
            } else
                null
        }
        TextFieldErrorType.MENU_DESCRIPTION -> {
            if(text.length > 50) {
                "50자 이내로 작성해야 합니다."
            } else
                null
        }
        TextFieldErrorType.NAME -> {
            if(text.length > 15) {
                "15자 이내로 작성해야 합니다."
            } else
                null
        }
        TextFieldErrorType.MENU_NAME -> {
            when {
                text.length > 15 -> {
                    "15자 이내로 작성해야 합니다."
                }
                Auth.menuCategoryList.value.any { category ->
                    category.menuList.any { menu ->
                        menu.name == text && exceptText != menu.name
                    }
                } -> {
                    "같은 이름의 메뉴가 존재합니다."
                }
                else -> {
                    null
                }
            }
        }
        TextFieldErrorType.MENU_CATEGORY_NAME -> {
            when {
                text.length > 20 -> {
                    "20자 이내로 작성해야 합니다."
                }
                Auth.menuCategoryList.value.any { category ->
                    category.categoryName == text && exceptText != category.categoryName
                } -> {
                    "같은 이름의 카테고리가 존재합니다."
                }
                else -> {
                    null
                }
            }
        }
        TextFieldErrorType.COUPON_CONTENT -> {
            if(text.length > 15) {
                "15자 이내로 작성해야 합니다."
            } else
                null
        }
        TextFieldErrorType.STORE_NAME -> {
            if(text.length > 30) {
                "30자 이내로 작성해야 합니다."
            } else
                null
        }

        else -> null
    }

    LaunchedEffect(errorMessage) {
        onErrorChange(errorMessage != null)
    }


    /*DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                focusManager.clearFocus()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }*/

    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        singleLine = singleLine,
        keyboardOptions = if(singleLine) KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ) else KeyboardOptions.Default,
        placeholder = {
            Text(
                text = placeholderText,
                style = storeMeTextStyle(FontWeight.Normal, 0)
            )
        },
        textStyle = storeMeTextStyle(FontWeight.Normal, 0),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HighlightTextFieldColor,
            errorBorderColor = ErrorTextFieldColor,
            errorLabelColor = ErrorTextFieldColor,
        ),
        label = { when(errorMessage) {
            null -> {  }
            else -> { Text(text = errorMessage, style = storeMeTextStyle(FontWeight.Normal, 0)) }
        } },
        isError = errorMessage != null,
        trailingIcon = {
            if(text.isNotEmpty())
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_text_clear),
                        contentDescription = "모두 지우기 아이콘",
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape),
                        tint = Color.Unspecified
                    )
                }
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        enabled = enabled,
    )
}

@Composable
fun TextLengthRow(text: String, limitSize: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ){
        Text(
            text = text.length.toString(),
            style = storeMeTextStyle(FontWeight.Bold, 2),
            color = if(text.length > limitSize) ErrorTextFieldColor else Black
        )

        Text(
            text = "/${limitSize}",
            style = storeMeTextStyle(FontWeight.Bold, 2),
            color = UndefinedTextColor
        )
    }
}

fun Modifier.addFocusCleaner(
    focusManager: FocusManager,
    doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}

fun filterNonNumeric(text: String): String {
    return text.filter { it in '0'..'9' }
}

@Composable
fun DateOutLineTextField(
    selectedDate: LocalDate?,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    errorType: TextFieldErrorType? = TextFieldErrorType.DATE,
    onErrorChange: (Boolean) -> Unit = {},
    onClick: () -> Unit
) {
    val dateText =
        if(selectedDate == null)
            ""
        else {
            val date = DateTimeUtils().localDateToDateData(selectedDate)

            "${date.year}년 ${date.month}월 ${date.day}일 까지"
        }

    val errorMessage = when (errorType) {
        TextFieldErrorType.DATE -> {
            if(selectedDate != null && selectedDate.isBefore(LocalDate.now()))
                "오늘 이전 날짜는 선택이 불가능합니다."
            else
                null
        }

        else -> null
    }

    LaunchedEffect(errorMessage) {
        onErrorChange(errorMessage != null)
    }

    OutlinedTextField(
        value = dateText,
        onValueChange = {  },
        placeholder = { Text(
            text = placeholderText,
            style = storeMeTextStyle(FontWeight.Normal, 0)) },
        textStyle = storeMeTextStyle(FontWeight.Normal, 0),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HighlightTextFieldColor,
            disabledBorderColor = if(errorMessage == null) Black else ErrorTextFieldColor,
            disabledTextColor = Black,
        ),
        label = { when(errorMessage) {
            null -> {  }
            else -> {
                Text(
                    text = errorMessage,
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    color = ErrorTextFieldColor
                )
            }
        } },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = "달력 아이콘",
                modifier = Modifier
                    .size(18.dp),
                tint = Black
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = null
            ),
        enabled = false,
    )
}

@Composable
fun NumberOutLineTextField(
    text: String,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    onValueChange: (Int?) -> Unit,
    suffixText: String = "",
    errorType: TextFieldErrorType? = null,
    onErrorChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {

    val errorMessage = when (errorType) {
        TextFieldErrorType.COUPON_QUANTITY -> {
            if(text.toIntOrNull() == null && text.isNotEmpty()) {
                "숫자만 입력이 가능합니다."
            } else if (text.length > 4) {
                "최대 9999장의 쿠폰 생성이 가능합니다."
            } else
                null
        }
        TextFieldErrorType.PRICE -> {
            if (text.toIntOrNull() == null && text.isNotEmpty()) {
                "숫자만 입력이 가능합니다."
            } else if (text.length > 8) {
                "최대 99,999,999 까지만 입력이 가능합니다."
            } else
                null
        }
        TextFieldErrorType.COUPON_RATE -> {
            if(text.toIntOrNull() == null && text.isNotEmpty()) {
                "숫자만 입력이 가능합니다."
            } else if ((text.toIntOrNull() ?: 0) !in 0..100) {
                "유효한 범위 (0 ~ 100)의 숫자를 입력해 주세요."
            } else
                null
        }

        else -> null
    }

    LaunchedEffect(errorMessage) {
        onErrorChange(errorMessage != null)
    }

    //쉼표 포함 시키는 함수
    fun formatNumberWithCommas(input: String): String {
        return input.replace(",", "").toIntOrNull()?.let {
            "%,d".format(it)
        } ?: ""
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            when(errorType) {
                TextFieldErrorType.COUPON_QUANTITY -> {
                    if (it.length <= 4){
                        onValueChange(it.toIntOrNull())
                    }
                }
                TextFieldErrorType.PRICE -> {
                    if (it.length <= 8) {
                        onValueChange(it.toIntOrNull())
                    }
                }
                TextFieldErrorType.COUPON_RATE -> {
                    if (it.length <= 3){
                        onValueChange(it.toIntOrNull())
                    }
                }

                else -> {
                    onValueChange(it.toIntOrNull())
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        placeholder = { Text(
            text = placeholderText,
            style = storeMeTextStyle(FontWeight.Normal, 0)) },
        textStyle = storeMeTextStyle(FontWeight.Normal, 0),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HighlightTextFieldColor,
            errorBorderColor = ErrorTextFieldColor,
            errorLabelColor = ErrorTextFieldColor,
        ),
        suffix = { Text(text = suffixText, style = storeMeTextStyle(FontWeight.Normal, 0)) },
        label = { when(errorMessage) {
            null -> {  }
            else -> { Text(text = errorMessage, style = storeMeTextStyle(FontWeight.Normal, 0)) }
        } },
        isError = errorMessage != null,
        trailingIcon = {
            if(text.isNotEmpty())
                IconButton(onClick = {
                    onValueChange(null)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_text_clear),
                        contentDescription = "모두 지우기 아이콘",
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape),
                        tint = Color.Unspecified
                    )
                }
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        enabled = enabled
    )
}

@Composable
fun KeyBoardInputField(
    text: String,
    placeholderText: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    var hasFocus by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(
                color = Black.copy(alpha = 0.3f)
            )
            .clickable(
                onClick = { onDismiss() },
                indication = null,
                interactionSource = null
            )
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(White)
                .imePadding(),
            verticalArrangement = Arrangement.Bottom
        ) {
            TextField(
                value = text,
                onValueChange = { onValueChange(it) },
                textStyle = storeMeTextStyle(FontWeight.Normal, 2),
                placeholder = { Text(
                    text = placeholderText,
                    style = storeMeTextStyle(FontWeight.Bold, 2)
                ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (!it.hasFocus && hasFocus) {
                            onDismiss()
                        }
                        hasFocus = it.hasFocus
                    },
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismiss) {
                    Text("취소")
                }
                TextButton(onClick = {
                    onSend(text)
                    keyboardController?.hide() // 키보드 숨기기
                }) {
                    Text("전송")
                }
            }
        }
    }



    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}