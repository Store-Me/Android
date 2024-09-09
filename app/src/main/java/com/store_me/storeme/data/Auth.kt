package com.store_me.storeme.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object Auth {
    enum class AccountType{
        CUSTOMER, OWNER
    }

    //로그인 상태 관련
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    /**
     * 로그인 상태 설정 함수.
     * 로그인 성공시, true.
     * 로그인 실패시, false.
     * @param stateValue 해당 값에 따라 로그인 상태를 설정
     */
    fun setLoginState(stateValue: Boolean) {
        _isLoggedIn.value = stateValue
    }

    //계정 타입 관련
    private var _accountType = AccountType.OWNER
    val accountType = _accountType

    /**
     * 계정 타입 설정 함수.
     * 사장님 계정시,OWNER
     * 일반 계정시, CUSTOMER
     * @param type 해당 값에 따라 계정의 타입을 설정
     */
    fun setAccountType(type: AccountType) {
        _accountType = type
    }

    fun changeAccountType() {
        _accountType = if(accountType == AccountType.CUSTOMER) {
            AccountType.OWNER
        } else {
            AccountType.CUSTOMER
        }
    }

    //계정 정보 관련
    private val _userData = MutableStateFlow(UserData("도구리", "도구리야", "https://i.namu.wiki/i/vJ_iVx2uAFkYUmfaxSwP0QSDbPjRz-OzilacQpDBLQmls9oOM0pV4qUk8mCbgL41v4_wGV-kdotau0LIpZu261XmIpWq0qLg3gKfSSBg78Px_EGRyNlmZk6d5N6KKx6zgsZArniJ3t2cwmB4IvS-0A.webp"))
    val userData: StateFlow<UserData> = _userData

    /**
     * 사용자 계정 정보 설정
     */
    fun setUserData(userData: UserData) {
        _userData.value = userData
    }

    fun updateProfileImage(newProfileImage: String) {
        _userData.update {
            it.copy(profileImage = newProfileImage)
        }
    }

    /**
     * 사장님 계정 정보 설정
     */

    /*   Link 관리   */
    private val _linkListData = MutableStateFlow(SocialMediaAccountData(emptyList()))
    val linkListData: StateFlow<SocialMediaAccountData> = _linkListData

    fun setLinkListData(socialMediaAccountData: SocialMediaAccountData) {
        _linkListData.value = socialMediaAccountData
    }

    fun addLinkListData(url: String) {
        val currentList = _linkListData.value.urlList.toMutableList()
        currentList.add(url)
        _linkListData.value = SocialMediaAccountData(currentList)
    }

    /*   메인 화면 순서 및 숨기기 설정   */

    private val _storeHomeItemList = MutableStateFlow<List<StoreHomeItemData>>(emptyList())
    val storeHomeItemList: StateFlow<List<StoreHomeItemData>> = _storeHomeItemList

    fun setStoreHomeItemData(storeHomeItemList: List<StoreHomeItemData>) {
        _storeHomeItemList.value = storeHomeItemList
    }

    /*   영업 시간 정보 및 휴무일 설정   */
    private val _storeHoursData = MutableStateFlow(StoreHoursData(emptyList(), emptyList(), emptyList(),""))
    val storeHoursData: StateFlow<StoreHoursData> = _storeHoursData

    fun setStoreHoursData(storeHoursData: StoreHoursData) {
        _storeHoursData.value = storeHoursData
    }

    /*   쿠폰 관리   */
    private val _couponDetailList = MutableStateFlow(listOf(
        OwnerCouponDetailData(
            couponInfoData = CouponInfoData.Other(
                couponId = "DefaultCouponId",
                content = "혜택이다",
                name = "초코쿠키 25개 무료 체험권",
                available = CouponAvailable.REPEAT,
                quantity = CouponQuantity.Infinite,
                dueDate = DateData(2024, 9, 3),
                image = "",
                description = "",
                createdAt = "2024-09-01T10:00:00"
            ),
            usedCouponData = UsedCouponData(0, 0)
        ),OwnerCouponDetailData(
            couponInfoData = CouponInfoData.Discount(
                couponId = "DefaultCouponId2",
                discountType = CouponDiscountType.PRICE,
                discountValue = 4000,
                name = "초코쿠키 25개 무료 체험권2",
                available = CouponAvailable.REPEAT,
                quantity = CouponQuantity.Infinite,
                dueDate = DateData(2024, 9, 23),
                image = "",
                description = "",
                createdAt = "2024-09-01T10:00:00"
            ),
            usedCouponData = UsedCouponData(0, 0)
        )
        )
    )
    val couponDetailList: StateFlow<List<OwnerCouponDetailData>> = _couponDetailList

    fun addCouponDetailData(ownerCouponDetailData: OwnerCouponDetailData){
        _couponDetailList.value = _couponDetailList.value.toMutableList().apply {
            add(ownerCouponDetailData)
        }
    }

    fun deleteCouponDetailData(ownerCouponDetailData: OwnerCouponDetailData){
        _couponDetailList.value = _couponDetailList.value.toMutableList().apply {
            remove(ownerCouponDetailData)
        }
    }

    fun updateCouponDetailData(updatedCouponDetailData: OwnerCouponDetailData) {
        _couponDetailList.value = _couponDetailList.value.map { coupon ->
            if (coupon.couponInfoData.couponId == updatedCouponDetailData.couponInfoData.couponId) {
                updatedCouponDetailData
            } else {
                coupon
            }
        }
    }

    private val _menuCategoryList = MutableStateFlow<List<MenuCategory>>(emptyList())
    val menuCategoryList: StateFlow<List<MenuCategory>> = _menuCategoryList

}