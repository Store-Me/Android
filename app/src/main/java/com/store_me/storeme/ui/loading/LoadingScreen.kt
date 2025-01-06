package com.store_me.storeme.ui.loading

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel

/**
 * 로딩 화면
 */
@Composable
fun LoadingScreen() {
    val loadingViewModel = LocalLoadingViewModel.current

    Dialog(
        onDismissRequest = {
            loadingViewModel.hideLoading()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        CircularProgressIndicator(
            color = Color.White
        )
    }
}