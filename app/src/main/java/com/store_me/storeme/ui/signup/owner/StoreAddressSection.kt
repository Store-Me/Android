@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.store_me.storeme.ui.signup.owner

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.viewinterop.AndroidView
import com.store_me.storeme.R
import com.store_me.storeme.data.response.DaumPostcodeResponse
import com.store_me.storeme.ui.signup.NextButton
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.DeleteTextColor
import com.store_me.storeme.ui.theme.DisabledColor
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightGuidTextColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.TextClearIconColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreDataViewModel

@Composable
fun AddressSection(onFinish: () -> Unit) {
    val storeDataViewModel = LocalStoreDataViewModel.current

    val storeLocationAddress by storeDataViewModel.storeLocationAddress.collectAsState()
    val storeLocationDetail by storeDataViewModel.storeLocationDetail.collectAsState()
    val storeLocationCode by storeDataViewModel.storeLocationCode.collectAsState()
    val hasAddress by storeDataViewModel.hasAddress.collectAsState()

    val showWebView = remember { mutableStateOf(false) }

    val isError = remember { mutableStateOf(false) }

    val onSearch = remember { mutableStateOf(false) }

    val annotatedString = buildAnnotatedString {
        append("손님들이 ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) {
            append("방문할 수 있는 주소")
        }
        append("를 입력해주세요.\n주소가 없다면 ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) {
            append("주소가 없어요")
        }
        append("를 체크해주세요.")
    }

    LaunchedEffect(storeLocationDetail) {
        isError.value = storeLocationDetail.length > 100
    }

    if(showWebView.value) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddressWebView(
                onAddressSelected = { daumPostcodeResponse ->
                    storeDataViewModel.updateDaumPostcodeResponse(daumPostcodeResponse)
                    showWebView.value = false
                },
                onCloseWebView = {
                    showWebView.value = false
                }
            )
        }
    } else {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = onSearch.value,
                label = ""
            ) { targetState ->
                if(targetState) {
                    //검색
                    SearchStoreLocationSection(
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
                    ) {
                        item {
                            SignupTitleText(title = "주소를 설정해주세요.")
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

                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "주소",
                                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                                    color = if(hasAddress) Color.Black else DisabledColor
                                )

                                OutlinedTextField(
                                    value = if(hasAddress) storeLocationAddress else "",
                                    onValueChange = {  },
                                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            onClick = { showWebView.value = true },
                                            interactionSource = null,
                                            indication = null
                                        ),
                                    shape = RoundedCornerShape(14.dp),
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_search),
                                            contentDescription = "검색",
                                            modifier = Modifier
                                                .size(24.dp),
                                            tint = if(hasAddress) TextClearIconColor else DisabledColor
                                        )
                                    },
                                    placeholder = {
                                        Text(
                                            text = "주소를 검색해 주세요.",
                                            style = storeMeTextStyle(FontWeight.Normal, 1),
                                            color = UndefinedTextColor
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = HighlightTextFieldColor,
                                        errorBorderColor = ErrorTextFieldColor,
                                        errorLabelColor = ErrorTextFieldColor,
                                        disabledBorderColor = if(hasAddress) Color.Black else DisabledColor,
                                        disabledTextColor = if(storeLocationAddress.isNotEmpty()) Color.Black else DisabledColor
                                    ),
                                    supportingText = {  },
                                    enabled = false
                                )

                                OutlinedTextField(
                                    value = storeLocationDetail,
                                    onValueChange = {
                                        storeDataViewModel.updateStoreLocationDetail(it)
                                    },
                                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    trailingIcon = {
                                        if(storeLocationDetail.isNotEmpty()){
                                            IconButton(onClick = { storeDataViewModel.updateStoreLocationDetail("") }) {
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
                                            text = "상세 주소를 입력해 주세요.",
                                            style = storeMeTextStyle(FontWeight.Normal, 1),
                                            color = UndefinedTextColor
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = HighlightTextFieldColor,
                                        errorBorderColor = ErrorTextFieldColor,
                                        errorLabelColor = ErrorTextFieldColor,
                                        disabledBorderColor = DisabledColor,
                                        disabledTextColor = DisabledColor,
                                        unfocusedBorderColor = Color.Black
                                    ),
                                    isError = isError.value,
                                    supportingText = {
                                        if(isError.value){
                                            Text(
                                                text = "상세 주소는 100자 이내로 작성되어야 합니다.",
                                                style = storeMeTextStyle(FontWeight.Normal, 0),
                                                color = ErrorTextFieldColor
                                            )
                                        }
                                    },
                                    enabled = hasAddress
                                )
                            }

                            //TextLengthRow(text = storeLocationDetail, limitSize = 100)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.weight(1f))

                                Row(
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                storeDataViewModel.updateStoreLocationAddress("")
                                                storeDataViewModel.updateStoreLocationDetail("")
                                                storeDataViewModel.updateHasAddress(!hasAddress)
                                            },
                                            indication = null,
                                            interactionSource = null
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "주소가 없어요",
                                        style = storeMeTextStyle(FontWeight.Normal, -1),
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Icon(
                                        painter = painterResource(id = if(!hasAddress) R.drawable.ic_check_on else R.drawable.ic_check_off),
                                        contentDescription = "체크",
                                        tint = if(!hasAddress) Color.Black else DeleteTextColor,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                            }
                        }

                        item {
                            AnimatedVisibility(!hasAddress) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "지역명",
                                        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                                        color = Color.Black
                                    )

                                    OutlinedTextField(
                                        value = if(!hasAddress) storeLocationAddress else "",
                                        onValueChange = {  },
                                        textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                onClick = { onSearch.value = true },
                                                interactionSource = null,
                                                indication = null
                                            )
                                            .sharedElement(
                                                rememberSharedContentState(key = "store_location_search"),
                                                animatedVisibilityScope = this@AnimatedContent
                                            ),
                                        shape = RoundedCornerShape(14.dp),
                                        trailingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_search),
                                                contentDescription = "검색",
                                                modifier = Modifier
                                                    .size(24.dp),
                                                tint = if(hasAddress) TextClearIconColor else DisabledColor
                                            )
                                        },
                                        placeholder = {
                                            Text(
                                                text = "지역을 검색해 주세요.",
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
                                            disabledTextColor = if(storeLocationAddress.isNotEmpty()) Color.Black else Color.Unspecified
                                        ),
                                        supportingText = {  },
                                        enabled = false
                                    )

                                    Text(
                                        text = "· 상세한 주소 대신 지역명을 등록해 주세요.",
                                        style = storeMeTextStyle(FontWeight.Bold, 0),
                                        color = HighlightGuidTextColor
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(48.dp))
                        }

                        item {
                            NextButton(
                                buttonText = "다음",
                                enabled = storeLocationAddress.isNotEmpty() && storeLocationCode != null
                            ) {
                                onFinish()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddressWebView(
    onAddressSelected: (DaumPostcodeResponse) -> Unit,
    onCloseWebView: () -> Unit
) {
    BackHandler {
        onCloseWebView()
    }

    AndroidView(
        factory = {
            WebView(it).apply {
                // WebView 설정
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()  // 자체 웹뷰에서 띄우도록 설정

                // 자바스크립트 인터페이스 등록
                addJavascriptInterface(
                    WebAppInterface(onAddressSelected),
                    "StoreMe"  // JS에서 window.StoreMe 로 접근
                )
                loadUrl("https://storeme.shop/html/daum_postcode.html")
            }
        },
        update = {  },
        modifier = Modifier
            .fillMaxSize()
    )
}