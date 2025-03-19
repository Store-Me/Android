package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.response.ReissueResponse
import retrofit2.Call
import retrofit2.http.GET

interface AuthApiService {
    /**
     * 인증 관련 API
     */
    @GET("jwt/reissue")
    fun reissueTokens(

    ): Call<ReissueResponse>
}