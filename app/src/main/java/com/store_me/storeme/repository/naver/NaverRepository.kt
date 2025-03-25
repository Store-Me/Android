package com.store_me.storeme.repository.naver

import com.naver.maps.geometry.LatLng
import com.store_me.storeme.network.naver.NaverApiService
import com.store_me.storeme.utils.ServerResponse
import timber.log.Timber
import javax.inject.Inject

interface NaverRepository {
    suspend fun reverseGeocode(latLng: LatLng): Result<NaverApiService.ReverseGeocodeResponse>

    suspend fun geocode(roadAddress: String): Result<NaverApiService.GeocodeResponse>
}

class NaverRepositoryImpl @Inject constructor(
    private val naverApiService: NaverApiService
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
}