@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.store_me.storeme.ui.signup.owner

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.store_me.storeme.ui.theme.DeleteTextColor
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.TextClearIconColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreSignupDataViewModel
import com.store_me.storeme.utils.StoreCategory

@Composable
fun StoreCategorySection(onFinish: () -> Unit) {
    val storeDataViewModel = LocalStoreSignupDataViewModel.current

    val storeCategory by storeDataViewModel.storeCategory.collectAsState()

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) {
            append("어떤 서비스")
        }
        append("를 제공하는지 손님들에게 알릴 수 있어요.")
    }

    val onSearch = remember { mutableStateOf(false) }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = onSearch.value,
            label = ""
        ) { targetState ->
            if(targetState) {
                //검색
                SearchStoreCategorySection(
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                ) {
                    onSearch.value = false
                }
            } else {
                //일반
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxSize()
                ) {
                    item {
                        SignupTitleText(title = "스토어 업종을 설정해 주세요.")
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

                    //스토어 업종 선택
                    item {
                        OutlinedTextField(
                            value = storeCategory?.displayName ?: "",
                            onValueChange = {  },
                            textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = { onSearch.value = !onSearch.value },
                                    interactionSource = null,
                                    indication = null
                                )
                                .sharedElement(
                                    rememberSharedContentState(key = "category_search"),
                                    animatedVisibilityScope = this@AnimatedContent
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
                                    text = "업종을 선택해 주세요.",
                                    style = storeMeTextStyle(FontWeight.Normal, 1),
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

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        GuidTextBoxItem(
                            title = "업종 입력 가이드",
                            content = "스토어에서 제공하는 서비스를 등록해 주세요.\n스토어를 대표할 수 있는 서비스를 등록해보세요."
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        AdditionalGuidText(text = "· 한 번 설정한 업종은 고객센터를 통한 변경만 가능해요.")

                        Spacer(modifier = Modifier.height(4.dp))

                        AdditionalGuidText(text = "· 내부 검수 과정에서 더 적합한 업종이 추가될 수 있어요.")
                    }

                    item {
                        Spacer(modifier = Modifier.height(36.dp))
                    }

                    item {
                        DefaultButton(
                            buttonText = "다음",
                            enabled = storeCategory != null
                        ) {
                            onFinish()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoreCategoryList(onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StoreCategory.entries.forEach {
            if(it != StoreCategory.ALL) {
                StoreCategoryItem(storeCategory = it) {
                    onClick()
                }
            }
        }
    }
}

@Composable
fun StoreCategoryItem(storeCategory: StoreCategory, onClick: () -> Unit) {
    val storeDataViewModel = LocalStoreSignupDataViewModel.current
    val selectedCategory = storeDataViewModel.storeCategory.collectAsState()
    
    val isChecked = selectedCategory.value == storeCategory

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    storeDataViewModel.updateStoreCategory(storeCategory)
                    onClick()
                },
                indication = null,
                interactionSource = null
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = storeCategory.displayName,
            style = storeMeTextStyle(FontWeight.Normal, 2),
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = if(isChecked) R.drawable.ic_check_on else R.drawable.ic_check_off),
            contentDescription = "체크",
            tint = if(isChecked) Color.Black else DeleteTextColor,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
fun AdditionalGuidText(text: String) {
    Text(
        text = text,
        style = storeMeTextStyle(FontWeight.Bold, -1),
        color = UndefinedTextColor
    )
}