package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.model.check_duplicate.CheckAccountIdDuplicate
import com.store_me.storeme.data.response.CheckAccountIdDuplicateResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.request.AppLoginRequest
import com.store_me.storeme.data.request.KakaoLoginRequest
import com.store_me.storeme.data.response.LoginResponse
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.signup.CustomerSignupKakao
import com.store_me.storeme.data.model.signup.OwnerSignupApp
import com.store_me.storeme.data.model.signup.OwnerSignupKakao
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
        @Part profileImageFile: MultipartBody.Part?,
        @Part ("appCustomerSignupRequestDto") appCustomerSignupRequestDto: CustomerSignupApp
    ): Response<StoreMeResponse<Unit>>

    @Multipart
    @POST("signup/kakao/customer")
    suspend fun customerSignupKakao(
        @Part profileImageFile: MultipartBody.Part?,
        @Part ("kakaoCustomerSignupRequestDto") kakaoCustomerSignupRequestDto: CustomerSignupKakao
    ): Response<StoreMeResponse<Unit>>

    @Multipart
    @POST("signup/app/owner")
    suspend fun ownerSignupApp(
        @Part storeProfileImageFile: MultipartBody.Part?,
        @Part storeFeaturedImageFile: MultipartBody.Part?,
        @Part storeImageFileList: List<MultipartBody.Part>?,
        @Part("appOwnerSignupRequestDto") appOwnerSignupRequestDto: OwnerSignupApp
    ): Response<StoreMeResponse<Unit>>
    @Multipart
    @POST("signup/kakao/owner")
    suspend fun ownerSignupKakao(
        @Part storeProfileImageFile: MultipartBody.Part?,
        @Part storeFeaturedImageFile: MultipartBody.Part?,
        @Part storeImageFileList: List<MultipartBody.Part>?,
        @Part("kakaoOwnerSignupRequestDto") kakaoOwnerSignupRequestDto: OwnerSignupKakao
    ): Response<StoreMeResponse<Unit>>

    @POST("login/app")
    suspend fun loginWithApp(
        @Body appLoginRequest: AppLoginRequest
    ): Response<StoreMeResponse<LoginResponse>>

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