package com.store_me.storeme.utils.preference

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.store_me.storeme.utils.DateTimeUtils

object TokenPreferencesHelper {
    private const val PREFERENCES_FILE_NAME = "token_preferences"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_EXPIRED_TIME = "expired_time"

    private const val TOKEN_EXPIRATION_BUFFER = 1 * 60 * 1000L //1분

    private lateinit var sharedPreferences: SharedPreferences

    /**
     * 초기화 메서드
     */
    fun init(application: Application) {
        val masterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            application,
            PREFERENCES_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // AES256_SIV으로 key를 암호화
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // AES256_GCM으로 value를 암호화
        )
    }

    /**
     * AccessToken 및 RefreshToken 저장
     */
    fun saveTokens(accessToken: String, refreshToken: String, expireTime: String) {
        sharedPreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putLong(KEY_EXPIRED_TIME, DateTimeUtils().dateTimeToLong(expireTime))
            apply()
        }
    }

    /**
     * AccessToken 불러오기
     */
    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    /**
     * RefreshToken 불러오기
     */
    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    /**
     * AccessToken 만료 여부 확인
     */
    fun isAccessTokenExpired(): Boolean {
        val expireTime = sharedPreferences.getLong(KEY_EXPIRED_TIME, 0L)
        return System.currentTimeMillis() >= expireTime
    }

    /**
     * 토큰 제거
     */
    fun clearTokens() {
        sharedPreferences.edit().clear().apply()
    }
}