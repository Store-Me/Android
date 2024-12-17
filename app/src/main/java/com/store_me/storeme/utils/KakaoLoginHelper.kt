package com.store_me.storeme.utils

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object KakaoLoginHelper {
    /**
     * Kakao 로그인 진행 후, 카카오 아이디를 반환하는 함수
     * @return kakao Id String?
     */
    suspend fun getKakaoId(context: Context): String? {
        return suspendCancellableCoroutine { continuation ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    // 로그인 실패
                    continuation.resume(null)
                } else if (token != null) {
                    // 로그인 성공
                    // 사용자 정보 요청
                    UserApiClient.instance.me { user, instanceError ->
                        if (instanceError != null) {
                            // 사용자 정보 요청 실패
                            continuation.resume(null)
                        } else if (user != null) {
                            // 사용자 정보 요청 성공
                            continuation.resume(user.id.toString())
                        } else {
                            continuation.resume(null)
                        }
                    }
                } else {
                    continuation.resume(null)
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context = context)) {
                // 카카오톡으로 로그인
                UserApiClient.instance.loginWithKakaoTalk(context = context, callback = callback)
            } else {
                // 카카오 계정으로 로그인
                UserApiClient.instance.loginWithKakaoAccount(context = context, callback = callback)
            }
        }
    }
}