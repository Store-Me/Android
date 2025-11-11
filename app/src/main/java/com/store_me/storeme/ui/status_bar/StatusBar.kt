package com.store_me.storeme.ui.status_bar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 반투명한 Status Bar를 위한 Composable
 * @param heightProvider Status Bar의 높이를 제공하는 람다
 */
@Composable
fun StatusBarProtection(
    heightProvider: () -> Float = calculateStatusBarHeightPixel(),
) {

    Canvas(Modifier.fillMaxSize()) {
        val calculatedHeight = heightProvider()

        drawRect(
            color = Color.Black.copy(0.3f),
            size = Size(size.width, calculatedHeight),
        )
    }
}

/**
 * StatusBar 만큼의 Padding을 제공하는 Composable
 */
@Composable
fun StatusBarPadding(
    heightProvider: () -> Dp = calculateStausBarHeightDp()
) {
    val calculatedHeight = heightProvider()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(calculatedHeight)
    )
}

@Composable
fun calculateStatusBarHeightPixel(): () -> Float {
    val statusBars = WindowInsets.statusBars
    val density = LocalDensity.current
    return { statusBars.getTop(density).times(1f) }
}

@Composable
fun calculateStausBarHeightDp(): () -> Dp {
    val statusBars = WindowInsets.statusBars
    val topPaddingDp = statusBars.asPaddingValues().calculateTopPadding()

    return { topPaddingDp }
}