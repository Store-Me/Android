package com.store_me.storeme.utils

import com.store_me.storeme.data.enums.menu.MenuPriceType
import java.text.NumberFormat
import java.util.Locale

class PriceUtils {

    fun numberToPrice(priceType: String, price: Long?, minPrice: Long?, maxPrice: Long?): String {
        fun getPriceText(price: Long?): String {
            if(price == null)
                return ""

            val formatter = NumberFormat.getNumberInstance(Locale.US)
            return formatter.format(price) + "원"
        }

        return when(priceType) {
            MenuPriceType.Fixed.name -> getPriceText(price)
            MenuPriceType.Ranged.name -> {
                val minPriceText = getPriceText(minPrice)
                val maxPriceText = getPriceText(maxPrice)

                when {
                    minPrice != null && maxPrice != null -> "$minPriceText ~ $maxPriceText"
                    minPrice == null && maxPrice != null -> "최대 $maxPriceText"
                    minPrice != null && maxPrice == null -> "최소 $minPriceText"
                    else -> { "" }
                }
            }
            MenuPriceType.Variable.name -> "변동"
            else -> { "" }
        }
    }
}