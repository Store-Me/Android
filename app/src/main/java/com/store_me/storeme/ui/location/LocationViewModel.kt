package com.store_me.storeme.ui.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.store_me.storeme.R
import com.store_me.storeme.data.database.location.LocationDataBaseHelper
import com.store_me.storeme.data.database.location.LocationEntity
import com.store_me.storeme.data.datastore.getLocation
import com.store_me.storeme.data.datastore.saveLocation
import com.store_me.storeme.network.naver.NaverRepository
import com.store_me.storeme.utils.ServerResponse
import com.store_me.storeme.utils.ToastMessageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dbHelper: LocationDataBaseHelper,
    private val naverRepository: NaverRepository
) : ViewModel() {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val _reverseGeoCodeCompleted = MutableStateFlow(false)
    val reverseGeoCodeCompleted: StateFlow<Boolean> = _reverseGeoCodeCompleted

    private val _lastLocation = MutableStateFlow("")
    val lastLocation: StateFlow<String> = _lastLocation

    private val _code = MutableStateFlow<Int?>(null)
    val code: StateFlow<Int?> = _code

    private val _searchResults = MutableStateFlow<List<LocationEntity>>(emptyList())
    val searchResults: StateFlow<List<LocationEntity>> = _searchResults

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _locationText = MutableStateFlow<String?>(null)
    val locationText: StateFlow<String?> = _locationText

    init {
        viewModelScope.launch {
            getLocation(context).collect { location ->
                _lastLocation.value = location
            }
        }
    }

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for(location in locationResult.locations) {
                val latLng = LatLng(location.latitude, location.longitude)

                reverseGeoCode(latLng)
                fusedLocationClient.removeLocationUpdates(this)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun setLocation() {
        viewModelScope.launch {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun reverseGeoCode(latLng: LatLng) {
        viewModelScope.launch {
            val result = naverRepository.reverseGeocode(latLng)

            result.onSuccess { response ->
                val first = response.results.firstOrNull()?.region?.area1?.name
                val second = response.results.firstOrNull()?.region?.area2?.name
                val third = response.results.firstOrNull()?.region?.area3?.name

                if (first != null && second != null && third != null) {

                    _locationText.value = "$first $second $third"

                    val code = withContext(Dispatchers.IO) {
                        dbHelper.getLocationCode(first, second, third)
                    }

                    _lastLocation.value = third

                    if(code != null) {
                        saveLocation(context, third, code)
                        setReverseGeocodeCompletedValue(true)
                    }

                } else {
                    failGetLocation()
                    return@onSuccess
                }
            }.onFailure {
                failGetLocation(it.message)
            }

        }
    }

    /**
     * 주소값 반환에 실패한 경우 실행되는 함수
     * @param message API 호출에 실패한 경우에 따른 Error Message
     */
    private fun failGetLocation(message: String? = null){
        when(message) {
            ServerResponse.FAIL.code -> {
                ToastMessageUtils.showToast(context, R.string.network_fail)
            }
            ServerResponse.ERROR.code -> {
                ToastMessageUtils.showToast(context, R.string.network_error)
            }
            else -> {
                ToastMessageUtils.showToast(context, R.string.fail_reverse_gc)
            }
        }
    }

    /**
     * 검색어를 통해 동네를 검색하는 함수
     */
    fun getSearchResults(query: String){
        _searchQuery.value = query

        viewModelScope.launch {
            _searchResults.value = dbHelper.searchLocations(query)
        }
    }

    fun setReverseGeocodeCompletedValue(value: Boolean) {
        viewModelScope.launch {
            _reverseGeoCodeCompleted.value = value
            Log.d("ASD", "${_reverseGeoCodeCompleted.value}")
        }
    }

    fun saveLocation(third: String, code: Int) {
        viewModelScope.launch {
            saveLocation(context, third, code)
        }
    }
}