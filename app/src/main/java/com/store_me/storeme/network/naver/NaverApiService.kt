package com.store_me.storeme.network.naver

import com.store_me.storeme.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverApiService {
    @Headers("X-NCP-APIGW-API-KEY-ID: ${BuildConfig.NAVER_CLIENT_ID}", "X-NCP-APIGW-API-KEY: ${BuildConfig.NAVER_CLIENT_SECRET}")
    @GET("gc")
    suspend fun reverseGeocode(
        @Query("coords") coords: String,
        @Query("orders") orders: String,
        @Query("output") output: String,
    ): Response<ReverseGeocodeResponse>

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule{
        @Provides
        fun create(): NaverApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(NaverApiService::class.java)
        }
    }

    data class ReverseGeocodeResponse(
        val status: Status,
        val results: List<ReverseGeocodeResult>
    )

    data class Status(
        val code: Int,
        val name: String,
        val message: String
    )

    data class ReverseGeocodeResult(
        val name: String,
        val code: Code?,
        val region: Region?
    )

    data class Code(
        val id: String,
        val type: String,
        val mappingId: String
    )

    data class Region(
        val area0: Area,
        val area1: Area,
        val area2: Area,
        val area3: Area
    )

    data class Area(
        val name: String?,
    )
}