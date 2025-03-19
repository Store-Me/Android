package com.store_me.storeme.ui.home.owner

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

/**
 * 가게 배경 이미지 Composable
 * @param imageUrl 배경 이미지 url
 */
@Composable
fun BackgroundSection(modifier: Modifier = Modifier, imageUrl: String?, showCanvas: Boolean) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f / 1f)
    ) {
        imageUrl?.let {
            AsyncImage(
                model = imageUrl,
                contentDescription = "배경 이미지",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        if(showCanvas) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = Color.Black.copy(alpha = 0.3f))
            }
        }
    }
}

/**
 * 가게 배경 이미지 Composable
 * @param imageUri 배경 이미지 uri
 */
@Composable
fun BackgroundSection(modifier: Modifier = Modifier, imageUri: Uri?, showCanvas: Boolean) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f / 1f)
    ) {
        imageUri?.let {
            AsyncImage(
                model = imageUri,
                contentDescription = "배경 이미지",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        if(showCanvas) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = Color.Black.copy(alpha = 0.3f))
            }
        }
    }
}