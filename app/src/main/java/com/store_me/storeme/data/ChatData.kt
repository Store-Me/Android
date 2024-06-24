package com.store_me.storeme.data

data class ChatRoomData(
    val id: String,
    val userName: String,
    val userAvatarUrl: String,
    val recentMessage: String,
    val timestamp: String,
    val unreadCount: Int
)
