package com.store_me.storeme.repository.storeme

import android.content.Context
import android.util.Log
import com.store_me.storeme.data.response.StoreListResponse
import com.store_me.storeme.network.storeme.OwnerApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

/**
 * 사장님 관련 Repository
 */
interface OwnerRepository {
    suspend fun getStoreList(): Result<StoreListResponse>
}

class OwnerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ownerApiService: OwnerApiService
): OwnerRepository {
    override suspend fun getStoreList(): Result<StoreListResponse> {
        return try {
            val response = ownerApiService.getStoreList()

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(response.body().toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(responseBody.result ?: StoreListResponse(emptyList()))
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }

            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

}