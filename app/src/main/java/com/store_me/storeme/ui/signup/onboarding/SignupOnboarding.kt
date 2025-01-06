@file:OptIn(ExperimentalPagerApi::class)

package com.store_me.storeme.ui.signup.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.signup.NextButton
import com.store_me.storeme.ui.theme.OnboardingSelectedIndicatorColor
import com.store_me.storeme.ui.theme.OnboardingUnselectedIndicatorColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalSignupOnboardingViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalSignupViewModel
import kotlinx.coroutines.launch

@Composable
fun SignupOnboardingSection(onFinish: () -> Unit) {
    val signupViewModel = LocalSignupViewModel.current
    val signupOnboardingViewModel = LocalSignupOnboardingViewModel.current

    val accountType by signupViewModel.accountType.collectAsState()

    val onboardingImageList = signupOnboardingViewModel.getOnboardingImageList(accountType ?: Auth.AccountType.CUSTOMER)
    val onboardingTitleList = signupOnboardingViewModel.getOnboardingTitleList(accountType ?: Auth.AccountType.CUSTOMER)
    val onboardingContentList = signupOnboardingViewModel.getOnboardingContentList(accountType ?: Auth.AccountType.CUSTOMER)

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        HorizontalPager(
            count = onboardingImageList.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            Column(

            ) {
                Image(
                    painter = painterResource(id = onboardingImageList[page]),
                    contentDescription = "$page 번째 온보딩 이미지",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .height(240.dp)
                ) {
                    Text(
                        text = onboardingTitleList[page],
                        style = storeMeTextStyle(FontWeight.ExtraBold, 10),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = onboardingContentList[page],
                        style = storeMeTextStyle(FontWeight.Normal, 5),
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(48.dp))

            repeat(onboardingImageList.size) { index ->
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                        .background(
                            color = if (index == pagerState.currentPage) OnboardingSelectedIndicatorColor else OnboardingUnselectedIndicatorColor,
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.width(48.dp))

            NextButton(
                buttonText = "다음"
            ) {
                if(pagerState.currentPage == onboardingTitleList.lastIndex)
                    onFinish()
                else {
                    coroutineScope.launch {
                        if(pagerState.currentPage + 1 < pagerState.pageCount)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(200.dp))
    }

}