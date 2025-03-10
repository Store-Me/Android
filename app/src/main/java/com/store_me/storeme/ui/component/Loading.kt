package com.store_me.storeme.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun LoadingProgress(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress = animateFloatAsState(
        targetValue = (progress / 100),
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "progressAnimation"
    )

    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                progress = { animatedProgress.value },
                color = Color.White,
                strokeWidth = 4.dp,
                trackColor = Color.Black,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${progress.toInt()}%",
                style = storeMeTextStyle(FontWeight.Bold, 0),
                color = Color.White
            )
        }
    }
}