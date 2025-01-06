package com.store_me.storeme.ui.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.store_me.storeme.ui.component.DefaultButton

@Composable
fun FinishSection(onFinish: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        DefaultButton(buttonText = "가입 완료") {
            onFinish()
        }
    }
}