package com.store_me.storeme.ui.store_setting.stamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.response.StampCouponPasswordResponse
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
class StampCouponSettingViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository
): ViewModel() {
    private val _stampPassword = MutableStateFlow<String?>(null)
    val stampPassword: StateFlow<String?> = _stampPassword

    fun updateStampPassword(stampPassword: String) {
        _stampPassword.value = stampPassword
    }

    fun getStampPassword(storeId: String) {
        viewModelScope.launch {
            val response = ownerRepository.getStampCouponPassword(storeId = storeId)

            response.onSuccess {
                updateStampPassword(it.result.password)
            }.onFailure {
                if (it is ApiException) {
                      ErrorEventBus.errorFlow.emit(it.message)
                  } else {
                      ErrorEventBus.errorFlow.emit(null)
                  }
            }
        }
    }

    fun patchStampCouponPassword(storeId: String, password: String) {
        viewModelScope.launch {
            val response = ownerRepository.patchStampCouponPassword(storeId = storeId, patchStampCouponPasswordRequest = StampCouponPasswordResponse(password = password))

            response.onSuccess {
                updateStampPassword(password)

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