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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.PwOutlinedTextField
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.onboarding.OnboardingActivity
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.KakaoLoginButtonColor
import com.store_me.storeme.ui.theme.LoginDividerColor
import com.store_me.storeme.ui.theme.StoreMeLoginButtonColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.KakaoLoginHelper
import com.store_me.storeme.utils.ValidationUtils
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val snackbarHostState = LocalSnackbarHostState.current

    val loadingViewModel = LocalLoadingViewModel.current

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val isKakaoLoginFailed by loginViewModel.isKakaoLoginFailed.collectAsState()

    val accountId by loginViewModel.accountId.collectAsState()
    val accountPw by loginViewModel.accountPw.collectAsState()

    val isIdError = remember { mutableStateOf(false) }
    val isPwError = remember { mutableStateOf(false) }

    val loginErrorMessage by loginViewModel.errorMessage.collectAsState()

    LaunchedEffect(isKakaoLoginFailed) {
        if(isKakaoLoginFailed){
           loginViewModel.clearKakaoLoginFailedState()
            navController.navigate(OnboardingActivity.Screen.Signup.route.name + "/${LoginType.KAKAO}?additionalData=${loginViewModel.kakaoId.value}")
        }
    }

    LaunchedEffect(accountId) {
        if(isIdError.value) {
            isIdError.value = !ValidationUtils.isValidId(accountId)
        }
    }

    LaunchedEffect(accountPw) {
        if(isPwError.value) {
            isPwError.value = !ValidationUtils.isValidPw(accountPw)
        }
    }

    LaunchedEffect(loginErrorMessage) {
        if(loginErrorMessage != null) {
            loadingViewModel.hideLoading()

            snackbarHostState.showSnackbar(loginErrorMessage.toString())

            loginViewModel.updateErrorMessage(null)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { StoreMeSnackbar(snackbarData = it) }
        ) },
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.logo_color),
                        contentDescription = "로고",
                        modifier = Modifier
                            .size(120.dp)
                    )
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.logo_home),
                        contentDescription = "로고 텍스트",
                        modifier = Modifier
                            .width(120.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "내 주변 나만의 가게들",
                        style = storeMeTextStyle(FontWeight.Normal, 6)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(48.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "카카오 계정으로 시작하기",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = KakaoLoginButtonColor,
                            contentColor = Black
                        ),
                        diffValue = 1,
                        fontWeight = FontWeight.Bold,
                        leftIconResource = R.drawable.ic_kakao,
                        leftIconTint = Black
                    ) {
                        loadingViewModel.showLoading()

                        coroutineScope.launch {
                            when (val kakaoId = KakaoLoginHelper.getKakaoId(context)) {
                                null -> {
                                    loadingViewModel.hideLoading()

                                    loginViewModel.updateErrorMessage(context.getString(R.string.kakao_account_error_message))
                                }
                                else -> {
                                    loginViewModel.loginWithKakao(kakaoId)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "회원가입 하기",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StoreMeLoginButtonColor,
                            contentColor = Black
                        ),
                        diffValue = 1,
                        fontWeight = FontWeight.Bold
                    ) {
                        navController.navigate(OnboardingActivity.Screen.Signup.route.name + "/${LoginType.APP}")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    LoginHorizontalDivider()
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    AccountIdSection(
                        value = accountId,
                        onValueChange = { loginViewModel.updateAccountId(it) },
                        onClearText = { loginViewModel.updateAccountId("") },
                        isError = isIdError.value
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                item {
                    AccountPwSection(
                        value = accountPw,
                        onValueChange = { loginViewModel.updateAccountPw(it) },
                        isError = isPwError.value
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "로그인"
                    ) {
                        if(accountId.isEmpty() || accountPw.isEmpty()){
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("아이디와 비밀번호를 모두 입력해 주세요.")
                                return@launch
                            }

                            return@DefaultButton
                        }

                        when {
                            !ValidationUtils.isValidId(accountId) && !ValidationUtils.isValidPw(accountPw) -> {
                                isIdError.value = true
                                isPwError.value = true
                            }
                            !ValidationUtils.isValidId(accountId) -> {
                                isIdError.value = true
                            }
                            !ValidationUtils.isValidPw(accountPw) -> {
                                isPwError.value = true
                            }
                            else -> {
                                loginViewModel.loginWithApp()
                            }
                        }
                    }
                }

            }
        }
    )
}

@Composable
fun LoginHorizontalDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 0.5.dp,
            color = LoginDividerColor
        )

        Text(
            text = "또는",
            style = storeMeTextStyle(FontWeight.Bold, 1),
            color = LoginDividerColor
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 0.5.dp,
            color = LoginDividerColor
        )
    }
}

@Composable
fun AccountIdSection(
    value: String,
    onValueChange: (String) -> Unit,
    onClearText: () -> Unit,
    isError: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "아이디",
            style = storeMeTextStyle(FontWeight.Bold, 1),
            color = Black
        )

        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            textStyle = storeMeTextStyle(FontWeight.Normal, 1),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = {
                if(value.isNotEmpty()){
                    IconButton(onClick = { onClearText() }) {
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
                    text = "아이디를 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 1),
                    color = UndefinedTextColor
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HighlightTextFieldColor,
                errorBorderColor = ErrorTextFieldColor,
                errorLabelColor = ErrorTextFieldColor,
            ),
            isError = isError,
            supportingText = {
                if(isError){
                    Text(
                        text = "4 ~ 20 글자 영어 혹은 숫자로 구성되어야 합니다.",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = ErrorTextFieldColor
                    )
                }
            }
        )
    }
}

@Composable
fun AccountPwSection(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    val isHidePw = remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "비밀번호",
            style = storeMeTextStyle(FontWeight.Bold, 1)
        )

        PwOutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            isHidePw = isHidePw.value,
            onHideValueChange = { isHidePw.value = !isHidePw.value },
            isError = isError,
            imeAction = ImeAction.Done
        )
    }
}