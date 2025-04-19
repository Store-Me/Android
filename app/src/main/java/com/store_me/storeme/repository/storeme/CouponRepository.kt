package com.store_me.storeme.repository.storeme

import com.store_me.auth.Auth
import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.request.store.CouponRequest
import com.store_me.storeme.data.response.AcceptCouponResponse
import com.store_me.storeme.data.response.CouponsResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.response.UseCouponResponse
import com.store_me.storeme.network.storeme.CouponApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import timber.log.Timber
import javax.inject.Inject

interface CouponRepository {
    suspend fun getStoreCoupons(): Result<CouponsResponse>

    suspend fun postStoreCoupon(couponRequest: CouponRequest): Result<StoreMeResponse<CouponData>>

    suspend fun patchStoreCoupon(couponRequest: CouponRequest): Result<StoreMeResponse<CouponData>>

    suspend fun deleteStoreCoupon(couponId: String): Result<StoreMeResponse<Unit>>

    suspend fun acceptStoreCoupon(couponId: String): Result<StoreMeResponse<AcceptCouponResponse>>

    suspend fun useStoreCoupon(couponId: String): Result<StoreMeResponse<UseCouponResponse>>
}

class CouponRepositoryImpl @Inject constructor(
    private val couponApiService: CouponApiService,
    private val auth: Auth
): CouponRepository {
    override suspend fun getStoreCoupons(): Result<CouponsResponse> {
        return try {
            val response = couponApiService.getStoreCoupons(
                storeId = auth.getStoreId(),
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                Result.success(responseBody ?: CouponsResponse(coupons = emptyList()))
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun postStoreCoupon(couponRequest: CouponRequest): Result<StoreMeResponse<CouponData>> {
        return try {
            val response = couponApiService.postStoreCoupon(
                storeId = auth.getStoreId(),
                couponRequest = couponRequest
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

    override suspend fun patchStoreCoupon(couponRequest: CouponRequest): Result<StoreMeResponse<CouponData>> {
        return try {
            val response = couponApiService.patchStoreCoupon(
                storeId = auth.getStoreId(),
                couponRequest = couponRequest
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

    override suspend fun deleteStoreCoupon(couponId: String): Result<StoreMeResponse<Unit>> {
        return try {
            val response = couponApiService.deleteStoreCoupon(
                storeId = auth.getStoreId(),
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

    override suspend fun acceptStoreCoupon(
        couponId: String
    ): Result<StoreMeResponse<AcceptCouponResponse>> {
        return try {
            val response = couponApiService.acceptStoreCoupon(
                storeId = auth.getStoreId(),
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

    override suspend fun useStoreCoupon(
        couponId: String
    ): Result<StoreMeResponse<UseCouponResponse>> {
        return try {
            val response = couponApiService.useStoreCoupon(
                storeId = auth.getStoreId(),
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