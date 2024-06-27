package com.store_me.storeme.ui.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatListScreen(
    chatListViewModel: ChatViewModel = viewModel(),
    onChatRoomClick: (String) -> Unit
){
    val chatRooms by chatListViewModel.chatRooms.collectAsState()


}