package com.store_me.storeme.utils

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.store_me.storeme.ui.theme.defaultFontSize

class SizeUtils {
    /**
     * Text의 크기에 따른 Dp 값 반환 함수
     * @param density Density
     * @param diffValue defaultFontSize 와의 차이
     * @param offset 추가 offset 값
     */
    fun textSizeToDp(density: Density, diffValue: Int, offset: Int = 0): Dp {

        return with(density) { ((defaultFontSize + diffValue).sp).toDp() } + offset.dp
    }
}