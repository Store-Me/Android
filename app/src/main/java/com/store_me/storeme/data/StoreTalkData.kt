package com.store_me.storeme.data


/**
 * 채팅방 Data Class
 * @param id 채팅방 아이디
 * @param name 채팅방 이름
 * @param lastMessage 최근 메시지 내용
 * @param timeStamp 마지막 메시지 시간
 * @param unreadCount 읽지 않은 메시지 개수
 */
data class ChatRoomWithStoreIdData(
    val id: String,
    val name: String,
    val storeId: String,
    val lastMessage: String,
    val timeStamp: String,
    val unreadCount: Int,
)

/**
 * 채팅방 Data Class
 * @param id 채팅방 아이디
 * @param name 채팅방 이름
 * @param lastMessage 최근 메시지 내용
 * @param timeStamp 마지막 메시지 시간
 * @param unreadCount 읽지 않은 메시지 개수
 */
data class ChatRoomWithStoreInfoData(
    val id: String,
    val name: String,
    val storeInfoData: StoreInfoData,
    val lastMessage: String,
    val timeStamp: String,
    val unreadCount: Int,
)

/**
 * 메시지 Data Class
 * @param id 채팅 아이디
 * @param message 메시지 내용
 * @param timestamp 전송 시간
 * @param isRead 읽음 여부
 */
data class ChatMessageData(
    val id: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean,
)

data class ChatMessagesData(
    val messages: Map<String, ChatMessageData>
)