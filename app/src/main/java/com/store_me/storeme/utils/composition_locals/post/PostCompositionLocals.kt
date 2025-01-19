package com.store_me.storeme.utils.composition_locals.post

import androidx.compose.runtime.staticCompositionLocalOf
import com.store_me.storeme.ui.post.add.AddPostViewModel


val LocalAddPostViewModel = staticCompositionLocalOf<AddPostViewModel> {
    error("No AddPostViewModel Provided")
}