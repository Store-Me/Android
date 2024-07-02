package com.store_me.storeme.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.ui.theme.HomeSearchBoxColor
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.ToastMessageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { TopLayout() }, // TopLayout을 topBar로 사용
        content = { innerPadding -> // 컨텐츠 영역
            Column(modifier = Modifier.padding(innerPadding)) {
                LocationLayout()
            }
        }
    )
}

@Composable
fun TopLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.width(20.dp))
        Image(
            painter = painterResource(id = R.drawable.storeme_logo_black),
            contentDescription = "로고",
            modifier = Modifier
                .height(20.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        SearchField(modifier = Modifier
            .weight(1f)
            .height(40.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.notification_off),
            contentDescription = "알림",
            modifier = Modifier
                .size(40.dp)
                .padding(7.dp)
        )
        Spacer(modifier = Modifier.width(13.dp))
    }
}
@Composable
fun SearchField(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .height(36.dp)
            .background(HomeSearchBoxColor, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            textStyle = storeMeTypography.bodySmall,
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(
                                text = "내 주변 가게를 찾아보세요.",
                                color = Color.Gray,
                                style = storeMeTypography.bodySmall
                            )
                        }
                        innerTextField()
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
                        contentDescription = "검색",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun LocationLayout(){
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 20.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = false, radius = 25.dp),  // Ripple 색상

                    onClick = {
                        ToastMessageUtils.showToast(context, "지역 설정")
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "강남구",
                style = storeMeTypography.labelMedium
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "지역 설정 아이콘",
                modifier = Modifier.size(12.dp)
            )
        }
        Spacer(Modifier.weight(1f)) //중간 공백

        SetLocationButton(
            onClick = {
            /*  */
            }
        )
    }
}

@Composable
fun SetLocationButton(onClick: () -> Unit){
    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(30.dp)
            .padding(end = 20.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = "동네 설정", style = storeMeTypography.labelSmall)
    }
}