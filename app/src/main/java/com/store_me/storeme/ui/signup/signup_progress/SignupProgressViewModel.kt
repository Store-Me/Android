package com.store_me.storeme.ui.signup.signup_progress

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.LoginType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignupProgressViewModel: ViewModel() {
    //진행 상태
    private val _signupState = MutableStateFlow<SignupState>(SignupState.Signup(SignupProgress.TERMS))
    val signupState: StateFlow<SignupState> = _signupState

    fun moveToNextProgress(loginType: LoginType, accountType: AccountType?) {
        when(_signupState.value) {
            is SignupState.Signup -> {
                moveToNextSignupProgress(loginType = loginType)
            }
            is SignupState.Onboarding -> {
                accountType?.let { moveToNextOnboardingProgress(accountType = it) }
            }
            is SignupState.Customer -> {
                moveToNextCustomerProgress()
            }
            is SignupState.Owner -> {
                moveToNextOwnerProgress()
            }
        }
    }

    fun moveToPreviousProgress(loginType: LoginType) {
        when(_signupState.value) {
            is SignupState.Signup -> {
                moveToPreviousSignupProgress(loginType)
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

    private fun moveToNextSignupProgress(loginType: LoginType) {
        val nextProgress = when((_signupState.value as SignupState.Signup).progress) {
            SignupProgress.TERMS -> SignupProgress.NUMBER
            SignupProgress.NUMBER -> SignupProgress.CERTIFICATION
            SignupProgress.CERTIFICATION -> {
                when(loginType){
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

    private fun moveToPreviousSignupProgress(loginType: LoginType) {
        val nextProgress = when((_signupState.value as SignupState.Signup).progress) {
            SignupProgress.TERMS -> return
            SignupProgress.NUMBER -> SignupProgress.TERMS
            SignupProgress.CERTIFICATION -> SignupProgress.NUMBER
            SignupProgress.ACCOUNT_DATA -> SignupProgress.CERTIFICATION
            SignupProgress.ACCOUNT_TYPE -> {
                when(loginType){
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

    private fun moveToNextOnboardingProgress(accountType: AccountType) {
        when(accountType){
            AccountType.CUSTOMER -> {
                _signupState.value = SignupState.Customer(CustomerProgress.NICKNAME)
            }
            AccountType.OWNER -> {
                _signupState.value = SignupState.Owner(OwnerProgress.STORE_NAME)
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
}