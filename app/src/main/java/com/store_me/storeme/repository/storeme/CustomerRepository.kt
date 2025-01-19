package com.store_me.storeme.repository.storeme

import android.content.Context
import com.store_me.storeme.data.response.CustomerInfoResponse
import com.store_me.storeme.network.storeme.CustomerApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

interface CustomerRepository {
    suspend fun getCustomerInfo(): Result<CustomerInfoResponse>
}

class CustomerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val customerApiService: CustomerApiService
): CustomerRepository {
    override suspend fun getCustomerInfo(): Result<CustomerInfoResponse> {
        return try {
            val response = customerApiService.getCustomerInfo()

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i("CustomerInfoResponse: $responseBody")

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(responseBody.result)
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