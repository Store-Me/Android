package com.store_me.storeme.ui.home.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.request.store.PatchLinksRequest
import com.store_me.storeme.data.request.store.PatchStoreProfileImagesRequest
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoreDataViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository
): ViewModel() {
    private val _storeInfoData = MutableStateFlow<StoreInfoData?>(null)
    val storeInfoData: StateFlow<StoreInfoData?> = _storeInfoData

    private val _businessHours = MutableStateFlow<List<BusinessHourData>?>(emptyList())
    val businessHours: StateFlow<List<BusinessHourData>?> = _businessHours

    private val _links = MutableStateFlow<List<String>?>(emptyList())
    val links: StateFlow<List<String>?> = _links

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
                updateBusinessHours(it.businessHours)
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

    /**
     * StoreData 갱신 함수
     */
    fun updateStoreInfoData(storeInfoData: StoreInfoData) {
        _storeInfoData.value = storeInfoData
    }

    /**
     * BusinessHours 갱신 함수
     */
    fun updateBusinessHours(businessHours: List<BusinessHourData>?) {
        _businessHours.value = businessHours
    }

    /**
     * StoreLinks 갱신 함수
     */
    fun updateStoreLinks(links: List<String>?) {
        _links.value = links
    }

    //편집 버튼 Text
    fun getEditButtonText(storeHomeItem: StoreHomeItem, isEmpty: Boolean = true): String {
        return when(storeHomeItem) {
            StoreHomeItem.NOTICE -> { if (isEmpty) "공지사항 작성" else "공지사항 편집"}
            //StoreHomeItem.INTRO -> { if (isEmpty) "소개 내용 작성" else "소개 내용 편집"}
            StoreHomeItem.IMAGE -> { "사진 업로드"}
            StoreHomeItem.COUPON -> { "쿠폰 관리" }
            StoreHomeItem.MENU -> { "메뉴 관리" }
            StoreHomeItem.STORY -> { "스토리 업로드" }
            StoreHomeItem.REVIEW -> { "리뷰 관리"}
            StoreHomeItem.NEWS -> { "소식 작성"}
        }
    }
}