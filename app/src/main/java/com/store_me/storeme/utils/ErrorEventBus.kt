package com.store_me.storeme.utils

import kotlinx.coroutines.flow.MutableSharedFlow

object ErrorEventBus {
    val errorFlow = MutableSharedFlow<String?>(replay = 0)
}