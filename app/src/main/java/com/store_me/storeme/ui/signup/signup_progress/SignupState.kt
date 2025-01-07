package com.store_me.storeme.ui.signup.signup_progress

sealed class SignupState: Comparable<SignupState> {
    data class Signup(val progress: SignupProgress) : SignupState()
    data object Onboarding : SignupState()
    data class Customer(val progress: CustomerProgress) : SignupState()
    data class Owner(val progress: OwnerProgress) : SignupState()

    override fun compareTo(other: SignupState): Int {
        return this.order - other.order
    }

    private val order: Int
        get() = when (this) {
            is Signup -> 0
            Onboarding -> 1
            is Customer -> 2
            is Owner -> 3
        }
}