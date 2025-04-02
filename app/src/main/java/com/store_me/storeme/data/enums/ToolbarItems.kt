package com.store_me.storeme.data.enums

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class ToolbarItems {
    IMAGE,
    ALIGN,
    TEXT_STYLE,
    EMOJI
}

enum class TextStyleOptions {
    SIZE,
    WEIGHT,
    ITALICS,
    UNDERLINE,
    COLOR
}

enum class TextSizeOptions(val spSize: TextUnit) {
    SMALL(10.sp),
    REGULAR(12.sp),
    BIGGER(14.sp),
    BIGGEST(16.sp)
}

enum class TextColors(val color: Color) {
    Black(Color.Black),
    DarkGray(Color.DarkGray),
    Gray(Color.Gray),
    LightGray(Color.LightGray),
    Red(Color.Red),
    Green(Color.Green),
    Blue(Color.Blue),
    Yellow(Color.Yellow),
    Cyan(Color.Cyan),
    Magenta(Color.Magenta)
}