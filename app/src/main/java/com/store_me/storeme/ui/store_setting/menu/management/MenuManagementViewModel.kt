package com.store_me.storeme.ui.store_setting.menu.management

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.MenuData
import com.store_me.storeme.data.enums.menu.MenuPriceType
import com.store_me.storeme.repository.storeme.ImageRepository
import com.store_me.storeme.utils.DefaultMenuCategoryName
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.StoragePaths
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuManagementViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow(DefaultMenuCategoryName)
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _priceType = MutableStateFlow<String?>(null)
    val priceType: StateFlow<String?> = _priceType

    private val _price = MutableStateFlow<Long?>(null)
    val price: StateFlow<Long?> = _price

    private val _minPrice = MutableStateFlow<Long?>(null)
    val minPrice: StateFlow<Long?> = _minPrice

    private val _maxPrice = MutableStateFlow<Long?>(null)
    val maxPrice: StateFlow<Long?> = _maxPrice

    private val _image = MutableStateFlow<String?>(null)
    val image: StateFlow<String?> = _image

    private val _description = MutableStateFlow<String>("")
    val description: StateFlow<String> = _description

    private val _isSignature = MutableStateFlow(false)
    val isSignature: StateFlow<Boolean> = _isSignature

    private val _isPopular = MutableStateFlow(false)
    val isPopular: StateFlow<Boolean> = _isPopular

    private val _isRecommend = MutableStateFlow(false)
    val isRecommend: StateFlow<Boolean> = _isRecommend

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val _uploadProgress = MutableStateFlow<Float>(0.0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress


    fun updateSelectedCategory(selectedCategory: String) {
        _selectedCategory.value = selectedCategory
    }

    fun updateMenuData(menuData: MenuData) {
        updateMenuName(menuData.name)
        updateMenuPriceType(MenuPriceType.valueOf(menuData.priceType))
        updateMenuPrice(menuData.price, menuData.minPrice, menuData.maxPrice)
        updateMenuImage(menuData.image)
        updateMenuDescription(menuData.description ?: "")
        updateMenuSignature(menuData.isSignature)
        updateMenuPopular(menuData.isPopular)
        updateMenuRecommend(menuData.isRecommend)
    }

    fun updateMenuName(name: String) {
        _name.value = name
    }

    fun updateMenuPriceType(menuPriceType: MenuPriceType) {
        _priceType.value = menuPriceType.name
    }

    fun updateMenuPrice(price: Long?, minPrice: Long?, maxPrice: Long?) {
        _price.value = price
        _minPrice.value = minPrice
        _maxPrice.value = maxPrice
    }

    fun updateMenuImage(image: String?) {
        _image.value = image
    }

    fun updateMenuDescription(description: String) {
        _description.value = description
    }

    fun updateMenuSignature(isSignature: Boolean) {
        _isSignature.value = isSignature
    }

    fun updateMenuPopular(isPopular: Boolean) {
        _isPopular.value = isPopular
    }

    fun updateMenuRecommend(isRecommend: Boolean) {
        _isRecommend.value = isRecommend
    }

    fun updateImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun uploadStoreMenuImage(storeName: String) {
        if(imageUri.value == null)
            return

        viewModelScope.launch {
            val response = imageRepository.uploadImage(
                folderName = StoragePaths.STORE_MENU_IMAGES,
                uri = imageUri.value!!,
                uniqueName = storeName
            ) {
                _uploadProgress.value = it
            }

            response.onSuccess {
                updateMenuImage(it)
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

}