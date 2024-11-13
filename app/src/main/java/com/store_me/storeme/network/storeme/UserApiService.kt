package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.model.StoreMeResponse
import retrofit2.Response
import retrofit2.http.POST

interface UserApiService {
    @POST("signup/app")
    suspend fun signupApp(

    ): Response<StoreMeResponse<Unit>>

}