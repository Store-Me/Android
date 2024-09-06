package com.store_me.storeme.ui.store_setting.coupon.edit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.CouponInfoData
import com.store_me.storeme.data.CouponType
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponViewModel
import com.store_me.storeme.ui.store_setting.coupon.create.LocalCreateCouponViewModel
import com.store_me.storeme.ui.store_setting.coupon.create.ProgressBar
import com.store_me.storeme.ui.store_setting.coupon.create.SetAvailableSection
import com.store_me.storeme.ui.store_setting.coupon.create.SetDescriptionSection
import com.store_me.storeme.ui.store_setting.coupon.create.SetDueDateSection
import com.store_me.storeme.ui.store_setting.coupon.create.SetImageSection
import com.store_me.storeme.ui.store_setting.coupon.create.SetNameSection
import com.store_me.storeme.ui.store_setting.coupon.create.SetQuantitySection
import com.store_me.storeme.ui.store_setting.coupon.create.SetValueSection



@Composable
fun EditCouponScreen(
    navController: NavController,
    selectedCouponId: String,
    createCouponViewModel: CreateCouponViewModel = viewModel()
) {
    val currentProgress by createCouponViewModel.currentProgress.collectAsState()

    val focusManager = LocalFocusManager.current

    BackHandler {
        if (currentProgress != CreateCouponViewModel.CreateCouponProgress.SET_VALUE) {
            createCouponViewModel.previousProgress()
        } else {
            navController.popBackStack()
        }
    }

    val selectedCouponDetail = Auth.couponDetailList.value.find {
        it.couponInfoData.couponId == selectedCouponId
    } ?: run {
        return
    }

    val couponType = when(selectedCouponDetail.couponInfoData){
        is CouponInfoData.Discount -> { CouponType.DISCOUNT }
        is CouponInfoData.Giveaway -> { CouponType.GIVEAWAY }
        is CouponInfoData.Other -> { CouponType.OTHER }
    }

    LaunchedEffect(selectedCouponId) {
        createCouponViewModel.getCouponData(selectedCouponDetail.couponInfoData)
    }

    var showDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalCreateCouponViewModel provides createCouponViewModel) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = Color.White,
            topBar = {
                ProgressBar(currentProgress)
                TitleWithDeleteButton(navController = navController, title = "쿠폰 수정")
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    AnimatedContent(
                        targetState = currentProgress,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInHorizontally { it } + fadeIn()).togetherWith(
                                    slideOutHorizontally { -it } + fadeOut())
                            } else {
                                (slideInHorizontally { -it } + fadeIn()).togetherWith(
                                    slideOutHorizontally { it } + fadeOut())
                            }
                        }, label = ""
                    ) { targetProgress ->
                        when(targetProgress){
                            CreateCouponViewModel.CreateCouponProgress.SET_VALUE -> { SetValueSection(couponType, true) {
                                showDialog = true
                            } }
                            CreateCouponViewModel.CreateCouponProgress.SET_NAME -> { SetNameSection() }
                            CreateCouponViewModel.CreateCouponProgress.SET_AVAILABLE -> { SetAvailableSection() }
                            CreateCouponViewModel.CreateCouponProgress.SET_QUANTITY -> { SetQuantitySection() }
                            CreateCouponViewModel.CreateCouponProgress.SET_DUE_DATE -> { SetDueDateSection() }
                            CreateCouponViewModel.CreateCouponProgress.SET_IMAGE -> { SetImageSection() }
                            CreateCouponViewModel.CreateCouponProgress.SET_DESCRIPTION -> { SetDescriptionSection{
                                createCouponViewModel.updateCouponData(selectedCouponDetail)

                                navController.popBackStack()
                            } }
                        }
                    }
                }
            }
        )

        if(showDialog) {
            WarningDialog(
                title = "쿠폰을 삭제할까요?",
                warningContent = selectedCouponDetail.couponInfoData.name,
                content = "위의 쿠폰이 삭제되며, 삭제 이후 복구되지 않아요.",
                actionText = "삭제",
                onDismiss = { showDialog = false },
                onAction = {
                    createCouponViewModel.deleteCoupon(selectedCouponDetail)

                    navController.popBackStack()
                }
            )
        }
    }
}