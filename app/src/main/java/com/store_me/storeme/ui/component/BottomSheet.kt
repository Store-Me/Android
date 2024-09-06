@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp

@Composable
fun DefaultBottomSheet(hasDeleteButton: Boolean = true, onDismiss: () -> Unit, containerColor: Color = White, sheetState: SheetState, content: @Composable ColumnScope.() -> Unit) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,

        ),
        scrimColor = Color.Black.copy(0.5f),
        containerColor = containerColor,
    ) {
        if(hasDeleteButton) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                DeleteButton(onClick = {
                    onDismiss()
                })
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        content()
    }
}
