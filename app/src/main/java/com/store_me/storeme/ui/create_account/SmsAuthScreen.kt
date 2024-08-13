package com.store_me.storeme.ui.create_account

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.login.LoginActivity

@Composable
fun SmsAuthScreen(navController: NavController, smsAuthViewModel: SmsAuthViewModel = viewModel()) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DefaultButton(text = "SMS 보내기") {

        }
    }
}