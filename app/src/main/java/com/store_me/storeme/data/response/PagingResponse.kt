package com.store_me.storeme.data.response

data class PagingResponse<T>(
    val result: T,
    val pagination: PaginationData
)
