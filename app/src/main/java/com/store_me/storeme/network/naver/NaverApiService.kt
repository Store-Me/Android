package com.store_me.storeme.network.naver

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverApiService {
    @GET("map-reversegeocode/v2/gc")
    suspend fun reverseGeocode(
        @Query("coords") coords: String,
        @Query("orders") orders: String,
        @Query("output") output: String,
    ): Response<ReverseGeocodeResponse>

    @GET("map-geocode/v2/geocode")
    suspend fun geocode(
        @Query("query") query: String,
    ): Response<GeocodeResponse>

    @GET("https://naveropenapi.apigw.ntruss.com/map-static/v2/raster")
    suspend fun getStaticMap(
        @Query("w") width: Int,
        @Query("h") height: Int,
        @Query("level") level: Int,
        @Query("scale") scale: Int?,
        @Query("lang") lang: String?,
        @Query("markers") markers: String?
    ): Response<ResponseBody>

    data class GeocodeResponse(
        val status: String,
        val addresses: List<Address>
    )

    data class Address(
        val roadAddress: String, //도로명 주소
        val x: String, //longitude
        val y: String  //latitude
    )

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