package com.store_me.storeme.ui.store_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.ui.theme.storeMeTypography

@Composable
fun StoreDetailScreen(navController: NavController, storeName: String) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Store Detail Screen", style = storeMeTypography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = storeName, style = storeMeTypography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Home")
        }
    }
}