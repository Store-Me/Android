package com.store_me.storeme.repository.naver

import com.naver.maps.geometry.LatLng
import com.store_me.storeme.network.naver.NaverApiService
import com.store_me.storeme.network.naver.NaverGeocodingApiService
import com.store_me.storeme.utils.ServerResponse
import timber.log.Timber
import javax.inject.Inject

class NaverRepository @Inject constructor(private val naverApiService: NaverApiService, private val naverGeocodingApiService: NaverGeocodingApiService) {
    suspend fun reverseGeocode(latLng: LatLng): Result<NaverApiService.ReverseGeocodeResponse> {
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

    suspend fun geocode(roadAddress: String): Result<NaverGeocodingApiService.GeocodeResponse> {
        /*   주소를 좌표로 변환   */

        return try {
            val response = naverGeocodingApiService.geocode(roadAddress)

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
}