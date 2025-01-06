package com.store_me.storeme.utils.composition_locals.signup

import androidx.compose.runtime.staticCompositionLocalOf
import com.store_me.storeme.ui.signup.SignupViewModel
import com.store_me.storeme.ui.signup.account_data.AccountDataViewModel
import com.store_me.storeme.ui.signup.customer.CustomerDataViewModel
import com.store_me.storeme.ui.signup.onboarding.SignupOnboardingViewModel
import com.store_me.storeme.ui.signup.owner.StoreDataViewModel
import com.store_me.storeme.ui.signup.phone_authentication.PhoneNumberViewModel
import com.store_me.storeme.ui.signup.terms.TermsViewModel

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

val LocalStoreDataViewModel = staticCompositionLocalOf<StoreDataViewModel> {
    error("No StoreDataViewModel Provided")
}

val LocalSignupOnboardingViewModel = staticCompositionLocalOf<SignupOnboardingViewModel> {
    error("No SignupOnboardingViewModel Provided")
}

val LocalCustomerDataViewModel = staticCompositionLocalOf<CustomerDataViewModel> {
    error("No CustomerDataViewModel Provided")
}