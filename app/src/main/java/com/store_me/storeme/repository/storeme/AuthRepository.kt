package com.store_me.storeme.repository.storeme

import com.store_me.storeme.data.response.ReissueResponse
import com.store_me.storeme.network.storeme.AuthApiService
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.preference.TokenPreferencesHelper
import timber.log.Timber
import javax.inject.Inject

interface AuthRepository {
    suspend fun reissueTokens(): Result<ReissueResponse>
}

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
): AuthRepository {

    override suspend fun reissueTokens(): Result<ReissueResponse> {
        return try {
            val response = authApiService.reissueTokens()

            Timber.d(response.toString())

            TokenPreferencesHelper.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )

            Result.success(response)
        } catch (e: Exception) {
            Timber.e(e)
            e.toResult()
        }
    }
}