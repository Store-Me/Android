package com.store_me.storeme.data.response

data class MyStoresResponse(
    val stores: List<MyStore> = emptyList()
)

data class MyStore(
    val storeId: String,
    val storeName: String,
    val storeProfileImage: String?
)
