package com.store_me.storeme.ui.home.owner.tab

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.store_me.storeme.data.store.review.ReviewData
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.store_setting.review.ReviewViewModel
import com.store_me.storeme.ui.store_setting.review.StoreReviewCount
import com.store_me.storeme.ui.store_setting.review.StoreReviews
import com.store_me.storeme.ui.store_setting.review.WriteReplyBox
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import kotlinx.coroutines.launch

@Composable
fun ReviewTab(
    reviewViewModel: ReviewViewModel,
    onClickMenu: (String) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val auth = LocalAuth.current
    val loadingViewModel = LocalLoadingViewModel.current
    val storeDataViewModel = LocalStoreDataViewModel.current
    val accountType by auth.accountType.collectAsState()
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val reviewCount by reviewViewModel.reviewCounts.collectAsState()
    val reviews by reviewViewModel.reviews.collectAsState()

    var isWritingReply by remember { mutableStateOf(false) }

    var selectedReview by remember { mutableStateOf<ReviewData?>(null) }

    var showWarnDialog by remember { mutableStateOf(false) }

    BackHandler {
        when {
            isWritingReply -> {
                showWarnDialog = true
            }
            else -> {
                onBack()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            StoreReviewCount(
                reviewCount = reviewCount
            )

            Spacer(modifier = Modifier.height(40.dp))

            StoreReviews(
                accountType = accountType,
                storeName = storeInfoData!!.storeName,
                reviews = reviews,
                onClickMenu = {
                    if(storeDataViewModel.menuCategories.value.any { category ->
                            category.menus.any { menu -> menu.name == it }
                        }) {
                        onClickMenu(it)
                    } else {
                        scope.launch {
                            ErrorEventBus.errorFlow.emit("$it 메뉴가 존재하지 않습니다.")
                        }
                    }
                },
                onEditReview = {
                    //TODO EDIT REVIEW
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
                    loadingViewModel.showLoading()
                    reviewViewModel.deleteReviewReply(it.id)
                },
                onReportReview = {
                    //TODO 신고하기
                }
            )
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