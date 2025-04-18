package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.LabelData
import com.store_me.storeme.data.request.store.CreateCouponPostRequest
import com.store_me.storeme.data.request.store.CreatePostRequest
import com.store_me.storeme.data.request.store.CreateSurveyPostRequest
import com.store_me.storeme.data.request.store.CreateVotePostRequest
import com.store_me.storeme.data.request.store.PatchLabelRequest
import com.store_me.storeme.data.response.StoreMeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApiService {
    /**
     * 라벨 목록 요청 API
     */
    @GET("posts/store/{storeId}/labels")
    suspend fun getLabels(
        @Path("storeId") storeId: String
    ): Response<List<LabelData>>

    /**
     * 라벨 수정 API
     */
    @PATCH("posts/store/{storeId}/labels")
    suspend fun patchLabel(
        @Path("storeId") storeId: String,
        @Body patchLabelRequest: PatchLabelRequest
    ): Response<StoreMeResponse<List<LabelData>>>

    /**
     * 일반 게시글 추가
     */
    @POST("posts/store/{storeId}/normal")
    suspend fun createPost(
        @Path("storeId") storeId: String,
        @Body createPostRequest: CreatePostRequest
    ): Response<StoreMeResponse<Unit>>

    /**
     * 투표 게시글 추가
     */
    @POST("posts/store/{storeId}/vote")
    suspend fun createVotePost(
        @Path("storeId") storeId: String,
        @Body createVotePostRequest: CreateVotePostRequest
    ): Response<StoreMeResponse<Unit>>

    /**
     * 설문 게시글 추가
     */
    @POST("posts/store/{storeId}/survey")
    suspend fun createSurveyPost(
        @Path("storeId") storeId: String,
        @Body createSurveyPostRequest: CreateSurveyPostRequest
    ): Response<StoreMeResponse<Unit>>

    /**
     * 쿠폰 홍보 게시글 추가
     */
    @POST("posts/store/{storeId}/coupon")
    suspend fun createCouponPost(
        @Path("storeId") storeId: String,
        @Body createCouponPostRequest: CreateCouponPostRequest
    ): Response<StoreMeResponse<Unit>>
}