package com.store_me.di

import com.store_me.storeme.BuildConfig
import com.store_me.storeme.network.naver.NaverApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NaverModule {
    @Provides
    @Named("NaverBaseUrl")
    fun provideBaseUrl() = "https://naveropenapi.apigw.ntruss.com"

    @Provides
    @Singleton
    @Named("NaverClient")
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor() {
                val request = it.request().newBuilder()
                    .addHeader("x-ncp-apigw-api-key-id", BuildConfig.NAVER_CLIENT_ID)
                    .addHeader("x-ncp-apigw-api-key", BuildConfig.NAVER_CLIENT_SECRET)
                    .build()

                it.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("NaverRetrofit")
    fun provideRetrofit(
        @Named("NaverClient") okHttpClient: OkHttpClient,
        @Named("NaverBaseUrl") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("NaverApi")
    fun naverApiService(
        @Named("NaverRetrofit") retrofit: Retrofit
    ): NaverApiService {
        return retrofit.create(NaverApiService::class.java)
    }
}