package com.store_me.storeme.ui.my_menu

import androidx.lifecycle.ViewModel
import com.store_me.storeme.R
import com.store_me.storeme.data.UserData
import com.store_me.storeme.repository.storeme.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyMenuViewModel @Inject constructor(
    val userRepository: UserRepository
): ViewModel() {
    val userData: StateFlow<UserData> = MutableStateFlow(UserData("도구리", "도구리야", "https://i.namu.wiki/i/vJ_iVx2uAFkYUmfaxSwP0QSDbPjRz-OzilacQpDBLQmls9oOM0pV4qUk8mCbgL41v4_wGV-kdotau0LIpZu261XmIpWq0qLg3gKfSSBg78Px_EGRyNlmZk6d5N6KKx6zgsZArniJ3t2cwmB4IvS-0A.webp"))

    enum class MyProfileMenuItem(val displayName: String, val icon: Int){
        FAVORITE_STORE("관심 가게", R.drawable.ic_fill_like),
        MY_COUPON("받은 쿠폰함", R.drawable.ic_coupon),
        LIKE_LIST("관심 목록", R.drawable.ic_my_menu_list)
    }

    enum class MyMenuItem(val displayName: String, val icon: Int){
        PURCHASE_HISTORY("구매 내역", R.drawable.ic_my_menu_buy),
        RESERVATION_HISTORY("예약 내역", R.drawable.ic_my_menu_reservation),

        EVENT("이벤트", R.drawable.ic_my_menu_event),
        NOTICE("공지사항", R.drawable.ic_my_menu_notice)
    }

    enum class MyMenuNormalItem(val displayName: String){
        FQA("자주 묻는 질문"),
        INQUIRY("스토어미에 문의하기"),
        POLICY("약관 및 정책"),
        LOGOUT("로그아웃"),
        QUIT("탈퇴하기")
    }
}