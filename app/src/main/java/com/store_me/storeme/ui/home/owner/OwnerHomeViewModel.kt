package com.store_me.storeme.ui.home.owner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.StoreHomeItemData
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.SampleDataUtils.Companion.sampleStoreHomeItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OwnerHomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    val storeData = SampleDataUtils.sampleDetailData

    init {
        getStoreData()
    }

    private fun getStoreData() {
        //TODO 값 가져오기

        Auth.setLinkListData(storeData.socialMediaAccountData)
        Auth.setStoreHomeItemData(sampleStoreHomeItemData())
    }

    enum class OwnerHomeTabMenu(val displayName: String) {
        HOME("스토어 홈"),
        NEWS("소식"),
    }

    //Store Phone Number Copy 함수
    fun copyToClipboard(){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(storeData.storeInfo.storeName, storeData.storePhoneNumber)
        clipboard.setPrimaryClip(clip)
    }

    //빈 항목 Text
    fun getEmptySectionText(storeHomeItem: StoreHomeItem): String {
        return when(storeHomeItem) {
            StoreHomeItem.NOTICE -> { "가게의 공지사항을 입력해주세요." }
            StoreHomeItem.INTRO -> { "가게의 소개를 입력해주세요." }
            StoreHomeItem.PHOTO -> { "대표 사진을 업로드하여 메뉴나 서비스를 홍보하세요." }
            StoreHomeItem.COUPON -> { "쿠폰을 만들어 가게 홍보를 진행하세요." }
            StoreHomeItem.MENU -> { "가게의 서비스나 메뉴 가격을 등록해 보세요." }
            StoreHomeItem.STORY -> { "가게와 관련된 짧은 영상을 올려 보세요." }
            StoreHomeItem.REVIEW -> { "아직 가게 후기가 없어요." }
            StoreHomeItem.NEWS -> { "가게의 소식을 작성하여 홍보를 진행하세요." }
        }
    }

    //편집 버튼 Text
    fun getEditButtonText(storeHomeItem: StoreHomeItem, isEmpty: Boolean = true): String {
        return when(storeHomeItem) {
            StoreHomeItem.NOTICE -> { if (isEmpty) "공지사항 작성" else "공지사항 편집"}
            StoreHomeItem.INTRO -> { if (isEmpty) "소개 내용 작성" else "소개 내용 편집"}
            StoreHomeItem.PHOTO -> { "사진 업로드"}
            StoreHomeItem.COUPON -> { "쿠폰 관리" }
            StoreHomeItem.MENU -> { "메뉴 관리" }
            StoreHomeItem.STORY -> { "스토리 업로드" }
            StoreHomeItem.REVIEW -> { "리뷰 관리"}
            StoreHomeItem.NEWS -> { "소식 작성"}
        }
    }
}