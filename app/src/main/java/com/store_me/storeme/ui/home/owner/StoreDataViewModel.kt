package com.store_me.storeme.ui.home.owner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.StampCouponData
import com.store_me.storeme.data.request.store.PatchBusinessHoursRequest
import com.store_me.storeme.data.request.store.PatchStoreFeaturedImagesRequest
import com.store_me.storeme.data.request.store.PatchLinksRequest
import com.store_me.storeme.data.request.store.PatchStoreIntroRequest
import com.store_me.storeme.data.request.store.PatchStoreLocationRequest
import com.store_me.storeme.data.request.store.PatchStoreNoticeRequest
import com.store_me.storeme.data.request.store.PatchStoreProfileImagesRequest
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.data.response.MenusResponse
import com.store_me.storeme.data.store.BusinessHourData
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.repository.naver.NaverRepository
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreDataViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository,
    private val naverRepository: NaverRepository
): ViewModel() {
    private val _storeInfoData = MutableStateFlow<StoreInfoData?>(null)
    val storeInfoData: StateFlow<StoreInfoData?> = _storeInfoData

    private val _storeMapImage = MutableStateFlow<Bitmap?>(null)
    val storeMapImage: StateFlow<Bitmap?> = _storeMapImage

    private val _businessHours = MutableStateFlow<BusinessHoursResponse?>(null)
    val businessHours: StateFlow<BusinessHoursResponse?> = _businessHours

    private val _links = MutableStateFlow<List<String>?>(emptyList())
    val links: StateFlow<List<String>?> = _links

    private val _notice = MutableStateFlow("")
    val notice: StateFlow<String> = _notice

    private val _featuredImages = MutableStateFlow<List<FeaturedImageData>>(emptyList())
    val featuredImages: StateFlow<List<FeaturedImageData>> = _featuredImages

    private val _menuCategories = MutableStateFlow<List<MenuCategoryData>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategoryData>> = _menuCategories

    private val _coupons = MutableStateFlow<List<CouponData>>(emptyList())
    val coupons: StateFlow<List<CouponData>> = _coupons

    private val _stampCoupon = MutableStateFlow<StampCouponData?>(null)
    val stampCoupon: StateFlow<StampCouponData?> = _stampCoupon

    /**
     * StoreData 조회 함수
     */
    fun getStoreData(storeId: String) {
        viewModelScope.launch {
            val response = ownerRepository.getStoreData(storeId = storeId)

            response.onSuccess {
                updateStoreInfoData(it)
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * 지도 이미지 조회 함수
     */
    fun getStoreImage(storeLatLng: LatLng) {
        viewModelScope.launch {
            val response = naverRepository.getStaticMap(latLng = storeLatLng, storeName = storeInfoData.value!!.storeName)

            response.onSuccess {
                val byteArray = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                updateStoreMapImage(bitmap)
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * BusinessHours 조회 함수
     */
    fun getStoreBusinessHours(storeId:String) {
        viewModelScope.launch {
            val response = ownerRepository.getBusinessHours(storeId = storeId)

            response.onSuccess {
                updateBusinessHours(it)
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * StoreLinks 조회 함수
     */
    fun getStoreLinks(storeId: String) {
        viewModelScope.launch {
            val response = ownerRepository.getStoreLinks(storeId = storeId)

            response.onSuccess {
                updateStoreLinks(it.links)
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun patchStoreProfileImages(storeId: String, profileImage: String?, backgroundImage: String?) {
        viewModelScope.launch {
            val response = ownerRepository.patchStoreProfileImages(
                storeId = storeId,
                patchStoreProfileImagesRequest = PatchStoreProfileImagesRequest(storeProfileImage = profileImage, backgroundImage = backgroundImage)
            )

            response.onSuccess {
                updateStoreInfoData(it.result)

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun patchStoreLinks(storeId: String, links: List<String>) {
        viewModelScope.launch {
            val response = ownerRepository.patchStoreLinks(
                storeId = storeId,
                patchLinksRequest = PatchLinksRequest(links = links)
            )

            response.onSuccess {
                updateStoreLinks(it.result.links)

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun patchStoreIntro(storeId: String, storeIntro: String) {
        viewModelScope.launch {
            val response = ownerRepository.patchStoreIntro(
                storeId = storeId,
                patchStoreIntroRequest = PatchStoreIntroRequest(
                    storeIntro = storeIntro
                )
            )

            response.onSuccess {
                updateStoreInfoData(it.result)

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun patchStorePhoneNumber(storeId: String, storePhoneNumber: String) {
        viewModelScope.launch {
            val response = ownerRepository.patchStorePhoneNumber(
                storeId = storeId,
                phoneNumber = storePhoneNumber.ifEmpty { null }
            )

            response.onSuccess {
                updateStoreInfoData(it.result)

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun patchStoreBusinessHours(storeId: String, businessHours: List<BusinessHourData>, extraInfo: String?) {
        viewModelScope.launch {
            val response = ownerRepository.patchStoreBusinessHours(
                storeId = storeId,
                patchBusinessHoursRequest = PatchBusinessHoursRequest(
                    businessHours = businessHours,
                    extraInfo = extraInfo
                )
            )

            response.onSuccess {
                updateBusinessHours(it.result)

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * StoreData 갱신 함수
     */
    fun updateStoreInfoData(storeInfoData: StoreInfoData) {
        _storeInfoData.value = storeInfoData
    }

    /**
     * BusinessHours 갱신 함수
     */
    fun updateBusinessHours(businessHours: BusinessHoursResponse?) {
        _businessHours.value = businessHours
    }

    /**
     * StoreLinks 갱신 함수
     */
    fun updateStoreLinks(links: List<String>?) {
        _links.value = links
    }

    /**
     * Notice 갱신 함수
     */
    fun updateNotice(notice: String) {
        _notice.value = notice
    }

    /**
     * 지도 이미지 갱신 함수
     */
    fun updateStoreMapImage(storeMapImage: Bitmap?) {
        _storeMapImage.value = storeMapImage
    }

    /**
     * 대표 사진 갱신 함수
     */
    fun updateFeaturedImages(featuredImages: List<FeaturedImageData>) {
        _featuredImages.value = featuredImages
    }

    /**
     * 메뉴 갱신 함수
     */
    fun updateMenuCategories(menuCategories: List<MenuCategoryData>) {
        _menuCategories.value = menuCategories
    }

    /**
     * 쿠폰 갱신 함수
     */
    fun updateCoupons(coupons: List<CouponData>) {
        _coupons.value = coupons
    }

    /**
     * 스탬프 쿠폰 갱신 함수
     */
    fun updateStampCoupon(stampCoupon: StampCouponData?) {
        _stampCoupon.value = stampCoupon
    }


    /**
     * Notice 조회
     */
    fun getStoreNotice(storeId: String) {
        viewModelScope.launch {
            val response = ownerRepository.getStoreNotice(storeId = storeId)

            response.onSuccess {
                updateNotice(it.notice ?: "")
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * Notice 변경
     */
    fun patchStoreNotice(storeId: String, notice: String) {
        viewModelScope.launch {
            val response = ownerRepository.patchStoreNotice(
                storeId = storeId,
                patchStoreNoticeRequest = PatchStoreNoticeRequest(notice = notice)
            )

            response.onSuccess {
                updateNotice(it.result.notice ?: "")

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * Location 변경
     */
    fun patchStoreLocation(
        storeId: String,
        storeLocationAddress: String,
        storeLocationDetail: String?,
        storeLocationCode: Long,
        storeLocation: String,
        storeLat: Double?,
        storeLng: Double?
    ) {
        viewModelScope.launch {
            val response = ownerRepository.patchStoreLocation(
                storeId = storeId,
                patchStoreLocationRequest = PatchStoreLocationRequest(
                    storeId = storeId,
                    storeLocationAddress = storeLocationAddress,
                    storeLocationDetail = storeLocationDetail,
                    storeLocationCode = storeLocationCode,
                    storeLocation = storeLocation,
                    storeLat = storeLat,
                    storeLng = storeLng
                )
            )

            response.onSuccess {
                updateStoreInfoData(it.result)

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * FeaturedImages 조회
     */
    fun getStoreFeaturedImages(storeId: String) {
        viewModelScope.launch {
            val response = ownerRepository.getStoreFeaturedImages(storeId = storeId)

            response.onSuccess {
                updateFeaturedImages(it.images ?: emptyList())
            }.onFailure {
                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * FeaturedImages 변경
     */
    fun patchStoreFeaturedImages(storeId: String, featuredImages: List<FeaturedImageData>) {
        viewModelScope.launch {
            val response = ownerRepository.patchFeaturedImages(
                storeId = storeId,
                patchStoreFeaturedImagesRequest = PatchStoreFeaturedImagesRequest(images = featuredImages)
            )

            response.onSuccess {
                updateFeaturedImages(it.result.images ?: emptyList())

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * MenuCategories 조회
     */
    fun getStoreMenus(storeId: String) {
        viewModelScope.launch {
            val response = ownerRepository.getStoreMenus(storeId = storeId)

            response.onSuccess {
                updateMenuCategories(it.categories)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * MenuCategories 변경
     */
    fun patchStoreMenus(storeId: String, menuCategories: List<MenuCategoryData>) {
        viewModelScope.launch {
            val response = ownerRepository.patchStoreMenus(
                storeId = storeId,
                patchStoreMenusRequest = MenusResponse(categories = menuCategories)
            )

            response.onSuccess {
                updateMenuCategories(it.result.categories)

                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * Coupons 조회
     */
    fun getStoreCoupons(storeId: String) {
        viewModelScope.launch {
            val response = ownerRepository.getStoreCoupons(storeId = storeId)

            response.onSuccess {
                updateCoupons(it.coupons)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    /**
     * StampCoupon 조회
     */
    fun getStampCoupon(storeId: String) {
        viewModelScope.launch {
            val response = ownerRepository.getStampCoupon(storeId = storeId)

            response.onSuccess {
                updateStampCoupon(it.stampCoupon)
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }
}