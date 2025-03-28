package com.store_me.storeme.ui.location

import android.annotation.SuppressLint
import android.content.Context
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
import com.store_me.storeme.repository.naver.NaverRepository
import com.store_me.storeme.utils.ServerResponse
import com.store_me.storeme.utils.ToastMessageUtils
import com.store_me.storeme.utils.preference.LocationPreferencesHelper
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
    private val naverRepository: NaverRepository,
    private val locationPreferencesHelper: LocationPreferencesHelper
) : ViewModel() {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _reverseGeoCodeCompleted = MutableStateFlow(false)
    val reverseGeoCodeCompleted: StateFlow<Boolean> = _reverseGeoCodeCompleted

    private val _lastLocation = MutableStateFlow("")
    val lastLocation: StateFlow<String> = _lastLocation

    private val _code = MutableStateFlow<Long?>(null)
    val code: StateFlow<Long?> = _code

    private val _searchResults = MutableStateFlow<List<LocationEntity>>(emptyList())
    val searchResults: StateFlow<List<LocationEntity>> = _searchResults

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _locationText = MutableStateFlow<String?>(null)
    val locationText: StateFlow<String?> = _locationText

    private val _storeLocation = MutableStateFlow("")
    val storeLocation: StateFlow<String> = _storeLocation

    private val _storeLocationCode = MutableStateFlow<Long?>(null)
    val storeLocationCode: StateFlow<Long?> = _storeLocationCode

    private val _storeLocationAddress = MutableStateFlow("")
    val storeLocationAddress: StateFlow<String> = _storeLocationAddress

    private val _storeLatLng = MutableStateFlow<LatLng?>(null)
    val storeLatLng: StateFlow<LatLng?> = _storeLatLng

    init {
        viewModelScope.launch {
            locationPreferencesHelper.getLocation().collect { location ->
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

    private val locationCallbackOwner = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for(location in locationResult.locations) {
                val latLng = LatLng(location.latitude, location.longitude)

                reverseGeoCodeOwner(latLng)
                _storeLatLng.value = LatLng(location.latitude, location.longitude)
                fusedLocationClient.removeLocationUpdates(this)
            }
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

    @SuppressLint("MissingPermission")
    fun setLocation() {
        setReverseGeocodeCompletedValue(false)

        viewModelScope.launch {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    @SuppressLint("MissingPermission")
    fun setLocationOwner() {
        setReverseGeocodeCompletedValue(false)

        viewModelScope.launch {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallbackOwner, null)
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
                        locationPreferencesHelper.saveLocation(third, code)
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

    private fun reverseGeoCodeOwner(latLng: LatLng) {
        viewModelScope.launch {
            val result = naverRepository.reverseGeocode(latLng)

            result.onSuccess { response ->
                val first = response.results.firstOrNull()?.region?.area1?.name
                val second = response.results.firstOrNull()?.region?.area2?.name
                val third = response.results.firstOrNull()?.region?.area3?.name

                if (first != null && second != null && third != null) {

                    val code = withContext(Dispatchers.IO) {
                        dbHelper.getLocationCode(first, second, third)
                    }

                    if(code != null) {
                        _storeLocationAddress.value = "$first $second $third"
                        _storeLocation.value = third
                        _storeLocationCode.value = code

                        setReverseGeocodeCompletedValue(true)
                    } else {
                        failGetLocation()
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
        }
    }

    fun saveLocation(third: String, code: Long) {
        viewModelScope.launch {
            locationPreferencesHelper.saveLocation(third, code)
        }
    }

    //사장님
    fun clearStoreLatLng() {
        _storeLatLng.value = null
    }
}