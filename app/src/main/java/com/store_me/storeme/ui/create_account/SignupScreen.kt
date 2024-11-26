package com.store_me.storeme.ui.create_account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.ui.component.StrokeButton

@Composable
fun SignupScreen(navController: NavController, signupViewModel: SignupViewModel = hiltViewModel()) {
    val context = LocalContext.current

    // 갤러리에서 이미지 선택 런처 설정
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // ViewModel에 URI 저장
            signupViewModel.setImageUri(it)
            signupViewModel.signup()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        StrokeButton(text = "가입") {
            launcher.launch("image/*")
        }
    }
}