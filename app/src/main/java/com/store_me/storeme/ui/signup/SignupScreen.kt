@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.signup

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth.LoginType
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.theme.DeleteTextColor
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.ui.theme.textClearIconColor
import com.store_me.storeme.utils.PhoneNumberUtils
import com.store_me.storeme.utils.PhoneNumberVisualTransformation
import com.store_me.storeme.utils.SizeUtils

val LocalSignupViewModel = staticCompositionLocalOf<SignupViewModel> {
    error("No LocalSignupViewModel provided")
}

val LocalTermsViewModel = staticCompositionLocalOf<TermsViewModel> {
    error("No LocalTermsVieWModel Provided")
}

val LocalPhoneNumberViewModel = staticCompositionLocalOf<PhoneNumberViewModel> {
    error("No PhoneNumberViewModel Provided")
}

val LocalAccountDataViewModel = staticCompositionLocalOf<AccountDataViewModel> {
    error("No AccountDataViewModel Provided")
}

@Composable
fun SignupScreen(
    navController: NavController,
    loginType: LoginType,
    signupViewModel: SignupViewModel = hiltViewModel(),
    phoneNumberViewModel: PhoneNumberViewModel = hiltViewModel(),
    termsViewModel: TermsViewModel = viewModel(),
    accountDataViewModel: AccountDataViewModel = hiltViewModel()
) {
    BackHandler {
        signupViewModel.moveToPreviousProgress()
    }

    LaunchedEffect(loginType) {
        signupViewModel.setLoginType(loginType)
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
        LocalSignupViewModel provides signupViewModel,
        LocalTermsViewModel provides termsViewModel,
        LocalPhoneNumberViewModel provides phoneNumberViewModel,
        LocalAccountDataViewModel provides accountDataViewModel
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = White,
            topBar = {
                TopAppBar(
                    title = {

                    },
                    navigationIcon = {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = White,
                                contentColor = Black
                            ),
                            interactionSource = remember { MutableInteractionSource() },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_left),
                                    contentDescription = "뒤로가기",
                                    modifier = Modifier
                                        .size(24.dp),
                                    tint = Black
                                )
                            },
                            onClick = {
                                Log.d("Icon Click", "Clicked")
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = White,
                        titleContentColor = White,
                    )
                )
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
                                        TermsSection {
                                            signupViewModel.moveToNextProgress()
                                        }
                                    }
                                    SignupProgress.NUMBER -> {
                                        PhoneNumberSection {
                                            signupViewModel.moveToNextProgress()
                                        }
                                    }
                                    SignupProgress.CERTIFICATION -> {
                                        CertificationSection {
                                            signupViewModel.moveToNextProgress()
                                        }
                                    }
                                    SignupProgress.ACCOUNT_DATA -> {
                                        AccountDataSection {

                                        }
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
fun AccountDataSection(onClick: () -> Unit) {
    val accountDataViewModel = LocalAccountDataViewModel.current

    LazyColumn(

    ) {
        item {
            SignupTitleText(title = "아이디와 비밀번호를\n 설정해주세요.")
        }

        item { Spacer(modifier = Modifier.height(36.dp)) }
    }
}

@Composable
fun CertificationSection(onClick: () -> Unit) {
    val phoneNumberViewModel = LocalPhoneNumberViewModel.current
    val signupViewModel = LocalSignupViewModel.current

    val phoneNumber by phoneNumberViewModel.phoneNumber.collectAsState()
    val verificationCode by phoneNumberViewModel.verificationCode.collectAsState()

    val verificationSuccess by phoneNumberViewModel.verificationSuccess.collectAsState()

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(verificationSuccess) {
        when(verificationSuccess){
            true -> {
                phoneNumberViewModel.clearVerificationSuccess()
                onClick()
            }
            false -> {
                isError.value = true
            }
            null -> {

            }
        }
    }

    LazyColumn(

    ) {
        item {
            SignupTitleText(title = "휴대폰으로 발송된\n인증 번호를 입력해주세요.")
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                ) {
                    Text(
                        text = PhoneNumberUtils().phoneNumberAddDashes(phoneNumber),
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2, isFixedSize = true)
                    )

                    Text(
                        text = "로 인증번호를 보냈어요.",
                        style = storeMeTextStyle(FontWeight.Normal, 2, isFixedSize = true)
                    )
                }
                Text(
                    text = "확인 후 인증번호를 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 2, isFixedSize = true)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(26.dp))
        }

        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clickable {
                        signupViewModel.moveToPreviousProgress()
                    }
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "휴대폰 번호 변경 / 다시 보내기",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2, isFixedSize = true),
                    color = HighlightTextFieldColor
                )
                
                Icon(
                    painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "화살표",
                    modifier = Modifier
                        .size(16.dp),
                    tint = HighlightTextFieldColor
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(26.dp))
        }

        item {
            Text(
                text = "인증 번호",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2, true),
                color = Black,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            OutlinedTextField(
                value = verificationCode,
                onValueChange = { phoneNumberViewModel.updateVerificationCode(it) },
                textStyle = storeMeTextStyle(FontWeight.Normal, 1, isFixedSize = true),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    if(verificationCode.isNotEmpty()){
                        IconButton(onClick = { phoneNumberViewModel.updateVerificationCode("") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_text_clear),
                                contentDescription = "삭제",
                                modifier = Modifier
                                    .size(24.dp),
                                tint = Unspecified
                            )
                        }
                    }
                },
                placeholder = {
                    Text(
                        text = "인증코드를 입력해주세요.",
                        style = storeMeTextStyle(FontWeight.Normal, 1, isFixedSize = true),
                        color = UndefinedTextColor
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HighlightTextFieldColor,
                    errorBorderColor = ErrorTextFieldColor,
                    errorLabelColor = ErrorTextFieldColor,
                ),
                isError = isError.value,
                supportingText = {
                    if(isError.value){
                        Text(
                            text = "인증번호가 일치하지 않습니다.",
                            style = storeMeTextStyle(FontWeight.Normal, 0, isFixedSize = true),
                            color = ErrorTextFieldColor
                        )
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }

        item {
            NextButton(
                buttonText = "확인",
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                //phoneNumberViewModel.confirmCode()
                onClick() //이거 지워야됨
            }
        }
    }
}

@Composable
fun PhoneNumberSection(onClick: () -> Unit) {
    val phoneNumberViewModel = LocalPhoneNumberViewModel.current

    val phoneNumber by phoneNumberViewModel.phoneNumber.collectAsState()
    val smsSentSuccess by phoneNumberViewModel.smsSentSuccess.collectAsState()

    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(smsSentSuccess) {
        if(smsSentSuccess) {
            phoneNumberViewModel.clearSmsSentSuccess()
            onClick()
        }
    }

    LazyColumn(

    ) {
        item {
            SignupTitleText(title = "본인 인증을 위해\n휴대폰 번호를 입력해주세요.")
        }

        item { Spacer(modifier = Modifier.height(36.dp)) }

        item {
            Text(
                text = "휴대폰 번호",
                style = storeMeTextStyle(FontWeight.ExtraBold, 2, true),
                color = Black,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if(it.length < 12) phoneNumberViewModel.updatePhoneNumber(it) },
                textStyle = storeMeTextStyle(FontWeight.Normal, 1, isFixedSize = true),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    if(phoneNumber.isNotEmpty()){
                        IconButton(onClick = { phoneNumberViewModel.updatePhoneNumber("") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_text_clear),
                                contentDescription = "삭제",
                                modifier = Modifier
                                    .size(24.dp),
                                tint = Unspecified
                            )
                        }
                    }
                },
                placeholder = {
                    Text(
                        text = "휴대폰 번호를 입력하세요",
                        style = storeMeTextStyle(FontWeight.Normal, 1, isFixedSize = true),
                        color = UndefinedTextColor
                    )
                },
                visualTransformation = PhoneNumberVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HighlightTextFieldColor,
                    errorBorderColor = ErrorTextFieldColor,
                    errorLabelColor = ErrorTextFieldColor,
                ),
                isError = isError.value,
                supportingText = {
                    if(isError.value){
                        Text(
                            text = "휴대폰 번호 형식에 맞지 않습니다.",
                            style = storeMeTextStyle(FontWeight.Normal, 0, isFixedSize = true),
                            color = ErrorTextFieldColor
                        )
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }

        item {
            NextButton(
                buttonText = "인증 요청",
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                if(PhoneNumberUtils().isValidPhoneNumber(phoneNumber = phoneNumber)){
                    isError.value = false

                    //phoneNumberViewModel.sendSmsMessage(phoneNumber = phoneNumber)
                    onClick() //이거 지워야됨
                } else {
                    isError.value = true
                }
            }
        }
    }
}

@Composable
fun TermsSection(onClick: () -> Unit) {
    val termsViewModel = LocalTermsViewModel.current

    val requiredTerms by termsViewModel.requiredTermsState.collectAsState()
    val optionalTerms by termsViewModel.optionalTermsState.collectAsState()

    val isAllTermsAgreed by termsViewModel.isAllTermsAgreed.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {

        item {
            SignupTitleText(title = "서비스 이용을 위한\n약관에 동의해주세요.")
        }

        item { DefaultHorizontalDivider() }

        //전체 동의
        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "전체 동의",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2, isFixedSize = true),
                    color = Black
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = if(isAllTermsAgreed) R.drawable.ic_check_on else R.drawable.ic_check_off),
                    contentDescription = "체크",
                    tint = if(isAllTermsAgreed) Black else DeleteTextColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            onClick = {
                                if (isAllTermsAgreed) {
                                    termsViewModel.updateAllTerms(false)
                                } else {
                                    termsViewModel.updateAllTerms(true)
                                }
                            },
                            indication = null,
                            interactionSource = null
                        )
                )

            }
        }

        item { TermItem("서비스 이용 약관 (필수)", stringResource(id = R.string.example_terms), requiredTerms[RequiredTerms.USE] ?: false) {
            termsViewModel.updateRequiredTerms(RequiredTerms.USE)
        } }

        item { TermItem("개인정보 이용 약관 (필수)", stringResource(id = R.string.example_terms), requiredTerms[RequiredTerms.PRIVACY] ?: false) {
            termsViewModel.updateRequiredTerms(RequiredTerms.PRIVACY)
        } }

        item { TermItem("마케팅 활용 정보 동의 약관 (선택)", stringResource(id = R.string.example_terms), optionalTerms[OptionalTerms.MARKETING] ?: false) {
            termsViewModel.updateOptionalTerms(OptionalTerms.MARKETING)
        } }

        item {
            NextButton(
                buttonText = "다음",
                modifier = Modifier
                    .padding(top = 48.dp)
                    .padding(horizontal = 20.dp),
                enabled = requiredTerms.all { it.value }
            ) {
                onClick()
            }
        }
    }
}

@Composable
fun SignupTitleText(title: String) {
    Text(
        text = title,
        style = storeMeTextStyle(FontWeight.ExtraBold, 6, isFixedSize = true),
        color = Black,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    )
}

@Composable
fun TermItem(title: String, content: String, isChecked: Boolean, onClick: () -> Unit) {
    val isFolded = remember { mutableStateOf(true) }

    fun onClickTitle() {
        isFolded.value = !isFolded.value
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2, isFixedSize = true),
                color = Black,
                modifier = Modifier
                    .clickable(
                        onClick = { onClickTitle() }
                    )
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(id = if(isFolded.value) R.drawable.ic_arrow_right else R.drawable.ic_arrow_down),
                contentDescription = "화살표",
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        onClick = { onClickTitle() },
                        indication = ripple(bounded = false),
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
            
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = if(isChecked) R.drawable.ic_check_on else R.drawable.ic_check_off),
                contentDescription = "체크",
                tint = if(isChecked) Black else DeleteTextColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = {
                            onClick()
                            isFolded.value = true
                        },
                        indication = null,
                        interactionSource = null
                    )
            )
        }

        AnimatedVisibility(visible = !isFolded.value) {
            Text(
                text = content,
                style = storeMeTextStyle(FontWeight.Normal, 0, isFixedSize = true),
                modifier = Modifier
                    .padding(top = 4.dp, end = 24.dp)
            )
        }
    }
}

@Composable
fun NextButton(buttonText: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = White,
            containerColor = Black
        ),
        enabled = enabled
    ) {
        Text(
            text = buttonText,
            style = storeMeTextStyle(FontWeight.ExtraBold, 3, isFixedSize = true),
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}