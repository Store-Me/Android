package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.response.CheckAccountIdDuplicateResponse
import com.store_me.storeme.data.response.LoginResponse
import com.store_me.storeme.data.request.signup.CustomerSignupRequest
import com.store_me.storeme.data.request.signup.OwnerSignupRequest
import com.store_me.storeme.data.request.login.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiService {
    @POST("auth/signup/customer")
    suspend fun customerSignup(
        @Body customerSignupRequest: CustomerSignupRequest
    ): Response<Unit>

    @POST("auth/signup/owner")
    suspend fun ownerSignup(
        @Body ownerSignupRequest: OwnerSignupRequest
    ): Response<Unit>
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("auth/check-duplicate")
    suspend fun checkAccountIdDuplication(
        @Query("accountId") accountId: String
    ): Response<CheckAccountIdDuplicateResponse>


}