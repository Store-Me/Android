package com.store_me.storeme.ui.home.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.request.store.PatchBusinessHoursRequest
import com.store_me.storeme.data.request.store.PatchLinksRequest
import com.store_me.storeme.data.request.store.PatchStoreIntroRequest
import com.store_me.storeme.data.request.store.PatchStoreNoticeRequest
import com.store_me.storeme.data.request.store.PatchStoreProfileImagesRequest
import com.store_me.storeme.data.response.BusinessHoursResponse
import com.store_me.storeme.data.store.BusinessHourData
import com.store_me.storeme.data.store.StoreInfoData
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
    private val ownerRepository: OwnerRepository
): ViewModel() {
    private val _storeInfoData = MutableStateFlow<StoreInfoData?>(null)
    val storeInfoData: StateFlow<StoreInfoData?> = _storeInfoData

    private val _businessHours = MutableStateFlow<BusinessHoursResponse?>(null)
    val businessHours: StateFlow<BusinessHoursResponse?> = _businessHours

    private val _links = MutableStateFlow<List<String>?>(emptyList())
    val links: StateFlow<List<String>?> = _links

    private val _notice = MutableStateFlow("")
    val notice: StateFlow<String> = _notice

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

    fun patchBusinessHours(storeId: String, businessHours: List<BusinessHourData>, extraInfo: String?) {
        viewModelScope.launch {
            val response = ownerRepository.patchBusinessHours(
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
}