package com.store_me.storeme.utils

import com.store_me.storeme.ui.main.ALL
import com.store_me.storeme.ui.main.BEAUTY
import com.store_me.storeme.ui.main.CAFE
import com.store_me.storeme.ui.main.EXERCISE
import com.store_me.storeme.ui.main.MEDICAL
import com.store_me.storeme.ui.main.RESTAURANT
import com.store_me.storeme.ui.main.SALON

class CategoryUtils {
    companion object {
        fun getCategories(): List<String> {
            return listOf(
                ALL,
                RESTAURANT,
                CAFE,
                BEAUTY,
                MEDICAL,
                EXERCISE,
                SALON
            )
        }
    }
}