package com.store_me.storeme.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.utils.composition_locals.onboarding.LocalOnboardingComposition

@Composable
fun OnboardingScreen(navController: NavController) {
    val composition = LocalOnboardingComposition.current
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition
        )
    }

    Scaffold(
        containerColor = Color.White,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(120.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_color),
                    contentDescription = "로고",
                    modifier = Modifier
                        .size(150.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.logo_home),
                    contentDescription = "로고 텍스트",
                    modifier = Modifier
                        .width(150.dp)
                )

                Spacer(modifier = Modifier.height(180.dp))

                DefaultButton(
                    buttonText = "시작하기",
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                ) {
                    navController.navigate(OnboardingActivity.Screen.Login.route.name)
                }

                Spacer(modifier = Modifier.weight(1f))


                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    speed = 0.3f,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    )
}