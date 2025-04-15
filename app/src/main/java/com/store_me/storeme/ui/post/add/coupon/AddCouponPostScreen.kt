@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.post.add.coupon

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.StampCouponData
import com.store_me.storeme.data.enums.post.PostType
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.SimpleTextField
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.post.add.normal.AddPostTopBar
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponInfo
import com.store_me.storeme.ui.store_setting.stamp.StampCouponItem
import com.store_me.storeme.ui.theme.DividerColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.BACKGROUND_ROUNDING_VALUE
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel

@Composable
fun AddCouponPostScreen(
    addCouponPostViewModel: AddCouponPostViewModel = hiltViewModel()
) {
    val focusManager  = LocalFocusManager.current
    val context = LocalContext.current

    val snackbarHostState = LocalSnackbarHostState.current
    val loadingViewModel = LocalLoadingViewModel.current

    var showBackWarningDialog by remember{ mutableStateOf(false) }

    val title by addCouponPostViewModel.title.collectAsState()
    val description by addCouponPostViewModel.description.collectAsState()
    val coupons by addCouponPostViewModel.coupons.collectAsState()
    val stamp by addCouponPostViewModel.stamp.collectAsState()
    val selectedCoupon by addCouponPostViewModel.selectedCoupon.collectAsState()
    val selectedStamp by addCouponPostViewModel.selectedStamp.collectAsState()
    val isSuccess by addCouponPostViewModel.isSuccess.collectAsState()

    fun onClose() {
        showBackWarningDialog = true
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(isSuccess) {
        if(isSuccess) {
            (context as Activity).finish()
        }
    }

    LaunchedEffect(Unit) {
        addCouponPostViewModel.getStoreCoupons()
        addCouponPostViewModel.getStampCoupon()
    }

    Scaffold(
        modifier = Modifier
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { StoreMeSnackbar(snackbarData = it) }
        ) },
        topBar = {
            AddPostTopBar(
                postType = PostType.SURVEY,
                onClose = { onClose() },
                onFinish = {
                    loadingViewModel.showLoading()
                    addCouponPostViewModel.createCouponPost()
                }
            ) },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    CouponTitleItem(
                        title = title,
                        onTitleChange = { addCouponPostViewModel.updateTitle(it) }
                    )
                }

                item {
                    SelectCouponItem(
                        coupons = coupons,
                        stamp = stamp,
                        selectedCoupon = selectedCoupon,
                        selectedStamp = selectedStamp,
                        onSelect = { coupon, stamp ->
                            addCouponPostViewModel.updateSelectedCoupon(coupon)
                            addCouponPostViewModel.updateSelectedStamp(stamp)
                        }
                    )
                }

                item {
                    CouponDescriptionItem(
                        description = description,
                        onDescriptionChange = { addCouponPostViewModel.updateDescription(it) }
                    )
                }
            }
        }
    )

    if(showBackWarningDialog) {
        BackWarningDialog(
            onDismiss = { showBackWarningDialog = false },
            onAction = {
                showBackWarningDialog = false
                (context as Activity).finish()
            }
        )
    }
}

/**
 * 쿠폰 홍보 제목 Composable
 */
@Composable
fun CouponTitleItem(
    title: String,
    onTitleChange: (String) -> Unit,
) {
    SimpleTextField(
        value = title,
        onValueChange = { onTitleChange(it) },
        placeholderText = "쿠폰 홍보 게시글의 제목을 입력하세요.",
        singleLine = true
    )
}

@Composable
fun SelectCouponItem(
    coupons: List<CouponData>,
    stamp: StampCouponData?,
    selectedCoupon: CouponData?,
    selectedStamp: StampCouponData?,
    onSelect: (CouponData?, StampCouponData?) -> Unit
) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val activateCoupons by remember(coupons) {
        mutableStateOf(coupons.filter { DateTimeUtils.isValid(it.dueDate) })
    }

    when {
        selectedStamp == null && selectedCoupon == null -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .border(
                        width = 2.dp,
                        color = DividerColor,
                        shape = RoundedCornerShape(BACKGROUND_ROUNDING_VALUE)
                    )
                    .clip(RoundedCornerShape(BACKGROUND_ROUNDING_VALUE))
                    .clickable { showBottomSheet = true }
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "쿠폰을 선택해주세요.",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = GuideColor
                )
            }
        }
        selectedStamp != null -> {
            Box(
                modifier = Modifier.clickable(
                    onClick =
                        {
                            onSelect(null, null)
                            showBottomSheet = false
                        },
                    interactionSource = null,
                    indication = null
                )
            ) {
                StampCouponItem(stampCoupon = selectedStamp)
            }
        }
        selectedCoupon != null -> {
            Box(
                modifier = Modifier.clickable(
                    onClick = { onSelect(null, null) },
                    interactionSource = null,
                    indication = null
                )
            ) {
                CouponInfo(selectedCoupon, storeName = "")
            }
        }
    }

    if(showBottomSheet) {
        DefaultBottomSheet(
            onDismiss = { showBottomSheet = false },
            sheetState = state
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "쿠폰",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    )
                }

                items(activateCoupons) {
                    Box(
                        modifier = Modifier.clickable { onSelect(it, null) }
                    ) {
                        CouponInfo(it, storeName = "")
                    }
                }

                if(activateCoupons.isEmpty()) {
                    item {
                        Text(
                            text = "활성화된 쿠폰이 없습니다.",
                            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                            color = GuideColor
                        )
                    }
                }

                item {
                    Text(
                        text = "스탬프 쿠폰",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    )
                }

                item {
                    Box(
                        modifier = Modifier.clickable { onSelect(null, stamp) }
                    ) {
                        if(stamp == null) {
                            Text(
                                text = "스탬프 쿠폰이 없습니다.",
                                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                                color = GuideColor
                            )
                        } else {
                            StampCouponItem(stampCoupon = stamp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 설문의 설명 Composable
 */
@Composable
fun CouponDescriptionItem(
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    SimpleTextField(
        value = description,
        onValueChange = { onDescriptionChange(it) },
        textStyle = storeMeTextStyle(FontWeight.Bold, 0),
        placeholderText = "쿠폰에 대한 설명을 입력해주세요.",
        singleLine = false
    )
}