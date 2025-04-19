package com.store_me.storeme.ui.store_setting.coupon.management

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.coupon.CouponData
import com.store_me.storeme.data.enums.coupon.CouponDiscountType
import com.store_me.storeme.data.enums.coupon.CouponType
import com.store_me.storeme.data.request.store.CouponRequest
import com.store_me.storeme.repository.storeme.CouponRepository
import com.store_me.storeme.repository.storeme.ImageRepository
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.StoragePaths
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CouponManagementViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val ownerRepository: OwnerRepository,
    private val couponRepository: CouponRepository
) : ViewModel() {
    private val _editCoupon = MutableStateFlow<CouponData?>(null)
    val editCoupon: StateFlow<CouponData?> = _editCoupon

    private val _discountType = MutableStateFlow(CouponDiscountType.PRICE)
    val discountType: StateFlow<CouponDiscountType> = _discountType

    private val _value = MutableStateFlow<String?>(null)
    val value: StateFlow<String?> = _value

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _target = MutableStateFlow<String?>(null)
    val target: StateFlow<String?> = _target

    private val _quantity = MutableStateFlow<Long?>(null)
    val quantity: StateFlow<Long?> = _quantity

    private val _dueDate = MutableStateFlow<String?>(null)
    val dueDate: StateFlow<String?> = _dueDate

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val _uploadProgress = MutableStateFlow<Float>(0.0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress

    private val _description = MutableStateFlow<String?>(null)
    val description: StateFlow<String?> = _description

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished

    fun updateDiscountType(discountType: CouponDiscountType) {
        _discountType.value = discountType
    }

    fun updateValue(value: String?) {
        _value.value = value
    }

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateTarget(target: String?) {
        _target.value = target
    }

    fun updateQuantity(quantity: Long?) {
        _quantity.value = quantity
    }

    fun updateDueDate(dueDate: LocalDate?) {
        if(dueDate == null){
            _dueDate.value = null
            return
        }

        val dueDateTime = dueDate.atTime(23, 59, 59) // LocalDateTime 생성
        val formatted = dueDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) // "2024-03-26T23:59:59"

        if(!DateTimeUtils.isValid(formatted)) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("오늘 이후의 날짜만 선택이 가능합니다.")
            }

            return
        }

        _dueDate.value = formatted
    }

    fun updateImageUrl(imageUrl: String?) {
        _imageUrl.value = imageUrl
    }

    fun updateImageUri(imageUri: Uri?) {
        _imageUri.value = imageUri
    }

    fun updateDescription(description: String?) {
        _description.value = description
    }

    fun uploadStoreCouponImage() {
        if(imageUri.value == null)
            return

        viewModelScope.launch {
            val response = imageRepository.uploadImage(
                folderName = StoragePaths.STORE_COUPON_IMAGES,
                uri = imageUri.value!!,
            ) {
                _uploadProgress.value = it
            }

            response.onSuccess {
                updateImageUrl(it)
                updateImageUri(null)
                _uploadProgress.value = 0.0f
            }.onFailure {
                updateImageUri(null)

                if(it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun getCouponData(couponId: String, coupons: List<CouponData>): CouponData? {
        val coupon = coupons.find { it.couponId == couponId }
        _editCoupon.value = coupon

        if(coupon == null) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("쿠폰 정보를 불러올 수 없습니다.")
            }
        } else {
            updateCouponData(coupon)
        }

        return coupon
    }

    private fun updateCouponData(couponData: CouponData) {
        if(couponData.type == CouponType.Discount.name) {
            val longValue = couponData.value.toLongOrNull()

            when (longValue) {
                null -> updateDiscountType(CouponDiscountType.PRICE)
                in 0..100 -> updateDiscountType(CouponDiscountType.RATE)
                else -> updateDiscountType(CouponDiscountType.PRICE)
            }
        }

        updateValue(couponData.value)
        updateName(couponData.name)
        updateTarget(couponData.target)
        updateQuantity(couponData.quantity)
        _dueDate.value = couponData.dueDate
        updateImageUrl(couponData.image)
        updateDescription(couponData.description)
    }

    fun postCoupon(couponType: CouponType) {
        if(value.value == null || target.value == null || dueDate.value == null) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("필수 정보가 누락되었습니다.")
            }

            return
        }

        viewModelScope.launch {
            val response = couponRepository.postStoreCoupon(
                couponRequest = CouponRequest(
                    name = name.value,
                    type = couponType.name,
                    value = value.value!!,
                    target = target.value!!,
                    quantity = quantity.value,
                    dueDate = dueDate.value!!,
                    image = imageUrl.value,
                    description = description.value
                )
            )

            response.onSuccess {
                _isFinished.value = true
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

    fun patchCoupon(couponType: CouponType) {
        if(value.value == null || target.value == null || dueDate.value == null) {
            viewModelScope.launch {
                ErrorEventBus.errorFlow.emit("필수 정보가 누락되었습니다.")
            }
            return
        }

        viewModelScope.launch {
            val response = couponRepository.patchStoreCoupon(
                couponRequest = CouponRequest(
                    name = name.value,
                    type = couponType.name,
                    value = value.value!!,
                    target = target.value!!,
                    quantity = quantity.value,
                    dueDate = dueDate.value!!,
                    image = imageUrl.value,
                    description = description.value
                )
            )

            response.onSuccess {
                _isFinished.value = true
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

    fun deleteCoupon(couponId: String) {
        viewModelScope.launch {
            val response = couponRepository.deleteStoreCoupon(
                couponId = couponId
            )

            response.onSuccess {
                _isFinished.value = true

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