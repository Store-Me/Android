package com.store_me.storeme.utils.composition_locals.loading

import androidx.compose.runtime.staticCompositionLocalOf
import com.store_me.storeme.ui.loading.LoadingViewModel

val LocalLoadingViewModel = staticCompositionLocalOf<LoadingViewModel> {
    error("No LoadingViewModel provided")
}