package com.store_me.storeme.repository.storeme

import android.content.Context
import android.util.Log
import com.store_me.storeme.data.response.StoreListResponse
import com.store_me.storeme.network.storeme.OwnerApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * 사장님 관련 Repository
 */
interface OwnerRepository {
    suspend fun getStoreList(): Result<StoreListResponse>
}

class OwnerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ownerApiService: OwnerApiService
): OwnerRepository {
    override suspend fun getStoreList(): Result<StoreListResponse> {
        return try {
            val response = ownerApiService.getStoreList()

            if(response.isSuccessful) {
                Log.d("getStoreList", response.body().toString())

                Result.success(response.body()?.result ?: StoreListResponse(emptyList()))
            } else {
                Log.d("getStoreList", response.errorBody()?.string() ?: "")

                Result.failure(Exception("오류가 발생했습니다. ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.d("getStoreList", e.message.toString())

            Result.failure(e)
        }
    }

}