package com.store_me.storeme.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Auth {

    //계정 정보 관련
    private val _userData = MutableStateFlow(UserData("도구리", "도구리야", "https://i.namu.wiki/i/vJ_iVx2uAFkYUmfaxSwP0QSDbPjRz-OzilacQpDBLQmls9oOM0pV4qUk8mCbgL41v4_wGV-kdotau0LIpZu261XmIpWq0qLg3gKfSSBg78Px_EGRyNlmZk6d5N6KKx6zgsZArniJ3t2cwmB4IvS-0A.webp"))
    val userData: StateFlow<UserData> = _userData

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

    private val _menuCategoryList = MutableStateFlow(listOf(
        OldMenuCategory("테스트 카테고리 1", menuList = listOf(
            OldMenuData(
                name = "이름3",
                price = MenuPrice.Variable,
                image = "",
                description = "",
                isSignature = true,
                isPopular = true,
                isRecommend = true,
            ),
            OldMenuData(
                name = "이름4",
                price = MenuPrice.Fixed(10000),
                image = "",
                description = "",
                isSignature = true,
                isPopular = true,
                isRecommend = true,
            )
        )),
        OldMenuCategory(DEFAULT_MENU_CATEGORY, menuList = listOf(
            OldMenuData(
                name = "이름1",
                price = MenuPrice.Variable,
                image = "",
                description = "",
                isSignature = true,
                isPopular = true,
                isRecommend = true,
            ),
            OldMenuData(
                name = "이름2",
                price = MenuPrice.Fixed(10000),
                image = "",
                description = "",
                isSignature = true,
                isPopular = true,
                isRecommend = true,
            )
        ))
    ))
    val menuCategoryList: StateFlow<List<OldMenuCategory>> = _menuCategoryList

    fun updateCategory(menuCategoryList: List<OldMenuCategory>) {
        _menuCategoryList.value = menuCategoryList
    }

    fun addCategory(categoryName: String) {
        val currentList = _menuCategoryList.value.toMutableList()

        currentList.add(
            OldMenuCategory(
                categoryName = categoryName,
                menuList = emptyList()
            )
        )

        val defaultCategoryIndex = currentList.indexOfFirst { it.categoryName == DEFAULT_MENU_CATEGORY }

        if(defaultCategoryIndex == -1)
            return

        currentList.apply {
            add(lastIndex, removeAt(defaultCategoryIndex))
        }

        _menuCategoryList.value = currentList
    }

    fun deleteCategory(categoryName: String) {
        if(categoryName == DEFAULT_MENU_CATEGORY)
            return

        val currentList = _menuCategoryList.value.toMutableList()

        val targetIndex = currentList.indexOfFirst { it.categoryName == categoryName }

        if(targetIndex == -1)
            return

        currentList.removeAt(targetIndex)

        _menuCategoryList.value = currentList
    }

    fun addMenuData(menuData: OldMenuData, categoryName: String) {
        val currentList = _menuCategoryList.value.toMutableList()

        when(_menuCategoryList.value.isEmpty()) {
            true -> {
                _menuCategoryList.value = currentList.apply {
                    add(
                        OldMenuCategory(
                            categoryName = DEFAULT_MENU_CATEGORY,
                            menuList = listOf(menuData)
                        )
                    )
                }
            }
            false -> {
                val targetCategory = _menuCategoryList.value.find {
                    it.categoryName == categoryName
                } ?: return

                val updatedMenuList = targetCategory.menuList.toMutableList().apply {
                    add(menuData)
                }

                val updatedCategory = targetCategory.copy(menuList = updatedMenuList)
                currentList[currentList.indexOf(targetCategory)] = updatedCategory

                _menuCategoryList.value = currentList
            }
        }
    }

    fun deleteMenuData(menuData: OldMenuData) {
        val currentList = _menuCategoryList.value.toMutableList()

        val categoryIndex = currentList.indexOfFirst { category ->
            category.menuList.any { it.name == menuData.name }
        }

        val updatedMenuList = currentList[categoryIndex].menuList.toMutableList()

        updatedMenuList.removeAll { it.name == menuData.name }

        val updatedMenuCategory = currentList[categoryIndex].copy(menuList = updatedMenuList)
        currentList[categoryIndex] = updatedMenuCategory

        _menuCategoryList.value = currentList
    }


    fun updateMenuData(menuList: List<OldMenuData>, categoryIndex: Int) {
        val currentList = _menuCategoryList.value.toMutableList()

        val updatedMenuCategory = _menuCategoryList.value[categoryIndex].copy(menuList = menuList)
        currentList[categoryIndex] = updatedMenuCategory

        _menuCategoryList.value = currentList
    }

    fun updateMenuData(originMenuData: OldMenuData, changedMenuData: OldMenuData, originCategoryName: String, changedCategoryName: String) {
        val currentList = _menuCategoryList.value.toMutableList()

        val destination = currentList.find {
            it.categoryName == changedCategoryName
        } ?: return

        if(originCategoryName == changedCategoryName){
            val updatedMenuList = destination.menuList.toMutableList()

            val menuIndex = updatedMenuList.indexOf(originMenuData)

            updatedMenuList[menuIndex] = changedMenuData

            _menuCategoryList.value = currentList
        } else {
            deleteMenuData(originMenuData)
            addMenuData(changedMenuData, changedCategoryName)
        }
    }
}