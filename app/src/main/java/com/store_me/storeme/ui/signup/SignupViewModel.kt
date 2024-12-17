package com.store_me.storeme.ui.signup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.Auth.AccountType
import com.store_me.storeme.data.Auth.LoginType
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.repository.storeme.UserRepository
import com.store_me.storeme.ui.signup.SignupViewModel.CustomerProgress
import com.store_me.storeme.ui.signup.SignupViewModel.OwnerProgress
import com.store_me.storeme.ui.signup.SignupViewModel.SignupProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SignupState {
    data class Signup(val progress: SignupProgress) : SignupState()
    data object Onboarding : SignupState()
    data class Customer(val progress: CustomerProgress) : SignupState()
    data class Owner(val progress: OwnerProgress) : SignupState()
}

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    //계정 타입
    private val _accountType = MutableStateFlow<AccountType?>(null)
    val accountType: StateFlow<AccountType?> = _accountType
    //로그인 타입
    private val _loginType = MutableStateFlow<LoginType?>(null)
    val loginType: StateFlow<LoginType?> = _loginType

    private val _signupState = MutableStateFlow<SignupState>(SignupState.Signup(SignupProgress.TERMS))
    val signupState: StateFlow<SignupState> = _signupState

    fun moveToNextProgress() {
        when(_signupState.value) {
            is SignupState.Signup -> {
                moveToNextSignupProgress()
            }
            is SignupState.Onboarding -> {
                moveToNextOnboardingProgress()
            }
            is SignupState.Customer -> {
                moveToNextCustomerProgress()
            }
            is SignupState.Owner -> {
                moveToNextOwnerProgress()
            }
        }
    }

    fun moveToPreviousProgress() {
        when(_signupState.value) {
            is SignupState.Signup -> {
                moveToPreviousSignupProgress()
            }
            is SignupState.Onboarding -> {
                moveToPreviousOnboardingProgress()
            }
            is SignupState.Customer -> {
                moveToPreviousCustomerProgress()
            }
            is SignupState.Owner -> {
                moveToPreviousOwnerProgress()
            }
        }
    }

    private fun moveToNextSignupProgress() {
        val nextProgress = when((_signupState.value as SignupState.Signup).progress) {
            SignupProgress.TERMS -> SignupProgress.NUMBER
            SignupProgress.NUMBER -> SignupProgress.CERTIFICATION
            SignupProgress.CERTIFICATION -> {
                when(_loginType.value){
                    LoginType.KAKAO -> {
                        SignupProgress.ACCOUNT_TYPE
                    }
                    LoginType.APP -> {
                        SignupProgress.ACCOUNT_DATA
                    }
                    else -> {
                        return
                    }
                }
            }
            SignupProgress.ACCOUNT_DATA -> SignupProgress.ACCOUNT_TYPE
            SignupProgress.ACCOUNT_TYPE -> {
                _signupState.value = SignupState.Onboarding
                return
            }
        }

        _signupState.value = SignupState.Signup(nextProgress)
    }

    private fun moveToPreviousSignupProgress() {
        val nextProgress = when((_signupState.value as SignupState.Signup).progress) {
            SignupProgress.TERMS -> return
            SignupProgress.NUMBER -> SignupProgress.TERMS
            SignupProgress.CERTIFICATION -> SignupProgress.NUMBER
            SignupProgress.ACCOUNT_DATA -> SignupProgress.CERTIFICATION
            SignupProgress.ACCOUNT_TYPE -> {
                when(_loginType.value){
                    LoginType.KAKAO -> {
                        SignupProgress.CERTIFICATION
                    }
                    LoginType.APP -> {
                        SignupProgress.ACCOUNT_DATA
                    }
                    else -> {
                        return
                    }
                }
            }
        }

        _signupState.value = SignupState.Signup(nextProgress)
    }

    private fun moveToNextOnboardingProgress() {
        when(_accountType.value){
            AccountType.CUSTOMER -> {
                _signupState.value = SignupState.Customer(CustomerProgress.NICKNAME)
            }
            AccountType.OWNER -> {
                _signupState.value = SignupState.Owner(OwnerProgress.STORE_NAME)
            }
            else -> {

            }
        }
    }

    private fun moveToPreviousOnboardingProgress() {
        _signupState.value = SignupState.Signup(SignupProgress.ACCOUNT_TYPE)
    }

    private fun moveToNextCustomerProgress() {
        val nextProgress = when((_signupState.value as SignupState.Customer).progress) {
            CustomerProgress.NICKNAME -> CustomerProgress.PROFILE_IMAGE
            CustomerProgress.PROFILE_IMAGE -> CustomerProgress.FINISH
            CustomerProgress.FINISH -> return
        }
        _signupState.value = SignupState.Customer(nextProgress)
    }

    private fun moveToPreviousCustomerProgress() {
        val nextProgress = when((_signupState.value as SignupState.Customer).progress) {
            CustomerProgress.NICKNAME -> {
                _signupState.value = SignupState.Onboarding
                return
            }
            CustomerProgress.PROFILE_IMAGE -> CustomerProgress.NICKNAME
            CustomerProgress.FINISH -> CustomerProgress.PROFILE_IMAGE
        }
        _signupState.value = SignupState.Customer(nextProgress)
    }

    private fun moveToNextOwnerProgress() {
        val nextProgress = when((_signupState.value as SignupState.Owner).progress) {
            OwnerProgress.STORE_NAME -> OwnerProgress.CATEGORY
            OwnerProgress.CATEGORY -> OwnerProgress.CUSTOM_CATEGORY
            OwnerProgress.CUSTOM_CATEGORY -> OwnerProgress.ADDRESS
            OwnerProgress.ADDRESS -> OwnerProgress.STORE_PROFILE_IMAGE
            OwnerProgress.STORE_PROFILE_IMAGE -> OwnerProgress.STORE_IMAGE
            OwnerProgress.STORE_IMAGE -> OwnerProgress.INTRO
            OwnerProgress.INTRO -> OwnerProgress.NUMBER
            OwnerProgress.NUMBER -> OwnerProgress.FINISH
            OwnerProgress.FINISH -> return
        }
        _signupState.value = SignupState.Owner(nextProgress)
    }

    private fun moveToPreviousOwnerProgress() {
        val nextProgress = when((_signupState.value as SignupState.Owner).progress) {
            OwnerProgress.STORE_NAME -> {
                _signupState.value = SignupState.Onboarding
                return
            }
            OwnerProgress.CATEGORY -> OwnerProgress.STORE_NAME
            OwnerProgress.CUSTOM_CATEGORY -> OwnerProgress.CATEGORY
            OwnerProgress.ADDRESS -> OwnerProgress.CUSTOM_CATEGORY
            OwnerProgress.STORE_PROFILE_IMAGE -> OwnerProgress.ADDRESS
            OwnerProgress.STORE_IMAGE -> OwnerProgress.STORE_PROFILE_IMAGE
            OwnerProgress.INTRO -> OwnerProgress.STORE_IMAGE
            OwnerProgress.NUMBER -> OwnerProgress.INTRO
            OwnerProgress.FINISH -> OwnerProgress.NUMBER
        }
        _signupState.value = SignupState.Owner(nextProgress)
    }

    enum class SignupProgress {
        TERMS,
        NUMBER,
        CERTIFICATION,
        ACCOUNT_DATA,
        ACCOUNT_TYPE
    }

    enum class CustomerProgress {
        NICKNAME,
        PROFILE_IMAGE,
        FINISH
    }

    enum class OwnerProgress {
        STORE_NAME,
        CATEGORY,
        CUSTOM_CATEGORY,
        ADDRESS,
        STORE_PROFILE_IMAGE,
        STORE_IMAGE,
        INTRO,
        NUMBER,
        FINISH
    }

    private val _profileImage = MutableStateFlow<Uri?>(null)
    val profileImage: StateFlow<Uri?> = _profileImage


    fun setImageUri(uri: Uri) {
        _profileImage.value =  uri
    }

    fun signup() {
        viewModelScope.launch {
            val result = userRepository.customerSignupApp(customerSignupApp = CustomerSignupApp(
                accountId = "joonbo97",
                password = "tlawnsqh123",
                phoneNumber = "01071649416",
                nickname= "딕네임",
                privacyConsent = true,
                marketingConsent = true,
                verificationCode = "iesDfY"),
                profileImage = profileImage.value
            )
        }
    }
}