package com.store_me.storeme.ui.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun DefaultOutlineTextField(
    text: String,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    errorType: TextFieldErrorType? = null,
    onValueChange: (String) -> Unit,
    onErrorChange: (Boolean) -> Unit = {},
    singleLine: Boolean = true
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val urlList = Auth.linkListData.value?.urlList.orEmpty()

    val errorMessage = when (errorType) {
        TextFieldErrorType.LINK -> {
            if (urlList.contains(text))
                "이미 존재하는 링크입니다."
            else if (text.isNotEmpty() && !text.startsWith("http://") && !text.startsWith("https://"))
                "링크는 https:// 혹은 http:// 로 시작해야 합니다."
            else
                null
        }
        TextFieldErrorType.OPENING_HOURS_DESCRIPTION -> {
            if(text.length > 100) {
                "100자 이내로 작성해야 합니다."
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
        placeholder = { Text(
            text = placeholderText,
            style = storeMeTextStyle(FontWeight.Normal, 0)) },
        textStyle = storeMeTextStyle(FontWeight.Normal, 0),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HighlightTextFieldColor,
            errorBorderColor = ErrorTextFieldColor,
            errorLabelColor = ErrorTextFieldColor
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
            .wrapContentHeight()
    )
}

enum class TextFieldErrorType {
    LINK, OPENING_HOURS_DESCRIPTION
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