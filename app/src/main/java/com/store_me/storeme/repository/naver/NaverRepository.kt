package com.store_me.storeme.repository.naver

import com.naver.maps.geometry.LatLng
import com.store_me.storeme.network.naver.NaverApiService
import com.store_me.storeme.utils.ServerResponse
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

interface NaverRepository {
    suspend fun reverseGeocode(latLng: LatLng): Result<NaverApiService.ReverseGeocodeResponse>

    suspend fun geocode(roadAddress: String): Result<NaverApiService.GeocodeResponse>

    suspend fun getStaticMap(latLng: LatLng, storeName: String): Result<InputStream>
}

class NaverRepositoryImpl @Inject constructor(
    @Named("NaverApi") private val naverApiService: NaverApiService
): NaverRepository {
    override suspend fun reverseGeocode(latLng: LatLng): Result<NaverApiService.ReverseGeocodeResponse> {
        /*   Lat Lng 으로 지역 반환   */

        return try {
            val response = naverApiService.reverseGeocode("${latLng.longitude},${latLng.latitude}", "admcode", "json")

            if(response.isSuccessful){
                Timber.i("ReverseGeocode Success: ${response.body()}")
                Result.success(response.body()!!)
            }
            else{
                Timber.w("ReverseGeocode Fail: ${response.errorBody()?.string()}")
                Result.failure(Exception(ServerResponse.FAIL.code))
            }
        } catch (e: Exception) {
            Timber.e("ReverseGeocode Error: $e")
            Result.failure(Exception(ServerResponse.ERROR.code))
        }
    }

    override suspend fun geocode(roadAddress: String): Result<NaverApiService.GeocodeResponse> {
        /*   주소를 좌표로 변환   */

        return try {
            val response = naverApiService.geocode(roadAddress)

            if(response.isSuccessful) {
                Timber.i("Geocode Success: ${response.body()}")

                Result.success(response.body()!!)
            } else {
                Timber.w("Geocode Fail: ${response.errorBody()?.string()}")
                Result.failure(Exception(ServerResponse.FAIL.code))
            }
        } catch (e: Exception) {
            Timber.e("Geocode Error: $e")
            Result.failure(Exception(ServerResponse.ERROR.code))
        }
    }

    override suspend fun getStaticMap(latLng: LatLng, storeName: String): Result<InputStream> {
        return try {
            val response = naverApiService.getStaticMap(
                width = 512,
                height = 256,
                center = "${latLng.longitude} ${latLng.latitude}",
                level = 16, //Zoom
                scale = 2,
                lang = "ko",
                //d: 기본, e: 커스텀
                //icon: iconURL
                markers = "type:d|size:mid|pos:${latLng.longitude} ${latLng.latitude}"
            )

            Timber.d("getStaticMap: $response")

            if(response.isSuccessful) {
                val responseBody = response.body()
                if(responseBody != null) {
                    Result.success(responseBody.byteStream())
                } else {
                    ResponseHandler.handleErrorResponse(response)
                }
            } else {
                ResponseHandler.handleErrorResponse(response)
            }
        } catch (e: Exception) {
            e.toResult()
        }
    }
}