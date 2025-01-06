package com.store_me.storeme.data.response

data class StoreListResponse(
    val storeInfoList: List<StoreShortInfo>
)

data class StoreShortInfo(
    val storeId: Long,
    val storeName: String,
    val storeProfileImageUrl: String?,
)