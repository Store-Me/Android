package com.store_me.storeme.data.request.store

data class PatchStoreLocationRequest(
    val storeId: String,
    val storeLocationAddress: String,
    val storeLocationDetail: String?,
    val storeLocationCode: Long,
    val storeLocation: String,
    val storeLat: Double?,
    val storeLng: Double?
)
