package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.request.store.ReplyRequest
import com.store_me.storeme.data.request.store.ReviewRequest
import com.store_me.storeme.data.response.ReviewCountResponse
import com.store_me.storeme.data.response.ReviewTextResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.review.ReviewData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
interface ReviewApiService {
    //스토어 category 별 리뷰 Text 조회
    @GET("review/store/texts/{category}")
    suspend fun getStoreReviewTexts(
        @Path("category") category: String,
    ): Response<ReviewTextResponse>

    //전체 리뷰 Count 조회
    @GET("review/store/{storeId}/reviews-count")
    suspend fun getStoreReviewCounts(
        @Path("storeId") storeId: String
    ): Response<Map<String, ReviewCountResponse>>

    //특정 스토어 리뷰 조회
    @GET("review/store/{storeId}")
    suspend fun getStoreReviews(
        @Path("storeId") storeId: String,
    ): Response<List<ReviewData>>

    //리뷰 추가
    @POST("review/store/{storeId}")
    suspend fun postStoreReview(
        @Path("storeId") storeId: String,
        @Body reviewRequest: ReviewRequest,
    ): Response<StoreMeResponse<ReviewData>>

    //리뷰 수정
    @PATCH("review/store/{storeId}/{reviewId}")
    suspend fun patchStoreReview(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
        @Body reviewRequest: ReviewRequest,
    ): Response<StoreMeResponse<ReviewData>>

    //리뷰 삭제
    @DELETE("review/store/{storeId}/{reviewId}")
    suspend fun deleteStoreReview(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
    ): Response<StoreMeResponse<Unit>>

    //리뷰 답글 추가
    @POST("review/store/{storeId}/{reviewId}/reply")
    suspend fun postStoreReviewReply(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
        @Body replyRequest: ReplyRequest
    ): Response<StoreMeResponse<ReviewData>>

    //리뷰 답글 수정
    @PATCH("review/store/{storeId}/{reviewId}/reply")
    suspend fun patchStoreReviewReply(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
        @Body replyRequest: ReplyRequest
    ): Response<StoreMeResponse<ReviewData>>

    //리뷰 답글 삭제
    @DELETE("review/store/{storeId}/{reviewId}/reply")
    suspend fun deleteStoreReviewReply(
        @Path("storeId") storeId: String,
        @Path("reviewId") reviewId: String,
    ): Response<StoreMeResponse<Unit>>
}