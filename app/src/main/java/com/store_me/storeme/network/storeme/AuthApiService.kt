package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.request.login.ReissueRequest
import com.store_me.storeme.data.response.ReissueResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    /**
     * 인증 관련 API
     */
    @POST("api/auth/refresh-token")
    suspend fun reissueTokens(
        @Body request: ReissueRequest
    ): ReissueResponse
}