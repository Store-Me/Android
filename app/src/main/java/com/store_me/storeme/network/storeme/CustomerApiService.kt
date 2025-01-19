package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.response.CustomerInfoResponse
import com.store_me.storeme.data.response.StoreMeResponse
import retrofit2.Response
import retrofit2.http.GET

interface CustomerApiService {
    /**
     * 회원 정보 요청 (손님)
     */
    @GET("user/customer")
    suspend fun getCustomerInfo(

    ): Response<StoreMeResponse<CustomerInfoResponse>>
}