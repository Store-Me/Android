package com.store_me.storeme.ui.home.owner.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.store_me.storeme.data.coupon.CouponData
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponInfo
import com.store_me.storeme.utils.DateTimeUtils

/**
 * 홈 화면 쿠폰 탭
 */
@Composable
fun CouponTab(
    storeInfoData: StoreInfoData,
    coupons: List<CouponData>
) {
    val filteredCoupons = coupons.filter { DateTimeUtils.isAfterDatetime(it.dueDate) }.chunked(2)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        filteredCoupons.forEach { coupon ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CouponInfo(
                    coupon = coupon.first(),
                    modifier = Modifier
                        .weight(1f),
                    storeName = storeInfoData.storeName
                )

                if(coupon.size != 1) {
                    CouponInfo(
                        coupon = coupon.last(),
                        modifier = Modifier
                            .weight(1f),
                        storeName = storeInfoData.storeName
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}