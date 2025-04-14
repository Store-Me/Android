package com.store_me.storeme.repository.storeme

import com.store_me.storeme.data.LabelData
import com.store_me.storeme.data.request.store.CreatePostRequest
import com.store_me.storeme.data.request.store.PatchLabelRequest
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.network.storeme.PostApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import timber.log.Timber
import javax.inject.Inject

interface PostRepository {
    suspend fun getLabels(storeId: String): Result<List<LabelData>>

    suspend fun patchLabel(storeId: String, patchLabelRequest: PatchLabelRequest): Result<StoreMeResponse<List<LabelData>>>

    suspend fun createPost(storeId: String, createPostRequest: CreatePostRequest): Result<StoreMeResponse<Unit>>
}

class PostRepositoryImpl @Inject constructor(
    private val postApiService: PostApiService
) : PostRepository {
    override suspend fun getLabels(storeId: String): Result<List<LabelData>> {
        return try {
            val response = postApiService.getLabels(storeId = storeId)

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

    override suspend fun patchLabel(storeId: String, patchLabelRequest: PatchLabelRequest): Result<StoreMeResponse<List<LabelData>>> {
        return try {
            val response = postApiService.patchLabel(
                storeId = storeId,
                patchLabelRequest = patchLabelRequest
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

    override suspend fun createPost(
        storeId: String,
        createPostRequest: CreatePostRequest
    ): Result<StoreMeResponse<Unit>> {
        return try {
            val response = postApiService.createPost(
                storeId = storeId,
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
}