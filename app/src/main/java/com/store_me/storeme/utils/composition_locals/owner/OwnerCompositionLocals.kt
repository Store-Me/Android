package com.store_me.storeme.utils.composition_locals.owner

import androidx.compose.runtime.staticCompositionLocalOf
import com.store_me.storeme.ui.home.owner.StoreDataViewModel


val LocalStoreDataViewModel = staticCompositionLocalOf<StoreDataViewModel> {
    error("No StoreDataViewModel Provided")
}