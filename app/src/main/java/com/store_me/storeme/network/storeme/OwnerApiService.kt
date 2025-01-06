package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.model.StoreMeResponse
import com.store_me.storeme.data.response.StoreListResponse
import retrofit2.Response
import retrofit2.http.GET

interface OwnerApiService {
    /**
     * 가게 목록 요청 API
     */
    @GET("store/list")
    suspend fun getStoreList(

    ): Response<StoreMeResponse<StoreListResponse>>
}