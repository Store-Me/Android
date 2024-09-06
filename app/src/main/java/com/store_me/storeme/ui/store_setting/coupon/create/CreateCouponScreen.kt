@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.coupon.create

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.CouponAvailable
import com.store_me.storeme.data.CouponDiscountType
import com.store_me.storeme.data.CouponQuantity
import com.store_me.storeme.data.CouponType
import com.store_me.storeme.ui.component.DateOutLineTextField
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultDialogButton
import com.store_me.storeme.ui.component.DefaultFinishButton
import com.store_me.storeme.ui.component.DefaultOutlineTextField
import com.store_me.storeme.ui.component.LeftCheckButton
import com.store_me.storeme.ui.component.NumberOutLineTextField
import com.store_me.storeme.ui.component.StoreMeSelectDateCalendar
import com.store_me.storeme.ui.component.SubTitleSection
import com.store_me.storeme.ui.component.TextFieldErrorType
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponViewModel.CreateCouponProgress.SET_AVAILABLE
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponViewModel.CreateCouponProgress.SET_DESCRIPTION
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponViewModel.CreateCouponProgress.SET_DUE_DATE
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponViewModel.CreateCouponProgress.SET_IMAGE
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponViewModel.CreateCouponProgress.SET_NAME
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponViewModel.CreateCouponProgress.SET_QUANTITY
import com.store_me.storeme.ui.store_setting.coupon.create.CreateCouponViewModel.CreateCouponProgress.SET_VALUE
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.HighlightTextColor
import com.store_me.storeme.ui.theme.UndefinedBorderColor

val LocalCreateCouponViewModel = staticCompositionLocalOf<CreateCouponViewModel> {
    error("No LocalCreateCouponViewModel provided")
}

@Composable
fun CreateCouponScreen(
    navController: NavController,
    selectedCouponType: String,
    createCouponViewModel: CreateCouponViewModel = viewModel()
){
    val couponType: CouponType = when(selectedCouponType) {
        CouponType.DISCOUNT.name -> { CouponType.DISCOUNT }
        CouponType.GIVEAWAY.name -> { CouponType.GIVEAWAY }
        CouponType.OTHER.name -> { CouponType.OTHER }
        else -> { CouponType.OTHER }
    }

    val currentProgress by createCouponViewModel.currentProgress.collectAsState()

    val focusManager = LocalFocusManager.current

    BackHandler {
        if (currentProgress != CreateCouponViewModel.CreateCouponProgress.SET_VALUE) {
            createCouponViewModel.previousProgress()
        } else {
            navController.popBackStack()
        }
    }

    CompositionLocalProvider(LocalCreateCouponViewModel provides createCouponViewModel) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = White,
            topBar = {
                ProgressBar(currentProgress)
                TitleWithDeleteButton(navController = navController, title = "${couponType.displayName} 쿠폰 만들기")
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
                                (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                            } else {
                                (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                            }
                        }, label = ""
                    ) { targetProgress ->
                        when(targetProgress){
                            SET_VALUE -> { SetValueSection(couponType) }
                            SET_NAME -> { SetNameSection() }
                            SET_AVAILABLE -> { SetAvailableSection() }
                            SET_QUANTITY -> { SetQuantitySection() }
                            SET_DUE_DATE -> { SetDueDateSection() }
                            SET_IMAGE -> { SetImageSection() }
                            SET_DESCRIPTION -> { SetDescriptionSection{
                                createCouponViewModel.createCoupon(couponType)

                                navController.popBackStack()
                            } }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SetValueSection(couponType: CouponType, isEdit: Boolean = false, onDelete: () -> Unit = {}) {
    val createCouponViewModel = LocalCreateCouponViewModel.current

    val discountType by createCouponViewModel.discountType.collectAsState()
    val discountPrice by createCouponViewModel.discountPrice.collectAsState()
    val discountRate by createCouponViewModel.discountRate.collectAsState()

    val giveawayContent by createCouponViewModel.giveawayContent.collectAsState()
    val otherContent by createCouponViewModel.otherContent.collectAsState()

    var isError by remember { mutableStateOf(false) }
    var isPriceError by remember { mutableStateOf(false) }
    var isRateError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        fun isEnabled(): Boolean {
            return when (couponType) {
                CouponType.OTHER -> !isError && otherContent.isNotEmpty()
                CouponType.GIVEAWAY -> !isError && giveawayContent.isNotEmpty()
                CouponType.DISCOUNT -> {
                    discountType != null && (
                            (discountType == CouponDiscountType.PRICE && discountPrice != null && !isPriceError) ||
                            (discountType == CouponDiscountType.RATE && discountRate != null && !isRateError))
                }
            }
        }

        when(couponType){
            CouponType.DISCOUNT -> {
                LeftCheckButton(
                    text = "금액 할인 쿠폰을 만들고 싶어요.",
                    fontWeight = FontWeight.ExtraBold,
                    isSelected = discountType == CouponDiscountType.PRICE,
                    diffValue = 2
                ) {
                    createCouponViewModel.updateDiscountType(CouponDiscountType.PRICE)
                }

                NumberOutLineTextField(
                    placeholderText = "할인을 제공할 금액을 입력해 주세요.",
                    onValueChange = {
                        createCouponViewModel.updateDiscountPrice(it)
                    },
                    suffixText = "원",
                    errorType = TextFieldErrorType.COUPON_PRICE,
                    onErrorChange = { isPriceError = it },
                    enabled = discountType == CouponDiscountType.PRICE
                )

                Spacer(modifier = Modifier.height(5.dp))

                TextLengthRow(
                    text = (discountPrice ?: "").toString(), limitSize = 9
                )

                LeftCheckButton(
                    text = "할인율을 제공하고 싶어요.",
                    fontWeight = FontWeight.ExtraBold,
                    isSelected = discountType == CouponDiscountType.RATE,
                    diffValue = 2
                ) {
                    createCouponViewModel.updateDiscountType(CouponDiscountType.RATE)
                }

                NumberOutLineTextField(
                    placeholderText = "할인율을 입력해 주세요.",
                    onValueChange = {
                        createCouponViewModel.updateDiscountRate(it)
                    },
                    suffixText = "%",
                    errorType = TextFieldErrorType.COUPON_RATE,
                    onErrorChange = { isRateError = it },
                    enabled = discountType == CouponDiscountType.RATE
                )
            }

            CouponType.GIVEAWAY -> {
                SubTitleSection(text = "증정 내용")

                Spacer(modifier = Modifier.height(20.dp))

                DefaultOutlineTextField(
                    text = giveawayContent,
                    onValueChange = {
                        createCouponViewModel.updateGiveawayContent(it)
                    },
                    errorType = TextFieldErrorType.COUPON_CONTENT,
                    placeholderText = "증정할 메뉴나 서비스를 입력해주세요.",
                    onErrorChange = { isError = it }
                )

                Spacer(modifier = Modifier.height(5.dp))

                TextLengthRow(
                    text = giveawayContent, limitSize = 15
                )
            }

            CouponType.OTHER -> {
                SubTitleSection(text = "헤택 내용")

                Spacer(modifier = Modifier.height(20.dp))

                DefaultOutlineTextField(
                    text = otherContent,
                    onValueChange = {
                        createCouponViewModel.updateOtherContent(it)
                    },
                    errorType = TextFieldErrorType.COUPON_CONTENT,
                    placeholderText = "혜택의 내용을 입력해주세요.",
                    onErrorChange = { isError = it }
                )

                Spacer(modifier = Modifier.height(5.dp))

                TextLengthRow(
                    text = otherContent, limitSize = 15
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row {
            if(isEdit){
                DefaultDialogButton(text = "쿠폰 삭제", containerColor = EditButtonColor, contentColor = Black, modifier = Modifier.weight(1f)) {
                    onDelete()
                }
            }
            
            Spacer(modifier = Modifier.width(10.dp))
            
            DefaultFinishButton(
                text = "다음",
                enabled = isEnabled(),
                modifier = Modifier.weight(1f)
            ) {
                createCouponViewModel.nextProgress()
            }
        }
        
    }
}

@Composable
fun SetNameSection() {
    val createCouponViewModel = LocalCreateCouponViewModel.current

    val name by createCouponViewModel.couponName.collectAsState()

    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        SubTitleSection(text = "쿠폰 이름")

        Spacer(modifier = Modifier.height(20.dp))

        DefaultOutlineTextField(
            text = name,
            onValueChange = {
                createCouponViewModel.updateName(it)
            },
            errorType = TextFieldErrorType.COUPON_NAME,
            placeholderText = "쿠폰 이름을 입력해 주세요.",
            onErrorChange = { isError = it }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextLengthRow(
            text = name, limitSize = 15
        )

        Spacer(modifier = Modifier.height(40.dp))

        DefaultFinishButton(text = "다음", enabled = !isError && name.isNotEmpty()) {
            createCouponViewModel.nextProgress()
        }
    }
}

@Composable
fun SetAvailableSection() {
    val createCouponViewModel = LocalCreateCouponViewModel.current

    val availableType by remember { createCouponViewModel.couponAvailable }.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        SubTitleSection(text = "제공 대상")

        Spacer(modifier = Modifier.height(20.dp))

        LeftCheckButton(
            text = "모든 손님에게 제공하고 싶어요",
            fontWeight = FontWeight.ExtraBold,
            isSelected = availableType == CouponAvailable.ALL,
            diffValue = 2,
            description = "가게를 방문하려는 누구나 쿠폰을 받을 수 있어요."
        ) {
            createCouponViewModel.updateAvailable(CouponAvailable.ALL)
        }

        Spacer(modifier = Modifier.height(20.dp))

        LeftCheckButton(
            text = "재방문 손님에게만 제공하고 싶어요",
            fontWeight = FontWeight.ExtraBold,
            isSelected = availableType == CouponAvailable.REPEAT,
            diffValue = 2,
            description = "가게를 방문한 적이 있는 고객만 쿠폰을 받을 수 있어요."
        ) {
            createCouponViewModel.updateAvailable(CouponAvailable.REPEAT)
        }

        Spacer(modifier = Modifier.height(40.dp))

        DefaultFinishButton(text = "다음", enabled = availableType != null) {
            createCouponViewModel.nextProgress()
        }
    }
}

@Composable
fun SetQuantitySection() {
    val createCouponViewModel = LocalCreateCouponViewModel.current
    
    val quantityType by remember { createCouponViewModel.couponQuantity }.collectAsState()

    var quantity by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        SubTitleSection(text = "발급 개수")

        Spacer(modifier = Modifier.height(20.dp))

        LeftCheckButton(
            text = "제한 없이 제공",
            fontWeight = FontWeight.ExtraBold,
            isSelected = quantityType is CouponQuantity.Infinite,
            diffValue = 2,
        ) {
            createCouponViewModel.updateQuantity(isInfinite = true)
        }

        Spacer(modifier = Modifier.height(20.dp))

        LeftCheckButton(
            text = "갯수 제한",
            fontWeight = FontWeight.ExtraBold,
            isSelected = quantityType is CouponQuantity.Limit,
            diffValue = 2
        ) {
            createCouponViewModel.updateQuantity(isInfinite = false, quantity = 0)
        }
        
        AnimatedVisibility(visible = quantityType is CouponQuantity.Limit) {
            Column {
                NumberOutLineTextField(
                    placeholderText = "발급할 쿠폰 개수를 입력해 주세요",
                    onValueChange = {
                        createCouponViewModel.updateQuantity(false, quantity = it)
                        quantity = it.toString()
                    },
                    suffixText = "장",
                    errorType = TextFieldErrorType.COUPON_QUANTITY,
                    onErrorChange = { isError = it }
                )

                Spacer(modifier = Modifier.height(5.dp))

                TextLengthRow(
                    text = quantity, limitSize = 4
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        DefaultFinishButton(
            text = "다음",
            enabled = quantityType != null && !isError &&
                    ((quantityType is CouponQuantity.Limit && (quantity.toIntOrNull() ?: 0) > 0) || (quantityType is CouponQuantity.Infinite))
        ) {
            createCouponViewModel.nextProgress()
        }
    }
}

@Composable
fun SetDueDateSection() {
    val createCouponViewModel = LocalCreateCouponViewModel.current

    val selectedDate by remember { createCouponViewModel.couponDueDate }.collectAsState()

    var isError by remember { mutableStateOf(false) }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        SubTitleSection(text = "사용 기한")

        Spacer(modifier = Modifier.height(20.dp))
        
        DateOutLineTextField(
            selectedDate = selectedDate,
            placeholderText = "사용 기한을 선택해 주세요.",
            onErrorChange = { isError = it }
        ) {
            showBottomSheet = true
        }

        Spacer(modifier = Modifier.height(40.dp))

        DefaultFinishButton(text = "다음", enabled = !isError && (selectedDate != null)) {
            createCouponViewModel.nextProgress()
        }
    }

    if(showBottomSheet) {
        DefaultBottomSheet(onDismiss = { showBottomSheet = false }, sheetState = sheetState) {
            StoreMeSelectDateCalendar(onDateChange = {
                createCouponViewModel.updateDueDate(it)
                showBottomSheet = false
            })

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SetImageSection() {
    val createCouponViewModel = LocalCreateCouponViewModel.current

    val imageUrl by remember { createCouponViewModel.couponImageUrl }.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        SubTitleSection(text = "쿠폰 사진 (선택)")

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable(
                    onClick = {
                        //TODO GET IMAGE
                    },
                    enabled = imageUrl.isEmpty()
                )
                .border(1.dp, UndefinedBorderColor, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            when(imageUrl.isEmpty()){
                true -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "이미지 추가 아이콘",
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
                false -> {
                    /*AsyncImage*/
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        DefaultFinishButton(text = "다음") {
            createCouponViewModel.nextProgress()
        }
    }
}

@Composable
fun SetDescriptionSection(isEdit: Boolean = false, onCreateCoupon: () -> Unit) {
    val createCouponViewModel = LocalCreateCouponViewModel.current

    val text by remember { createCouponViewModel.couponDescription }.collectAsState()
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
        ) {
        SubTitleSection(text = "안내 사항 (선택)")

        Spacer(modifier = Modifier.height(20.dp))

        DefaultOutlineTextField(
            text = text,
            onValueChange = { createCouponViewModel.updateDescription(it) },
            placeholderText = "쿠폰에 대해 알리고 싶은 내용을 남겨보세요.",
            errorType = TextFieldErrorType.DESCRIPTION,
            onErrorChange = { isError = it },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextLengthRow(text = text, limitSize = 100)

        Spacer(modifier = Modifier.height(40.dp))

        DefaultFinishButton(text = if(isEdit) "수정 완료" else "쿠폰 만들기") {
            onCreateCoupon()
        }
    }
}

@Composable
fun ProgressBar(currentProgress: CreateCouponViewModel.CreateCouponProgress) {
    // 전체 단계 수
    val totalSteps = CreateCouponViewModel.CreateCouponProgress.values().size

    val targetProgress = (currentProgress.ordinal + 1) / totalSteps.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    // LinearProgressIndicator를 사용하여 애니메이션된 진행률 표시
    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier
            .fillMaxWidth(),
        color = HighlightTextColor,
        trackColor = EditButtonColor,
        strokeCap = StrokeCap.Butt
    )
}