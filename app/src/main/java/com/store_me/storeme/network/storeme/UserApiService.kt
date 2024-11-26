package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.model.StoreMeResponse
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
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

}