package com.store_me.storeme.ui.store_setting.menu.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.DEFAULT_MENU_CATEGORY
import com.store_me.storeme.data.MenuData
import com.store_me.storeme.data.MenuPrice
import com.store_me.storeme.data.MenuPriceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddMenuViewModel: ViewModel() {
    enum class MenuHighLightType(val displayName: String) {
        SIGNATURE("대표 메뉴"), POPULAR("인기 메뉴"), RECOMMEND("사장님 추천")
    }

    private val _selectedCategory = MutableStateFlow(DEFAULT_MENU_CATEGORY)
    val selectedCategory: StateFlow<String> = _selectedCategory

    fun updateSelectedCategory(selectedCategory: String) {
        _selectedCategory.value = selectedCategory
    }

    private var originCategory: String = ""
    var originMenuData: MenuData = MenuData("", MenuPrice.Variable, "", "",
        isSignature = false,
        isPopular = false,
        isRecommend = false
    )

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    fun updateName(newName: String) {
        _name.value = newName
    }

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    fun updateDescription(newDescription: String){
        _description.value = newDescription
    }

    private val _isSignature = MutableStateFlow(false)
    val isSignature: StateFlow<Boolean> = _isSignature

    fun updateIsSignature(value: Boolean? = null) {
        if(value == null)
            _isSignature.value = !_isSignature.value
        else
            _isSignature.value = value
    }

    private val _isPopular = MutableStateFlow(false)
    val isPopular: StateFlow<Boolean> = _isPopular

    fun updateIsPopular(value: Boolean? = null) {
        if(value == null)
            _isPopular.value = !_isPopular.value
        else
            _isPopular.value = value
    }

    private val _isRecommend = MutableStateFlow(false)
    val isRecommend: StateFlow<Boolean> = _isRecommend

    fun updateIsRecommend(value: Boolean? = null) {
        if(value == null)
            _isRecommend.value = !_isRecommend.value
        else
            _isRecommend.value = value
    }

    /**
     * 가격 타입 설정
     */
    private val _menuPriceType = MutableStateFlow<MenuPriceType?>(null)
    val menuPriceType: StateFlow<MenuPriceType?> = _menuPriceType

    fun updateMenuPriceType(newMenuPriceType: MenuPriceType) {
        _menuPriceType.value = newMenuPriceType
    }

    private val _fixedPrice = MutableStateFlow<Int?>(null)
    val fixedPrice: StateFlow<Int?> = _fixedPrice

    fun updateFixedPrice(newFixedPrice: Int?) {
        _fixedPrice.value = newFixedPrice
    }

    private val _rangeMinPrice = MutableStateFlow<Int?>(null)
    val rangeMinPrice: StateFlow<Int?> = _rangeMinPrice

    fun updateRangeMinPrice(newRangePrice: Int?) {
        _rangeMinPrice.value = newRangePrice
    }

    private val _rangeMaxPrice = MutableStateFlow<Int?>(null)
    val rangeMaxPrice: StateFlow<Int?> = _rangeMaxPrice

    fun updateRangeMaxPrice(newRangePrice: Int?) {
        _rangeMaxPrice.value = newRangePrice
    }

    private val _imageUrl = MutableStateFlow<String>("")
    val imageUrl: StateFlow<String> = _imageUrl

    fun updateImageUrl(imageUrl: String) {
        _imageUrl.value = imageUrl
    }

    //ERROR
    private val _isNameError = MutableStateFlow(false)
    val isNameError: StateFlow<Boolean> = _isNameError

    fun updateNameError(isError: Boolean) {
        _isNameError.value = isError
    }

    private val _isFixedError = MutableStateFlow(false)
    val isFixedError: StateFlow<Boolean> = _isFixedError

    fun updateFixedError(isError: Boolean) {
        _isFixedError.value = isError
    }

    private val _isMinRangeError = MutableStateFlow(false)
    val isMinRangeError: StateFlow<Boolean> = _isMinRangeError

    fun updateMinRangeError(isError: Boolean) {
        _isMinRangeError.value = isError
    }

    private val _isMaxRangeError = MutableStateFlow(false)
    val isMaxRangeError: StateFlow<Boolean> = _isMaxRangeError

    fun updateMaxRangeError(isError: Boolean) {
        _isMaxRangeError.value = isError
    }

    private val _isDescriptionError = MutableStateFlow(false)
    val isDescriptionError: StateFlow<Boolean> = _isDescriptionError

    fun updateDescriptionError(isError: Boolean) {
        _isDescriptionError.value = isError
    }

    fun addMenuData() {
        Auth.addMenuData(
            menuData = MenuData(
                name = name.value,
                price = when(menuPriceType.value) {
                    MenuPriceType.FIXED -> {
                        MenuPrice.Fixed(price = fixedPrice.value!!)
                    }
                    MenuPriceType.RANGE -> {
                        MenuPrice.Range(
                            minPrice = rangeMinPrice.value,
                            maxPrice = rangeMaxPrice.value
                        )
                    }
                    MenuPriceType.VARIABLE -> {
                        MenuPrice.Variable
                    }
                    else -> {
                        MenuPrice.Variable
                    }
                },
                imageUrl = "",
                description = description.value,
                isSignature = isSignature.value,
                isPopular = isPopular.value,
                isRecommend = isRecommend.value,
            ),
            categoryName = selectedCategory.value
        )
    }

    fun getMenuData(selectedMenuName: String){
        val (categoryName, menuData) = Auth.menuCategoryList.value.firstNotNullOfOrNull { menuCategory ->
            menuCategory.menuList.find { it.name == selectedMenuName }?.let { menuData ->
                menuCategory.categoryName to menuData
            }
        } ?: return

        originMenuData = menuData
        originCategory = categoryName

        updateSelectedCategory(categoryName)

        updateName(menuData.name)
        when(menuData.price) {
            is MenuPrice.Fixed -> {
                updateMenuPriceType(MenuPriceType.FIXED)
                updateFixedPrice(menuData.price.price)
            }
            is MenuPrice.Range -> {
                updateMenuPriceType(MenuPriceType.FIXED)
                updateRangeMinPrice(menuData.price.minPrice)
                updateRangeMaxPrice(menuData.price.maxPrice)
            }
            is MenuPrice.Variable -> {
                updateMenuPriceType(MenuPriceType.VARIABLE)
            }
        }
        updateImageUrl(menuData.imageUrl)
        updateDescription(menuData.description)

        updateIsSignature(menuData.isSignature)
        updateIsPopular(menuData.isPopular)
        updateIsRecommend(menuData.isRecommend)
    }

    fun updateMenuData() {
        Auth.updateMenuData(
            changedMenuData = MenuData(
                name = name.value,
                price = when(menuPriceType.value) {
                    MenuPriceType.FIXED -> {
                        MenuPrice.Fixed(price = fixedPrice.value!!)
                    }
                    MenuPriceType.RANGE -> {
                        MenuPrice.Range(
                            minPrice = rangeMinPrice.value,
                            maxPrice = rangeMaxPrice.value
                        )
                    }
                    MenuPriceType.VARIABLE -> {
                        MenuPrice.Variable
                    }
                    else -> {
                        MenuPrice.Variable
                    }
                },
                imageUrl = "",
                description = description.value,
                isSignature = isSignature.value,
                isPopular = isPopular.value,
                isRecommend = isRecommend.value,
            ),
            originMenuData = originMenuData,
            originCategoryName = originCategory,
            changedCategoryName = selectedCategory.value
        )
    }
}