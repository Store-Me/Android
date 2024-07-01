package com.store_me.storeme.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.store_me.storeme.R
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.utils.ToastMessageUtils

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            StoreMeTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }

    @Preview
    @Composable
    private fun LoginScreen() {
        /*var backPressedTime by remember { mutableStateOf(0L) }
        val context = LocalContext.current

        BackHandler {
            val currentTime = System.currentTimeMillis()
            if(currentTime - backPressedTime < 2000){
                finishAffinity()
            } else {
                backPressedTime = currentTime
                ToastMessageUtils.showToast(context, R.string.backpress_message)
            }
        }*/

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Login Screen")
        }
    }
}