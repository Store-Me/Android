package com.store_me.auth

import com.store_me.storeme.repository.storeme.AuthRepository
import com.store_me.storeme.utils.preference.TokenPreferencesHelper
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class AuthInterceptor(
    private val authRepository: AuthRepository,
    private val auth: Auth
): Interceptor {
    @Volatile
    private var tokenMutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        //token 추가
        val accessToken = TokenPreferencesHelper.getAccessToken()

        val authenticatedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        //Unauthorized 응답 처리
        if(response.code != 401) return response

        response.close()

        return runBlocking {
            tokenMutex.withLock {
                Timber.d("Reissue Token")

                val latestToken = TokenPreferencesHelper.getAccessToken()

                if(accessToken != latestToken) {
                    val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $latestToken")
                        .build()
                    return@runBlocking chain.proceed(newRequest)
                }

                val reissueResult = authRepository.reissueTokens()

                reissueResult.fold(
                    onSuccess = {
                        TokenPreferencesHelper.saveTokens(
                            accessToken = it.accessToken
                        )

                        val newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer ${it.accessToken}")
                            .build()
                        return@runBlocking chain.proceed(newRequest)
                    },
                    onFailure = {
                        auth.updateIsLoggedIn(false)

                        throw IOException("토큰 갱신에 실패하였습니다.", it)
                    }
                )
            }
        }
    }
}