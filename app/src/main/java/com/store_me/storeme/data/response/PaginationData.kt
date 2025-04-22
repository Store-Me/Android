package com.store_me.storeme.data.response

import com.google.firebase.Timestamp

data class PaginationData(
    val hasNextPage: Boolean,
    val lastCreatedAt: Timestamp?,
)
