package com.store_me.storeme.utils.composition_locals

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.store_me.auth.Auth

val LocalAuth = staticCompositionLocalOf<Auth> {
    error("No Auth Provided")
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}