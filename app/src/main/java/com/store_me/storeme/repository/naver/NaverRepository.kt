package com.store_me.storeme.repository.naver

import android.util.Log
import com.naver.maps.geometry.LatLng
import com.store_me.storeme.network.naver.NaverApiService
import com.store_me.storeme.network.naver.NaverGeocodingApiService
import com.store_me.storeme.utils.ServerResponse
import javax.inject.Inject

class NaverRepository @Inject constructor(private val naverApiService: NaverApiService, private val naverGeocodingApiService: NaverGeocodingApiService) {
    suspend fun reverseGeocode(latLng: LatLng): Result<NaverApiService.ReverseGeocodeResponse> {
        /*   Lat Lng 으로 지역 반환   */

        return try {
            val response = naverApiService.reverseGeocode("${latLng.longitude},${latLng.latitude}", "admcode", "json")

            if(response.isSuccessful){
                Log.d("reverseGeocode", "SUCCESS: ${response.body()}")
                Result.success(response.body()!!)
            }
            else{
                Log.d("reverseGeocode", "FAIL: ${response.errorBody()?.string()}")
                Result.failure(Exception(ServerResponse.FAIL.code))
            }
        } catch (e: Exception) {
            Log.e("reverseGeocode", "ERROR", e)
            Result.failure(Exception(ServerResponse.ERROR.code))
        }
    }

    suspend fun geocode(roadAddress: String): Result<NaverGeocodingApiService.GeocodeResponse> {
        /*   주소를 좌표로 변환   */

        return try {
            val response = naverGeocodingApiService.geocode(roadAddress)

            if(response.isSuccessful) {
                Log.d("geocode", "SUCCESS: ${response.body()}")
                Result.success(response.body()!!)
            } else {
                Log.d("geocode", "FAIL: ${response.errorBody()?.string()}")
                Result.failure(Exception(ServerResponse.FAIL.code))
            }
        } catch (e: Exception) {
            Log.e("geocode", "ERROR", e)
            Result.failure(Exception(ServerResponse.ERROR.code))
        }
    }
}