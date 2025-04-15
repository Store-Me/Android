package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.response.CouponResponse
import com.store_me.storeme.data.response.CustomerInfoResponse
import com.store_me.storeme.data.response.StoreMeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CustomerApiService {
    /**
     * 회원 정보 요청 (손님)
     */
    @GET("api/customer")
    suspend fun getCustomerInfo(

    ): Response<CustomerInfoResponse>

    /**
     * 쿠폰 Id를 통해 단일 쿠폰을 조회
     */
    @GET("api/auth/coupon/{couponId}")
    suspend fun getCouponData(
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<CouponResponse>>
}