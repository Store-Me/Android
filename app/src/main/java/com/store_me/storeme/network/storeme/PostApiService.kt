package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.enums.post.PostType
import com.store_me.storeme.data.request.store.CommentRequest
import com.store_me.storeme.data.request.store.CreateCouponPostRequest
import com.store_me.storeme.data.request.store.CreatePostRequest
import com.store_me.storeme.data.request.store.CreateSurveyPostRequest
import com.store_me.storeme.data.request.store.CreateVotePostRequest
import com.store_me.storeme.data.request.store.PatchLabelRequest
import com.store_me.storeme.data.response.CommentResponse
import com.store_me.storeme.data.response.NormalPostListResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.store.post.LabelData
import com.store_me.storeme.data.store.post.NormalPostData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    /**
     * 전체 Normal 게시글 조회
     */
    @GET("posts/store/{storeId}/posts")
    suspend fun getNormalPost(
        @Path("storeId") storeId: String,
    ): Response<NormalPostListResponse>

    /**
     * 특정 Label 게시글 조회
     */
    @GET("posts/store/{storeId}/posts/{labelId}")
    suspend fun getNormalPostByLabelId(
        @Path("storeId") storeId: String,
        @Path("labelId") labelId: String
    ): Response<NormalPostListResponse>

    /**
     * 투표 게시글 조회
     */
    @GET("posts/store/{storeId}/posts/{labelId}")
    suspend fun getVotePost(
        @Path("storeId") storeId: String,
        @Path("labelId") labelId: String = PostType.VOTE.name
    )

    /**
     * 설문 게시글 조회
     */
    @GET("posts/store/{storeId}/posts/{labelId}")
    suspend fun getSurveyPost(
        @Path("storeId") storeId: String,
        @Path("labelId") labelId: String = PostType.SURVEY.name
    )

    /**
     * 쿠폰 홍보 게시글 조회
     */
    @GET("posts/store/{storeId}/posts/{labelId}")
    suspend fun getCouponPost(
        @Path("storeId") storeId: String,
        @Path("labelId") labelId: String = PostType.COUPON.name
    )

    /**
     * 게시글 좋아요 추가
     */
    @POST("posts/store/{storeId}/posts/{postId}/like")
    suspend fun postNormalPostLike(
        @Path("storeId") storeId: String,
        @Path("postId") postId: String
    ): Response<StoreMeResponse<NormalPostData>>

    /**
     * 게시글 좋아요 삭제
     */
    @DELETE("posts/store/{storeId}/posts/{postId}/like")
    suspend fun deleteNormalPostLike(
        @Path("storeId") storeId: String,
        @Path("postId") postId: String
    ): Response<StoreMeResponse<NormalPostData>>

    /**
     * 게시글 댓글 추가
     */
    @POST("posts/store/{storeId}/posts/{postId}/comment")
    suspend fun postPostComment(
        @Path("storeId") storeId: String,
        @Path("postId") postId: String,
        @Body postCommentRequest: CommentRequest
    ): Response<StoreMeResponse<Unit>>

    /**
     * 게시글 댓글 조회
     */
    @GET("posts/store/{storeId}/posts/{postId}/comments")
    suspend fun getPostComment(
        @Path("storeId") storeId: String,
        @Path("postId") postId: String
    ): Response<CommentResponse>

    /**
     * 게시글 댓글 수정
     */
    @PATCH("posts/store/{storeId}/posts/{postId}/comments/{commentId}")
    suspend fun patchPostComment(
        @Path("storeId") storeId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
        @Body patchCommentRequest: CommentRequest
    ): Response<StoreMeResponse<Unit>>

    /**
     * 게시글 댓글 삭제
     */
    @DELETE("posts/store/{storeId}/posts/{postId}/comments/{commentId}")
    suspend fun deletePostComment(
        @Path("storeId") storeId: String,
        @Path("postId") postId: String,
        @Path("commentId") commentId: String
    ): Response<StoreMeResponse<Unit>>

    /**
     * 게시글 조회수 추가
     */
    @POST("posts/store/{storeId}/posts/{postId}/view")
    suspend fun postPostViews(
        @Path("storeId") storeId: String,
        @Path("postId") postId: String
    ): Response<StoreMeResponse<Unit>>
}