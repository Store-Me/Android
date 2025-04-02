package com.store_me.storeme.repository.storeme

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

    suspend fun getStoreData(storeId: String): Result<StoreInfoData>

    suspend fun patchStoreProfileImages(storeId: String, patchStoreProfileImagesRequest: PatchStoreProfileImagesRequest): Result<PatchResponse<StoreInfoData>>

    suspend fun patchStoreIntro(storeId: String, patchStoreIntroRequest: PatchStoreIntroRequest): Result<PatchResponse<StoreInfoData>>

    suspend fun patchStoreDescription(storeId: String, patchStoreDescriptionRequest: PatchStoreDescriptionRequest): Result<PatchResponse<StoreInfoData>>

    suspend fun getBusinessHours(storeId: String): Result<BusinessHoursResponse>

    suspend fun patchStoreBusinessHours(storeId: String, patchBusinessHoursRequest: PatchBusinessHoursRequest): Result<PatchResponse<BusinessHoursResponse>>

    suspend fun getStoreLinks(storeId: String): Result<LinksResponse>

    suspend fun patchStoreLinks(storeId: String, patchLinksRequest: PatchLinksRequest): Result<PatchResponse<LinksResponse>>

    suspend fun patchStorePhoneNumber(storeId: String, phoneNumber: String?): Result<PatchResponse<StoreInfoData>>

    suspend fun getStoreNotice(storeId: String): Result<NoticeResponse>

    suspend fun patchStoreNotice(storeId: String, patchStoreNoticeRequest: PatchStoreNoticeRequest): Result<PatchResponse<NoticeResponse>>

    suspend fun patchStoreLocation(storeId: String, patchStoreLocationRequest: PatchStoreLocationRequest): Result<PatchResponse<StoreInfoData>>

    suspend fun getStoreFeaturedImages(storeId: String): Result<FeaturedImagesResponse>

    suspend fun patchFeaturedImages(storeId: String, patchStoreFeaturedImagesRequest: PatchStoreFeaturedImagesRequest): Result<PatchResponse<FeaturedImagesResponse>>

    suspend fun getStoreCoupons(storeId: String): Result<CouponsResponse>

    suspend fun postStoreCoupon(storeId: String, couponRequest: CouponRequest): Result<PatchResponse<CouponData>>

    suspend fun patchStoreCoupon(storeId: String, couponRequest: CouponRequest): Result<PatchResponse<CouponData>>

    suspend fun deleteStoreCoupon(storeId: String, couponId: String): Result<PatchResponse<Unit>>

    suspend fun acceptStoreCoupon(storeId: String, couponId: String): Result<PatchResponse<AcceptCouponResponse>>

    suspend fun useStoreCoupon(storeId: String, couponId: String): Result<PatchResponse<UseCouponResponse>>

    suspend fun getStoreMenus(storeId: String): Result<MenusResponse>

    suspend fun patchStoreMenus(storeId: String, patchStoreMenusRequest: MenusResponse): Result<PatchResponse<MenusResponse>>

    suspend fun getStampCoupon(storeId: String): Result<StampCouponResponse>

    suspend fun patchStampCoupon(storeId: String, patchStampCouponRequest: StampCouponData): Result<PatchResponse<StampCouponResponse>>

    suspend fun postStampCoupon(storeId: String, postStampCouponRequest: PostStampCouponRequest): Result<PatchResponse<StampCouponResponse>>

    suspend fun getStampCouponPassword(storeId: String): Result<PatchResponse<StampCouponPasswordResponse>>

    suspend fun patchStampCouponPassword(storeId: String, patchStampCouponPasswordRequest: StampCouponPasswordResponse): Result<PatchResponse<Unit>>
}

class OwnerRepositoryImpl @Inject constructor(
    private val ownerApiService: OwnerApiService
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

    override suspend fun getStoreData(storeId: String): Result<StoreInfoData> {
        return try {
            val response = ownerApiService.getStoreData(storeId = storeId)

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
        storeId: String,
        patchStoreProfileImagesRequest: PatchStoreProfileImagesRequest
    ): Result<PatchResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStoreProfileImages(
                storeId = storeId,
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
        storeId: String,
        patchStoreIntroRequest: PatchStoreIntroRequest
    ): Result<PatchResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStoreIntro(
                storeId = storeId,
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
        storeId: String,
        patchStoreDescriptionRequest: PatchStoreDescriptionRequest
    ): Result<PatchResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStoreDescription(
                storeId = storeId,
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

    override suspend fun getBusinessHours(storeId: String): Result<BusinessHoursResponse> {
        return try {
            val response = ownerApiService.getBusinessHours(
                storeId = storeId
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

    override suspend fun patchStoreBusinessHours(storeId: String, patchBusinessHoursRequest: PatchBusinessHoursRequest): Result<PatchResponse<BusinessHoursResponse>> {
        return try {
            val response = ownerApiService.patchBusinessHours(
                storeId = storeId,
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

    override suspend fun getStoreLinks(storeId: String): Result<LinksResponse> {
        return try {
            val response = ownerApiService.getStoreLinks(
                storeId = storeId
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
        storeId: String,
        patchLinksRequest: PatchLinksRequest
    ): Result<PatchResponse<LinksResponse>> {
        return try {
            val response = ownerApiService.patchStoreLinks(
                storeId = storeId,
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
        storeId: String,
        phoneNumber: String?
    ): Result<PatchResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStorePhoneNumber(
                storeId = storeId,
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

    override suspend fun getStoreNotice(storeId: String): Result<NoticeResponse> {
        return try {
            val response = ownerApiService.getStoreNotice(
                storeId = storeId
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

    override suspend fun patchStoreNotice(storeId: String, patchStoreNoticeRequest: PatchStoreNoticeRequest): Result<PatchResponse<NoticeResponse>> {
        return try {
            val response = ownerApiService.patchStoreNotice(
                storeId = storeId,
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

    override suspend fun patchStoreLocation(storeId: String, patchStoreLocationRequest: PatchStoreLocationRequest): Result<PatchResponse<StoreInfoData>> {
        return try {
            val response = ownerApiService.patchStoreLocation(
                storeId = storeId,
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

    override suspend fun getStoreFeaturedImages(storeId: String): Result<FeaturedImagesResponse> {
        return try {
            val response = ownerApiService.getStoreFeaturedImages(
                storeId = storeId
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

    override suspend fun patchFeaturedImages(storeId: String, patchStoreFeaturedImagesRequest: PatchStoreFeaturedImagesRequest): Result<PatchResponse<FeaturedImagesResponse>> {
        return try {
            val response = ownerApiService.patchStoreFeaturedImages(
                storeId = storeId,
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

    override suspend fun getStoreCoupons(storeId: String): Result<CouponsResponse> {
        return try {
            val response = ownerApiService.getStoreCoupons(
                storeId = storeId
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

    override suspend fun postStoreCoupon(storeId: String, couponRequest: CouponRequest): Result<PatchResponse<CouponData>> {
        return try {
            val response = ownerApiService.postStoreCoupon(
                storeId = storeId,
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

    override suspend fun patchStoreCoupon(storeId: String, couponRequest: CouponRequest): Result<PatchResponse<CouponData>> {
        return try {
            val response = ownerApiService.patchStoreCoupon(
                storeId = storeId,
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

    override suspend fun deleteStoreCoupon(storeId: String, couponId: String): Result<PatchResponse<Unit>> {
        return try {
            val response = ownerApiService.deleteStoreCoupon(
                storeId = storeId,
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
        storeId: String,
        couponId: String
    ): Result<PatchResponse<AcceptCouponResponse>> {
        return try {
            val response = ownerApiService.acceptStoreCoupon(
                storeId = storeId,
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
        storeId: String,
        couponId: String
    ): Result<PatchResponse<UseCouponResponse>> {
        return try {
            val response = ownerApiService.useStoreCoupon(
                storeId = storeId,
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

    override suspend fun getStoreMenus(storeId: String): Result<MenusResponse> {
        return try {
            val response = ownerApiService.getStoreMenus(
                storeId = storeId
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
        storeId: String,
        patchStoreMenusRequest: MenusResponse
    ): Result<PatchResponse<MenusResponse>> {
        return try {
            val response = ownerApiService.patchStoreMenus(
                storeId = storeId,
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

    override suspend fun getStampCoupon(storeId: String): Result<StampCouponResponse> {
        return try {
            val response = ownerApiService.getStampCoupon(
                storeId = storeId
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
        storeId: String,
        patchStampCouponRequest: StampCouponData
    ): Result<PatchResponse<StampCouponResponse>> {
        return try {
            val response = ownerApiService.patchStampCoupon(
                storeId = storeId,
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
        storeId: String,
        postStampCouponRequest: PostStampCouponRequest
    ): Result<PatchResponse<StampCouponResponse>> {
        return try {
            val response = ownerApiService.postStampCoupon(
                storeId = storeId,
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

    override suspend fun getStampCouponPassword(storeId: String): Result<PatchResponse<StampCouponPasswordResponse>> {
        return try {
            val response = ownerApiService.getStampCouponPassword(
                storeId = storeId
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
        storeId: String,
        patchStampCouponPasswordRequest: StampCouponPasswordResponse
    ): Result<PatchResponse<Unit>> {
        return try {
            val response = ownerApiService.patchStampCouponPassword(
                storeId = storeId,
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
}