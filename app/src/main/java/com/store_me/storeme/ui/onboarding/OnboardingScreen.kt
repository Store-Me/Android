package com.store_me.storeme.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.SmallButton

@Composable
fun OnboardingScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(240.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_color),
                    contentDescription = "로고",
                    modifier = Modifier
                        .size(240.dp)
                )

                Spacer(modifier = Modifier.height(120.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_home),
                    contentDescription = "로고 텍스트",
                    modifier = Modifier
                        .width(240.dp)
                )

                Spacer(modifier = Modifier.height(360.dp))


                SmallButton(
                    text = "시작하기",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 20.dp),
                    containerColor = Color.Black,
                    contentColor = Color.White
                ) {
                    navController.navigate(OnboardingActivity.Screen.Login.route.name)
                }

            }
        }
    )
}