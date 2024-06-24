package com.store_me.storeme.ui.chat

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.ChatRoomData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatListViewModel : ViewModel() {
    private val _chatRooms = MutableStateFlow<List<ChatRoomData>>(emptyList())
    val chatRooms: StateFlow<List<ChatRoomData>> = _chatRooms
}