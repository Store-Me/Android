package com.store_me.storeme.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.store_me.storeme.R

// Set of Material typography styles to start with
val appFontFamily = FontFamily(
    Font(R.font.nanum_square_neo_rg, weight = FontWeight.Normal),
    Font(R.font.nanum_square_neo_bd, weight = FontWeight.Bold),
    Font(R.font.nanum_square_neo_ebd, weight = FontWeight.ExtraBold),
    Font(R.font.nanum_square_neo_hv, weight = FontWeight.Black)
)

//기본 글자 크기
const val defaultFontSize = 12

/**
 * TextStyle 반환 함수
 * @param fontWeight FontWeight
 * @param changeSizeValue fontSize 변화량
 */
fun storeMeTextStyle(fontWeight: FontWeight, changeSizeValue: Int, color: Color = Color.Unspecified): TextStyle {
    val fontSize = defaultFontSize + changeSizeValue

    return TextStyle(
        fontWeight = fontWeight,
        fontFamily = appFontFamily,
        fontSize = fontSize.sp,
        letterSpacing = 0.7.sp,
        lineHeight = (fontSize * 1.4f).sp,
        color = color
    )
}

val storeMeTypography = Typography(
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = appFontFamily,
        fontSize = 18.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),

    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = appFontFamily,
        fontSize = 12.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),

    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = appFontFamily,
        fontSize = 16.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),

    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = appFontFamily,
        fontSize = 18.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),

    titleSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = appFontFamily,
        fontSize = 12.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),

    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = appFontFamily,
        fontSize = 16.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),

    labelLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontFamily = appFontFamily,
        fontSize = 18.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),

    labelSmall = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontFamily = appFontFamily,
        fontSize = 12.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),

    labelMedium = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontFamily = appFontFamily,
        fontSize = 16.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp
    ),
)