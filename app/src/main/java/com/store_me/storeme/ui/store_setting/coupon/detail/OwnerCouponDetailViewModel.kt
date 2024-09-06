package com.store_me.storeme.ui.store_setting.coupon.detail

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.CouponWithUserData
import com.store_me.storeme.data.UserData
import com.store_me.storeme.data.UserReceivedCouponData
import com.store_me.storeme.data.UserUsedCouponData

class OwnerCouponDetailViewModel: ViewModel() {
    enum class CouponDetailTabTitles(val displayName: String){
        RECEIVED("받은 손님"), USED("사용한 손님")
    }

    val sampleCouponWithUserData = CouponWithUserData(
        couponId = "",
        userReceivedCouponList = listOf(
            UserReceivedCouponData(UserData("테스트 사용자1", "테스트 사용자1", "https://via.placeholder.com/200x200")),
            UserReceivedCouponData(UserData("테스트 사용자2", "테스트 사용자2", "")),
            UserReceivedCouponData(UserData("테스트 사용자3", "테스트 사용자3", "https://via.placeholder.com/200x200")),
            UserReceivedCouponData(UserData("테스트 사용자4", "테스트 사용자4", "")),
            UserReceivedCouponData(UserData("테스트 사용자5", "테스트 사용자5", "https://via.placeholder.com/200x200")),
            UserReceivedCouponData(UserData("테스트 사용자6", "테스트 사용자6", "")),
            UserReceivedCouponData(UserData("테스트 사용자7", "테스트 사용자7", "https://via.placeholder.com/200x200")),
            UserReceivedCouponData(UserData("테스트 사용자8", "테스트 사용자8", "")),
            UserReceivedCouponData(UserData("테스트 사용자9", "테스트 사용자9", "https://via.placeholder.com/200x200")),
            UserReceivedCouponData(UserData("테스트 사용자10", "테스트 사용자10", "")),
            UserReceivedCouponData(UserData("테스트 사용자11", "테스트 사용자11", "https://via.placeholder.com/200x200")),
            UserReceivedCouponData(UserData("테스트 사용자12", "테스트 사용자12", "")),
            UserReceivedCouponData(UserData("테스트 사용자13", "테스트 사용자13", "https://via.placeholder.com/200x200")),
            UserReceivedCouponData(UserData("테스트 사용자14", "테스트 사용자14", "")),
            UserReceivedCouponData(UserData("테스트 사용자15", "테스트 사용자15", "https://via.placeholder.com/200x200")),
            UserReceivedCouponData(UserData("테스트 사용자16", "테스트 사용자16", "")),
        ),
        userUsedCouponList = listOf(
            UserUsedCouponData(UserData("테스트 사용자1", "테스트 사용자1", "https://via.placeholder.com/200x200"), "2024-09-01T17:25:00"),
            UserUsedCouponData(UserData("테스트 사용자2", "테스트 사용자2", ""), "2024-09-01T18:25:00"),
            UserUsedCouponData(UserData("테스트 사용자3", "테스트 사용자3", "https://via.placeholder.com/200x200"), "2024-09-01T19:25:00"),
            UserUsedCouponData(UserData("테스트 사용자4", "테스트 사용자4", ""), "2024-09-01T20:25:00"),
            UserUsedCouponData(UserData("테스트 사용자5", "테스트 사용자5", "https://via.placeholder.com/200x200"), "2024-09-01T21:25:00"),
            UserUsedCouponData(UserData("테스트 사용자6", "테스트 사용자6", ""), "2024-09-01T22:25:00"),
            UserUsedCouponData(UserData("테스트 사용자7", "테스트 사용자7", "https://via.placeholder.com/200x200"), "2024-09-01T23:25:00"),
            UserUsedCouponData(UserData("테스트 사용자8", "테스트 사용자8", ""), "2024-09-02T17:25:00"),
            UserUsedCouponData(UserData("테스트 사용자9", "테스트 사용자9", "https://via.placeholder.com/200x200"), "2024-09-03T17:25:00"),
            UserUsedCouponData(UserData("테스트 사용자10", "테스트 사용자10", ""), "2024-09-04T17:25:00"),
            UserUsedCouponData(UserData("테스트 사용자11", "테스트 사용자11", "https://via.placeholder.com/200x200"), "2024-09-05T17:25:00"),
            UserUsedCouponData(UserData("테스트 사용자12", "테스트 사용자12", ""), "2024-09-06T07:25:00"),
            UserUsedCouponData(UserData("테스트 사용자13", "테스트 사용자13", "https://via.placeholder.com/200x200"), "2024-09-06T07:26:00"),
            UserUsedCouponData(UserData("테스트 사용자14", "테스트 사용자14", ""), "2024-09-06T07:27:00"),
            UserUsedCouponData(UserData("테스트 사용자15", "테스트 사용자15", "https://via.placeholder.com/200x200"), "2024-09-06T07:28:00"),
        )
    )
}