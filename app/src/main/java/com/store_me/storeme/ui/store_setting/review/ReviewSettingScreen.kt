@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.store_me.storeme.ui.store_setting.review

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.review.ReviewCount
import com.store_me.storeme.data.review.ReviewData
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.ImageDetailDialog
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.component.SimpleTextField
import com.store_me.storeme.ui.component.SkeletonAsyncImage
import com.store_me.storeme.ui.component.TextBox
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.theme.ErrorColor
import com.store_me.storeme.ui.theme.ExpiredColor
import com.store_me.storeme.ui.theme.FinishedColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.OnboardingSelectedIndicatorColor
import com.store_me.storeme.ui.theme.OwnerReplyBoxColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.store_me.storeme.utils.toTimeAgo
import kotlinx.coroutines.launch

@Composable
fun ReviewSettingScreen(
    navController: NavController,
    reviewViewModel: ReviewViewModel
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val loadingViewModel = LocalLoadingViewModel.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val reviewCount by reviewViewModel.reviewCounts.collectAsState()
    val reviews by reviewViewModel.reviews.collectAsState()

    var isWritingReply by remember { mutableStateOf(false) }

    var selectedReview by remember { mutableStateOf<ReviewData?>(null) }

    var showWarnDialog by remember { mutableStateOf(false) }

    fun onClose() {
        when {
            isWritingReply -> {
                showWarnDialog = true
            }
            else -> {
                navController.popBackStack()
            }
        }
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(reviews) {
        isWritingReply = false
        selectedReview = null
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        topBar = { TopAppBar(title = {
            TitleWithDeleteButton(
                title = "리뷰 관리",
                isInTopAppBar = true
            ) {
                onClose()
            } },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                scrolledContainerColor = Color.White
            )
        ) },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    item { Spacer(
                        modifier = Modifier.height(20.dp)
                    ) }

                    item { StoreReviewCount(
                        reviewCount = reviewCount
                    ) }

                    item { Spacer(
                        modifier = Modifier.height(40.dp)
                    ) }

                    item { StoreReviews(
                        accountType = AccountType.OWNER,
                        storeName = storeInfoData!!.storeName,
                        reviews = reviews,
                        onClickMenu = {
                            if(storeDataViewModel.menuCategories.value.any { category ->
                                    category.menus.any { menu -> menu.name == it }
                                }) {
                                navController.navigate(OwnerRoute.MenuSetting(it).fullRoute)
                            } else {
                                scope.launch {
                                    ErrorEventBus.errorFlow.emit("$it 메뉴가 존재하지 않습니다.")
                                }
                            }
                        },
                        onEditReview = {

                        },
                        onDeleteReview = {
                            loadingViewModel.showLoading()
                            reviewViewModel.deleteReview(it.id)
                        },
                        onAddReply = {
                            if(it.reply != null) {
                                scope.launch {
                                    ErrorEventBus.errorFlow.emit("답글이 이미 존재합니다.")
                                }
                            } else {
                                isWritingReply = true
                                selectedReview = it
                            }
                        },
                        onEditReply = {
                            isWritingReply = true
                            selectedReview = it
                        },
                        onDeleteReply = {
                            reviewViewModel.deleteReviewReply(it.id)
                        },
                        onReportReview = {
                            //TODO 신고하기
                        }
                    ) }
                }

                //답글 작성 중
                if(isWritingReply) {
                    selectedReview?.let { review ->
                        WriteReplyBox(
                            boxScope = this,
                            selectedReview = review,
                            storeInfoData = storeInfoData!!,
                            onPostReply = {
                                loadingViewModel.showLoading()
                                reviewViewModel.postReviewReply(reviewId = review.id, text = it)
                            },
                            onPatchReply = {
                                loadingViewModel.showLoading()
                                reviewViewModel.patchReviewReply(reviewId = review.id, text = it)
                            }
                        )
                    }
                }
            }

            if (showWarnDialog) {
                WarningDialog(
                    title = "답글 작성을 취소할까요?",
                    warningContent = "",
                    content = "취소 시 작성 내용이 모두 사라져요.",
                    actionText = "확인",
                    onDismiss = {
                        showWarnDialog = false
                    },
                    onAction = {
                        showWarnDialog = false
                        isWritingReply = false
                    }
                )
            }
        }
    )
}

@Composable
fun StoreReviewCount(
    modifier: Modifier = Modifier,
    reviewCount: List<ReviewCount>
) {
    val totalCount by remember(reviewCount) {
        derivedStateOf { reviewCount.sumOf { it.count } }
    }

    val sampleReviewCounts = listOf(
        ReviewCount(emoji = "\uD83D\uDE0D", text = "리뷰 값 1", count = 5),
        ReviewCount(emoji = "\uD83D\uDE0A", text = "리뷰 값 2", count = 4),
        ReviewCount(emoji = "\uD83D\uDE2C", text = "리뷰 값 3", count = 3),
        ReviewCount(emoji = "\uD83D\uDE42", text = "리뷰 값 4", count = 2),
        ReviewCount(emoji = "\uD83D\uDE33", text = "리뷰 값 5", count = 1)
    )
    val sampleRatios = listOf(0.5f, 0.4f, 0.3f, 0.2f, 0.1f)

    val sortedReviewCount by remember(reviewCount) {
        derivedStateOf {
            reviewCount
                .sortedByDescending { it.count }
                .take(5)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "스토어 리뷰 $totalCount",
            style = storeMeTextStyle(FontWeight.ExtraBold, 4)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if(totalCount < 10) {
                            Modifier.blur(16.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        } else {
                            Modifier
                        }
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if(totalCount < 10) {
                    repeat(5) { index ->
                        ReviewCountItem(
                            ratio = sampleRatios[index],
                            reviewCount = sampleReviewCounts[index]
                        )
                    }
                } else {
                    sortedReviewCount.forEach {
                        val ratio by remember(totalCount) { derivedStateOf {
                            if (totalCount > 0) it.count / totalCount.toFloat() else 0f
                        } }

                        ReviewCountItem(
                            ratio = ratio,
                            reviewCount = it
                        )
                    }
                }
            }

            if(totalCount < 10) {
                Box(
                    modifier = Modifier
                        .matchParentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "등록된 리뷰 10개 이상 부터 \n조회가 가능합니다.",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 4, color = GuideColor),
                        modifier = Modifier
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                            .background(color = Color.White, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                            .padding(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "상위 5개의 항목이 대표 항목으로 노출돼요.",
            style = storeMeTextStyle(FontWeight.Normal, 0),
            color = Color.Black
        )
    }
}

@Composable
fun ReviewCountItem(
    ratio: Float,
    reviewCount: ReviewCount
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .shadow(4.dp, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .background(color = Color.White, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .clip(shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(ratio)
                .fillMaxHeight()
                .background(color = OnboardingSelectedIndicatorColor.copy(alpha = 0.3f))
                .align(Alignment.CenterStart)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = reviewCount.emoji,
                style = storeMeTextStyle(FontWeight.ExtraBold, 8)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = reviewCount.text,
                style = storeMeTextStyle(FontWeight.Bold, 0),
                modifier = Modifier
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = reviewCount.count.toString(),
                style = storeMeTextStyle(FontWeight.ExtraBold, 2)
            )
        }
    }
}

/**
 * 리뷰 목록 Composable
 * @param accountType 현재 로그인 계정 타입
 * @param onClickMenu 메뉴 클릭시 메뉴 이름 반환
 * @param onEditReview 리뷰 수정 (ONLY CUSTOMER)
 * @param onDeleteReview 리뷰 삭제 (ONLY CUSTOMER)
 * @param onEditReply 답글 수정 (ONLY OWNER)
 * @param onDeleteReply 답글 삭제 (ONLY OWNER)
 */
@Composable
fun StoreReviews(
    accountType: AccountType,
    storeName: String,
    reviews: List<ReviewData>,
    onClickMenu: (String) -> Unit,
    onEditReview: (ReviewData) -> Unit,
    onDeleteReview: (ReviewData) -> Unit,
    onAddReply: (ReviewData) -> Unit,
    onEditReply: (ReviewData) -> Unit,
    onDeleteReply: (ReviewData) -> Unit,
    onReportReview: (ReviewData) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showEditAndDeleteBottomSheetCondition by remember { mutableStateOf(false) }
    var showAddReplyBottomSheetCondition by remember { mutableStateOf(false) }
    var selectedReview by remember { mutableStateOf<ReviewData?>(null) }

    Text(
        text = "전체 후기",
        style = storeMeTextStyle(FontWeight.ExtraBold, 6),
        color = Color.Black
    )

    Spacer(modifier = Modifier.height(20.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        reviews.forEach { review ->
            ReviewItem(
                review = review,
                storeName = storeName,
                onClickMenu = {
                    if(review.purchasedMenus.isNotEmpty()) {
                        onClickMenu(review.purchasedMenus.first())
                    }
                },
                onClickReviewMenu = {
                    when(accountType) {
                        AccountType.CUSTOMER -> {
                            showEditAndDeleteBottomSheetCondition = true
                            selectedReview = review
                        }
                        AccountType.OWNER -> {
                            showAddReplyBottomSheetCondition = true
                            selectedReview = review
                        }
                    }
                },
                onClickReplyMenu = {
                    when(accountType) {
                        AccountType.CUSTOMER -> {  }
                        AccountType.OWNER -> {
                            showEditAndDeleteBottomSheetCondition = true
                            selectedReview = review
                        }
                    }
                }
            )
        }
    }

    fun dismissBottomSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if(!sheetState.isVisible) {
                showEditAndDeleteBottomSheetCondition = false
                showAddReplyBottomSheetCondition = false
            }
        }
    }

    if(showEditAndDeleteBottomSheetCondition) {
        EditDeleteBottomSheet(
            sheetState = sheetState,
            onDismiss = {
                dismissBottomSheet()
            },
            onClickEdit = {
                when(accountType) {
                    AccountType.CUSTOMER -> {
                        selectedReview?.let { onEditReview(it) }
                    }
                    AccountType.OWNER -> {
                        selectedReview?.let { onEditReply(it) }
                    }
                }

                dismissBottomSheet()
            },
            onClickDelete = {
                when(accountType) {
                    AccountType.CUSTOMER -> {
                        selectedReview?.let { onDeleteReview(it) }
                        selectedReview = null
                    }
                    AccountType.OWNER -> {
                        selectedReview?.let { onDeleteReply(it) }
                        selectedReview = null
                    }
                }

                dismissBottomSheet()
            }
        )
    }

    if(showAddReplyBottomSheetCondition) {
        AddReplyBottomSheet(
            sheetState = sheetState,
            onDismiss = {
                dismissBottomSheet()
            },
            onClickAddReply = {
                selectedReview?.let { onAddReply(it) }
                dismissBottomSheet()
            },
            onClickReport = {
                selectedReview?.let { onReportReview(it) }
                dismissBottomSheet()
            }
        )
    }
}

/**
 * 리뷰 항목 Composable
 * @param review 리뷰 데이터
 * @param onClickMenu 메뉴 클릭
 * @param onClickReviewMenu 리뷰 설정 클릭
 * @param onClickReplyMenu 리뷰 답글 설정 클릭
 */
@Composable
fun ReviewItem(
    modifier: Modifier = Modifier,
    storeName: String,
    review: ReviewData,
    onClickMenu: () -> Unit,
    onClickReviewMenu: () -> Unit,
    onClickReplyMenu: () -> Unit
) {
    //이미지 상세보기
    var showImageDetailDialog by remember { mutableStateOf(false) }
    //comment 확장 상태
    var isCommentExpanded by remember { mutableStateOf(false) }
    //Reply 확장 상태
    var isReplyExpanded by remember { mutableStateOf(false) }
    //Review 내용 확장 상태
    var isSelectedReviewsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //상단 사용자 정보 및 메뉴
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileImage(
                accountType = AccountType.CUSTOMER,
                url = review.customerInfoData.profileImage,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
            )

            FlowRow (
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = review.customerInfoData.nickname,
                    style = storeMeTextStyle(FontWeight.Bold, 0)
                )

                Text(
                    text = "•",
                    style = storeMeTextStyle(FontWeight.Thin, 0)
                )

                Text(
                    text = review.createdAt.toTimeAgo(),
                    style = storeMeTextStyle(FontWeight.Bold, 0)
                )

                if(review.updatedAt != review.createdAt) {
                    Text(
                        text = "수정됨",
                        style = storeMeTextStyle(FontWeight.Bold, 0)
                    )
                }
            }

            IconButton(
                onClick = {
                    onClickReviewMenu()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "메뉴",
                    modifier = Modifier.size(16.dp),
                    tint = GuideColor
                )
            }
        }

        //중단 리뷰 내용
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 44.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if(review.comment.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = { isCommentExpanded = !isCommentExpanded },
                                interactionSource = null,
                                indication = null
                            ),
                        text = review.comment,
                        style = storeMeTextStyle(FontWeight.Bold, 0),
                        maxLines = if(isCommentExpanded) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                ContextualFlowRow (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = { isSelectedReviewsExpanded = !isSelectedReviewsExpanded },
                            interactionSource = null,
                            indication = null
                        ),
                    maxLines = if(isSelectedReviewsExpanded) Int.MAX_VALUE else 1,
                    overflow = ContextualFlowRowOverflow.expandIndicator {
                        TextBox(
                            text = "+${totalItemCount - shownItemCount}",
                            style = storeMeTextStyle(FontWeight.Normal, -2, FinishedColor),
                            boxColor = SubHighlightColor
                        )
                    },
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    itemCount = review.selectedReviews.size
                ) { index ->
                    TextBox(
                        text = review.selectedReviews[index],
                        style = storeMeTextStyle(FontWeight.Normal, -2, FinishedColor),
                        boxColor = SubHighlightColor
                    )
                }
            }

            if(review.images.isNotEmpty()) {
                SkeletonAsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)),
                    imageUrl = review.images.first(),
                    contentScale = ContentScale.Crop,
                ) {
                    showImageDetailDialog = true
                }
            }
        }

        if(review.purchasedMenus.isNotEmpty()) {
            val leftMenuCount by remember { mutableStateOf(review.purchasedMenus.size - 1) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 44.dp)
                    .border(width = 1.dp, color = ExpiredColor, shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                    .clip(shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
                    .clickable { onClickMenu() }
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "메뉴",
                        style = storeMeTextStyle(FontWeight.Bold, -1, FinishedColor)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = review.purchasedMenus.first(),
                        style = storeMeTextStyle(FontWeight.Bold, -1, GuideColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    if(leftMenuCount > 0) {
                        Text(
                            text = "외 $leftMenuCount",
                            style = storeMeTextStyle(FontWeight.Bold, -1, GuideColor)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "더보기 아이콘",
                        modifier = Modifier.size(12.dp),
                        tint = ExpiredColor
                    )
                }
            }
        }

        if(review.reply != null) {
            Column(
                modifier = Modifier
                    .padding(start = 44.dp)
                    .background(
                        color = OwnerReplyBoxColor,
                        shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)
                    )
                    .padding(start = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = storeName,
                        style = storeMeTextStyle(FontWeight.Bold, -1),
                        modifier = Modifier
                            .weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        onClick = {
                            onClickReplyMenu()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "메뉴",
                            modifier = Modifier
                                .size(16.dp),
                            tint = GuideColor
                        )
                    }
                }

                Text(
                    text = review.reply.text,
                    style = storeMeTextStyle(FontWeight.Normal, -2),
                    maxLines = if(isReplyExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clickable(
                            onClick = { isReplyExpanded = !isReplyExpanded },
                            interactionSource = null,
                            indication = null
                        )
                )
            }
        }
    }

    if(showImageDetailDialog) {
        ImageDetailDialog(
            images = review.images,
            onDismiss = { showImageDetailDialog = false }
        )
    }
}

@Composable
fun EditDeleteBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit
) {
    DefaultBottomSheet(
        hasDeleteButton = false,
        onDismiss = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onClickEdit,
            ) {
                Text(
                    text = "수정하기",
                    style = storeMeTextStyle(FontWeight.Bold, 2, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onClickDelete
            ) {
                Text(
                    text = "삭제하기",
                    style = storeMeTextStyle(FontWeight.Bold, 2),
                    color = ErrorColor,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * OWNER 가 Review Menu 선택 시 나오는 BottomSheet
 */
@Composable
fun AddReplyBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClickAddReply: () -> Unit,
    onClickReport: () -> Unit
) {
    DefaultBottomSheet(
        hasDeleteButton = false,
        onDismiss = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onClickAddReply,
            ) {
                Text(
                    text = "답글추가",
                    style = storeMeTextStyle(FontWeight.Bold, 2, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onClickReport
            ) {
                Text(
                    text = "신고하기",
                    style = storeMeTextStyle(FontWeight.Bold, 2, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun WriteReplyBox(
    boxScope: BoxScope,
    selectedReview: ReviewData,
    storeInfoData: StoreInfoData,
    onPostReply: (String) -> Unit,
    onPatchReply: (String) -> Unit
) {
    var replyText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if(selectedReview.reply != null) {
            replyText = selectedReview.reply.text
        }
    }

    with(boxScope) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            event.changes.forEach { it.consume() }
                        }
                    }
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(color = Color.White)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReviewItem(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                storeName = storeInfoData.storeName,
                review = selectedReview,
                onClickMenu = {  },
                onClickReviewMenu = {  },
                onClickReplyMenu = {  }
            )

            DefaultHorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SimpleTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
                        .heightIn(max = 320.dp),
                    value = replyText,
                    onValueChange = { replyText = it },
                    placeholderText = "답글을 입력해주세요.",
                    textStyle = storeMeTextStyle(FontWeight.Normal, 0),
                    singleLine = false,
                    minLines = 1
                )

                IconButton(
                    onClick = {
                        if(selectedReview.reply == null) {
                            //추가
                            onPostReply(replyText)
                        } else {
                            //수정
                            onPatchReply(replyText)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = "답글 작성",
                        modifier = Modifier.size(16.dp),
                        tint = GuideColor
                    )
                }
            }
        }
    }
}