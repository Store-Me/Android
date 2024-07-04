package com.store_me.storeme.data

data class NotificationData(
    val type: String,
    val datetime: String,
    val storeImage: String,
    val content: String,
    var isRead: Boolean
)
