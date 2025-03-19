package com.store_me.storeme.utils

import kotlinx.coroutines.flow.MutableSharedFlow

object SuccessEventBus {
    val successFlow = MutableSharedFlow<String?>(replay = 0)
}