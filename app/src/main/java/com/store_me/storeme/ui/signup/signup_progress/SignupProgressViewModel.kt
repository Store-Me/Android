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
                moveToNextSignupProgress(loginType = loginType, accountType)
            }
            is SignupState.Customer -> {
                moveToNextCustomerProgress()
            }
            is SignupState.Owner -> {
                moveToNextOwnerProgress()
            }
            is SignupState.Onboarding -> {

            }
        }
    }

    fun moveToPreviousProgress(loginType: LoginType) {
        when(_signupState.value) {
            is SignupState.Signup -> {
                moveToPreviousSignupProgress(loginType)
            }

            is SignupState.Customer -> {
                moveToPreviousCustomerProgress()
            }
            is SignupState.Owner -> {
                moveToPreviousOwnerProgress()
            }
            is SignupState.Onboarding -> {
                //moveToPreviousOnboardingProgress(accountType)
            }
        }
    }

    private fun moveToNextSignupProgress(loginType: LoginType, accountType: AccountType?) {
        val nextProgress = when((_signupState.value as SignupState.Signup).progress) {
            SignupProgress.TERMS -> SignupProgress.NUMBER
            SignupProgress.NUMBER -> SignupProgress.CERTIFICATION
            SignupProgress.CERTIFICATION -> SignupProgress.ACCOUNT_DATA
            SignupProgress.ACCOUNT_DATA -> SignupProgress.ACCOUNT_TYPE
            SignupProgress.ACCOUNT_TYPE -> {
                when(accountType){
                    AccountType.CUSTOMER -> {
                        _signupState.value = SignupState.Customer(CustomerProgress.NICKNAME)
                    }
                    AccountType.OWNER -> {
                        _signupState.value = SignupState.Owner(OwnerProgress.STORE_NAME)
                    }
                    else -> {  }
                }
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
            SignupProgress.ACCOUNT_TYPE -> SignupProgress.ACCOUNT_DATA
        }

        _signupState.value = SignupState.Signup(nextProgress)
    }

    private fun moveToPreviousOnboardingProgress(accountType: AccountType?) {
        when(accountType) {
            AccountType.OWNER -> { _signupState.value = SignupState.Owner(OwnerProgress.FINISH) }
            AccountType.CUSTOMER -> { _signupState.value = SignupState.Customer(CustomerProgress.FINISH) }
            else -> {  }
        }
    }

    private fun moveToNextCustomerProgress() {
        val nextProgress = when((_signupState.value as SignupState.Customer).progress) {
            CustomerProgress.NICKNAME -> CustomerProgress.PROFILE_IMAGE
            CustomerProgress.PROFILE_IMAGE -> CustomerProgress.FINISH
            CustomerProgress.FINISH ->  {
                _signupState.value = SignupState.Onboarding
                return
            }
        }
        _signupState.value = SignupState.Customer(nextProgress)
    }

    private fun moveToPreviousCustomerProgress() {
        val nextProgress = when((_signupState.value as SignupState.Customer).progress) {
            CustomerProgress.NICKNAME -> {
                _signupState.value = SignupState.Signup(SignupProgress.ACCOUNT_TYPE)
                return
            }
            CustomerProgress.PROFILE_IMAGE -> CustomerProgress.NICKNAME
            CustomerProgress.FINISH -> return
        }
        _signupState.value = SignupState.Customer(nextProgress)
    }

    private fun moveToNextOwnerProgress() {
        val nextProgress = when((_signupState.value as SignupState.Owner).progress) {
            OwnerProgress.STORE_NAME -> OwnerProgress.CATEGORY
            OwnerProgress.CATEGORY -> OwnerProgress.CUSTOM_CATEGORY
            OwnerProgress.CUSTOM_CATEGORY -> OwnerProgress.ADDRESS
            OwnerProgress.ADDRESS -> OwnerProgress.STORE_PROFILE_IMAGE
            OwnerProgress.STORE_PROFILE_IMAGE -> OwnerProgress.INTRO
            OwnerProgress.INTRO -> OwnerProgress.NUMBER
            OwnerProgress.NUMBER -> OwnerProgress.FINISH
            OwnerProgress.FINISH -> {
                _signupState.value = SignupState.Onboarding
                return
            }
        }
        _signupState.value = SignupState.Owner(nextProgress)
    }

    private fun moveToPreviousOwnerProgress() {
        val nextProgress = when((_signupState.value as SignupState.Owner).progress) {
            OwnerProgress.STORE_NAME -> {
                _signupState.value = SignupState.Signup(SignupProgress.ACCOUNT_TYPE)
                return
            }
            OwnerProgress.CATEGORY -> OwnerProgress.STORE_NAME
            OwnerProgress.CUSTOM_CATEGORY -> OwnerProgress.CATEGORY
            OwnerProgress.ADDRESS -> OwnerProgress.CUSTOM_CATEGORY
            OwnerProgress.STORE_PROFILE_IMAGE -> OwnerProgress.ADDRESS
            OwnerProgress.INTRO -> OwnerProgress.STORE_PROFILE_IMAGE
            OwnerProgress.NUMBER -> OwnerProgress.INTRO
            OwnerProgress.FINISH -> return
        }
        _signupState.value = SignupState.Owner(nextProgress)
    }
}