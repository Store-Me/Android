package com.store_me.storeme.ui.home.owner.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.data.StampCouponData
import com.store_me.storeme.ui.store_setting.stamp.RewardItem
import com.store_me.storeme.ui.store_setting.stamp.StampCouponItem
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun StampTab(
    stampCoupon: StampCouponData?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        if(stampCoupon != null) {

            StampCouponItem(stampCoupon = stampCoupon)

            Spacer(modifier = Modifier.height(12.dp))

            when(stampCoupon.rewardInterval) {
                5 -> {
                    RewardItem(indexText = "보상 1",rewardText = stampCoupon.rewardFor5 ?: "올바르지 않은 값입니다.")
                    Spacer(modifier = Modifier.height(8.dp))
                    RewardItem(indexText = "보상 2",rewardText = stampCoupon.rewardFor10)
                }
                10 -> {
                    RewardItem(indexText = "보상 2",rewardText = stampCoupon.rewardFor10)
                }
            }

        } else {
            Text(
                text = "발급중인 스탬프 쿠폰이 없습니다. 스탬프를 새로 생성해보세요.",
                style = storeMeTextStyle(FontWeight.Normal, 0),
                color = GuideColor
            )
        }
    }
}