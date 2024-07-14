package com.store_me.storeme.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.ui.theme.HomeSearchBoxColor
import com.store_me.storeme.ui.theme.storeMeTypography

/*
* 여러 곳에서 사용 되는 Composable 함수 모음
* */
@Composable
fun TitleWithDeleteButton(navController: NavController, title: String){
    val interactionSource = remember { MutableInteractionSource() }
    val clickable = remember { mutableStateOf(true) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            style = storeMeTypography.labelLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
            contentDescription = "닫기",
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    onClick = {
                        if (navController.currentBackStackEntry?.destination?.id != navController.graph.startDestinationId) {
                            clickable.value = false
                            navController.popBackStack()
                        }
                    },
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = false)
                )
                .padding(2.dp)
        )
    }
}

/**
 * 기본 버튼
 */
@Composable
fun DefaultButton(text: String, onClick: () -> Unit){
    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(26.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = text, style = storeMeTypography.labelSmall)
    }
}

/**
 * 검색 완료 결과를 람다 함수로 반환 하는 검색 창
 */
@Composable
fun SearchField(modifier: Modifier = Modifier, hint: String, onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .height(40.dp)
            .background(HomeSearchBoxColor, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            textStyle = storeMeTypography.bodySmall,
            cursorBrush = SolidColor(Color.Black),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                autoCorrectEnabled = false
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(text)
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(
                                text = hint,
                                color = Color.Gray,
                                style = storeMeTypography.bodySmall
                            )
                        }
                        innerTextField()
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
                        contentDescription = "검색",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable (
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false, radius = 15.dp),
                                onClick = {
                                    onSearch(text)
                                }
                            )
                    )
                }
            }
        )
    }
}