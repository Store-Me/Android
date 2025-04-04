package com.store_me.storeme.utils

import android.content.Context
import android.net.Uri

object MimeUtils {
    fun getMimeType(context: Context, uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }
}