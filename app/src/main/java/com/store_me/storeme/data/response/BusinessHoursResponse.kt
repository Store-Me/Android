package com.store_me.storeme.data.response

import com.store_me.storeme.data.store.BusinessHourData

data class BusinessHoursResponse(
    val businessHours : List<BusinessHourData>? = emptyList()
)
