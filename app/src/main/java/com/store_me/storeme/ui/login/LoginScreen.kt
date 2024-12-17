package com.store_me.storeme.ui.login

import androidx.compose.foundation.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.component.LargeButton
import com.store_me.storeme.ui.onboarding.OnboardingActivity
import com.store_me.storeme.ui.theme.KakaoLoginButtonColor
import com.store_me.storeme.ui.theme.LoginDividerColor
import com.store_me.storeme.ui.theme.StoreMeLoginButtonColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.KakaoLoginHelper
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val isKakaoLoginFailed by loginViewModel.isKakaoLoginFailed.collectAsState()

    LaunchedEffect(isKakaoLoginFailed) {
        if(isKakaoLoginFailed){
            loginViewModel.clearKakaoLoginFailedState()
            navController.navigate(OnboardingActivity.Screen.Signup.route.name + "/${Auth.LoginType.KAKAO}")
        }
    }

    Scaffold(
        containerColor = Color.White,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_color),
                    contentDescription = "로고",
                    modifier = Modifier
                        .size(120.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.logo_home),
                    contentDescription = "로고 텍스트",
                    modifier = Modifier
                        .width(120.dp)
                )

                Text(
                    text = "내 주변 나만의 가게들",
                    style = storeMeTextStyle(FontWeight.Normal, 6, isFixedSize = true),
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 24.dp)
                )

                LargeButton(
                    text = "카카오 계정으로 시작하기",
                    iconResource = R.drawable.ic_kakao,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    containerColor = KakaoLoginButtonColor,
                    contentColor = Black
                ) {
                    coroutineScope.launch {

                        when (val kakaoId = KakaoLoginHelper.getKakaoId(context)) {
                            null -> {}
                            else -> {
                                loginViewModel.loginWithKakao(kakaoId)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LargeButton(
                    text = "회원가입 하기",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    containerColor = StoreMeLoginButtonColor,
                    contentColor = Black
                ) {
                    navController.navigate(OnboardingActivity.Screen.Signup.route.name)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 0.5.dp,
                        color = LoginDividerColor
                    )

                    Text(
                        text = "또는",
                        style = storeMeTextStyle(FontWeight.Bold, 1, isFixedSize = true),
                        color = LoginDividerColor
                    )

                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 0.5.dp,
                        color = LoginDividerColor
                    )
                }


            }
        }
    )
}
