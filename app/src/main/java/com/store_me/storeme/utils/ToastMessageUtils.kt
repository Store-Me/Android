package com.store_me.storeme.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat.getString

object ToastMessageUtils {
    private var toast: Toast? = null

    fun showToast(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun showToast(context: Context, message: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, getString(context, message), Toast.LENGTH_SHORT)
        toast?.show()
    }
}