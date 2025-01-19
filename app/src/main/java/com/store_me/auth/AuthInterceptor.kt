package com.store_me.auth

import com.store_me.storeme.repository.storeme.AuthRepository
import com.store_me.storeme.utils.preference.TokenPreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor(
    private val authRepository: AuthRepository,
    private val auth: Auth
): Interceptor {
    @Volatile
    private var isRefreshing = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        //token 추가
        val accessToken = TokenPreferencesHelper.getAccessToken()

        val authenticatedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        //Unauthorized 응답 처리
        if(response.code == 401) {
            synchronized(this) {
                if(!isRefreshing) {

                    isRefreshing = true

                    try {
                        val reissueResult = authRepository.reissueTokens()
                        isRefreshing = false

                        reissueResult.onSuccess {
                            //기존 요청 재시도
                            val retriedRequest = originalRequest.newBuilder()
                                .addHeader("Authorization", "Bearer ${it.accessToken}")
                                .build()

                            return chain.proceed(retriedRequest)
                        }.onFailure {
                            auth.updateIsLoggedIn(false)

                            Timber.e("Token reissue Failed: $it")
                        }

                    } finally {
                        isRefreshing = false
                    }
                }
            }
        }

        return response
    }
}