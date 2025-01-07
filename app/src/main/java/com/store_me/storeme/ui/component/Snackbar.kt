package com.store_me.storeme.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun StoreMeSnackbar(snackbarData: SnackbarData) {
    Snackbar(
        /*snackbarData = snackbarData,
        contentColor = Color.White,
        backgroundColor = Color(0xFF323232),
        shape = RoundedCornerShape(8.dp), */
        action = {
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { snackbarData.performAction() }) {
                    Text(
                        text = actionLabel,
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                    )
                }
            }
        },
        modifier = Modifier
            .padding(
                start = 12.dp,
                end = 12.dp,
                bottom = 12.dp
            )
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = storeMeTextStyle(FontWeight.Bold, 2)
        )
    }
}