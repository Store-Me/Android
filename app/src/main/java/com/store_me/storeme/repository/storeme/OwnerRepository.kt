package com.store_me.storeme.repository.storeme

import android.content.Context
import com.store_me.storeme.data.StoreData
import com.store_me.storeme.data.request.menu.MenuCategoryRequest
import com.store_me.storeme.data.request.menu.UpdateStoreMenuCategoryNameRequestDto
import com.store_me.storeme.data.request.store_image.StoreImageOrderRequest
import com.store_me.storeme.data.response.MenuCategoryList
import com.store_me.storeme.data.response.StoreListResponse
import com.store_me.storeme.network.storeme.OwnerApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

/**
 * 사장님 관련 Repository
 */
interface OwnerRepository {
    suspend fun getStoreList(): Result<StoreListResponse>

    suspend fun getStoreData(storeId: Long): Result<StoreData>

    suspend fun addStoreImages(storeId: Long, storeImageList: List<MultipartBody.Part>): Result<Unit>

    suspend fun patchStoreImageOrder(storeImageOrderRequest: StoreImageOrderRequest): Result<Unit>

    suspend fun deleteStoreImage(storeId: Long, storeImageId: Long): Result<Unit>

    suspend fun getMenuCategory(storeId: Long): Result<MenuCategoryList>

    suspend fun addMenuCategory(menuCategoryRequest: MenuCategoryRequest): Result<Unit>
    suspend fun patchMenuCategoryOrder(menuCategoryList: MenuCategoryList): Result<Unit>
    suspend fun updateStoreMenuCategoryName(updateStoreMenuCategoryNameRequestDto: UpdateStoreMenuCategoryNameRequestDto): Result<Unit>
    suspend fun deleteMenuCategory(storeId: Long, storeMenuCategoryId: Int): Result<Unit>
}

class OwnerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ownerApiService: OwnerApiService
): OwnerRepository {
    override suspend fun getStoreList(): Result<StoreListResponse> {
        return try {
            val response = ownerApiService.getStoreList()

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(responseBody.result)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }

            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun getStoreData(storeId: Long): Result<StoreData> {
        return try {
            val response = ownerApiService.getStoreData(storeId = storeId)

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(responseBody.result)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }

            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun addStoreImages(storeId: Long, storeImageList: List<MultipartBody.Part>): Result<Unit> {
        return try {
            val response = ownerApiService.addStoreImages(
                storeId = storeId,
                storeImageFileList = storeImageList
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun patchStoreImageOrder(storeImageOrderRequest: StoreImageOrderRequest): Result<Unit> {
        return try {
            val response = ownerApiService.patchStoreImageOrder(
                storeImageOrderRequest = storeImageOrderRequest
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun deleteStoreImage(storeId: Long, storeImageId: Long): Result<Unit> {
        return try {
            val response = ownerApiService.deleteStoreImage(
                storeId = storeId,
                storeImageId = storeImageId
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun getMenuCategory(storeId: Long): Result<MenuCategoryList> {
        return try {
            val response = ownerApiService.getMenuCategory(
                storeId = storeId
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(responseBody.result)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun addMenuCategory(menuCategoryRequest: MenuCategoryRequest): Result<Unit> {
        return try {
            val response = ownerApiService.addMenuCategory(
                menuCategoryRequest
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun patchMenuCategoryOrder(menuCategoryList: MenuCategoryList): Result<Unit> {
        return try {
            val response = ownerApiService.patchMenuCategoryOrder(
                menuCategoryList
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun updateStoreMenuCategoryName(updateStoreMenuCategoryNameRequestDto: UpdateStoreMenuCategoryNameRequestDto): Result<Unit> {
        return try {
            val response = ownerApiService.updateStoreMenuCategoryName(
                updateStoreMenuCategoryNameRequestDto
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun deleteMenuCategory(storeId: Long, storeMenuCategoryId: Int): Result<Unit> {
        return try {
            val response = ownerApiService.deleteMenuCategory(
                storeId = storeId,
                storeMenuCategoryId = storeMenuCategoryId
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }


}