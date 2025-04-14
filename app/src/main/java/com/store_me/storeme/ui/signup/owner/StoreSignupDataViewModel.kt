package com.store_me.storeme.ui.signup.owner

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.store_me.storeme.data.database.location.LocationDataBaseHelper
import com.store_me.storeme.data.response.DaumPostcodeResponse
import com.store_me.storeme.repository.naver.NaverRepository
import com.store_me.storeme.repository.storeme.ImageRepository
import com.store_me.storeme.ui.component.filterNonNumeric
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.StoragePaths
import com.store_me.storeme.utils.StoreCategory
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreSignupDataViewModel @Inject constructor(
    private val dbHelper: LocationDataBaseHelper,
    private val naverRepository: NaverRepository,
    private val imageRepository: ImageRepository
): ViewModel() {
    private val _storeName = MutableStateFlow("")
    val storeName: StateFlow<String> = _storeName

    //가게 업종 관련
    private val _storeCategory = MutableStateFlow<StoreCategory?>(null)
    val storeCategory: StateFlow<StoreCategory?> = _storeCategory

    private val _storeDetailCategory = MutableStateFlow("")
    val storeDetailCategory: StateFlow<String> = _storeDetailCategory

    //가게 위치 관련
    //주소 유무
    private val _hasAddress = MutableStateFlow(true)
    val hasAddress: StateFlow<Boolean> = _hasAddress

    //주소 검색 응답
    private val _daumPostcodeResponse = MutableStateFlow<DaumPostcodeResponse?>(null)
    val daumPostcodeResponse: StateFlow<DaumPostcodeResponse?> = _daumPostcodeResponse

    //도로명 주소
    private val _storeLocationAddress = MutableStateFlow("")
    val storeLocationAddress: StateFlow<String> = _storeLocationAddress

    //상세 주소
    private val _storeLocationDetail = MutableStateFlow("")
    val storeLocationDetail: StateFlow<String> = _storeLocationDetail

    //위도 경도
    private val _storeLatLng = MutableStateFlow<LatLng?>(null)
    val storeLatLng: StateFlow<LatLng?> = _storeLatLng

    //지역 코드
    private val _storeLocationCode = MutableStateFlow<Long?>(null)
    val storeLocationCode: StateFlow<Long?> = _storeLocationCode

    //동 정보
    private val _storeLocation = MutableStateFlow("")
    val storeLocation: StateFlow<String> = _storeLocation

    //프로필 이미지 정보
    private val _storeProfileImage = MutableStateFlow<Uri?>(null)
    val storeProfileImage: StateFlow<Uri?> = _storeProfileImage

    //프로필 URL 정보
    private val _storeProfileImageUrl = MutableStateFlow<String?>(null)
    val storeProfileImageUrl: StateFlow<String?> = _storeProfileImageUrl

    //스토어 대표 이미지
    private val _storeImages = MutableStateFlow<List<Uri>>(emptyList())
    val storeImages: StateFlow<List<Uri>> = _storeImages

    //이미지 업로드 상태
    private val _storeProfileImageProgress = MutableStateFlow<Float>(0.0f)
    val storeProfileImageProgress: StateFlow<Float> = _storeProfileImageProgress

    private val _storeImageUrls = MutableStateFlow<List<String>>(emptyList())
    val storeImageUrls: StateFlow<List<String>> = _storeImageUrls

    //스토어 소개
    private val _storeIntro = MutableStateFlow("")
    val storeIntro: StateFlow<String> = _storeIntro

    //스토어 번호
    private val _storeNumber = MutableStateFlow("")
    val storeNumber: StateFlow<String> = _storeNumber

    fun updateStoreName(newStoreName: String) {
        _storeName.value = newStoreName
    }

    fun updateStoreCategory(newStoreCategory: StoreCategory) {
        _storeCategory.value = newStoreCategory
    }

    fun updateStoreDetailCategory(newStoreDetailCategory: String) {
        _storeDetailCategory.value = newStoreDetailCategory
    }

    fun updateHasAddress(hasAddress: Boolean) {
        _hasAddress.value = hasAddress
    }

    fun updateStoreLocationAddress(newLocationAddress: String) {
        _storeLocationAddress.value = newLocationAddress
    }

    fun updateStoreLocationDetail(newLocationDetail: String) {
        _storeLocationDetail.value = newLocationDetail
    }

    fun updateStoreLocation(newStoreLocation: String) {
        _storeLocation.value = newStoreLocation
    }

    fun updateStoreLocationCode(newStoreLocationCode: Long) {
        _storeLocationCode.value = newStoreLocationCode
    }

    fun updateStoreLatLng(newStoreLatLng: LatLng?) {
        _storeLatLng.value = newStoreLatLng
    }

    fun updateDaumPostcodeResponse(newDaumPostcodeResponse: DaumPostcodeResponse) {
        _daumPostcodeResponse.value = newDaumPostcodeResponse
        _storeLocationAddress.value = newDaumPostcodeResponse.roadAddress

        if(newDaumPostcodeResponse.administrativeDong.isEmpty()) {
            _storeLocation.value = newDaumPostcodeResponse.legalDong
        } else {
            _storeLocation.value = newDaumPostcodeResponse.administrativeDong
        }

        roadAddressToLatLng(newDaumPostcodeResponse.roadAddress)

        getStoreLocationCode()
    }

    private fun getStoreLocationCode() {
        if(_daumPostcodeResponse.value != null && _daumPostcodeResponse.value?.sigunguCode != null && _daumPostcodeResponse.value?.legalDong != null) {
            viewModelScope.launch {
                val result = dbHelper.getStoreLocationCode(sigunguCode = _daumPostcodeResponse.value!!.sigunguCode, legalDong = _daumPostcodeResponse.value!!.legalDong)

                _storeLocationCode.value = result
            }
        }
    }

    fun updateStoreProfileImage(newUri: Uri?) {
        _storeProfileImage.value = newUri

        if(newUri == null)
            _storeProfileImageUrl.value = null
    }

    fun uploadStoreProfileImage() {
        if(storeProfileImage.value == null)
            return

        viewModelScope.launch {

            val response = imageRepository.uploadImage(
                folderName = StoragePaths.STORE_PROFILE_IMAGE,
                uri = storeProfileImage.value!!,
            ) {
                _storeProfileImageProgress.value = it
            }

            response.onSuccess {
                _storeProfileImageUrl.value = it
            }.onFailure {
                updateStoreProfileImage(null)

                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    private fun roadAddressToLatLng(roadAddress: String) {
        viewModelScope.launch {
            val result = naverRepository.geocode(roadAddress)

            result.onSuccess {
                _storeLatLng.value = LatLng(it.addresses[0].y.toDouble(), it.addresses[0].x.toDouble())
            }.onFailure {

            }
        }
    }

    fun addStoreImage(uris: List<Uri>) {
        _storeImages.value += uris
    }

    fun removeStoreImage(uri: Uri) {
        _storeImages.value -= uri
    }

    fun updateStoreIntro(newStoreIntro: String) {
        _storeIntro.value = newStoreIntro
    }


    fun updateStoreNumber(newStoreNumber: String) {
        _storeNumber.value = filterNonNumeric(newStoreNumber)
    }
}