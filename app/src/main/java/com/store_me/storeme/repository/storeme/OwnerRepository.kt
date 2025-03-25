package com.store_me.storeme.repository.storeme

import com.store_me.storeme.data.request.store.PatchBusinessHoursRequest
import com.store_me.storeme.data.request.store.PatchLinksRequest
import com.store_me.storeme.data.request.store.PatchStoreNoticeRequest
import com.store_me.storeme.data.request.store.PatchStoreDescriptionRequest
import com.store_me.storeme.data.request.store.PatchStoreIntroRequest
import com.store_me.storeme.data.request.store.PatchStoreLocationRequest
import com.store_me.storeme.data.request.store.PatchStorePhoneNumberRequest
import com.store_me.storeme.data.request.store.PatchStoreProfileImagesRequest
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.data.response.LinksResponse
import com.store_me.storeme.data.response.MyStoresResponse
import com.store_me.storeme.data.response.NoticeResponse
import com.store_me.storeme.data.response.PatchResponse
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

    suspend fun patchBusinessHours(storeId: String, patchBusinessHoursRequest: PatchBusinessHoursRequest): Result<PatchResponse<BusinessHoursResponse>>

    suspend fun getStoreLinks(storeId: String): Result<LinksResponse>

    suspend fun patchStoreLinks(storeId: String, patchLinksRequest: PatchLinksRequest): Result<PatchResponse<LinksResponse>>

    suspend fun patchStorePhoneNumber(storeId: String, phoneNumber: String?): Result<PatchResponse<StoreInfoData>>

    suspend fun getStoreNotice(storeId: String): Result<NoticeResponse>

    suspend fun patchStoreNotice(storeId: String, patchStoreNoticeRequest: PatchStoreNoticeRequest): Result<PatchResponse<NoticeResponse>>

    suspend fun patchStoreLocation(storeId: String, patchStoreLocationRequest: PatchStoreLocationRequest): Result<PatchResponse<StoreInfoData>>
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

    override suspend fun patchBusinessHours(storeId: String, patchBusinessHoursRequest: PatchBusinessHoursRequest): Result<PatchResponse<BusinessHoursResponse>> {
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
}