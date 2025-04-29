package com.store_me.storeme.data.response

import com.store_me.storeme.data.store.coupon.CouponUserData

data class CouponUsersResponse(
    val receivedUsers: List<CouponUserData>,
    val usedUsers: List<CouponUserData>
)