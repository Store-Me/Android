@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.TextClearIconColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun TextLengthRow(text: String, limitSize: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ){
        Text(
            text = text.length.toString(),
            style = storeMeTextStyle(FontWeight.Bold, 0),
            color = if(text.length > limitSize) ErrorTextFieldColor else Black
        )

        Text(
            text = "/${limitSize}",
            style = storeMeTextStyle(FontWeight.Bold, 0),
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
fun PwOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isHidePw: Boolean,
    onHideValueChange: () -> Unit,
    isError: Boolean,
    imeAction: ImeAction
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        textStyle = storeMeTextStyle(FontWeight.Normal, 1),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        visualTransformation = if (isHidePw) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = { onHideValueChange() }) {
                Icon(
                    painter = painterResource(id = if(isHidePw) R.drawable.ic_hide else R.drawable.ic_show),
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HighlightTextFieldColor,
            errorBorderColor = ErrorTextFieldColor,
            errorLabelColor = ErrorTextFieldColor,
        ),
        isError = isError,
        supportingText = {
            if(isError){
                Text(
                    text = "4 ~ 20 글자로 구성되어야 합니다.",
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    color = ErrorTextFieldColor
                )
            }
        }
    )
}

@Composable
fun SimpleOutLinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholderText: String,
    onValueChange: (String) -> Unit,
    trailingIconResource: Int? = null,
    isError: Boolean,
    errorText: String,
    enabled: Boolean = true,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = HighlightTextFieldColor,
        errorBorderColor = ErrorTextFieldColor,
        errorLabelColor = ErrorTextFieldColor,
    )
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        textStyle = storeMeTextStyle(FontWeight.Normal, 1),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        trailingIcon = {
            if(text.isNotEmpty() && trailingIconResource == null){
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_text_clear),
                        contentDescription = "삭제",
                        modifier = Modifier
                            .size(24.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            if(trailingIconResource != null) {
                Icon(
                    painter = painterResource(id = trailingIconResource),
                    contentDescription = "아이콘",
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color.Unspecified
                )
            }
        },
        placeholder = {
            Text(
                text = placeholderText,
                style = storeMeTextStyle(FontWeight.Normal, 1),
                color = UndefinedTextColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        colors = colors,
        isError = isError,
        supportingText = {
            if(isError){
                Text(
                    text = errorText,
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    color = ErrorTextFieldColor
                )
            }
        },
        enabled = enabled
    )
}

@Composable
fun SimpleNumberOutLinedTextField(
    text: String,
    placeholderText: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorText: String,
    suffixText: String? = null,
    enabled: Boolean = true,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = text,
        onValueChange = {
            if (it.length <= 11 && (it.isEmpty() || it.last().isDigit())) {
                onValueChange(it)
            }
        },
        textStyle = storeMeTextStyle(FontWeight.Normal, 1),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        enabled = enabled,
        trailingIcon = {
            if(text.isNotEmpty()){
                IconButton(onClick = { onValueChange("") }) {
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
                text = placeholderText,
                style = storeMeTextStyle(FontWeight.Normal, 1),
                color = UndefinedTextColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HighlightTextFieldColor,
            errorBorderColor = ErrorTextFieldColor,
            errorLabelColor = ErrorTextFieldColor,
        ),
        isError = isError,
        supportingText = {
            if(isError){
                Text(
                    text = errorText,
                    style = storeMeTextStyle(FontWeight.Normal, 0),
                    color = ErrorTextFieldColor
                )
            }
        },
        suffix = {
            if(!suffixText.isNullOrEmpty()) {
                Text(
                    text = suffixText,
                    style = storeMeTextStyle(FontWeight.Normal, 2),
                    color = GuideColor
                )
            }
        },
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    textStyle: TextStyle = storeMeTextStyle(FontWeight.ExtraBold, 4),
    singleLine: Boolean,
    minLines: Int = 1,
) {
    Box(
        modifier = modifier
    ) {
        if(value.isEmpty()) {
            Text(
                text = placeholderText,
                style = textStyle,
                color = GuideColor
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            textStyle = textStyle,
            singleLine = singleLine,
            minLines = minLines
        )
    }


}