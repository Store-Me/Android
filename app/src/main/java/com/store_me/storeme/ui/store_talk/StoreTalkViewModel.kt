package com.store_me.storeme.ui.store_talk

import androidx.lifecycle.ViewModel
import com.store_me.storeme.utils.SampleDataUtils

class StoreTalkViewModel : ViewModel() {
    val sampleChatRoomData = SampleDataUtils.sampleChatRooms()
}