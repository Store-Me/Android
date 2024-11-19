package com.store_me.di

import com.store_me.storeme.network.storeme.UserApiService
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
    fun provideAuthorizedOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .addHeader("Bearer", "")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("Unauthorized")
    fun provideUnauthorizedOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
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

    @Provides
    @Singleton
    fun signupApp(
        @Named("UnauthorizedRetrofit") retrofit: Retrofit
    ): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }
}