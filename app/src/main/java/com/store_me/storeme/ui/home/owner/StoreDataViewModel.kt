package com.store_me.storeme.ui.home.owner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.auth.Auth
import com.store_me.storeme.R
import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.StoreData
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.repository.storeme.OwnerRepository
import com.store_me.storeme.utils.exception.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreDataViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: Auth,
    private val ownerRepository: OwnerRepository
): ViewModel() {
    private val _storeData = MutableStateFlow<StoreData?>(null)
    val storeData: StateFlow<StoreData?> = _storeData

    val storeId = auth.storeId

    private val _menuCategoryList = MutableStateFlow<List<MenuCategoryData>>(emptyList())
    val menuCategoryList: StateFlow<List<MenuCategoryData>> = _menuCategoryList

    //오류 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        storeId.value?.let {
            getStoreData(it)
        }
    }

    /**
     * StoreData 조회 함수
     */
    fun getStoreData(storeId: String) {
        /*viewModelScope.launch {
            val response = ownerRepository.getStoreData(storeId = storeId)

            response.onSuccess {
                updateStoreData(it)

                getMenuCategory(storeId)
            }.onFailure {
                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }*/
    }

    /**
     * StoreData 갱신 함수
     */
    fun updateStoreData(storeData: StoreData) {
        _storeData.value = storeData
    }

    /**
     * MenuCategory 조회 함수
     */
    fun getMenuCategory(storeId: Long) {
        viewModelScope.launch {
            val response = ownerRepository.getMenuCategory(storeId = storeId)

            response.onSuccess {
                updateMenuCategoryList(it.storeMenuCategoryInfoList)
            }.onFailure {
                if(it is ApiException) {
                    _errorMessage.value = it.message
                } else {
                    _errorMessage.value = context.getString(R.string.unknown_error_message)
                }
            }
        }
    }

    fun updateMenuCategoryList(menuCategoryList: List<MenuCategoryData>) {
        _menuCategoryList.value = menuCategoryList
    }

    //Store Phone Number Copy 함수
    fun copyToClipboard(storePhoneNumber: String){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(storeData.value?.storeName, storePhoneNumber)
        clipboard.setPrimaryClip(clip)
    }

    //편집 버튼 Text
    fun getEditButtonText(storeHomeItem: StoreHomeItem, isEmpty: Boolean = true): String {
        return when(storeHomeItem) {
            StoreHomeItem.NOTICE -> { if (isEmpty) "공지사항 작성" else "공지사항 편집"}
            StoreHomeItem.INTRO -> { if (isEmpty) "소개 내용 작성" else "소개 내용 편집"}
            StoreHomeItem.IMAGE -> { "사진 업로드"}
            StoreHomeItem.COUPON -> { "쿠폰 관리" }
            StoreHomeItem.MENU -> { "메뉴 관리" }
            StoreHomeItem.STORY -> { "스토리 업로드" }
            StoreHomeItem.REVIEW -> { "리뷰 관리"}
            StoreHomeItem.NEWS -> { "소식 작성"}
        }
    }
}