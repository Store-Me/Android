package com.store_me.storeme.ui.post.add.coupon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.coupon.CouponData
import com.store_me.storeme.data.StampCouponData
import com.store_me.storeme.data.request.store.CreateCouponPostRequest
import com.store_me.storeme.repository.storeme.CouponRepository
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.repository.storeme.PostRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCouponPostViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository,
    private val couponRepository: CouponRepository,
    private val postRepository: PostRepository
): ViewModel() {
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _coupons = MutableStateFlow<List<CouponData>>(emptyList())
    val coupons: StateFlow<List<CouponData>> = _coupons

    private val _stamp = MutableStateFlow<StampCouponData?>(null)
    val stamp: StateFlow<StampCouponData?> = _stamp

    private val _selectedCoupon = MutableStateFlow<CouponData?>(null)
    val selectedCoupon: StateFlow<CouponData?> = _selectedCoupon

    private val _selectedStamp = MutableStateFlow<StampCouponData?>(null)
    val selectedStamp: StateFlow<StampCouponData?> = _selectedStamp

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun updateTitle(title: String) {
        _title.value = title
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun updateSelectedCoupon(coupon: CouponData?) {
        _selectedCoupon.value = coupon
    }

    fun updateSelectedStamp(stamp: StampCouponData?) {
        _selectedStamp.value = stamp
    }

    fun getStoreCoupons() {
        viewModelScope.launch {
            val response = couponRepository.getStoreCoupons()

            response.onSuccess {
                _coupons.value = it.coupons
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun getStampCoupon() {
        viewModelScope.launch {
            val response = ownerRepository.getStampCoupon()

            response.onSuccess {
                _stamp.value = it.stampCoupon
            }.onFailure {
                if (it is ApiException) {
                    ErrorEventBus.errorFlow.emit(it.message)
                } else {
                    ErrorEventBus.errorFlow.emit(null)
                }
            }
        }
    }

    fun createCouponPost() {
        viewModelScope.launch {
            if(title.value.isEmpty()) {
                ErrorEventBus.errorFlow.emit("제목을 입력해주세요.")
                return@launch
            }

            if(selectedCoupon.value == null && selectedStamp.value == null) {
                ErrorEventBus.errorFlow.emit("쿠폰을 선택해주세요.")
                return@launch
            }

            val response = postRepository.createCouponPost(
                createCouponPostRequest = CreateCouponPostRequest(
                    title = title.value,
                    description = description.value,
                    couponId = if(selectedCoupon.value == null) null else selectedCoupon.value!!.couponId,
                    isStampCoupon = selectedStamp.value != null
                )
            )

            response.onSuccess {
                _isSuccess.value = true
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
}