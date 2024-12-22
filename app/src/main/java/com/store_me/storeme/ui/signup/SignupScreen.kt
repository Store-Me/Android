package com.store_me.storeme.ui.signup

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth.LoginType
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils

val LocalSignupViewModel = staticCompositionLocalOf<SignupViewModel> {
    error("No LocalSignupViewModel provided")
}

val LocalTermsViewModel = staticCompositionLocalOf<TermsViewModel> {
    error("No LocalTermsVieWModel Provided")
}

@Composable
fun SignupScreen(
    navController: NavController,
    loginType: LoginType,
    signupViewModel: SignupViewModel = hiltViewModel(),
    termsViewModel: TermsViewModel = viewModel()
) {
    LaunchedEffect(loginType) {
        signupViewModel.setLoginType(loginType)
        Log.d("SignupScreen", loginType.name)
    }

    val signupState by signupViewModel.signupState.collectAsState()

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


    CompositionLocalProvider(
        LocalSignupViewModel provides signupViewModel, LocalTermsViewModel provides termsViewModel
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = White,
            topBar = {

            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = signupState,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                            } else {
                                (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                            }
                        },
                        label = ""
                    ) { targetState ->
                        when(targetState) {
                            is SignupState.Signup -> {
                                when(targetState.progress) {
                                    SignupProgress.TERMS -> {
                                        TermsSection()
                                    }
                                    SignupProgress.NUMBER -> {

                                    }
                                    SignupProgress.CERTIFICATION -> {

                                    }
                                    SignupProgress.ACCOUNT_DATA -> {

                                    }
                                    SignupProgress.ACCOUNT_TYPE -> {

                                    }
                                }
                            }
                            is SignupState.Onboarding -> {

                            }
                            is SignupState.Customer -> {

                            }
                            is SignupState.Owner -> {

                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun TermsSection() {
    val termsViewModel = LocalTermsViewModel.current

    val requiredTerms by termsViewModel.requiredTermsState.collectAsState()
    val optionalTerms by termsViewModel.optionalTermsState.collectAsState()

    LazyColumn(

    ) {
        item {
            Text(
                text = "서비스 이용을 위한\n약관에 동의해주세요.",
                style = storeMeTextStyle(FontWeight.ExtraBold, 6, isFixedSize = true),
                color = Black
            )
        }

        item { DefaultHorizontalDivider() }

        item { TermItem("서비스 이용 약관", "내용", requiredTerms[RequiredTerms.USE] ?: false) {
            termsViewModel.updateRequiredTerms(RequiredTerms.USE)
        } }

        item { TermItem("개인정보 이용 약관", "내용", requiredTerms[RequiredTerms.PRIVACY] ?: false) {
            termsViewModel.updateRequiredTerms(RequiredTerms.PRIVACY)
        } }

        item { TermItem("마케팅 활용 정보 동의 약관", "내용", optionalTerms[OptionalTerms.MARKETING] ?: false) {
            termsViewModel.updateRequiredTerms(RequiredTerms.PRIVACY)
        } }
    }
}

@Composable
fun TermItem(title: String, content: String, isChecked: Boolean, onClick: () -> Unit) {
    val density = LocalDensity.current

    val isFolded = remember { mutableStateOf(true) }

    Column(

    ) {
        Row(

        ) {
            Text(
                text = title,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2, isFixedSize = true),
                color = Black
            )

            Icon(
                painter = painterResource(id = if(isFolded.value) R.drawable.arrow_right else R.drawable.arrow_down),
                contentDescription = "화살표",
                modifier = Modifier
                    .size(SizeUtils().textSizeToDp(density = density, 2, 0))
                    .clickable {
                        isFolded.value = !isFolded.value
                    }
            )
            
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = if(isChecked) R.drawable.ic_check_on else R.drawable.ic_check_off),
                contentDescription = "체크",
                modifier = Modifier
                    .size(SizeUtils().textSizeToDp(density = density, 2, 0))
                    .clickable {
                        onClick()
                    }
            )
        }

        AnimatedVisibility(visible = !isFolded.value) {
            Text(
                text = content,
                style = storeMeTextStyle(FontWeight.Normal, 0, isFixedSize = true)
            )
        }
    }
}