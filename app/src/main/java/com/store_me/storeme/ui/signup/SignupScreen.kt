package com.store_me.storeme.ui.signup

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.Auth.LoginType
import com.store_me.storeme.ui.component.StrokeButton

@Composable
fun SignupScreen(
    navController: NavController,
    loginType: LoginType,
    signupViewModel: SignupViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // 갤러리에서 이미지 선택 런처 설정
    /*val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // ViewModel에 URI 저장
            signupViewModel.setImageUri(it)
            signupViewModel.signup()
        }
    }*/


}