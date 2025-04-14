package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.StampCouponData
import com.store_me.storeme.data.StoryData
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
import com.store_me.storeme.data.request.store.PostStoryRequest
import com.store_me.storeme.data.response.AcceptCouponResponse
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.data.response.CouponsResponse
import com.store_me.storeme.data.response.FeaturedImagesResponse
import com.store_me.storeme.data.response.LinksResponse
import com.store_me.storeme.data.response.MenusResponse
import com.store_me.storeme.data.response.MyStoresResponse
import com.store_me.storeme.data.response.NoticeResponse
import com.store_me.storeme.data.response.PagingResponse
import com.store_me.storeme.data.response.StoreMeResponse
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
import retrofit2.http.Query

interface OwnerApiService {
    /**
     * 가게 목록 요청 API
     */
    @GET("api/auth/stores")
    suspend fun getMyStores(
    ): Response<MyStoresResponse>

    /**
     * 가게 정보 요청 API
     */
    @GET("api/auth/store/{storeId}")
    suspend fun getStoreData(
        @Path("storeId") storeId: String
    ): Response<StoreInfoData>

    /**
     * 가게 이미지 수정 API
     */
    @PATCH("api/auth/store/{storeId}/images")
    suspend fun patchStoreProfileImages(
        @Path("storeId") storeId: String,
        @Body patchStoreProfileImagesRequest: PatchStoreProfileImagesRequest
    ): Response<StoreMeResponse<StoreInfoData>>

    /**
     * 가게 소개 수정 API
     */
    @PATCH("api/auth/store/{storeId}/intro")
    suspend fun patchStoreIntro(
        @Path("storeId") storeId: String,
        @Body patchStoreIntroRequest: PatchStoreIntroRequest
    ): Response<StoreMeResponse<StoreInfoData>>

    /**
     * 가게 설명 수정 API
     */
    @PATCH("api/auth/store/{storeId}/description")
    suspend fun patchStoreDescription(
        @Path("storeId") storeId: String,
        @Body patchStoreDescriptionRequest: PatchStoreDescriptionRequest
    ): Response<StoreMeResponse<StoreInfoData>>

    /**
     * 영업 시간 조회 API
     */
    @GET("api/auth/store/{storeId}/business-hours")
    suspend fun getBusinessHours(
        @Path("storeId") storeId: String
    ): Response<BusinessHoursResponse>

    /**
     * 영업 시간 수정 API
     */
    @PATCH("api/auth/store/{storeId}/business-hours")
    suspend fun patchBusinessHours(
        @Path("storeId") storeId: String,
        @Body patchBusinessHoursRequest: PatchBusinessHoursRequest
    ): Response<StoreMeResponse<BusinessHoursResponse>>

    /**
     * 가게 링크 조회 API
     */
    @GET("api/auth/store/{storeId}/links")
    suspend fun getStoreLinks(
        @Path("storeId") storeId: String
    ): Response<LinksResponse>

    /**
     * 가게 링크 수정 API
     */
    @PATCH("api/auth/store/{storeId}/links")
    suspend fun patchStoreLinks(
        @Path("storeId") storeId: String,
        @Body patchLinksRequest: PatchLinksRequest
    ): Response<StoreMeResponse<LinksResponse>>

    /**
     * 가게 전화번호 수정 API
     */
    @PATCH("api/auth/store/{storeId}/phone-number")
    suspend fun patchStorePhoneNumber(
        @Path("storeId") storeId: String,
        @Body patchStorePhoneNumberRequest: PatchStorePhoneNumberRequest
    ): Response<StoreMeResponse<StoreInfoData>>

    /**
     * 공지사항 조회 API
     */
    @GET("api/auth/store/{storeId}/notice")
    suspend fun getStoreNotice(
        @Path("storeId") storeId: String
    ): Response<NoticeResponse>

    /**
     * 공지사항 수정 API
     */
    @PATCH("api/auth/store/{storeId}/notice")
    suspend fun patchStoreNotice(
        @Path("storeId") storeId: String,
        @Body patchStoreNoticeRequest: PatchStoreNoticeRequest
    ): Response<StoreMeResponse<NoticeResponse>>

    /**
     * 위치 수정 API
     */
    @PATCH("api/auth/store/{storeId}/location")
    suspend fun patchStoreLocation(
        @Path("storeId") storeId: String,
        @Body patchStoreLocationRequest: PatchStoreLocationRequest
    ): Response<StoreMeResponse<StoreInfoData>>

    /**
     * 대표 이미지 조회 API
     */
    @GET("api/auth/store/{storeId}/featured-images")
    suspend fun getStoreFeaturedImages(
        @Path("storeId") storeId: String
    ): Response<FeaturedImagesResponse>

    /**
     * 대표 이미지 수정 API
     */
    @PATCH("api/auth/store/{storeId}/featured-images")
    suspend fun patchStoreFeaturedImages(
        @Path("storeId") storeId: String,
        @Body patchStoreFeaturedImagesRequest: PatchStoreFeaturedImagesRequest
    ): Response<StoreMeResponse<FeaturedImagesResponse>>

    /**
     * 쿠폰 목록 조회 API
     */
    @GET("api/auth/store/{storeId}/coupons")
    suspend fun getStoreCoupons(
        @Path("storeId") storeId: String
    ): Response<CouponsResponse>

    /**
     * 쿠폰 생성 API
     */
    @POST("api/auth/store/{storeId}/coupons")
    suspend fun postStoreCoupon(
        @Path("storeId") storeId: String,
        @Body couponRequest: CouponRequest
    ): Response<StoreMeResponse<CouponData>>

    /**
     * 쿠폰 수정 API
     */
    @PATCH("api/auth/store/{storeId}/coupons/{couponId}")
    suspend fun patchStoreCoupon(
        @Path("storeId") storeId: String,
        @Body couponRequest: CouponRequest
    ): Response<StoreMeResponse<CouponData>>

    /**
     * 쿠폰 삭제 API
     */
    @DELETE("api/auth/store/{storeId}/coupons/{couponId}")
    suspend fun deleteStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<Unit>>

    /**
     * 쿠폰 수령 API
     */
    @POST("api/auth/store/{storeId}/coupons/{couponId}/accept")
    suspend fun acceptStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<AcceptCouponResponse>>

    /**
     * 쿠폰 사용 API
     */
    @POST("api/auth/store/{storeId}/coupons/{couponId}/use")
    suspend fun useStoreCoupon(
        @Path("storeId") storeId: String,
        @Path("couponId") couponId: String
    ): Response<StoreMeResponse<UseCouponResponse>>

    /**
     * 메뉴 조회 API
     */
    @GET("api/auth/store/{storeId}/menus")
    suspend fun getStoreMenus(
        @Path("storeId") storeId: String
    ): Response<MenusResponse>

    /**
     * 메뉴 수정 API
     */
    @PATCH("api/auth/store/{storeId}/menus")
    suspend fun patchStoreMenus(
        @Path("storeId") storeId: String,
        @Body patchStoreMenusRequest: MenusResponse
    ): Response<StoreMeResponse<MenusResponse>>

    /**
     * 스탬프 쿠폰 생성 API
     */
    @POST("api/auth/store/{storeId}/stamp-coupon")
    suspend fun postStampCoupon(
        @Path("storeId") storeId: String,
        @Body postStampCouponRequest: PostStampCouponRequest
    ): Response<StoreMeResponse<StampCouponResponse>>

    /**
     * 스탬프 쿠폰 조회 API
     */
    @GET("api/auth/store/{storeId}/stamp-coupon")
    suspend fun getStampCoupon(
        @Path("storeId") storeId: String
    ): Response<StampCouponResponse>

    /**
     * 스탬프 쿠폰 수정 API
     */
    @PATCH("api/auth/store/{storeId}/stamp-coupon")
    suspend fun patchStampCoupon(
        @Path("storeId") storeId: String,
        @Body patchStampCouponRequest: StampCouponData
    ): Response<StoreMeResponse<StampCouponResponse>>

    /**
     * 스탬프 비밀번호 조회 API
     */
    @GET("api/auth/store/{storeId}/stamp-coupon/password")
    suspend fun getStampCouponPassword(
        @Path("storeId") storeId: String
    ): Response<StoreMeResponse<StampCouponPasswordResponse>>

    /**
     * 스탬프 비밀번호 변경 API
     */
    @PATCH("api/auth/store/{storeId}/stamp-coupon/password")
    suspend fun patchStampCouponPassword(
        @Path("storeId") storeId: String,
        @Body patchStampCouponPasswordRequest: StampCouponPasswordResponse
    ): Response<StoreMeResponse<Unit>>

    /**
     * 스토리 목록 조회 API
     */
    @GET("story/store/{storeId}/stories")
    suspend fun getStoreStories(
        @Path("storeId") storeId: String,
        @Query("limit") limit: Int = 10,
        @Query("lastCreatedAt") lastCreatedAt: String?
    ): Response<PagingResponse<List<StoryData>>>

    /**
     * 스토리 추가 API
     */
    @POST("story/store/{storeId}/stories")
    suspend fun postStoreStory(
        @Path("storeId") storeId: String,
        @Body postStoreStoryRequest: PostStoryRequest
    ): Response<PagingResponse<List<StoryData>>>

    /**
     * 특정 스토리 조회 API
     */
    @GET("story/store/{storeId}/stories/{storyId}")
    suspend fun getStoreStory(
        @Path("storeId") storeId: String,
        @Path("storyId") storyId: String
    ): Response<StoryData>

    /**
     * 스토리 삭제 API
     */
    @DELETE("story/store/{storeId}/stories/{storyId}")
    suspend fun deleteStoreStory(
        @Path("storeId") storeId: String,
        @Path("storyId") storyId: String
    ): Response<StoreMeResponse<Unit>>
}