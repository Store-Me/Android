package com.store_me.di

import com.store_me.auth.AuthInterceptor
import com.store_me.storeme.network.storeme.AuthApiService
import com.store_me.storeme.network.storeme.OwnerApiService
import com.store_me.storeme.network.storeme.UserApiService
import com.store_me.storeme.utils.TokenPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * StoreMe API 관련 의존성을 제공하는 Hilt Module
 */
@Module
@InstallIn(SingletonComponent::class)
object StoreMeModule {
    @Provides
    fun provideBaseUrl() = "https://storeme.shop/"

    @Provides
    @Singleton
    @Named("Authorized")
    fun provideAuthorizedOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("Unauthorized")
    fun provideUnauthorizedOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    @Named("Reissue")
    fun provideReissueOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer ${TokenPreferencesHelper.getAccessToken()}")
                    .addHeader("Authorization-Refresh", "${TokenPreferencesHelper.getRefreshToken()}")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    // Authorization 헤더가 필요한 Retrofit 인스턴스
    @Provides
    @Singleton
    @Named("AuthorizedRetrofit")
    fun provideAuthorizedRetrofit(
        @Named("Authorized") okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Authorization 헤더가 필요 없는 Retrofit 인스턴스
    @Provides
    @Singleton
    @Named("UnauthorizedRetrofit")
    fun provideUnauthorizedRetrofit(
        @Named("Unauthorized") okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Reissue Retrofit 인스턴스
    @Provides
    @Singleton
    @Named("ReissueRetrofit")
    fun provideReissueRetrofit(
        @Named("Reissue") okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("UserApiServiceWithoutAuth")
    fun userApiServiceWithoutAuth(
        @Named("UnauthorizedRetrofit") retrofit: Retrofit
    ): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("UserApiServiceWithAuth")
    fun userApiServiceWithAuth(
        @Named("AuthorizedRetrofit") retrofit: Retrofit
    ): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun authApiService(
        @Named("ReissueRetrofit") retrofit: Retrofit
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun ownerApiService(
        @Named("AuthorizedRetrofit") retrofit: Retrofit
    ): OwnerApiService {
        return retrofit.create(OwnerApiService::class.java)
    }
}