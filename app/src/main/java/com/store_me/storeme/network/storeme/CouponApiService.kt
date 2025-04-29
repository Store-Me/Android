package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.store.coupon.CouponData
import com.store_me.storeme.data.store.coupon.CouponStats
import com.store_me.storeme.data.request.store.CouponRequest
import com.store_me.storeme.data.response.AcceptCouponResponse
import com.store_me.storeme.data.response.CouponUsersResponse
import com.store_me.storeme.data.response.CouponsResponse
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.response.UseCouponResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CouponApiService {
    /**
     * 쿠폰 목록 조회 API
     */
    @GET("coupon/store/{storeId}/coupons")
    suspend fun getStoreCoupons(
        @Path("storeId") storeId: String
    ): Response<CouponsResponse>

    /**
     * 쿠폰 생성 API
     */
    @POST("coupon/store/{storeId}/coupons")
    suspend fun postStoreCoupon(
        @Path("storeId") storeId: String,
        @Body couponRequest: CouponRequest
    ): Response<StoreMeResponse<CouponData>>

    /**
     * 쿠폰 수정 API
     */
    @PATCH("coupon/{storeId}/coupons/{couponId}")
    suspend fun patchStoreCoupon(
        @Path("storeId") storeId: String,
        @Body couponRequest: CouponRequest
    ): Response<StoreMeResponse<CouponData>>

    /**
     * 쿠폰 삭제 API
     */
    @DELETE("coupon/{storeId}/coupons/{couponId}")
    suspend fun deleteStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<Unit>>

    /**
     * 쿠폰 수령 API
     */
    @POST("coupon/{storeId}/coupons/{couponId}/accept")
    suspend fun acceptStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<AcceptCouponResponse>>

    /**
     * 쿠폰 사용 API
     */
    @POST("coupon/{storeId}/coupons/{couponId}/use")
    suspend fun useStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<UseCouponResponse>>

    /**
     * 쿠폰 통계 조회 API
     */
    @GET("coupon/store/{storeId}/coupons/{couponId}/stats")
    suspend fun getCouponStats(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<CouponStats>>

    /**
     * 쿠폰 사용자 조회 API
     */
    @GET("coupon/store/{storeId}/coupons/{couponId}/users")
    suspend fun getCouponUsers(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<CouponUsersResponse>>
}