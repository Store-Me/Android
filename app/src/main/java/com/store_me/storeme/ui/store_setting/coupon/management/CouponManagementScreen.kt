@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.coupon.management

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.coupon.CouponCreationProgress
import com.store_me.storeme.data.enums.coupon.CouponDiscountType
import com.store_me.storeme.data.enums.coupon.CouponTarget
import com.store_me.storeme.data.enums.coupon.CouponType
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.SimpleNumberOutLinedTextField
import com.store_me.storeme.ui.component.SimpleOutLinedTextField
import com.store_me.storeme.ui.component.StoreMeSelectDateCalendar
import com.store_me.storeme.ui.component.SubTitleSection
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.signup.GuideTextBoxItem
import com.store_me.storeme.ui.store_setting.menu.management.ImageBox
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightTextColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.CropUtils
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.yalantis.ucrop.UCrop
import java.time.LocalDate

@Composable
fun CouponManagementScreen(
    navController: NavController,
    selectedCouponType: String,
    couponId: String?,
    couponManagementProgressViewModel: CouponManagementProgressViewModel = viewModel(),
    couponManagementViewModel: CouponManagementViewModel = hiltViewModel()
){
    val couponType by remember {
        mutableStateOf(
            when (selectedCouponType) {
                CouponType.Discount.name -> { CouponType.Discount }

                CouponType.Giveaway.name -> { CouponType.Giveaway }

                CouponType.Other.name -> { CouponType.Other }

                else -> { CouponType.Discount }
            }
        )
    }

    val storeDataViewModel = LocalStoreDataViewModel.current
    val auth = LocalAuth.current
    val loadingViewModel = LocalLoadingViewModel.current

    val focusManager = LocalFocusManager.current

    val currentProgress by couponManagementProgressViewModel.currentProgress.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val discountType by couponManagementViewModel.discountType.collectAsState()
    val value by couponManagementViewModel.value.collectAsState()
    val name by couponManagementViewModel.name.collectAsState()
    val target by couponManagementViewModel.target.collectAsState()
    val quantity by couponManagementViewModel.quantity.collectAsState()
    val dueDate by couponManagementViewModel.dueDate.collectAsState()
    val imageUri by couponManagementViewModel.imageUri.collectAsState()
    val imageUrl by couponManagementViewModel.imageUrl.collectAsState()
    val progress by couponManagementViewModel.uploadProgress.collectAsState()
    val description by couponManagementViewModel.description.collectAsState()

    val isFinished by couponManagementViewModel.isFinished.collectAsState()

    val isEdit by remember { mutableStateOf(!couponId.isNullOrEmpty()) }

    LaunchedEffect(couponId) {
        if(!couponId.isNullOrEmpty()) {
            if(couponManagementViewModel.getCouponData(couponId, storeDataViewModel.coupons.value) == null)
                navController.popBackStack()
        }
    }

    LaunchedEffect(isFinished) {
        if(isFinished) {
            storeDataViewModel.getStoreCoupons(auth.storeId.value!!)
            navController.popBackStack()
        }
    }

    BackHandler {
        if (currentProgress != CouponCreationProgress.VALUE) {
            couponManagementProgressViewModel.previousProgress()
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        topBar = {
            ProgressBar(currentProgress)
            TitleWithDeleteButton(
                title = "${couponType.displayName} 쿠폰 만들기"
            ) {
                navController.popBackStack()
            }},
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
                    },
                    label = ""
                ) { targetState ->
                    when(targetState){
                        CouponCreationProgress.VALUE -> { ValueSection(
                            isEdit = isEdit,
                            couponType = couponType,
                            discountType = discountType,
                            value = value,
                            onDiscountTypeChange = {
                                couponManagementViewModel.updateDiscountType(it)
                                couponManagementViewModel.updateValue(null)
                            },
                            onValueChange = { couponManagementViewModel.updateValue(it) },
                            onFinish = { couponManagementProgressViewModel.nextProgress() },
                            onDelete = { showDialog = true }
                        ) }
                        CouponCreationProgress.NAME -> { NameSection(
                            name = name,
                            onValueChange = { couponManagementViewModel.updateName(it) },
                            onFinish = { couponManagementProgressViewModel.nextProgress() }
                        ) }
                        CouponCreationProgress.TARGET -> { TargetSection(
                            target = target,
                            onValueChange = { couponManagementViewModel.updateTarget(it.name) },
                            onFinish = { couponManagementProgressViewModel.nextProgress() }
                        ) }
                        CouponCreationProgress.QUANTITY -> { QuantitySection(
                            quantity = quantity,
                            onValueChange = { couponManagementViewModel.updateQuantity(it) },
                            onFinish = { couponManagementProgressViewModel.nextProgress() }
                        ) }
                        CouponCreationProgress.DUE_DATE -> { DueDateSection(
                            dueDate = dueDate,
                            onValueChange = { couponManagementViewModel.updateDueDate(it) },
                            onFinish = { couponManagementProgressViewModel.nextProgress() }
                        ) }
                        CouponCreationProgress.IMAGE -> { ImageSection(
                            imageUri = imageUri,
                            imageUrl = imageUrl,
                            progress = progress,
                            onUriChange = {
                                couponManagementViewModel.updateImageUri(it)
                                couponManagementViewModel.uploadStoreCouponImage(storeDataViewModel.storeInfoData.value?.storeName!!)
                            },
                            onFinish = { couponManagementProgressViewModel.nextProgress() }
                        ) }
                        CouponCreationProgress.DESCRIPTION -> { DescriptionSection(
                            isEdit = isEdit,
                            description = description,
                            onValueChange = { couponManagementViewModel.updateDescription(it) },
                            onFinish = {
                                loadingViewModel.showLoading()

                                if(isEdit) {
                                    couponManagementViewModel.patchCoupon(storeId = auth.storeId.value!!, couponType = couponType)
                                } else {
                                    couponManagementViewModel.postCoupon(storeId = auth.storeId.value!!, couponType = couponType)
                                }
                            }
                        ) }
                    }
                }
            }
        }
    )

    if(showDialog) {
        WarningDialog(
            title = "쿠폰을 삭제할까요?",
            warningContent = couponManagementViewModel.editCoupon.value?.name ?: "",
            content = "위의 쿠폰이 삭제되며, 삭제 이후 복구되지 않아요.",
            onAction = { couponManagementViewModel.deleteCoupon(storeId = auth.storeId.value!!, couponId = couponId!!) },
            onDismiss = { showDialog = false },
            actionText = "삭제"
        )
    }
}

@Composable
fun ValueSection(
    isEdit: Boolean,
    couponType: CouponType,
    discountType: CouponDiscountType,
    value: String?,
    onDiscountTypeChange: (CouponDiscountType) -> Unit,
    onValueChange: (String?) -> Unit,
    onFinish: () -> Unit,
    onDelete: () -> Unit
) {

    val isPriceError =
        if(value == null) false
        else if(discountType == CouponDiscountType.PRICE)
            (value.toLongOrNull() ?: -1) in 0..1000
        else
            false

    val isRateError =
        if(value == null) false
        else if(discountType == CouponDiscountType.RATE)
            (value.toLongOrNull() ?: -1) !in 0 .. 100
        else
            false

    val isValueError by remember(value) { derivedStateOf {
        (value?.length ?: 0) > 50
    } }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        when(couponType){
            CouponType.Discount-> {
                DefaultCheckButton(
                    text = "금액 할인 쿠폰을 만들고 싶어요.",
                    fontWeight = FontWeight.ExtraBold,
                    isSelected = discountType == CouponDiscountType.PRICE,
                    diffValue = 2,
                    isCheckIconOnLeft = true
                ) {
                    onDiscountTypeChange(CouponDiscountType.PRICE)
                }

                Spacer(modifier = Modifier.height(12.dp))

                SimpleNumberOutLinedTextField(
                    text = if(discountType == CouponDiscountType.RATE) "" else value ?: "",
                    onValueChange = { onValueChange(it) },
                    placeholderText = "할인 금액을 입력하세요.",
                    suffixText = "원",
                    isError = isPriceError,
                    errorText = "1000원 이상의 금액을 입력해주세요.",
                    enabled = discountType == CouponDiscountType.PRICE
                )

                Spacer(modifier = Modifier.height(20.dp))

                DefaultCheckButton(
                    text = "할인율을 제공하고 싶어요.",
                    fontWeight = FontWeight.ExtraBold,
                    isSelected = discountType == CouponDiscountType.RATE,
                    diffValue = 2,
                    isCheckIconOnLeft = true
                ) {
                    onDiscountTypeChange(CouponDiscountType.RATE)
                }

                Spacer(modifier = Modifier.height(12.dp))

                SimpleNumberOutLinedTextField(
                    text = if(discountType == CouponDiscountType.PRICE) "" else value ?: "",
                    onValueChange = { onValueChange(it) },
                    placeholderText = "할인 금액을 입력하세요.",
                    suffixText = "%",
                    isError = isRateError,
                    errorText = "0 ~ 100 사이 숫자만 입력이 가능합니다.",
                    enabled = discountType == CouponDiscountType.RATE
                )
            }

            CouponType.Giveaway -> {
                Text(
                    text = "증정 내용",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6)
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleOutLinedTextField(
                    text = value ?: "",
                    onValueChange = { onValueChange(it) },
                    placeholderText = "증정할 메뉴나 서비스를 입력해주세요.",
                    isError = isValueError,
                    errorText = "증정 내용을 50자 이내로 입력해주세요."
                )

                TextLengthRow(
                    text = value ?: "", limitSize = 50
                )
            }

            CouponType.Other -> {
                SubTitleSection(text = "헤택 내용")

                Spacer(modifier = Modifier.height(12.dp))

                SimpleOutLinedTextField(
                    text = value ?: "",
                    onValueChange = { onValueChange(it) },
                    placeholderText = "혜택의 내용을 입력해주세요.",
                    isError = isValueError,
                    errorText = "혜택 내용을 50자 이내로 입력해주세요."
                )

                TextLengthRow(
                    text = value ?: "", limitSize = 50
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if(isEdit) {
                DefaultButton(
                    buttonText = "쿠폰 삭제",
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = SubHighlightColor
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    onDelete()
                }
            }

            DefaultButton(
                buttonText = "다음",
                enabled = !value.isNullOrEmpty() && ((!isPriceError && !isRateError) || ((couponType == CouponType.Other || couponType == CouponType.Giveaway) && !isValueError)),
                modifier = Modifier.weight(1f)
            ) {
                onFinish()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun NameSection(
    name: String,
    onValueChange: (String) -> Unit,
    onFinish: () -> Unit
) {
    val isError by remember(name) { derivedStateOf {
        name.length > 20
    } }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "쿠폰 이름",
            style = storeMeTextStyle(FontWeight.ExtraBold, 6)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SimpleOutLinedTextField(
            text = name,
            onValueChange = { onValueChange(it) },
            placeholderText = "쿠폰 이름을 입력해 주세요.",
            isError = isError,
            errorText = "쿠폰 이름을 20자 이내로 입력해 주세요."
        )

        TextLengthRow(
            text = name, limitSize = 20
        )

        Spacer(modifier = Modifier.height(20.dp))

        GuideTextBoxItem(
            title = "쿠폰 이름 설정 가이드",
            content = "쿠폰 이름은 한눈에 혜택을 알아볼 수 있도록 간결하게 작성하는 것이 좋아요.\n\n예) '오픈기념 1천원 할인', '봄맞이 음료 증정'"
        )

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            buttonText = "다음",
            enabled = name.isNotEmpty() && !isError
        ) {
            onFinish()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun TargetSection(
    target: String?,
    onValueChange: (CouponTarget) -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "제공 대상",
            style = storeMeTextStyle(FontWeight.ExtraBold, 6)
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultCheckButton(
            isCheckIconOnLeft = true,
            text = "모든 손님에게 제공하고 싶어요",
            fontWeight = FontWeight.ExtraBold,
            isSelected = target == CouponTarget.All.name,
            diffValue = 2,
            description = "가게를 방문하려는 누구나 쿠폰을 받을 수 있어요."
        ) {
            onValueChange(CouponTarget.All)
        }

        Spacer(modifier = Modifier.height(20.dp))

        DefaultCheckButton(
            isCheckIconOnLeft = true,
            text = "알림받기 설정한 손님에게만 제공하고 싶어요",
            fontWeight = FontWeight.ExtraBold,
            isSelected = target == CouponTarget.Notification.name,
            diffValue = 2,
            description = "우리 가게를 알림받기 설정한 손님만 쿠폰을 받을 수 있어요."
        ) {
            onValueChange(CouponTarget.Notification)
        }

        Spacer(modifier = Modifier.height(20.dp))

        GuideTextBoxItem(
            title = "쿠폰 제공 대상이란?",
            content = "제공 대상 여부에 상관 없이 모든 쿠폰은 사용자들에게 노출되어 모든 사용자들이 확인할 수 있어요." +
                    "\n\n" +
                    "쿠폰 제공 대상을 설정하면, 쿠폰을 수령할 수 있는 사용자를 제한할 수 있어요."
        )

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            buttonText = "다음",
            enabled = target != null
        ) {
            onFinish()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun QuantitySection(
    quantity: Long?,
    onValueChange: (Long?) -> Unit,
    onFinish: () -> Unit
) {
    val isError by remember(quantity) { derivedStateOf {
        if(quantity != null) quantity > 10000 else false
    } }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "발급 개수",
            style = storeMeTextStyle(FontWeight.ExtraBold, 6)
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultCheckButton(
            isCheckIconOnLeft = true,
            text = "제한 없이 제공",
            fontWeight = FontWeight.ExtraBold,
            isSelected = quantity == null,
            diffValue = 2,
        ) {
            onValueChange(null)
        }

        Spacer(modifier = Modifier.height(20.dp))

        DefaultCheckButton(
            isCheckIconOnLeft = true,
            text = "갯수 제한",
            fontWeight = FontWeight.ExtraBold,
            isSelected = quantity != null,
            diffValue = 2
        ) {
            onValueChange(0)
        }

        Spacer(modifier = Modifier.height(12.dp))
        
        AnimatedVisibility(visible = quantity != null) {
            SimpleNumberOutLinedTextField(
                text = if(quantity == 0L) "" else quantity.toString(),
                onValueChange = { onValueChange(it.toLongOrNull()) },
                placeholderText = "발급할 쿠폰 개수를 입력해 주세요",
                suffixText = "장",
                isError = isError,
                errorText = "쿠폰은 최대 10000장 까지 발급이 가능합니다"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            buttonText = "다음",
            enabled = !isError && (quantity == null || quantity > 0)
        ) {
            onFinish()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun DueDateSection(
    dueDate: String?,
    onValueChange: (LocalDate?) -> Unit,
    onFinish: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "사용 기한",
            style = storeMeTextStyle(FontWeight.ExtraBold, 6)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SimpleOutLinedTextField(
            modifier = Modifier
                .clickable(
                    onClick = { showBottomSheet = true },
                    indication = null,
                    interactionSource = null
                ),
            text = DateTimeUtils.convertExpiredDateToKorean(dueDate),
            placeholderText = "사용 기한을 선택해주세요.",
            onValueChange = {},
            trailingIconResource = R.drawable.ic_calendar,
            isError = false,
            errorText = "",
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Black,
                disabledTextColor = Color.Black
                )
        )

        Spacer(modifier = Modifier.height(20.dp))

        GuideTextBoxItem(
            title = "사용기한 안내",
            content = "사용기한은 다음날 자정 이전까지 유효해요.\n\n예) 2025년 10월 1일 까지로 설정되어있으면, 2025년 10월 2일 00시 00분 이전까지 사용이 가능해요"
        )

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            buttonText = "다음",
            enabled = dueDate != null
        ) {
            onFinish()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if(showBottomSheet) {
        DefaultBottomSheet(onDismiss = { showBottomSheet = false }, sheetState = sheetState) {
            var selectedDueDate by remember { mutableStateOf<LocalDate?>(null) }

            StoreMeSelectDateCalendar(onDateChange = {
                selectedDueDate = it
            })

            Spacer(modifier = Modifier.height(20.dp))

            DefaultButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                buttonText = "확인",
                enabled = selectedDueDate != null
            ) {
                onValueChange(selectedDueDate)
                showBottomSheet = false
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ImageSection(
    imageUrl: String?,
    imageUri: Uri?,
    progress: Float,
    onUriChange: (Uri) -> Unit,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let { uri ->
                onUriChange(uri)
            }
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { sourceUri ->
            val cropIntent = CropUtils.getCropIntent(context = context, sourceUri = sourceUri, aspectRatio = Pair(1f, 1f))
            cropLauncher.launch(cropIntent)
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "쿠폰 사진",
            style = storeMeTextStyle(FontWeight.ExtraBold, 6)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ImageBox(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                image = imageUrl,
                imageUri = imageUri,
                progress = progress
            ) {
                galleryLauncher.launch("image/*")
            }

            GuideTextBoxItem(
                modifier = Modifier
                    .weight(1f),
                title = "쿠폰 이미지 안내",
                content = "혜택과 관련된 이미지를 설정해 주세요.\n\n이미지를 등록하지 않은 경우, 가게 프로필 사진과 동일한 이미지가 설정돼요."
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            buttonText = "다음",
            enabled = imageUri == null
        ) {
            onFinish()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun DescriptionSection(
    isEdit: Boolean,
    description: String?,
    onValueChange: (String) -> Unit,
    onFinish: () -> Unit
) {
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                Text(
                    text = "안내 사항",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                TextField(
                    value = description ?: "",
                    onValueChange = { onValueChange(it) },
                    textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                    placeholder = {
                        Text(
                            text = "쿠폰에 대해 안내하고 싶은 내용을 남겨보세요.\n",
                            style = storeMeTextStyle(FontWeight.Normal, 1),
                            color = GuideColor
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 350.dp) //최소 높이
                        .padding(horizontal = 4.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                    ),
                    singleLine = false,
                    minLines = 2    //1 -> 2줄 변화시 글자 크기 문제 해결
                )
            }
        }

        DefaultHorizontalDivider()

        Spacer(modifier = Modifier.height(20.dp))

        DefaultButton(
            buttonText = if(isEdit) "수정 완료" else "쿠폰 만들기",
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            onFinish()
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProgressBar(currentProgress: CouponCreationProgress) {
    // 전체 단계 수
    val totalSteps = CouponCreationProgress.entries.size

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
        trackColor = SubHighlightColor,
        strokeCap = StrokeCap.Butt
    )
}