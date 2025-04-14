package com.store_me.storeme.repository.storeme

import com.store_me.auth.Auth
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
import com.store_me.storeme.data.response.StampCouponPasswordResponse
import com.store_me.storeme.data.response.StampCouponResponse
import com.store_me.storeme.data.response.UseCouponResponse
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.network.storeme.OwnerApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import timber.log.Timber
import javax.inject.Inject

/**
 * 사장님 관련 Repository
 */
interface OwnerRepository {
    suspend fun getMyStores(): Result<MyStoresResponse>

    suspend fun getStoreData(): Result<StoreInfoData>

    suspend fun patchStoreProfileImages(patchStoreProfileImagesRequest: PatchStoreProfileImagesRequest): Result<StoreMeResponse<StoreInfoData>>

    suspend fun patchStoreIntro(patchStoreIntroRequest: PatchStoreIntroRequest): Result<StoreMeResponse<StoreInfoData>>

    suspend fun patchStoreDescription(patchStoreDescriptionRequest: PatchStoreDescriptionRequest): Result<StoreMeResponse<StoreInfoData>>

    suspend fun getBusinessHours(): Result<BusinessHoursResponse>

    suspend fun patchStoreBusinessHours(patchBusinessHoursRequest: PatchBusinessHoursRequest): Result<StoreMeResponse<BusinessHoursResponse>>

    suspend fun getStoreLinks(): Result<LinksResponse>

    suspend fun patchStoreLinks(patchLinksRequest: PatchLinksRequest): Result<StoreMeResponse<LinksResponse>>

    suspend fun patchStorePhoneNumber(phoneNumber: String?): Result<StoreMeResponse<StoreInfoData>>

    suspend fun getStoreNotice(): Result<NoticeResponse>

    suspend fun patchStoreNotice(patchStoreNoticeRequest: PatchStoreNoticeRequest): Result<StoreMeResponse<NoticeResponse>>

    suspend fun patchStoreLocation(patchStoreLocationRequest: PatchStoreLocationRequest): Result<StoreMeResponse<StoreInfoData>>

    suspend fun getStoreFeaturedImages(): Result<FeaturedImagesResponse>

    suspend fun patchFeaturedImages(patchStoreFeaturedImagesRequest: PatchStoreFeaturedImagesRequest): Result<StoreMeResponse<FeaturedImagesResponse>>

    suspend fun getStoreCoupons(): Result<CouponsResponse>

    suspend fun postStoreCoupon(couponRequest: CouponRequest): Result<StoreMeResponse<CouponData>>

    suspend fun patchStoreCoupon(couponRequest: CouponRequest): Result<StoreMeResponse<CouponData>>

    suspend fun deleteStoreCoupon(couponId: String): Result<StoreMeResponse<Unit>>

    suspend fun acceptStoreCoupon(couponId: String): Result<StoreMeResponse<AcceptCouponResponse>>

    suspend fun useStoreCoupon(couponId: String): Result<StoreMeResponse<UseCouponResponse>>

    suspend fun getStoreMenus(): Result<MenusResponse>

    suspend fun patchStoreMenus(patchStoreMenusRequest: MenusResponse): Result<StoreMeResponse<MenusResponse>>

    suspend fun getStampCoupon(): Result<StampCouponResponse>

    suspend fun patchStampCoupon(patchStampCouponRequest: StampCouponData): Result<StoreMeResponse<StampCouponResponse>>

    suspend fun postStampCoupon(postStampCouponRequest: PostStampCouponRequest): Result<StoreMeResponse<StampCouponResponse>>

    suspend fun getStampCouponPassword(): Result<StoreMeResponse<StampCouponPasswordResponse>>

    suspend fun patchStampCouponPassword(patchStampCouponPasswordRequest: StampCouponPasswordResponse): Result<StoreMeResponse<Unit>>

    suspend fun getStoreStories(lastCreatedAt: String?): Result<PagingResponse<List<StoryData>>>

    suspend fun postStoreStory(postStoreStoryRequest: PostStoryRequest): Result<PagingResponse<List<StoryData>>>

    suspend fun deleteStoreStory(storyId: String): Result<StoreMeResponse<Unit>>
}

class OwnerRepositoryImpl @Inject constructor(
    private val ownerApiService: OwnerApiService,
    private val auth: Auth
): OwnerRepository {
    override suspend fun getMyStores(): Result<MyStoresResponse> {
        return try {
            val response = ownerApiService.getMyStores()

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                Result.success(responseBody ?: MyStoresResponse(stores = emptyList()))
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun getStoreData(): Result<StoreInfoData> {
        return try {
            val response = ownerApiService.getStoreData(storeId = auth.getStoreId())

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

    override suspend fun patchStoreProfileImages(
        patchStoreProfileImagesRequest: PatchStoreProfileImagesRequest
    ): Result<StoreMeResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStoreProfileImages(
                storeId = auth.getStoreId(),
                patchStoreProfileImagesRequest = patchStoreProfileImagesRequest
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

    override suspend fun patchStoreIntro(
        patchStoreIntroRequest: PatchStoreIntroRequest
    ): Result<StoreMeResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStoreIntro(
                storeId = auth.getStoreId(),
                patchStoreIntroRequest = patchStoreIntroRequest
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

    override suspend fun patchStoreDescription(
        patchStoreDescriptionRequest: PatchStoreDescriptionRequest
    ): Result<StoreMeResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStoreDescription(
                storeId = auth.getStoreId(),
                patchStoreDescriptionRequest = patchStoreDescriptionRequest
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

    override suspend fun getBusinessHours(): Result<BusinessHoursResponse> {
        return try {
            val response = ownerApiService.getBusinessHours(
                storeId = auth.getStoreId(),
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                Result.success(responseBody ?: BusinessHoursResponse(businessHours = emptyList()))
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun patchStoreBusinessHours(patchBusinessHoursRequest: PatchBusinessHoursRequest): Result<StoreMeResponse<BusinessHoursResponse>> {
        return try {
            val response = ownerApiService.patchBusinessHours(
                storeId = auth.getStoreId(),
                patchBusinessHoursRequest = patchBusinessHoursRequest
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

    override suspend fun getStoreLinks(): Result<LinksResponse> {
        return try {
            val response = ownerApiService.getStoreLinks(
                storeId = auth.getStoreId(),
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                Result.success(responseBody ?: LinksResponse(links = emptyList()))
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun patchStoreLinks(
        patchLinksRequest: PatchLinksRequest
    ): Result<StoreMeResponse<LinksResponse>> {
        return try {
            val response = ownerApiService.patchStoreLinks(
                storeId = auth.getStoreId(),
                patchLinksRequest = patchLinksRequest
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

    override suspend fun patchStorePhoneNumber(
        phoneNumber: String?
    ): Result<StoreMeResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStorePhoneNumber(
                storeId = auth.getStoreId(),
                patchStorePhoneNumberRequest = PatchStorePhoneNumberRequest(storePhoneNumber = phoneNumber)
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

    override suspend fun getStoreNotice(): Result<NoticeResponse> {
        return try {
            val response = ownerApiService.getStoreNotice(
                storeId = auth.getStoreId(),
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

    override suspend fun patchStoreNotice(patchStoreNoticeRequest: PatchStoreNoticeRequest): Result<StoreMeResponse<NoticeResponse>> {
        return try {
            val response = ownerApiService.patchStoreNotice(
                storeId = auth.getStoreId(),
                patchStoreNoticeRequest = patchStoreNoticeRequest
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

    override suspend fun patchStoreLocation(patchStoreLocationRequest: PatchStoreLocationRequest): Result<StoreMeResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStoreLocation(
                storeId = auth.getStoreId(),
                patchStoreLocationRequest = patchStoreLocationRequest
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

    override suspend fun getStoreFeaturedImages(): Result<FeaturedImagesResponse> {
        return try {
            val response = ownerApiService.getStoreFeaturedImages(
                storeId = auth.getStoreId(),
            )
            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                Result.success(responseBody ?: FeaturedImagesResponse(images = emptyList()))
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun patchFeaturedImages(patchStoreFeaturedImagesRequest: PatchStoreFeaturedImagesRequest): Result<StoreMeResponse<FeaturedImagesResponse>> {
        return try {
            val response = ownerApiService.patchStoreFeaturedImages(
                storeId = auth.getStoreId(),
                patchStoreFeaturedImagesRequest = patchStoreFeaturedImagesRequest
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

    override suspend fun getStoreCoupons(): Result<CouponsResponse> {
        return try {
            val response = ownerApiService.getStoreCoupons(
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
            val response = ownerApiService.postStoreCoupon(
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
            val response = ownerApiService.patchStoreCoupon(
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
            val response = ownerApiService.deleteStoreCoupon(
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
            val response = ownerApiService.acceptStoreCoupon(
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
            val response = ownerApiService.useStoreCoupon(
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

    override suspend fun getStoreMenus(): Result<MenusResponse> {
        return try {
            val response = ownerApiService.getStoreMenus(
                storeId = auth.getStoreId()
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                Result.success(responseBody ?: MenusResponse(categories = emptyList()))
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun patchStoreMenus(
        patchStoreMenusRequest: MenusResponse
    ): Result<StoreMeResponse<MenusResponse>> {
        return try {
            val response = ownerApiService.patchStoreMenus(
                storeId = auth.getStoreId(),
                patchStoreMenusRequest = patchStoreMenusRequest
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

    override suspend fun getStampCoupon(): Result<StampCouponResponse> {
        return try {
            val response = ownerApiService.getStampCoupon(
                storeId = auth.getStoreId()
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                Result.success(responseBody ?: StampCouponResponse(stampCoupon = null))
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }

    override suspend fun patchStampCoupon(
        patchStampCouponRequest: StampCouponData
    ): Result<StoreMeResponse<StampCouponResponse>> {
        return try {
            val response = ownerApiService.patchStampCoupon(
                storeId = auth.getStoreId(),
                patchStampCouponRequest = patchStampCouponRequest
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

    override suspend fun postStampCoupon(
        postStampCouponRequest: PostStampCouponRequest
    ): Result<StoreMeResponse<StampCouponResponse>> {
        return try {
            val response = ownerApiService.postStampCoupon(
                storeId = auth.getStoreId(),
                postStampCouponRequest = postStampCouponRequest
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

    override suspend fun getStampCouponPassword(): Result<StoreMeResponse<StampCouponPasswordResponse>> {
        return try {
            val response = ownerApiService.getStampCouponPassword(
                storeId = auth.getStoreId()
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

    override suspend fun patchStampCouponPassword(
        patchStampCouponPasswordRequest: StampCouponPasswordResponse
    ): Result<StoreMeResponse<Unit>> {
        return try {
            val response = ownerApiService.patchStampCouponPassword(
                storeId = auth.getStoreId(),
                patchStampCouponPasswordRequest = patchStampCouponPasswordRequest
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

    override suspend fun getStoreStories(lastCreatedAt: String?): Result<PagingResponse<List<StoryData>>> {
        return try {
            val response = ownerApiService.getStoreStories(
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
            val response = ownerApiService.postStoreStory(
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
            val response = ownerApiService.deleteStoreStory(
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