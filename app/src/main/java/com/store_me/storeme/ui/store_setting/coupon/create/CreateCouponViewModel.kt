package com.store_me.storeme.ui.store_setting.coupon.create

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.CouponAvailable
import com.store_me.storeme.data.CouponDiscountType
import com.store_me.storeme.data.CouponInfoData
import com.store_me.storeme.data.CouponQuantity
import com.store_me.storeme.data.CouponType
import com.store_me.storeme.data.OwnerCouponDetailData
import com.store_me.storeme.data.UsedCouponData
import com.store_me.storeme.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class CreateCouponViewModel: ViewModel() {

    /*   쿠폰 생성 단계 진행 과정   */
    enum class CreateCouponProgress {
        SET_VALUE,
        SET_NAME,
        SET_AVAILABLE,
        SET_QUANTITY,
        SET_DUE_DATE,
        SET_IMAGE,
        SET_DESCRIPTION,
    }

    private val _currentProgress = MutableStateFlow(CreateCouponProgress.SET_VALUE)
    val currentProgress: StateFlow<CreateCouponProgress> = _currentProgress

    fun nextProgress() {
        val nextOrdinal = _currentProgress.value.ordinal + 1
        if(nextOrdinal < CreateCouponProgress.entries.size){
            _currentProgress.value = CreateCouponProgress.entries[nextOrdinal]
        }
    }

    fun previousProgress() {
        val previousOrdinal = _currentProgress.value.ordinal - 1
        if(previousOrdinal >= 0) {
            _currentProgress.value = CreateCouponProgress.entries[previousOrdinal]
        }
    }

    /*   할인 값 관련   */
    private val _discountType = MutableStateFlow<CouponDiscountType?>(null)
    val discountType: StateFlow<CouponDiscountType?> = _discountType

    fun updateDiscountType(discountType: CouponDiscountType) {
        _discountType.value = discountType
    }

    private val _discountPrice = MutableStateFlow<Int?>(null)
    val discountPrice: StateFlow<Int?> = _discountPrice

    fun updateDiscountPrice(discountPrice: Int?) {
        _discountPrice.value = discountPrice
    }

    private val _discountRate = MutableStateFlow<Int?>(null)
    val discountRate: StateFlow<Int?> = _discountRate

    fun updateDiscountRate(discountRate: Int?) {
        _discountRate.value = discountRate
    }

    /*   증정 관련   */
    private val _giveawayContent = MutableStateFlow("")
    val giveawayContent: StateFlow<String> = _giveawayContent

    fun updateGiveawayContent(giveawayContent: String) {
        _giveawayContent.value = giveawayContent
    }

    /*   혜택 내용 관련   */
    private val _otherContent = MutableStateFlow("")
    val otherContent: StateFlow<String> = _otherContent

    fun updateOtherContent(otherContent: String) {
        _otherContent.value = otherContent
    }

    /*   공통 항목   */
    //쿠폰 이름
    private val _couponName = MutableStateFlow("")
    val couponName: StateFlow<String> = _couponName

    fun updateName(couponName: String) {
        _couponName.value = couponName
    }

    //제공 대상
    private val _couponAvailable = MutableStateFlow<CouponAvailable?>(null)
    val couponAvailable: StateFlow<CouponAvailable?> = _couponAvailable

    fun updateAvailable(couponAvailable: CouponAvailable) {
        _couponAvailable.value = couponAvailable
    }

    //개수 제한
    private val _couponQuantity = MutableStateFlow<CouponQuantity?>(null)
    val couponQuantity: StateFlow<CouponQuantity?> = _couponQuantity

    fun updateQuantity(isInfinite: Boolean, quantity: Int? = null) {
        _couponQuantity.value = if(isInfinite) {
            CouponQuantity.Infinite
        } else {
            if(quantity != null && quantity > 0){
                CouponQuantity.Limit(quantity)
            } else {
                CouponQuantity.Limit(0)
            }
        }
    }

    //사용 기한
    private val _couponDueDate = MutableStateFlow<LocalDate?>(null)
    val couponDueDate: StateFlow<LocalDate?> = _couponDueDate

    fun updateDueDate(date: LocalDate) {
        _couponDueDate.value = date
    }

    //쿠폰 이미지
    private val _couponImageUrl = MutableStateFlow("")
    val couponImageUrl: StateFlow<String> = _couponImageUrl

    fun updateImageUrl(url: String) {
        _couponImageUrl.value = url
    }

    //안내 사항
    private val _couponDescription = MutableStateFlow("")
    val couponDescription: StateFlow<String> = _couponDescription

    fun updateDescription(description: String) {
        _couponDescription.value = description
    }

    fun createCoupon(type: CouponType) {
        val couponInfo = when(type){
            CouponType.DISCOUNT -> {
                CouponInfoData.Discount(
                    discountType = discountType.value!!,
                    discountValue = if(discountType.value == CouponDiscountType.PRICE) discountPrice.value!! else discountRate.value!!,
                    couponId = "sampleCoupon${Auth.couponDetailList.value.size}",
                    name = couponName.value,
                    available = couponAvailable.value!!,
                    quantity = couponQuantity.value!!,
                    dueDate = DateTimeUtils().localDateToDateData(couponDueDate.value!!),
                    image = couponImageUrl.value,
                    description = couponDescription.value,
                    createdAt = "2024-09-06T23:59:59"
                )
            }
            CouponType.GIVEAWAY -> {
                CouponInfoData.Giveaway(
                    couponId = "sampleCoupon${Auth.couponDetailList.value.size}",
                    content = giveawayContent.value,
                    name = couponName.value,
                    available = couponAvailable.value!!,
                    quantity = couponQuantity.value!!,
                    dueDate = DateTimeUtils().localDateToDateData(couponDueDate.value!!),
                    image = couponImageUrl.value,
                    description = couponDescription.value,
                    createdAt = "2024-09-06T23:59:59"
                )
            }
            CouponType.OTHER -> {
                CouponInfoData.Other(
                    couponId = "sampleCoupon${Auth.couponDetailList.value.size}",
                    content = otherContent.value,
                    name = couponName.value,
                    available = couponAvailable.value!!,
                    quantity = couponQuantity.value!!,
                    dueDate = DateTimeUtils().localDateToDateData(couponDueDate.value!!),
                    image = couponImageUrl.value,
                    description = couponDescription.value,
                    createdAt = "2024-09-06T23:59:59"
                )
            }
        }

        //TODO DELETE ADD FUNCTION
        Auth.addCouponDetailData(
            OwnerCouponDetailData(
                couponInfoData = couponInfo,
                usedCouponData = UsedCouponData(
                    receivedCount = 0,
                    usedCount = 0
                )
            )
        )
    }

    fun getCouponData(couponInfoData: CouponInfoData) {
        when(couponInfoData){
            is CouponInfoData.Discount -> {
                when(couponInfoData.discountType) {
                    CouponDiscountType.PRICE -> { updateDiscountPrice(couponInfoData.discountValue) }
                    CouponDiscountType.RATE -> { updateDiscountRate(couponInfoData.discountValue) }
                }
            }
            is CouponInfoData.Giveaway -> {
                updateGiveawayContent(couponInfoData.content)
            }
            is CouponInfoData.Other -> {
                updateOtherContent(couponInfoData.content)
            }
        }

        updateName(couponInfoData.name)

        updateAvailable(couponInfoData.available)

        when(couponInfoData.quantity) {
            is CouponQuantity.Limit -> { updateQuantity(isInfinite = false, quantity = (couponInfoData.quantity as CouponQuantity.Limit).quantity) }
            is CouponQuantity.Infinite -> { updateQuantity(isInfinite = true) }
        }

        updateDueDate(DateTimeUtils().dateDataToLocalDate(couponInfoData.dueDate))

        updateImageUrl(couponInfoData.image)

        updateDescription(couponInfoData.description)
    }

    fun updateCouponData(ownerCouponDetailData: OwnerCouponDetailData) {
        val couponInfoData = ownerCouponDetailData.couponInfoData

        val type = when(couponInfoData){
            is CouponInfoData.Discount -> { CouponType.DISCOUNT }
            is CouponInfoData.Giveaway -> { CouponType.GIVEAWAY }
            is CouponInfoData.Other -> { CouponType.OTHER }
        }

        val couponInfo = when(type){
            CouponType.DISCOUNT -> {
                CouponInfoData.Discount(
                    discountType = discountType.value!!,
                    discountValue = if(discountType.value == CouponDiscountType.PRICE) discountPrice.value!! else discountRate.value!!,
                    couponId = couponInfoData.couponId,
                    name = couponName.value,
                    available = couponAvailable.value!!,
                    quantity = couponQuantity.value!!,
                    dueDate = DateTimeUtils().localDateToDateData(couponDueDate.value!!),
                    image = couponImageUrl.value,
                    description = couponDescription.value,
                    createdAt = couponInfoData.createdAt
                )
            }
            CouponType.GIVEAWAY -> {
                CouponInfoData.Giveaway(
                    couponId = couponInfoData.couponId,
                    content = giveawayContent.value,
                    name = couponName.value,
                    available = couponAvailable.value!!,
                    quantity = couponQuantity.value!!,
                    dueDate = DateTimeUtils().localDateToDateData(couponDueDate.value!!),
                    image = couponImageUrl.value,
                    description = couponDescription.value,
                    createdAt = couponInfoData.createdAt
                )
            }
            CouponType.OTHER -> {
                CouponInfoData.Other(
                    couponId = couponInfoData.couponId,
                    content = otherContent.value,
                    name = couponName.value,
                    available = couponAvailable.value!!,
                    quantity = couponQuantity.value!!,
                    dueDate = DateTimeUtils().localDateToDateData(couponDueDate.value!!),
                    image = couponImageUrl.value,
                    description = couponDescription.value,
                    createdAt = couponInfoData.createdAt
                )
            }
        }

        //TODO DELETE UPDATE FUNCTION
        Auth.updateCouponDetailData(
            OwnerCouponDetailData(
                couponInfoData = couponInfo,
                usedCouponData = ownerCouponDetailData.usedCouponData
            )
        )
    }

    fun deleteCoupon(ownerCouponDetailData: OwnerCouponDetailData) {
        Auth.deleteCouponDetailData(ownerCouponDetailData)
    }
}