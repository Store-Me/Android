@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.store_me.storeme.ui.store_setting.review

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.ReviewComment
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.hasMenu
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.KeyBoardInputField
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.component.SubTitleSection
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.CircleProfileImage
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.OwnerReplyBoxColor
import com.store_me.storeme.ui.theme.ReviewCountTextColor
import com.store_me.storeme.ui.theme.ReviewMenuBorderColor
import com.store_me.storeme.ui.theme.ReviewMenuContentColor
import com.store_me.storeme.ui.theme.ReviewMenuTitleColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.SizeUtils
import kotlinx.coroutines.launch

val LocalReviewSettingViewModel = staticCompositionLocalOf<ReviewSettingViewModel> {
    error("No ReviewSettingViewModel")
}

@Composable
fun ReviewSettingScreen(
    navController: NavController,
    reviewSettingViewModel: ReviewSettingViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }

    val replyText by reviewSettingViewModel.replyText.collectAsState()

    var isWritingReply by remember { mutableStateOf(false) }

    var showWarnDialog by remember { mutableStateOf(false) }

    BackHandler {
        when {
            //경고창 보이는 상태
            showWarnDialog -> { showWarnDialog = false }

            //답글 작성 중 상태
            isWritingReply -> {
                if(replyText.isNotEmpty())
                    showWarnDialog = true
                else {
                    isWritingReply = false
                }
            }

            else -> {
                navController.popBackStack()
            }
        }
    }

    CompositionLocalProvider(LocalReviewSettingViewModel provides reviewSettingViewModel) {
        Scaffold (
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = White,
            topBar = { ReviewSettingTopLayout(navController, scrollBehavior) },
            snackbarHost = { SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { StoreMeSnackbar(snackbarData = it) }
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
                    ) {
                        item { Spacer(modifier = Modifier.height(20.dp)) }

                        item { TopReviewSection() }

                        item {
                            AllReviewSection(navController, snackbarHostState) {
                                isWritingReply = true
                            }
                        }
                    }

                    if (isWritingReply) {
                        KeyBoardInputField(
                            text = replyText,
                            placeholderText = "답글을 입력해 주세요.",
                            onValueChange = { reviewSettingViewModel.updateReplyText(it) },
                            onDismiss = {
                                if(replyText.isNotEmpty()){
                                    showWarnDialog = true
                                } else {
                                    isWritingReply = false
                                }
                            },
                            onSend = {  }
                        )
                    }
                }

                if(showWarnDialog) {
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
                            reviewSettingViewModel.clearReplyText()
                        }
                    )
                }
            }
        )        
    }
}

@Composable
fun AllReviewSection(navController: NavController, snackbarHostState: SnackbarHostState, onWriteReply: (String) -> Unit) {
    val reviewSettingViewModel = LocalReviewSettingViewModel.current

    val reviewComment by reviewSettingViewModel.reviewComment.collectAsState()

    val scope = rememberCoroutineScope()

    //답글 수정 종료 경고 dialog 상태
    var showWarnDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        SubTitleSection(text = "전체 후기")
        
        Spacer(modifier = Modifier.height(20.dp))

        reviewComment.forEach {
            //답글 작성 중 상태
            Column {
                ReviewItem(
                    it,
                    onClickMenu = { menuName ->
                        val menuExist = Auth.menuCategoryList.value.any { category ->
                            category.hasMenu(menuName) != -1
                        }
                        when (menuExist) {
                            true -> { NavigationUtils().navigateOwnerNav(navController, StoreHomeItem.MENU, additionalData = menuName) }
                            false -> { scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = "$menuName 메뉴가 존재하지 않습니다.",
                                    actionLabel = "확인",
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Short)
                            } }
                        }
                    },
                    onWriteReply = { onWriteReply(it.commentId) }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ReviewItem(reviewComment: ReviewComment, onClickMenu: (String) -> Unit, onWriteReply: () -> Unit) {

    val density = LocalDensity.current

    val interactionSource = remember { MutableInteractionSource() }

    //comment Text 내용 확장 상태
    var isTextExpanded by remember { mutableStateOf(false) }
    //Review 내용 확장 상태
    var isReviewExpanded by remember { mutableStateOf(false) }

    //보여줄 리뷰 개수
    val takeValue = if(!isReviewExpanded) 1 else reviewComment.selectedReviews.size

    //Bottom Sheet 상태
    var showCommentMenu by remember { mutableStateOf(false) }
    var showReplyMenu by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleProfileImage(reviewComment.userData.profileImage, 40, isUser = true)

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${reviewComment.userData.nickName} · ${DateTimeUtils().datetimeAgo(reviewComment.dateTime)}",
                    style = storeMeTextStyle(FontWeight.Bold, 0)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "메뉴 아이콘",
                tint = ReviewMenuContentColor,
                modifier = Modifier
                    .size(SizeUtils().textSizeToDp(density, 0))
                    .clickable(
                        onClick = { showCommentMenu = true },
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false),
                    )
            )
        }

        //리뷰 comment 내용이 있는 경우
        if(reviewComment.commentText.isNotEmpty()) {
            Text(
                text = reviewComment.commentText,
                style = storeMeTextStyle(FontWeight.Bold, 0),
                maxLines = if(isTextExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 50.dp, end = SizeUtils().textSizeToDp(density, 0))
                    .clickable(
                        onClick = { isTextExpanded = !isTextExpanded },
                        interactionSource = null,
                        indication = null
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        //사용자가 선택한 Review 내용
        FlowRow(
            modifier = Modifier
                .padding(start = 50.dp)
                .clickable(
                    onClick = { isReviewExpanded = !isReviewExpanded },
                    indication = null,
                    interactionSource = null,
                ),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            reviewComment.selectedReviews.take(takeValue).forEach {
                Text(
                    text = it,
                    style = storeMeTextStyle(FontWeight.Normal, -2),
                    modifier = Modifier
                        .background(
                            color = EditButtonColor,
                            shape = RoundedCornerShape(3.dp)
                        )
                        .padding(5.dp)
                )
            }

            if(!isReviewExpanded && reviewComment.selectedReviews.size > 1) {
                val leftCount = reviewComment.selectedReviews.size - 1

                Text(
                    text = "+$leftCount",
                    style = storeMeTextStyle(FontWeight.Normal, -2),
                    modifier = Modifier
                        .background(
                            color = EditButtonColor,
                            shape = RoundedCornerShape(3.dp)
                        )
                        .padding(5.dp)
                )
            }
        }

        if(reviewComment.purchasedMenu.isNotEmpty()){
            val leftMenuCount = reviewComment.purchasedMenu.size - 1

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .padding(start = 50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        color = ReviewMenuBorderColor,
                        width = 1.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onClickMenu(reviewComment.purchasedMenu[0]) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                ) {
                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "메뉴",
                        style = storeMeTextStyle(FontWeight.Bold, -1),
                        color = ReviewMenuTitleColor
                    )

                    Spacer(modifier = Modifier.width(10.dp))


                    Text(
                        text = reviewComment.purchasedMenu[0] + if(leftMenuCount > 0) " 외 $leftMenuCount" else "",
                        style = storeMeTextStyle(FontWeight.Bold, -1),
                        color = ReviewMenuContentColor
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = "더보기 아이콘",
                        modifier = Modifier.size(SizeUtils().textSizeToDp(density, -1)),
                        tint = ReviewMenuBorderColor
                    )

                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
        }

        reviewComment.replies.forEach {
            val replyInteractionSource = remember { MutableInteractionSource() }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .padding(start = 50.dp)
                    .background(
                        color = OwnerReplyBoxColor,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.storeName,
                            style = storeMeTextStyle(FontWeight.Bold, -1),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "메뉴 아이콘",
                            tint = ReviewMenuContentColor,
                            modifier = Modifier
                                .size(SizeUtils().textSizeToDp(density, 0))
                                .clickable(
                                    onClick = { showReplyMenu = true },
                                    interactionSource = replyInteractionSource,
                                    indication = ripple(bounded = false),
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = it.replyText,
                        style = storeMeTextStyle(FontWeight.Normal, -2),
                        modifier = Modifier.padding(end = SizeUtils().textSizeToDp(density, 0))
                    )
                }


            }
        }

        if(showCommentMenu) {
            DefaultBottomSheet(
                hasDeleteButton = false,
                onDismiss = { showCommentMenu = false },
                sheetState = sheetState
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "답글작성",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 4),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable {
                                onWriteReply()
                                showCommentMenu = false
                            }
                            .padding(vertical = 20.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "신고하기",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 4),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable { }
                            .padding(vertical = 20.dp)
                            .fillMaxWidth())
                }

            }
        }

        if(showReplyMenu) {
            DefaultBottomSheet(
                hasDeleteButton = false,
                onDismiss = { showReplyMenu = false },
                sheetState = sheetState
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "수정하기",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 4),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable { }
                            .padding(vertical = 20.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "삭제하기",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 4),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable { }
                            .padding(vertical = 20.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun TopReviewSection() {
    val density = LocalDensity.current

    val reviewSettingViewModel = LocalReviewSettingViewModel.current

    val totalCount by reviewSettingViewModel.totalReviews.collectAsState()
    val storeReviewData by reviewSettingViewModel.storeReviewData.collectAsState()

    var takeValue by remember { mutableStateOf(5) }

    fun showMoreReview(){
        //보여지는 종류가 더 적다면
        if(takeValue < storeReviewData.reviewCount.size) {
            val leftCount = storeReviewData.reviewCount.size - takeValue

            //최대 5개씩 추가로 보여줌
            takeValue += if(leftCount >= 5) 5 else leftCount
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SubTitleSection(text = "내 가게 후기 $totalCount")

        storeReviewData.reviewCount
            .sortedByDescending{ it.count }
            .take(takeValue)
            .forEach {
                val ratio = if (totalCount > 0) it.count / totalCount.toFloat() else 0f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SizeUtils().textSizeToDp(density, 4, 24))
                        .background(
                            color = EditButtonColor.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(6.dp)
                        ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(ratio)
                            .fillMaxHeight()
                            .background(
                                color = EditButtonColor,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .align(Alignment.CenterStart)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp, start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.emoji,
                                style = storeMeTextStyle(FontWeight.Bold, 3),
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = it.text,
                                style = storeMeTextStyle(FontWeight.Bold, 1),
                            )
                        }

                        Text(
                            text = it.count.toString(),
                            style = storeMeTextStyle(FontWeight.ExtraBold, 1),
                            color = ReviewCountTextColor
                        )
                    }
                }

            }

        if(takeValue < storeReviewData.reviewCount.size) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showMoreReview() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = "더보기 아이콘",
                    modifier = Modifier
                        .size(18.dp),
                    tint = ReviewCountTextColor
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { takeValue = 5 },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_up),
                    contentDescription = "축소 아이콘",
                    modifier = Modifier
                        .size(18.dp),
                    tint = ReviewCountTextColor
                )
            }
        }
    }
}

@Composable
fun ReviewSettingTopLayout(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = {
            TitleWithDeleteButton(navController = navController, title = "스토어 리뷰", isInTopAppBar = true)
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White,
            scrolledContainerColor = White
        )
    )
}
