package com.store_me.storeme.repository.storeme

import com.google.firebase.Timestamp
import com.store_me.auth.Auth
import com.store_me.storeme.data.store.story.StoryData
import com.store_me.storeme.data.request.store.PostStoryRequest
import com.store_me.storeme.data.response.PagingResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.network.storeme.StoryApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import com.store_me.storeme.utils.response.UserIdsResponse
import timber.log.Timber
import javax.inject.Inject

/**
 * 스토리 관련 Repository
 */
interface StoryRepository {
    suspend fun getStoreStories(lastCreatedAt: Timestamp?): Result<PagingResponse<List<StoryData>>>

    suspend fun postStoreStory(postStoreStoryRequest: PostStoryRequest): Result<PagingResponse<List<StoryData>>>

    suspend fun deleteStoreStory(storyId: String): Result<StoreMeResponse<Unit>>

    suspend fun postStoryLike(storyId: String): Result<StoreMeResponse<StoryData>>

    suspend fun deleteStoryLike(storyId: String): Result<StoreMeResponse<StoryData>>

    suspend fun getStoryLikeUsers(storyId: String): Result<StoreMeResponse<UserIdsResponse>>
}

class StoryRepositoryImpl @Inject constructor(
    private val storyApiService: StoryApiService,
    private val auth: Auth
): StoryRepository {
    override suspend fun getStoreStories(lastCreatedAt: Timestamp?): Result<PagingResponse<List<StoryData>>> {
        return try {
            val response = storyApiService.getStoreStories(
                storeId = auth.getStoreId(),
                lastCreatedAt = lastCreatedAt
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

    override suspend fun postStoreStory(
        postStoreStoryRequest: PostStoryRequest
    ): Result<PagingResponse<List<StoryData>>> {
        return try {
            val response = storyApiService.postStoreStory(
                storeId = auth.getStoreId(),
                postStoreStoryRequest = postStoreStoryRequest
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

    override suspend fun deleteStoreStory(
        storyId: String
    ): Result<StoreMeResponse<Unit>> {
        return try {
            val response = storyApiService.deleteStoreStory(
                storeId = auth.getStoreId(),
                storyId = storyId
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

    override suspend fun postStoryLike(storyId: String): Result<StoreMeResponse<StoryData>> {
        return try {
            val response = storyApiService.postStoryLike(
                storeId = auth.getStoreId(),
                storyId = storyId
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

    override suspend fun deleteStoryLike(storyId: String): Result<StoreMeResponse<StoryData>> {
        return try {
            val response = storyApiService.deleteStoryLike(
                storeId = auth.getStoreId(),
                storyId = storyId
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

    override suspend fun getStoryLikeUsers(storyId: String): Result<StoreMeResponse<UserIdsResponse>> {
        return try {
            val response = storyApiService.getStoryLikeUsers(
                storeId = auth.getStoreId(),
                storyId = storyId
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