package com.store_me.storeme.ui.near_place

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.NearPlaceStoreWithStoreInfoData
import com.store_me.storeme.ui.mystore.CategoryViewModel
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.SampleDataUtils.Companion.sampleNearPlaceStoreWithStoreInfoData
import com.store_me.storeme.utils.StoreCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearPlaceViewModel @Inject constructor(): ViewModel() {
    private val allStores: MutableList<NearPlaceStoreWithStoreInfoData> = mutableListOf()

    private val restaurantStores: MutableList<NearPlaceStoreWithStoreInfoData> = mutableListOf()
    private val cafeStores: MutableList<NearPlaceStoreWithStoreInfoData> = mutableListOf()
    private val beautyStores: MutableList<NearPlaceStoreWithStoreInfoData> = mutableListOf()
    private val medicalStores: MutableList<NearPlaceStoreWithStoreInfoData> = mutableListOf()
    private val exerciseStores: MutableList<NearPlaceStoreWithStoreInfoData> = mutableListOf()
    private val salonStores: MutableList<NearPlaceStoreWithStoreInfoData> = mutableListOf()

    private val _currentStores = MutableStateFlow<List<NearPlaceStoreWithStoreInfoData>>(emptyList())
    val currentStores: StateFlow<List<NearPlaceStoreWithStoreInfoData>> = _currentStores

    init{
        getStoreList()
    }

    fun observeCategoryChanges(categoryViewModel: CategoryViewModel) {
        viewModelScope.launch {
            categoryViewModel.selectedCategory.collectLatest { category ->
                Log.d("NearPlaceViewModel", "현재 카테고리는 ${category.displayName}")
                updateCurrentStores(category)
            }
        }
    }

    private fun updateCurrentStores(category: StoreCategory) {
        _currentStores.value = when(category) {
            StoreCategory.ALL -> { allStores }
            StoreCategory.RESTAURANT -> { restaurantStores }
            StoreCategory.CAFE -> { cafeStores }
            StoreCategory.BEAUTY -> { beautyStores }
            StoreCategory.MEDICAL -> { medicalStores }
            StoreCategory.EXERCISE -> { exerciseStores }
            StoreCategory.SALON -> { salonStores }
        }
    }


    fun getStoreList(){
        //TODO 리스트 받기

        allStores.clear()
        allStores.addAll(sampleNearPlaceStoreWithStoreInfoData())

        restaurantStores.addAll(allStores.filter { it.storeInfoList.any { storeInfo -> storeInfo.category == StoreCategory.RESTAURANT } })
        cafeStores.addAll(allStores.filter { it.storeInfoList.any { storeInfo -> storeInfo.category == StoreCategory.CAFE } })
        beautyStores.addAll(allStores.filter { it.storeInfoList.any { storeInfo -> storeInfo.category == StoreCategory.BEAUTY } })
        medicalStores.addAll(allStores.filter { it.storeInfoList.any { storeInfo -> storeInfo.category == StoreCategory.MEDICAL } })
        exerciseStores.addAll(allStores.filter { it.storeInfoList.any { storeInfo -> storeInfo.category == StoreCategory.EXERCISE } })
        salonStores.addAll(allStores.filter { it.storeInfoList.any { storeInfo -> storeInfo.category == StoreCategory.SALON } })
    }

}