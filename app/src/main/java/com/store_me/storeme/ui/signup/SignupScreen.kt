@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.signup

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.signup.CustomerSignupKakao
import com.store_me.storeme.data.model.signup.OwnerSignupApp
import com.store_me.storeme.data.model.signup.OwnerSignupKakao
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.signup.account_data.AccountDataSection
import com.store_me.storeme.ui.signup.account_data.AccountDataViewModel
import com.store_me.storeme.ui.signup.account_data.AccountTypeSection
import com.store_me.storeme.ui.signup.customer.CustomerDataViewModel
import com.store_me.storeme.ui.signup.customer.CustomerNickNameSection
import com.store_me.storeme.ui.signup.customer.CustomerProfileImageSection
import com.store_me.storeme.ui.signup.finish.FinishSection
import com.store_me.storeme.ui.signup.phone_authentication.PhoneNumberSection
import com.store_me.storeme.ui.signup.phone_authentication.PhoneNumberViewModel
import com.store_me.storeme.ui.signup.onboarding.SignupOnboardingSection
import com.store_me.storeme.ui.signup.onboarding.SignupOnboardingViewModel
import com.store_me.storeme.ui.signup.owner.AddressSection
import com.store_me.storeme.ui.signup.owner.StoreCategorySection
import com.store_me.storeme.ui.signup.owner.StoreCustomCategorySection
import com.store_me.storeme.ui.signup.owner.StoreDataViewModel
import com.store_me.storeme.ui.signup.owner.StoreImageSection
import com.store_me.storeme.ui.signup.owner.StoreIntroSection
import com.store_me.storeme.ui.signup.owner.StoreNameSection
import com.store_me.storeme.ui.signup.owner.StoreNumberSection
import com.store_me.storeme.ui.signup.owner.StoreProfileImageSection
import com.store_me.storeme.ui.signup.phone_authentication.AuthenticationSection
import com.store_me.storeme.ui.signup.signup_progress.CustomerProgress
import com.store_me.storeme.ui.signup.signup_progress.OwnerProgress
import com.store_me.storeme.ui.signup.signup_progress.SignupProgress
import com.store_me.storeme.ui.signup.signup_progress.SignupProgressViewModel
import com.store_me.storeme.ui.signup.signup_progress.SignupState
import com.store_me.storeme.ui.signup.terms.OptionalTerms
import com.store_me.storeme.ui.signup.terms.TermsSection
import com.store_me.storeme.ui.signup.terms.TermsViewModel
import com.store_me.storeme.ui.theme.SignupTextBoxColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalAccountDataViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalCustomerDataViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalPhoneNumberViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalSignupOnboardingViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalSignupProgressViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalSignupViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreDataViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalTermsViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    loginType: LoginType,
    additionalData: String = "",
    signupProgressViewModel: SignupProgressViewModel = viewModel(),
    signupViewModel: SignupViewModel = hiltViewModel(),
    phoneNumberViewModel: PhoneNumberViewModel = hiltViewModel(),
    termsViewModel: TermsViewModel = viewModel(),
    signupOnboardingViewModel: SignupOnboardingViewModel = viewModel(),
    accountDataViewModel: AccountDataViewModel = hiltViewModel(),
    storeDataViewModel: StoreDataViewModel = hiltViewModel(),
    customerDataViewModel: CustomerDataViewModel = viewModel()
) {
    val context = LocalContext.current

    val snackbarHostState = LocalSnackbarHostState.current
    val loadingViewModel = LocalLoadingViewModel.current

    val signupState by signupProgressViewModel.signupState.collectAsState()

    val accountType by signupViewModel.accountType.collectAsState()

    val isSignupFinish by signupViewModel.isSignupFinish.collectAsState()
    val errorMessage by signupViewModel.errorMessage.collectAsState()

    val focusManager = LocalFocusManager.current

    val showDialog = remember { mutableStateOf(false) }
    val showNumberWarningDialog = remember { mutableStateOf(false) }

    LaunchedEffect(loginType) {
        signupViewModel.setLoginType(loginType)
    }

    LaunchedEffect(errorMessage) {
        if(errorMessage != null) {
            loadingViewModel.hideLoading()

            snackbarHostState.showSnackbar(errorMessage.toString())

            signupViewModel.updateErrorMessage(null)
        }
    }

    fun onClickBackButton() {
        if(signupState is SignupState.Signup){
            when ((signupState as SignupState.Signup).progress) {
                SignupProgress.TERMS -> {
                    showDialog.value = true
                }
                SignupProgress.CERTIFICATION -> {
                    if(showNumberWarningDialog.value) {
                        showNumberWarningDialog.value = false
                        signupProgressViewModel.moveToPreviousProgress(loginType)
                    } else {
                        showNumberWarningDialog.value = true
                    }
                }
                else -> {
                    signupProgressViewModel.moveToPreviousProgress(loginType)
                }
            }
        } else {
            signupProgressViewModel.moveToPreviousProgress(loginType)
        }
    }

    fun moveToNextProgress(accountType: AccountType? = null) {
        signupProgressViewModel.moveToNextProgress(accountType = accountType, loginType = loginType)
    }

    LaunchedEffect(isSignupFinish) {
        if(isSignupFinish) {
            loadingViewModel.hideLoading()

            moveToNextProgress(accountType = accountType)

            snackbarHostState.showSnackbar(context.getString(R.string.signup_finish))
        }
    }

    BackHandler {
        onClickBackButton()
    }

    if(showDialog.value) {
        BackWarningDialog(
            onDismiss = { showDialog.value = false },
            onAction = {
                showDialog.value = false
                navController.popBackStack()
            }
        )
    }

    if(showNumberWarningDialog.value) {
        WarningDialog(
            title = "전화번호 입력 화면으로 이동할까요?",
            warningContent = null,
            content = "이전 화면으로 이동 시, 전화번호 재인증이 필요해요.",
            actionText = "확인",
            onDismiss = { showNumberWarningDialog.value = false },
            onAction = {
                onClickBackButton()
            }
        )
    }

    CompositionLocalProvider(
        LocalSignupProgressViewModel provides signupProgressViewModel,
        LocalSignupViewModel provides signupViewModel,
        LocalTermsViewModel provides termsViewModel,
        LocalPhoneNumberViewModel provides phoneNumberViewModel,
        LocalAccountDataViewModel provides accountDataViewModel,
        LocalSignupOnboardingViewModel provides signupOnboardingViewModel,
        LocalStoreDataViewModel provides storeDataViewModel,
        LocalCustomerDataViewModel provides customerDataViewModel
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { StoreMeSnackbar(snackbarData = it) }
            ) },
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager)
                .imePadding(),
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
                                onClickBackButton()
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
                                            moveToNextProgress()
                                        }
                                    }
                                    SignupProgress.NUMBER -> {
                                        PhoneNumberSection {
                                            moveToNextProgress()
                                        }
                                    }
                                    SignupProgress.CERTIFICATION -> {
                                        AuthenticationSection(
                                            onBack = { onClickBackButton() }
                                        ) {
                                            moveToNextProgress()
                                        }
                                    }
                                    SignupProgress.ACCOUNT_DATA -> {
                                        AccountDataSection {
                                            moveToNextProgress()
                                        }
                                    }
                                    SignupProgress.ACCOUNT_TYPE -> {
                                        AccountTypeSection {
                                            signupViewModel.setAccountType(it)
                                            moveToNextProgress(it)
                                        }
                                    }
                                }
                            }
                            is SignupState.Customer -> {
                                when(targetState.progress) {
                                    CustomerProgress.NICKNAME -> {
                                        CustomerNickNameSection {
                                            moveToNextProgress()
                                        }
                                    }
                                    CustomerProgress.PROFILE_IMAGE -> {
                                        CustomerProfileImageSection {
                                            if(isSignupFinish) {
                                                moveToNextProgress()
                                            } else {
                                                loadingViewModel.showLoading()

                                                when {
                                                    accountType == AccountType.CUSTOMER && loginType == LoginType.KAKAO-> {
                                                        signupViewModel.customerSignupKakao(
                                                            customerSignupKakao = CustomerSignupKakao(
                                                                kakaoId = additionalData,
                                                                phoneNumber = phoneNumberViewModel.phoneNumber.value,
                                                                privacyConsent = termsViewModel.isAllRequiredTermsAgreed(),
                                                                marketingConsent = termsViewModel.optionalTermsState.value[OptionalTerms.MARKETING] ?: false,
                                                                verificationCode = phoneNumberViewModel.verificationCode.value,

                                                                nickname = customerDataViewModel.nickName.value
                                                            ),
                                                            profileImage = customerDataViewModel.profileImage.value
                                                        )
                                                    }

                                                    accountType == AccountType.CUSTOMER && loginType == LoginType.APP-> {
                                                        signupViewModel.customerSignupApp(
                                                            customerSignupApp = CustomerSignupApp(
                                                                accountId = accountDataViewModel.accountId.value,
                                                                password = accountDataViewModel.accountPw.value,
                                                                phoneNumber = phoneNumberViewModel.phoneNumber.value,
                                                                privacyConsent = termsViewModel.isAllRequiredTermsAgreed(),
                                                                marketingConsent = termsViewModel.optionalTermsState.value[OptionalTerms.MARKETING] ?: false,
                                                                verificationCode = phoneNumberViewModel.verificationCode.value,

                                                                nickname = customerDataViewModel.nickName.value
                                                            ),
                                                            profileImage = customerDataViewModel.profileImage.value
                                                        )
                                                    }
                                                }
                                            }


                                        }
                                    }
                                    CustomerProgress.FINISH -> {
                                        FinishSection {
                                            moveToNextProgress()
                                        }
                                    }
                                }
                            }
                            is SignupState.Owner -> {
                                when(targetState.progress) {
                                    OwnerProgress.STORE_NAME -> {
                                        StoreNameSection {
                                            moveToNextProgress()
                                        }
                                    }
                                    OwnerProgress.CATEGORY -> {
                                        StoreCategorySection {
                                            moveToNextProgress()
                                        }
                                    }
                                    OwnerProgress.CUSTOM_CATEGORY -> {
                                        StoreCustomCategorySection {
                                            moveToNextProgress()
                                        }
                                    }
                                    OwnerProgress.ADDRESS -> {
                                        AddressSection {
                                            moveToNextProgress()
                                        }
                                    }
                                    OwnerProgress.STORE_PROFILE_IMAGE -> {
                                        StoreProfileImageSection {
                                            moveToNextProgress()
                                        }
                                    }
                                    OwnerProgress.STORE_IMAGE -> {
                                        StoreImageSection {
                                            moveToNextProgress()
                                        }
                                    }
                                    OwnerProgress.INTRO -> {
                                        StoreIntroSection {
                                            moveToNextProgress()
                                        }
                                    }
                                    OwnerProgress.NUMBER -> {
                                        StoreNumberSection {
                                            loadingViewModel.showLoading()

                                            when {
                                                accountType == AccountType.OWNER && signupViewModel.loginType.value == LoginType.KAKAO-> {
                                                    signupViewModel.ownerSignupKakao(
                                                        ownerSignupKakao = OwnerSignupKakao(
                                                            kakaoId = additionalData,
                                                            phoneNumber = phoneNumberViewModel.phoneNumber.value,
                                                            privacyConsent = termsViewModel.isAllRequiredTermsAgreed(),
                                                            marketingConsent = termsViewModel.optionalTermsState.value[OptionalTerms.MARKETING] ?: false,
                                                            verificationCode = phoneNumberViewModel.verificationCode.value,

                                                            storeName = storeDataViewModel.storeName.value,
                                                            storeDescription = "",
                                                            storeCategory = storeDataViewModel.storeCategory.value!!.name,
                                                            storeDetailCategory = storeDataViewModel.storeDetailCategory.value,
                                                            storeLocation = storeDataViewModel.storeLocation.value,
                                                            storeLocationCode = storeDataViewModel.storeLocationCode.value!!,
                                                            storeLocationDetail = storeDataViewModel.storeLocationDetail.value,
                                                            storeLat = storeDataViewModel.storeLatLng.value?.latitude,
                                                            storeLng = storeDataViewModel.storeLatLng.value?.longitude,
                                                            storePhoneNumber = storeDataViewModel.storeNumber.value,
                                                            storeIntro = storeDataViewModel.storeIntro.value
                                                        ),
                                                        storeProfileImage = storeDataViewModel.storeProfileImage.value,
                                                        storeImages = storeDataViewModel.storeImages.value
                                                    )
                                                }

                                                accountType == AccountType.OWNER && loginType == LoginType.APP-> {
                                                    signupViewModel.ownerSignupApp(
                                                        ownerSignupApp = OwnerSignupApp(
                                                            accountId = accountDataViewModel.accountId.value,
                                                            password = accountDataViewModel.accountPw.value,
                                                            phoneNumber = phoneNumberViewModel.phoneNumber.value,
                                                            privacyConsent = termsViewModel.isAllRequiredTermsAgreed(),
                                                            marketingConsent = termsViewModel.optionalTermsState.value[OptionalTerms.MARKETING] ?: false,
                                                            verificationCode = phoneNumberViewModel.verificationCode.value,

                                                            storeName = storeDataViewModel.storeName.value,
                                                            storeDescription = "",
                                                            storeCategory = storeDataViewModel.storeCategory.value!!.name,
                                                            storeDetailCategory = storeDataViewModel.storeDetailCategory.value,
                                                            storeLocation = storeDataViewModel.storeLocation.value,
                                                            storeLocationCode = storeDataViewModel.storeLocationCode.value!!,
                                                            storeLocationDetail = storeDataViewModel.storeLocationDetail.value,
                                                            storeLat = storeDataViewModel.storeLatLng.value?.latitude,
                                                            storeLng = storeDataViewModel.storeLatLng.value?.longitude,
                                                            storePhoneNumber = storeDataViewModel.storeNumber.value,
                                                            storeIntro = storeDataViewModel.storeIntro.value
                                                        ),
                                                        storeProfileImage = storeDataViewModel.storeProfileImage.value,
                                                        storeImages = storeDataViewModel.storeImages.value
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    OwnerProgress.FINISH -> {
                                        FinishSection {
                                            moveToNextProgress()
                                        }
                                    }
                                }
                            }
                            is SignupState.Onboarding -> {
                                SignupOnboardingSection {
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SignupTitleText(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = storeMeTextStyle(FontWeight.ExtraBold, 6),
        color = Black,
        modifier = modifier
            .padding(top = 20.dp)
    )
}

@Composable
fun GuidTextBoxItem(title: String, content: String) {
    Column(
        modifier = Modifier
            .background(color = SignupTextBoxColor, shape = RoundedCornerShape(14.dp))
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = storeMeTextStyle(FontWeight.ExtraBold, 1),
            color = Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = content,
            style = storeMeTextStyle(FontWeight.Normal, -1)
        )
    }
}