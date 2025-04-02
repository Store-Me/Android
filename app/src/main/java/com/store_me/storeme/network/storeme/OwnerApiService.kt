package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.StampCouponData
import com.store_me.storeme.data.request.store.CouponRequest
import com.store_me.storeme.data.request.store.PatchBusinessHoursRequest
import com.store_me.storeme.data.request.store.PatchStoreFeaturedImagesRequest
import com.store_me.storeme.data.request.store.PatchLinksRequest
import com.store_me.storeme.data.request.store.PatchStoreNoticeRequest
import com.store_me.storeme.data.request.store.PatchStoreDescriptionRequest
import com.store_me.storeme.data.request.store.PatchStoreIntroRequest
import com.store_me.storeme.data.request.store.PatchStoreLocationRequest
import com.store_me.storeme.data.request.store.PatchStorePhoneNumberRequest
import com.store_me.storeme.data.request.store.PatchStoreProfileImagesRequest
import com.store_me.storeme.data.request.store.PostStampCouponRequest
import com.store_me.storeme.data.response.AcceptCouponResponse
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.data.response.CouponsResponse
import com.store_me.storeme.data.response.FeaturedImagesResponse
import com.store_me.storeme.data.response.LinksResponse
import com.store_me.storeme.data.response.MenusResponse
import com.store_me.storeme.data.response.MyStoresResponse
import com.store_me.storeme.data.response.NoticeResponse
import com.store_me.storeme.data.response.PatchResponse
import com.store_me.storeme.data.response.StampCouponResponse
import com.store_me.storeme.data.response.StampCouponPasswordResponse
import com.store_me.storeme.data.response.UseCouponResponse
import com.store_me.storeme.data.store.StoreInfoData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface OwnerApiService {
    /**
     * 가게 목록 요청 API
     */
    @GET("auth/stores")
    suspend fun getMyStores(
    ): Response<MyStoresResponse>

    /**
     * 가게 정보 요청 API
     */
    @GET("auth/store/{storeId}")
    suspend fun getStoreData(
        @Path("storeId") storeId: String
    ): Response<StoreInfoData>

    /**
     * 가게 이미지 수정 API
     */
    @PATCH("auth/store/{storeId}/images")
    suspend fun patchStoreProfileImages(
        @Path("storeId") storeId: String,
        @Body patchStoreProfileImagesRequest: PatchStoreProfileImagesRequest
    ): Response<PatchResponse<StoreInfoData>>

    /**
     * 가게 소개 수정 API
     */
    @PATCH("auth/store/{storeId}/intro")
    suspend fun patchStoreIntro(
        @Path("storeId") storeId: String,
        @Body patchStoreIntroRequest: PatchStoreIntroRequest
    ): Response<PatchResponse<StoreInfoData>>

    /**
     * 가게 설명 수정 API
     */
    @PATCH("auth/store/{storeId}/description")
    suspend fun patchStoreDescription(
        @Path("storeId") storeId: String,
        @Body patchStoreDescriptionRequest: PatchStoreDescriptionRequest
    ): Response<PatchResponse<StoreInfoData>>

    /**
     * 영업 시간 조회 API
     */
    @GET("auth/store/{storeId}/business-hours")
    suspend fun getBusinessHours(
        @Path("storeId") storeId: String
    ): Response<BusinessHoursResponse>

    /**
     * 영업 시간 수정 API
     */
    @PATCH("auth/store/{storeId}/business-hours")
    suspend fun patchBusinessHours(
        @Path("storeId") storeId: String,
        @Body patchBusinessHoursRequest: PatchBusinessHoursRequest
    ): Response<PatchResponse<BusinessHoursResponse>>

    /**
     * 가게 링크 조회 API
     */
    @GET("auth/store/{storeId}/links")
    suspend fun getStoreLinks(
        @Path("storeId") storeId: String
    ): Response<LinksResponse>

    /**
     * 가게 링크 수정 API
     */
    @PATCH("auth/store/{storeId}/links")
    suspend fun patchStoreLinks(
        @Path("storeId") storeId: String,
        @Body patchLinksRequest: PatchLinksRequest
    ): Response<PatchResponse<LinksResponse>>

    /**
     * 가게 전화번호 수정 API
     */
    @PATCH("auth/store/{storeId}/phone-number")
    suspend fun patchStorePhoneNumber(
        @Path("storeId") storeId: String,
        @Body patchStorePhoneNumberRequest: PatchStorePhoneNumberRequest
    ): Response<PatchResponse<StoreInfoData>>

    /**
     * 공지사항 조회 API
     */
    @GET("auth/store/{storeId}/notice")
    suspend fun getStoreNotice(
        @Path("storeId") storeId: String
    ): Response<NoticeResponse>

    /**
     * 공지사항 수정 API
     */
    @PATCH("auth/store/{storeId}/notice")
    suspend fun patchStoreNotice(
        @Path("storeId") storeId: String,
        @Body patchStoreNoticeRequest: PatchStoreNoticeRequest
    ): Response<PatchResponse<NoticeResponse>>

    /**
     * 위치 수정 API
     */
    @PATCH("auth/store/{storeId}/location")
    suspend fun patchStoreLocation(
        @Path("storeId") storeId: String,
        @Body patchStoreLocationRequest: PatchStoreLocationRequest
    ): Response<PatchResponse<StoreInfoData>>

    /**
     * 대표 이미지 조회 API
     */
    @GET("auth/store/{storeId}/featured-images")
    suspend fun getStoreFeaturedImages(
        @Path("storeId") storeId: String
    ): Response<FeaturedImagesResponse>

    /**
     * 대표 이미지 수정 API
     */
    @PATCH("auth/store/{storeId}/featured-images")
    suspend fun patchStoreFeaturedImages(
        @Path("storeId") storeId: String,
        @Body patchStoreFeaturedImagesRequest: PatchStoreFeaturedImagesRequest
    ): Response<PatchResponse<FeaturedImagesResponse>>

    /**
     * 쿠폰 목록 조회 API
     */
    @GET("auth/store/{storeId}/coupons")
    suspend fun getStoreCoupons(
        @Path("storeId") storeId: String
    ): Response<CouponsResponse>

    /**
     * 쿠폰 생성 API
     */
    @POST("auth/store/{storeId}/coupons")
    suspend fun postStoreCoupon(
        @Path("storeId") storeId: String,
        @Body couponRequest: CouponRequest
    ): Response<PatchResponse<CouponData>>

    /**
     * 쿠폰 수정 API
     */
    @PATCH("auth/store/{storeId}/coupons/{couponId}")
    suspend fun patchStoreCoupon(
        @Path("storeId") storeId: String,
        @Body couponRequest: CouponRequest
    ): Response<PatchResponse<CouponData>>

    /**
     * 쿠폰 삭제 API
     */
    @DELETE("auth/store/{storeId}/coupons/{couponId}")
    suspend fun deleteStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<PatchResponse<Unit>>

    /**
     * 쿠폰 수령 API
     */
    @POST("auth/store/{storeId}/coupons/{couponId}/accept")
    suspend fun acceptStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<PatchResponse<AcceptCouponResponse>>

    /**
     * 쿠폰 사용 API
     */
    @POST("auth/store/{storeId}/coupons/{couponId}/use")
    suspend fun useStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<PatchResponse<UseCouponResponse>>

    /**
     * 메뉴 조회 API
     */
    @GET("auth/store/{storeId}/menus")
    suspend fun getStoreMenus(
        @Path("storeId") storeId: String
    ): Response<MenusResponse>

    /**
     * 메뉴 수정 API
     */
    @PATCH("auth/store/{storeId}/menus")
    suspend fun patchStoreMenus(
        @Path("storeId") storeId: String,
        @Body patchStoreMenusRequest: MenusResponse
    ): Response<PatchResponse<MenusResponse>>

    /**
     * 스탬프 쿠폰 생성 API
     */
    @POST("auth/store/{storeId}/stamp-coupon")
    suspend fun postStampCoupon(
        @Path("storeId") storeId: String,
        @Body postStampCouponRequest: PostStampCouponRequest
    ): Response<PatchResponse<StampCouponResponse>>

    /**
     * 스탬프 쿠폰 조회 API
     */
    @GET("auth/store/{storeId}/stamp-coupon")
    suspend fun getStampCoupon(
        @Path("storeId") storeId: String
    ): Response<StampCouponResponse>

    /**
     * 스탬프 쿠폰 수정 API
     */
    @PATCH("auth/store/{storeId}/stamp-coupon")
    suspend fun patchStampCoupon(
        @Path("storeId") storeId: String,
        @Body patchStampCouponRequest: StampCouponData
    ): Response<PatchResponse<StampCouponResponse>>

    /**
     * 스탬프 비밀번호 조회 API
     */
    @GET("auth/store/{storeId}/stamp-coupon/password")
    suspend fun getStampCouponPassword(
        @Path("storeId") storeId: String
    ): Response<PatchResponse<StampCouponPasswordResponse>>

    /**
     * 스탬프 비밀번호 변경 API
     */
    @PATCH("auth/store/{storeId}/stamp-coupon/password")
    suspend fun patchStampCouponPassword(
        @Path("storeId") storeId: String,
        @Body patchStampCouponPasswordRequest: StampCouponPasswordResponse
    ): Response<PatchResponse<Unit>>
}