package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.response.CheckAccountIdDuplicateResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.request.login.AppLoginRequest
import com.store_me.storeme.data.request.login.KakaoLoginRequest
import com.store_me.storeme.data.response.LoginResponse
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.signup.CustomerSignupKakao
import com.store_me.storeme.data.model.signup.CustomerSignupRequest
import com.store_me.storeme.data.model.signup.OwnerSignupApp
import com.store_me.storeme.data.model.signup.OwnerSignupKakao
import com.store_me.storeme.data.model.verification.ConfirmCode
import com.store_me.storeme.data.model.verification.PhoneNumber
import com.store_me.storeme.data.model.verification.PhoneNumberResponse
import com.store_me.storeme.data.request.login.LoginRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserApiService {
    @POST("signupCustomer")
    suspend fun customerSignup(
        @Body customerSignupRequest: CustomerSignupRequest
    ): Response<Unit>

    @POST("signupCustomer")
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

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

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

    @GET("checkAccountIdDuplicate")
    suspend fun checkAccountIdDuplication(
        @Query("accountId") accountId: String
    ): Response<CheckAccountIdDuplicateResponse>

    @DELETE("user")
    suspend fun deleteUser(

    ): Response<StoreMeResponse<Unit>>
}