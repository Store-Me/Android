package com.store_me.storeme.network.storeme

import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.StoreData
import com.store_me.storeme.data.request.menu.MenuCategoryRequest
import com.store_me.storeme.data.request.menu.UpdateStoreMenuCategoryNameRequestDto
import com.store_me.storeme.data.request.store_image.StoreImageOrderRequest
import com.store_me.storeme.data.response.MenuCategoryList
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.response.StoreListResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface OwnerApiService {
    /**
     * 가게 목록 요청 API
     */
    @GET("store/list")
    suspend fun getStoreList(

    ): Response<StoreMeResponse<StoreListResponse>>

    /**
     * 가게 정보 요청 API
     */
    @GET("store/{storeId}")
    suspend fun getStoreData(
        @Path("storeId") storeId: Long
    ): Response<StoreMeResponse<StoreData>>

    /**
     * 가게 이미지 저장 API
     */
    @Multipart
    @POST("store/image/{storeId}")
    suspend fun addStoreImages(
        @Path("storeId") storeId: Long,
        @Part storeImageFileList: List<MultipartBody.Part>
    ): Response<StoreMeResponse<Unit>>

    /**
     * 가게 이미지 순서 변경 API
     */
    @PATCH("store/image/order")
    suspend fun patchStoreImageOrder(
        @Body storeImageOrderRequest: StoreImageOrderRequest
    ): Response<StoreMeResponse<Unit>>

    /**
     * 가게 이미지 삭제 API
     */
    @DELETE("store/image/{storeId}/{storeImageId}")
    suspend fun deleteStoreImage(
        @Path("storeId") storeId: Long,
        @Path("storeImageId") storeImageId: Long
    ): Response<StoreMeResponse<Unit>>

    /**
     * 가게 메뉴 카테고리 조회
     */
    @GET("store/menu/category/{storeId}")
    suspend fun getMenuCategory(
        @Path("storeId") storeId: Long
    ): Response<StoreMeResponse<MenuCategoryList>>

    /**
     * 가게 메뉴 카테고리 추가
     */
    @POST("store/menu/category")
    suspend fun addMenuCategory(
        @Body saveStoreMenuCategoryRequestDto: MenuCategoryRequest
    ): Response<StoreMeResponse<Unit>>

    /**
     * 가게 메뉴 카테고리 순서 변경
     */
    @PATCH("store/menu/category/order")
    suspend fun patchMenuCategoryOrder(
        @Body updateStoreMenuCategoryOrderRequestDto: MenuCategoryList
    ): Response<StoreMeResponse<Unit>>

    /**
     * 가게 메뉴 카테고리 이름 변경
     */
    @PATCH("store/menu/category/name")
    suspend fun updateStoreMenuCategoryName(
        @Body updateStoreMenuCategoryNameRequestDto: UpdateStoreMenuCategoryNameRequestDto
    ): Response<StoreMeResponse<Unit>>

    /**
     * 가게 메뉴 카테고리 삭제
     */
    @DELETE("store/menu/category/{storeId}/{storeMenuCategoryId}")
    suspend fun deleteMenuCategory(
        @Path("storeId") storeId: Long,
        @Path("storeMenuCategoryId") storeMenuCategoryId: Int
    ): Response<StoreMeResponse<Unit>>
}