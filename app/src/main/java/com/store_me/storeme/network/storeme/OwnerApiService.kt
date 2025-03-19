package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.request.store.PatchBusinessHoursRequest
import com.store_me.storeme.data.request.store.PatchLinksRequest
import com.store_me.storeme.data.request.store.PatchStoreDescriptionRequest
import com.store_me.storeme.data.request.store.PatchStoreIntroRequest
import com.store_me.storeme.data.request.store.PatchStoreProfileImagesRequest
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.data.response.LinksResponse
import com.store_me.storeme.data.response.MyStoresResponse
import com.store_me.storeme.data.response.PatchResponse
import com.store_me.storeme.data.store.StoreInfoData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface OwnerApiService {
    /**
     * 가게 목록 요청 API
     */
    @GET("auth/stores")
    suspend fun getMyStores(
    ): Response<MyStoresResponse>

    /**
     * 가게 정보 요청 API
     */
    @GET("auth/store/{storeId}")
    suspend fun getStoreData(
        @Path("storeId") storeId: String
    ): Response<StoreInfoData>

    /**
     * 가게 이미지 수정 API
     */
    @PATCH("auth/store/{storeId}/images")
    suspend fun patchStoreProfileImages(
        @Path("storeId") storeId: String,
        @Body patchStoreProfileImagesRequest: PatchStoreProfileImagesRequest
    ): Response<PatchResponse<StoreInfoData>>

    /**
     * 가게 소개 수정 API
     */
    @PATCH("auth/store/{storeId}/intro")
    suspend fun patchStoreIntro(
        @Path("storeId") storeId: String,
        @Body patchStoreIntroRequest: PatchStoreIntroRequest
    ): Response<PatchResponse<StoreInfoData>>

    /**
     * 가게 설명 수정 API
     */
    @PATCH("auth/store/{storeId}/description")
    suspend fun patchStoreDescription(
        @Path("storeId") storeId: String,
        @Body patchStoreDescriptionRequest: PatchStoreDescriptionRequest
    ): Response<PatchResponse<StoreInfoData>>

    /**
     * 영업 시간 조회 API
     */
    @GET("auth/store/{storeId}/business-hours")
    suspend fun getBusinessHours(
        @Path("storeId") storeId: String
    ): Response<BusinessHoursResponse>

    /**
     * 영업 시간 수정 API
     */
    @PATCH("auth/store/{storeId}/business-hours")
    suspend fun patchBusinessHours(
        @Path("storeId") storeId: String,
        @Body patchBusinessHoursRequest: PatchBusinessHoursRequest
    ): Response<PatchResponse<BusinessHoursResponse>>

    /**
     * 가게 링크 조회 API
     */
    @GET("auth/store/{storeId}/links")
    suspend fun getStoreLinks(
        @Path("storeId") storeId: String
    ): Response<LinksResponse>

    /**
     * 가게 링크 수정 API
     */
    @PATCH("auth/store/{storeId}/links")
    suspend fun patchStoreLinks(
        @Path("storeId") storeId: String,
        @Body patchLinksRequest: PatchLinksRequest
    ): Response<PatchResponse<LinksResponse>>

}