package com.store_me.storeme.ui.store_setting.review

import androidx.lifecycle.ViewModel
import com.store_me.storeme.data.ReviewComment
import com.store_me.storeme.data.ReviewCount
import com.store_me.storeme.data.ReviewEmojis
import com.store_me.storeme.data.ReviewReply
import com.store_me.storeme.data.ReviewTexts
import com.store_me.storeme.data.StoreReviewData
import com.store_me.storeme.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReviewSettingViewModel: ViewModel() {
    /**
     * Sample Review Data
     */
    private val _storeReviewData = MutableStateFlow(
        StoreReviewData(
        reviewCount = listOf(
            ReviewCount(ReviewEmojis.cafeEmojis[0], ReviewTexts.cafeReviews[0], 50),
            ReviewCount(ReviewEmojis.cafeEmojis[1], ReviewTexts.cafeReviews[1], 87),
            ReviewCount(ReviewEmojis.cafeEmojis[2], ReviewTexts.cafeReviews[2], 867),
            ReviewCount(ReviewEmojis.cafeEmojis[3], ReviewTexts.cafeReviews[3], 7350),
            ReviewCount(ReviewEmojis.cafeEmojis[4], ReviewTexts.cafeReviews[4], 572),
            ReviewCount(ReviewEmojis.cafeEmojis[5], ReviewTexts.cafeReviews[5], 0),
            ReviewCount(ReviewEmojis.cafeEmojis[6], ReviewTexts.cafeReviews[6], 2),
            ReviewCount(ReviewEmojis.cafeEmojis[7], ReviewTexts.cafeReviews[7], 4),
            ReviewCount(ReviewEmojis.cafeEmojis[8], ReviewTexts.cafeReviews[8], 0),
            ReviewCount(ReviewEmojis.cafeEmojis[9], ReviewTexts.cafeReviews[9], 0)
        )
    )
    )
    val storeReviewData: StateFlow<StoreReviewData> = _storeReviewData

    private val _reviewComment = MutableStateFlow(listOf(
        ReviewComment(
            commentId = "C01",
            dateTime = "2024-09-11T10:00:00",
            userData = UserData(
                userId = "testUser1",
                nickName = "테스트유저1",
                profileImage = "https://via.placeholder.com/200x200"
            ),
            selectedReviews = listOf(
                ReviewTexts.cafeReviews[0],
                ReviewTexts.cafeReviews[1],
                ReviewTexts.cafeReviews[2],
                ReviewTexts.cafeReviews[3],
                ReviewTexts.cafeReviews[4]
            ),
            purchasedMenu = listOf(
                "이름1", "이름2"
            ),
            commentText = "이름1 메뉴와 이름2 메뉴를 주문하여 맛나게 먹었습니다. 스탬프도 적립하여 맛나게 먹어서 기분이 좋았습니다. 다음에 또 방문 예정입니다. 스탬프 5개를 모아 메뉴를 받을 거에요.",
            replies = listOf(
                ReviewReply(
                    replyId = "R01",
                    dateTime = "2024-09-12T10:00:00",
                    storeName = "YM ESPRESSO ROOM",
                    replyText = "맛있게 드셨다니 저도 기분이 좋습니다. 좋은 하루 보내세요 ^^"
                )
            )
        ),
        ReviewComment(
            commentId = "C02",
            dateTime = "2024-09-13T10:00:00",
            userData = UserData(
                userId = "testUser2",
                nickName = "테스트유저2",
                profileImage = "https://user-images.githubusercontent.com/14011726/94132137-7d4fc100-fe7c-11ea-8512-69f90cb65e48.gif"
            ),
            selectedReviews = listOf(
                ReviewTexts.cafeReviews[6],
                ReviewTexts.cafeReviews[7],
            ),
            purchasedMenu = listOf(
                "이름1"
            ),
            commentText = "이름1 메뉴가 맛있어서 좋았어요.",
            replies = emptyList()
        ),
        ReviewComment(
            commentId = "C03",
            dateTime = "2024-09-14T10:00:00",
            userData = UserData(
                userId = "testUser3",
                nickName = "테스트유저3",
                profileImage = "https://via.placeholder.com/200x200"
            ),
            selectedReviews = listOf(
                ReviewTexts.cafeReviews[1],
                ReviewTexts.cafeReviews[7],
            ),
            purchasedMenu = listOf(
                "이름1"
            ),
            commentText = "",
            replies = emptyList()
        ),
        ReviewComment(
            commentId = "C04",
            dateTime = "2024-09-15T10:00:00",
            userData = UserData(
                userId = "testUser4",
                nickName = "테스트유저4",
                profileImage = "https://via.placeholder.com/200x200"
            ),
            selectedReviews = listOf(
                ReviewTexts.cafeReviews[1]
            ),
            purchasedMenu = listOf(
                "이름1"
            ),
            commentText = "맛나요.",
            replies = emptyList()
        )
    ))
    val reviewComment: StateFlow<List<ReviewComment>> = _reviewComment

    private val _totalReviews = MutableStateFlow(storeReviewData.value.reviewCount.sumOf { it.count })
    val totalReviews: StateFlow<Int> = _totalReviews

    private val _replyText = MutableStateFlow("")
    val replyText: StateFlow<String> = _replyText

    fun updateReplyText(replyText: String) {
        _replyText.value = replyText
    }

    fun clearReplyText() {
        _replyText.value = ""
    }

}