package com.store_me.storeme.data.request.store_image

data class StoreImageOrderRequest(
    val storeId: Long,
    val storeImageOrderInfoList: List<StoreImageOrderInfo>
)

data class StoreImageOrderInfo(
    val storeImageId: Long,
    val storeImageOrder: Int
)
