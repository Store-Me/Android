package com.store_me.storeme.repository.storeme

import android.content.Context
import com.store_me.storeme.R
import com.store_me.storeme.data.response.LoginResponse
import com.store_me.storeme.network.storeme.AuthApiService
import com.store_me.storeme.utils.TokenPreferencesHelper
import com.store_me.storeme.utils.exception.ApiExceptionHandler
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

interface AuthRepository {
    fun reissueTokens(): Result<LoginResponse>
}

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApiService: AuthApiService
): AuthRepository {
    override fun reissueTokens(): Result<LoginResponse> {
        return try {
            val response = authApiService.reissueTokens().execute()

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        if(responseBody.result != null) {
                            val result = responseBody.result

                            TokenPreferencesHelper.saveTokens(
                                accessToken = result.accessToken,
                                refreshToken = result.refreshToken,
                                expireTime = result.expiredTime
                            )

                            Result.success(result)
                        }
                        else
                            Result.failure(
                                ApiExceptionHandler.apiException(
                                    code = responseBody.code, message = context.getString(R.string.token_expired_message)
                                ))
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
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