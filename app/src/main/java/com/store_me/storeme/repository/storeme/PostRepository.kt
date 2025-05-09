package com.store_me.storeme.repository.storeme

import com.store_me.auth.Auth
import com.store_me.storeme.data.store.post.LabelData
import com.store_me.storeme.data.request.store.CreateCouponPostRequest
import com.store_me.storeme.data.request.store.CreatePostRequest
import com.store_me.storeme.data.request.store.CreateSurveyPostRequest
import com.store_me.storeme.data.request.store.CreateVotePostRequest
import com.store_me.storeme.data.request.store.PatchLabelRequest
import com.store_me.storeme.data.response.NormalPostListResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.network.storeme.PostApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import timber.log.Timber
import javax.inject.Inject

interface PostRepository {
    suspend fun getLabels(): Result<List<LabelData>>

    suspend fun patchLabel(patchLabelRequest: PatchLabelRequest): Result<StoreMeResponse<List<LabelData>>>

    suspend fun createPost(createPostRequest: CreatePostRequest): Result<StoreMeResponse<Unit>>

    suspend fun createVotePost(createVotePostRequest: CreateVotePostRequest): Result<StoreMeResponse<Unit>>

    suspend fun createSurveyPost(createSurveyPostRequest: CreateSurveyPostRequest): Result<StoreMeResponse<Unit>>

    suspend fun createCouponPost(createCouponPostRequest: CreateCouponPostRequest): Result<StoreMeResponse<Unit>>

    suspend fun getNormalPostByLabelId(labelId: String?): Result<NormalPostListResponse>
}

class PostRepositoryImpl @Inject constructor(
    private val postApiService: PostApiService,
    private val auth: Auth
) : PostRepository {
    override suspend fun getLabels(): Result<List<LabelData>> {
        return try {
            val response = postApiService.getLabels(storeId = auth.getStoreId())

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    val sortedResponseBody = responseBody.sortedBy { it.order }

                    Result.success(sortedResponseBody)
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

    override suspend fun patchLabel(patchLabelRequest: PatchLabelRequest): Result<StoreMeResponse<List<LabelData>>> {
        return try {
            val response = postApiService.patchLabel(
                storeId = auth.getStoreId(),
                patchLabelRequest = patchLabelRequest
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if(responseBody != null) {
                    val sortedResponseBody = responseBody.result.sortedBy { it.order }

                    Result.success(responseBody.copy(result = sortedResponseBody))
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

    override suspend fun createPost(
        createPostRequest: CreatePostRequest
    ): Result<StoreMeResponse<Unit>> {
        return try {
            val response = postApiService.createPost(
                storeId = auth.getStoreId(),
                createPostRequest = createPostRequest
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

    override suspend fun createVotePost(createVotePostRequest: CreateVotePostRequest): Result<StoreMeResponse<Unit>> {
        return try {
            val response = postApiService.createVotePost(
                storeId = auth.getStoreId(),
                createVotePostRequest = createVotePostRequest
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

    override suspend fun createSurveyPost(createSurveyPostRequest: CreateSurveyPostRequest): Result<StoreMeResponse<Unit>> {
        return try {
            val response = postApiService.createSurveyPost(
                storeId = auth.getStoreId(),
                createSurveyPostRequest = createSurveyPostRequest
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

    override suspend fun createCouponPost(createCouponPostRequest: CreateCouponPostRequest): Result<StoreMeResponse<Unit>> {
        return try {
            val response = postApiService.createCouponPost(
                storeId = auth.getStoreId(),
                createCouponPostRequest = createCouponPostRequest
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

    override suspend fun getNormalPostByLabelId(labelId: String?): Result<NormalPostListResponse> {
        return try {
            val response =
                if(labelId != null) {
                    postApiService.getNormalPostByLabelId(
                        storeId = auth.getStoreId(),
                        labelId = labelId
                    )
                } else {
                    postApiService.getNormalPost(
                        storeId = auth.getStoreId()
                    )
                }

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