@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.response.MyStoresResponse
import com.store_me.storeme.ui.theme.CancelButtonColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils

@Composable
fun WarningDialog(
    title: String,
    warningContent: String? = null,
    content: String?,
    actionText: String,
    onDismiss: () -> Unit,
    onAction: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .background(shape = RoundedCornerShape(20.dp), color = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Icon(
                painterResource(id = R.drawable.ic_warning),
                contentDescription = "경고 아이콘",
                tint = Color.Unspecified,
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = title,
                style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                textAlign = TextAlign.Center
            )

            if(!warningContent.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text= warningContent,
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                )
            }

            if(!content.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text= content,
                    style = storeMeTextStyle(FontWeight.Bold, 2),
                    color = UnselectedItemColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LargeButton(text = "취소", containerColor = CancelButtonColor, contentColor = Color.Black, modifier = Modifier.weight(1f)) {
                    onDismiss()
                }

                LargeButton(text = actionText, containerColor = Color.Black, contentColor = Color.White, modifier = Modifier.weight(1f)) {
                    onAction()
                }
            }
        }
    }
}

@Composable
fun BackWarningDialog(
    onDismiss: () -> Unit,
    onAction: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .background(shape = RoundedCornerShape(20.dp), color = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Icon(
                painterResource(id = R.drawable.ic_warning),
                contentDescription = "경고 아이콘",
                tint = Color.Unspecified,
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "이전 페이지로 이동할까요?", style = storeMeTextStyle(FontWeight.ExtraBold, 6))

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text= "이전 페이지로 이동하면 현재 작성된 내용은 사라지고, 진행중이었던 작업은 처음부터 진행해야해요.",
                style = storeMeTextStyle(FontWeight.Bold, 2),
                color = UnselectedItemColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LargeButton(text = "취소", containerColor = CancelButtonColor, contentColor = Color.Black, modifier = Modifier.weight(1f)) {
                    onDismiss()
                }

                LargeButton(text = "확인", containerColor = Color.Black, contentColor = Color.White, modifier = Modifier.weight(1f)) {
                    onAction()
                }
            }
        }
    }
}