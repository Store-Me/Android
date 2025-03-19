package com.store_me.storeme.data.request.store

import com.store_me.storeme.data.store.BusinessHourData

data class PatchBusinessHoursRequest(
    val businessHours: List<BusinessHourData>
)
