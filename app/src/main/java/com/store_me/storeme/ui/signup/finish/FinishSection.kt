package com.store_me.storeme.ui.signup.finish

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun FinishSection(onFinish: () -> Unit) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.signup_finish_animation)
    )
    val lottieAnimatable = rememberLottieAnimatable()
    
    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {


        Box {

            Image(
                painter = painterResource(id = R.drawable.signup_finish_image),
                contentDescription = "가입 완료 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "스토어미\n가입이 완료되었습니다!",
            style = storeMeTextStyle(FontWeight.ExtraBold, 10)
        )

        Spacer(modifier = Modifier.height(36.dp))

        DefaultButton(buttonText = "확인") {
            onFinish()
        }
    }
}