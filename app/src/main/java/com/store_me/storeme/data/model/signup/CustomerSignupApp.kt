package com.store_me.storeme.data.model.signup

/**
 * Customer 의 App 계정 회원가입
 * @param accountId 계정 아이디
 * @param password 비밀번호
 * @param phoneNumber 전화번호
 * @param nickname 닉네임
 * @param privacyConsent 약관 동의 여부
 * @param marketingConsent 마케팅 동의 여부
 * @param verificationCode 인증 코드
 */
data class CustomerSignupApp(
    val accountId: String,
    val password: String,
    val phoneNumber: String,
    val nickname: String,
    val privacyConsent: Boolean,
    val marketingConsent: Boolean,
    val verificationCode: String
)
