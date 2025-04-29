package com.store_me.storeme.repository.storeme

import com.store_me.auth.Auth
import com.store_me.storeme.data.enums.StoreCategory
import com.store_me.storeme.data.request.store.ReplyRequest
import com.store_me.storeme.data.request.store.ReviewRequest
import com.store_me.storeme.data.response.ReviewCountResponse
import com.store_me.storeme.data.response.ReviewTextResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.store.review.ReviewData
import com.store_me.storeme.network.storeme.ReviewApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import timber.log.Timber
import javax.inject.Inject

/**
 * 리뷰 관련 Repository
 */
interface ReviewRepository{
    suspend fun getStoreReviewTexts(category: StoreCategory): Result<ReviewTextResponse>

    suspend fun getStoreReviewCounts(): Result<Map<String, ReviewCountResponse>>

    suspend fun getStoreReviews(): Result<List<ReviewData>>

    suspend fun postStoreReview(reviewRequest: ReviewRequest): Result<StoreMeResponse<ReviewData>>

    suspend fun patchStoreReview(reviewId: String, reviewRequest: ReviewRequest): Result<StoreMeResponse<ReviewData>>

    suspend fun deleteStoreReview(reviewId: String): Result<StoreMeResponse<Unit>>

    suspend fun postStoreReviewReply(reviewId: String, replyRequest: ReplyRequest): Result<StoreMeResponse<ReviewData>>

    suspend fun patchStoreReviewReply(reviewId: String, replyRequest: ReplyRequest): Result<StoreMeResponse<ReviewData>>

    suspend fun deleteStoreReviewReply(reviewId: String): Result<StoreMeResponse<Unit>>
}

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApiService: ReviewApiService,
    private val auth: Auth
): ReviewRepository {
    override suspend fun getStoreReviewTexts(category: StoreCategory): Result<ReviewTextResponse> {
        return try {
            val response = reviewApiService.getStoreReviewTexts(
                category = category.key
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun getStoreReviewCounts(): Result<Map<String, ReviewCountResponse>> {
        return try {
            val response = reviewApiService.getStoreReviewCounts(
                storeId = auth.getStoreId()
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun getStoreReviews(): Result<List<ReviewData>> {
        return try {
            val response = reviewApiService.getStoreReviews(
                storeId = auth.getStoreId()
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun postStoreReview(reviewRequest: ReviewRequest): Result<StoreMeResponse<ReviewData>> {
        return try {
            val response = reviewApiService.postStoreReview(
                storeId = auth.getStoreId(),
                reviewRequest = reviewRequest
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun patchStoreReview(
        reviewId: String,
        reviewRequest: ReviewRequest
    ): Result<StoreMeResponse<ReviewData>> {
        return try {
            val response = reviewApiService.patchStoreReview(
                storeId = auth.getStoreId(),
                reviewId = reviewId,
                reviewRequest = reviewRequest
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun deleteStoreReview(reviewId: String): Result<StoreMeResponse<Unit>> {
        return try {
            val response = reviewApiService.deleteStoreReview(
                storeId = auth.getStoreId(),
                reviewId = reviewId
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun postStoreReviewReply(
        reviewId: String,
        replyRequest: ReplyRequest
    ): Result<StoreMeResponse<ReviewData>> {
        return try {
            val response = reviewApiService.postStoreReviewReply(
                storeId = auth.getStoreId(),
                reviewId = reviewId,
                replyRequest = replyRequest
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun patchStoreReviewReply(
        reviewId: String,
        replyRequest: ReplyRequest
    ): Result<StoreMeResponse<ReviewData>> {
        return try {
            val response = reviewApiService.patchStoreReviewReply(
                storeId = auth.getStoreId(),
                reviewId = reviewId,
                replyRequest = replyRequest
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun deleteStoreReviewReply(reviewId: String): Result<StoreMeResponse<Unit>> {
        return try {
            val response = reviewApiService.deleteStoreReviewReply(
                storeId = auth.getStoreId(),
                reviewId = reviewId
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }
}