package com.store_me.storeme.ui.store_setting.stamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.StampCouponData
import com.store_me.storeme.data.request.store.PostStampCouponRequest
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.ErrorEventBus
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
class StampCouponCreateViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository
): ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _rewardInterval = MutableStateFlow(10)
    val rewardInterval: StateFlow<Int> = _rewardInterval

    private val _rewardFor5 = MutableStateFlow<String?>(null)
    val rewardFor5: StateFlow<String?> = _rewardFor5

    private val _rewardFor10 = MutableStateFlow("")
    val rewardFor10: StateFlow<String> = _rewardFor10

    private val _dueDate = MutableStateFlow<String?>(null)
    val dueDate: StateFlow<String?> = _dueDate

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _description = MutableStateFlow<String>("")
    val description: StateFlow<String> = _description

    private val _createdStampCoupon = MutableStateFlow<StampCouponData?>(null)
    val createdStampCoupon: StateFlow<StampCouponData?> = _createdStampCoupon

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateRewardInterval(rewardInterval: Int) {
        _rewardInterval.value = rewardInterval
    }

    fun updateRewardFor5(rewardFor5: String?) {
        _rewardFor5.value = rewardFor5
    }

    fun updateRewardFor10(rewardFor10: String) {
        _rewardFor10.value = rewardFor10
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

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun updateDescription(description: String) {
        _description.value = description
    }

    fun postStampCoupon(storeId: String) {
        viewModelScope.launch {
            when {
                name.value.isEmpty() || name.value.length > 20 -> {
                    ErrorEventBus.errorFlow.emit("스탬프 쿠폰 이름을 확인해주세요.")
                    return@launch
                }
                rewardInterval.value == 5 && rewardFor5.value.isNullOrEmpty() -> {
                    ErrorEventBus.errorFlow.emit("5개 보상을 입력해주세요.")
                    return@launch
                }
                rewardFor10.value.isEmpty() -> {
                    ErrorEventBus.errorFlow.emit("10개 보상을 입력해주세요.")
                    return@launch
                }
                dueDate.value == null -> {
                    ErrorEventBus.errorFlow.emit("유효기한을 선택해주세요.")
                    return@launch
                }
                password.value.length != 4 || password.value.toIntOrNull() == null -> {
                    ErrorEventBus.errorFlow.emit("비밀번호를 확인해주세요.")
                    return@launch
                }
            }

            val response = ownerRepository.postStampCoupon(
                storeId = storeId,
                postStampCouponRequest = PostStampCouponRequest(
                    name = name.value,
                    rewardInterval = rewardInterval.value,
                    rewardFor5 = rewardFor5.value,
                    rewardFor10 = rewardFor10.value,
                    dueDate = dueDate.value!!,
                    password = password.value,
                    description = description.value,
                )
            )

            response.onSuccess {
                _createdStampCoupon.value = it.result.stampCoupon

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