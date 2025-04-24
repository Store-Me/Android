package com.store_me.storeme.network.storeme

import com.google.firebase.Timestamp
import com.store_me.storeme.data.StoryData
import com.store_me.storeme.data.request.store.PostStoryRequest
import com.store_me.storeme.data.response.PagingResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.utils.response.UserIdsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryApiService {
    /**
     * 스토리 목록 조회 API
     */
    @GET("story/store/{storeId}/stories")
    suspend fun getStoreStories(
        @Path("storeId") storeId: String,
        @Query("limit") limit: Int = 10,
        @Query("lastCreatedAt") lastCreatedAt: Timestamp?
    ): Response<PagingResponse<List<StoryData>>>

    /**
     * 스토리 추가 API
     */
    @POST("story/store/{storeId}/stories")
    suspend fun postStoreStory(
        @Path("storeId") storeId: String,
        @Body postStoreStoryRequest: PostStoryRequest
    ): Response<PagingResponse<List<StoryData>>>

    /**
     * 스토리 삭제 API
     */
    @DELETE("story/store/{storeId}/stories/{storyId}")
    suspend fun deleteStoreStory(
        @Path("storeId") storeId: String,
        @Path("storyId") storyId: String
    ): Response<StoreMeResponse<Unit>>

    /**
     * 스토리 좋아요 요청 API
     */
    @POST("story/store/{storeId}/stories/{storyId}/like")
    suspend fun postStoryLike(
        @Path("storeId") storeId: String,
        @Path("storyId") storyId: String
    ): Response<StoreMeResponse<StoryData>>

    /**
     * 스토리 좋아요 취소 API
     */
    @DELETE("story/store/{storeId}/stories/{storyId}/like")
    suspend fun deleteStoryLike(
        @Path("storeId") storeId: String,
        @Path("storyId") storyId: String
    ): Response<StoreMeResponse<StoryData>>

    /**
     * 스토리 좋아요 사용자 목록 조회 API
     */
    @GET("story/store/{storeId}/stories/{storyId}/like")
    suspend fun getStoryLikeUsers(
        @Path("storeId") storeId: String,
        @Path("storyId") storyId: String,
    ): Response<StoreMeResponse<UserIdsResponse>>
}