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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.Auth.LoginType
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.signup.CustomerSignupKakao
import com.store_me.storeme.data.model.signup.OwnerSignupApp
import com.store_me.storeme.data.model.signup.OwnerSignupKakao
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.signup.account_data.AccountDataSection
import com.store_me.storeme.ui.signup.account_data.AccountDataViewModel
import com.store_me.storeme.ui.signup.account_data.AccountTypeSection
import com.store_me.storeme.ui.signup.customer.CustomerDataViewModel
import com.store_me.storeme.ui.signup.customer.CustomerNickNameSection
import com.store_me.storeme.ui.signup.customer.CustomerProfileImageSection
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
import com.store_me.storeme.ui.signup.terms.OptionalTerms
import com.store_me.storeme.ui.signup.terms.TermsSection
import com.store_me.storeme.ui.signup.terms.TermsViewModel
import com.store_me.storeme.ui.theme.SignupTextBoxColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.signup.LocalAccountDataViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalCustomerDataViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalPhoneNumberViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalSignupOnboardingViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalSignupViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreDataViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalTermsViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    loginType: LoginType,
    additionalData: String = "",
    signupViewModel: SignupViewModel = hiltViewModel(),
    phoneNumberViewModel: PhoneNumberViewModel = hiltViewModel(),
    termsViewModel: TermsViewModel = viewModel(),
    signupOnboardingViewModel: SignupOnboardingViewModel = viewModel(),
    accountDataViewModel: AccountDataViewModel = hiltViewModel(),
    storeDataViewModel: StoreDataViewModel = hiltViewModel(),
    customerDataViewModel: CustomerDataViewModel = viewModel()
) {

    LaunchedEffect(loginType) {
        signupViewModel.setLoginType(loginType)
    }

    val signupState by signupViewModel.signupState.collectAsState()

    val accountType by signupViewModel.accountType.collectAsState()

    val focusManager = LocalFocusManager.current

    fun onClickBackButton() {
        if(signupState is SignupState.Signup){
            if((signupState as SignupState.Signup).progress == SignupProgress.TERMS) {
                navController.popBackStack()
            } else {
                signupViewModel.moveToPreviousProgress()
            }
        } else {
            signupViewModel.moveToPreviousProgress()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    fun moveToNextProgress() {
        signupViewModel.moveToNextProgress()
    }

    BackHandler {
        onClickBackButton()
    }

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
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
                .addFocusCleaner(focusManager),
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
                                        AuthenticationSection {
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
                                            moveToNextProgress()
                                        }
                                    }
                                }
                            }
                            is SignupState.Onboarding -> {
                                SignupOnboardingSection {
                                    moveToNextProgress()
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
                                            moveToNextProgress()
                                        }
                                    }
                                    CustomerProgress.FINISH -> {

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
                                            moveToNextProgress()
                                        }
                                    }
                                    OwnerProgress.FINISH -> {
                                        FinishSection {
                                            when {
                                                accountType == Auth.AccountType.OWNER && signupViewModel.loginType.value == LoginType.KAKAO-> {
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

                                                accountType == Auth.AccountType.OWNER && loginType == LoginType.APP-> {
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

                                                accountType == Auth.AccountType.CUSTOMER && loginType == LoginType.KAKAO-> {
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

                                                accountType == Auth.AccountType.CUSTOMER && loginType == LoginType.APP-> {
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
        style = storeMeTextStyle(FontWeight.ExtraBold, 6, isFixedSize = true),
        color = Black,
        modifier = modifier
            .padding(top = 20.dp)
    )
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
            style = storeMeTextStyle(FontWeight.ExtraBold, 1, isFixedSize = true),
            color = Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = content,
            style = storeMeTextStyle(FontWeight.Normal, -1, isFixedSize = true)
        )
    }
}