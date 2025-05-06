package com.store_me.storeme.utils

import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import com.store_me.storeme.ui.theme.PostBackgroundColor0
import com.store_me.storeme.ui.theme.PostBackgroundColor1
import com.store_me.storeme.ui.theme.PostBackgroundColor2
import com.store_me.storeme.ui.theme.PostBackgroundColor3
import com.store_me.storeme.ui.theme.PostBackgroundColor4
import com.store_me.storeme.ui.theme.PostBackgroundColor5
import com.store_me.storeme.ui.theme.PostBackgroundColor6
import com.store_me.storeme.ui.theme.PostBackgroundColor7
import com.store_me.storeme.ui.theme.PostBackgroundColor8
import com.store_me.storeme.ui.theme.PostBackgroundColor9

object PostBackgroundUtils {

    /**
     * 게시글 배경 색 반환 함수
     * 생성 시간에 따른 배경 색을 반환
     * @param timestamp 생성 시간
     * @return 배경 색
     */
    fun getPostBackgroundColor(timestamp: Timestamp): Color {
        return when(timestamp.seconds % 10) {
            0L -> PostBackgroundColor0
            1L -> PostBackgroundColor1
            2L -> PostBackgroundColor2
            3L -> PostBackgroundColor3
            4L -> PostBackgroundColor4
            5L -> PostBackgroundColor5
            6L -> PostBackgroundColor6
            7L -> PostBackgroundColor7
            8L -> PostBackgroundColor8
            9L -> PostBackgroundColor9
            else -> PostBackgroundColor0
        }
    }
}