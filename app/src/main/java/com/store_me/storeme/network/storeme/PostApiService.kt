package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.LabelData
import com.store_me.storeme.data.request.store.CreatePostRequest
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
    @POST("posts/store/{storeId}/posts")
    suspend fun createPost(
        @Path("storeId") storeId: String,
        @Body createPostRequest: CreatePostRequest
    ): Response<StoreMeResponse<Unit>>
}