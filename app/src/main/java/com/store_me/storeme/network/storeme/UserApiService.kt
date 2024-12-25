package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.check_duplicate.CheckAccountIdDuplicate
import com.store_me.storeme.data.check_duplicate.CheckAccountIdDuplicateResponse
import com.store_me.storeme.data.model.StoreMeResponse
import com.store_me.storeme.data.model.login.KakaoLoginRequest
import com.store_me.storeme.data.model.login.LoginResponse
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.verification.ConfirmCode
import com.store_me.storeme.data.model.verification.PhoneNumber
import com.store_me.storeme.data.model.verification.PhoneNumberResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApiService {
    @Multipart
    @POST("signup/app/customer")
    suspend fun customerSignupApp(
        @Part("appCustomerSignupRequestDto") customerSignupApp: CustomerSignupApp,
        @Part profileImageFile: MultipartBody.Part?
    ): Response<StoreMeResponse<Unit>>

    @POST("login/kakao")
    suspend fun loginWithKakao(
        @Body kakaoLoginRequest: KakaoLoginRequest
    ): Response<StoreMeResponse<LoginResponse>>

    @POST("verification/send")
    suspend fun sendSmsMessage(
        @Body phoneNumber: PhoneNumber
    ): Response<StoreMeResponse<PhoneNumberResponse>>

    @POST("verification/confirm")
    suspend fun confirmVerificationCode(
        @Body confirmCode: ConfirmCode
    ): Response<StoreMeResponse<Unit>>

    @POST("signup/check/accountId")
    suspend fun checkAccountIdDuplication(
        @Body accountId: CheckAccountIdDuplicate
    ): Response<StoreMeResponse<CheckAccountIdDuplicateResponse>>
}