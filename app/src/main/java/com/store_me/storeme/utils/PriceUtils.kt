package com.store_me.storeme.utils

import java.text.NumberFormat
import java.util.Locale

class PriceUtils {
    fun numberToPrice(price: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        return formatter.format(price) + "원"
    }
}