package com.store_me.storeme.repository.storeme

import com.store_me.storeme.data.response.CouponResponse
import com.store_me.storeme.data.response.CustomerInfoResponse
import com.store_me.storeme.data.response.PatchResponse
import com.store_me.storeme.network.storeme.CustomerApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import timber.log.Timber
import javax.inject.Inject

interface CustomerRepository {
    suspend fun getCustomerInfo(): Result<CustomerInfoResponse>

    suspend fun getCouponData(couponId: String): Result<PatchResponse<CouponResponse>>
}

class CustomerRepositoryImpl @Inject constructor(
    private val customerApiService: CustomerApiService
): CustomerRepository {
    override suspend fun getCustomerInfo(): Result<CustomerInfoResponse> {
        return Result.success(customerApiService.getCustomerInfo().body()!!)
    }

    override suspend fun getCouponData(couponId: String): Result<PatchResponse<CouponResponse>> {
        return try {
            val response = customerApiService.getCouponData(
                couponId = couponId
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