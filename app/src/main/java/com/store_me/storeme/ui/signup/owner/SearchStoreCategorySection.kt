@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.store_me.storeme.ui.signup.owner

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.TextClearIconColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreDataViewModel

@Composable
fun SearchStoreCategorySection(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onClick: () -> Unit
) {
    val storeDataViewModel = LocalStoreDataViewModel.current

    val storeCategory by storeDataViewModel.storeCategory.collectAsState()

    BackHandler {
        onClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        with(sharedTransitionScope) {
            OutlinedTextField(
                value = storeCategory?.displayName ?: "",
                onValueChange = {  },
                textStyle = storeMeTextStyle(FontWeight.Normal, 1, isFixedSize = true),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { onClick() },
                        interactionSource = null,
                        indication = null
                    )
                    .sharedElement(
                        rememberSharedContentState(key = "category_search"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "검색",
                        modifier = Modifier
                            .size(24.dp),
                        tint = TextClearIconColor
                    )
                },
                placeholder = {
                    Text(
                        text = "업종을 선택해주세요.",
                        style = storeMeTextStyle(FontWeight.Normal, 1, isFixedSize = true),
                        color = UndefinedTextColor
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HighlightTextFieldColor,
                    errorBorderColor = ErrorTextFieldColor,
                    errorLabelColor = ErrorTextFieldColor,
                    disabledBorderColor = Color.Black,
                    disabledTextColor = if(storeCategory != null) Color.Black else Color.Unspecified
                ),
                supportingText = {

                },
                enabled = false
            )
        }

        StoreCategoryList {
            onClick()
        }
    }
}