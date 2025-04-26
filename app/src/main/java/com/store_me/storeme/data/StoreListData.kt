package com.store_me.storeme.data

import com.store_me.storeme.data.enums.StoreCategory

data class NearPlaceStoreWithStoreIdData(
    val title: String,
    val category: StoreCategory,
    val storeIdList: List<String>,
)

data class NearPlaceStoreWithStoreInfoData(
    val title: String,
    val category: StoreCategory,
    val storeInfoList: List<StoreInfoData>,
    val storePromotionList: List<StorePromotionData>
)