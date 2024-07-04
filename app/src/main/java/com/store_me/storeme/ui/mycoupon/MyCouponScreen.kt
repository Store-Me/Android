package com.store_me.storeme.ui.mycoupon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.store_me.storeme.ui.theme.storeMeTypography

@Composable
fun MyCouponScreen(){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "My Coupon Screen", style = storeMeTypography.labelLarge)
    }
}