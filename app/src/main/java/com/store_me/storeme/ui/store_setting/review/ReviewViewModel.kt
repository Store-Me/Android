package com.store_me.storeme.ui.store_setting.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store_me.storeme.data.enums.StoreCategory
import com.store_me.storeme.data.request.store.ReplyRequest
import com.store_me.storeme.data.request.store.ReviewRequest
import com.store_me.storeme.data.response.ReviewCountResponse
import com.store_me.storeme.data.review.ReviewCount
import com.store_me.storeme.data.review.ReviewData
import com.store_me.storeme.data.review.ReviewTextData
import com.store_me.storeme.repository.storeme.ReviewRepository
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {
    private val _reviewTexts = MutableStateFlow<List<ReviewTextData>>(emptyList())
    val reviewTexts: StateFlow<List<ReviewTextData>> = _reviewTexts

    private val _reviewCounts = MutableStateFlow<List<ReviewCount>>(emptyList())
    val reviewCounts: StateFlow<List<ReviewCount>> = _reviewCounts

    private val _reviews = MutableStateFlow<List<ReviewData>>(emptyList())
    val reviews: StateFlow<List<ReviewData>> = _reviews

    private fun updateReviewCount(reviewCountResponse: Map<String, ReviewCountResponse>) {
        val reviewCounts = reviewCountResponse.map { (text, response) ->
            ReviewCount(
                text = text,
                emoji = response.emoji,
                count = response.count
            )
        }
        _reviewCounts.value = reviewCounts
    }

    private fun updateReviewTexts(reviewTexts: List<ReviewTextData>) {
        _reviewTexts.value = reviewTexts
    }

    private fun updateReviews(reviews: List<ReviewData>) {
        _reviews.value = reviews
    }

    private fun addNewReview(review: ReviewData) {
        val currentReviews = _reviews.value.toMutableList()
        currentReviews.add(0, review)
        _reviews.value = currentReviews
    }

    private fun deleteOldReview(reviewId: String) {
        val currentReviews = _reviews.value.toMutableList()
        val reviewToDelete = currentReviews.find { it.id == reviewId }

        if (reviewToDelete != null) {
            _reviews.value = currentReviews
        }
    }

    private fun patchOldReview(reviewData: ReviewData) {
        val currentReviews = _reviews.value.toMutableList()
        val index = currentReviews.indexOfFirst { it.id == reviewData.id }
        if (index != -1) {
            currentReviews[index] = reviewData
            _reviews.value = currentReviews
        }
    }

    private fun deleteReply(reviewId: String) {
        val currentReviews = _reviews.value.toMutableList()
        val index = currentReviews.indexOfFirst { it.id == reviewId }
        if (index != -1) {
            currentReviews[index] = currentReviews[index].copy(reply = null)
            _reviews.value = currentReviews
        }
    }

    //리뷰 Text GET
    fun getReviewTexts(category: StoreCategory) {
        viewModelScope.launch {
            val response = reviewRepository.getStoreReviewTexts(
                category = category
            )

            response.onSuccess {
                updateReviewTexts(it.reviews)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }

    //리뷰 개수 GET
    fun getReviewCount() {
        viewModelScope.launch {
            val response = reviewRepository.getStoreReviewCounts()

            response.onSuccess {
                updateReviewCount(it)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }

    //리뷰 GET
    fun getReviews() {
        viewModelScope.launch {
            val response = reviewRepository.getStoreReviews()

            response.onSuccess {
                updateReviews(it)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }

    //리뷰 추가
    fun postReview(reviewRequest: ReviewRequest) {
        viewModelScope.launch {
            val response = reviewRepository.postStoreReview(
                reviewRequest = reviewRequest
            )
            response.onSuccess {
                addNewReview(it.result)
                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }

    //리뷰 수정
    fun patchReview(reviewId: String, reviewRequest: ReviewRequest) {
        viewModelScope.launch {
            val response = reviewRepository.patchStoreReview(
                reviewId = reviewId,
                reviewRequest = reviewRequest
            )
            response.onSuccess {
                patchOldReview(it.result)
                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }

    //리뷰 삭제
    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            val response = reviewRepository.deleteStoreReview(
                reviewId = reviewId
            )
            response.onSuccess {
                deleteOldReview(reviewId)
                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }

    //답글 추가
    fun postReviewReply(reviewId: String, text: String) {
        viewModelScope.launch {
            if(text.isEmpty()) {
                ErrorEventBus.errorFlow.emit("답글 내용을 입력해주세요.")
                return@launch
            }

            val response = reviewRepository.postStoreReviewReply(
                reviewId = reviewId,
                replyRequest = ReplyRequest(text = text)
            )

            response.onSuccess {
                patchOldReview(it.result)
                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }

    //답글 수정
    fun patchReviewReply(reviewId: String, text: String) {
        viewModelScope.launch {
            val response = reviewRepository.patchStoreReviewReply(
                reviewId = reviewId,
                replyRequest = ReplyRequest(text = text)
            )

            response.onSuccess {
                patchOldReview(it.result)
                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }

    //답글 삭제
    fun deleteReviewReply(reviewId: String) {
        viewModelScope.launch {
            val response = reviewRepository.deleteStoreReviewReply(
                reviewId = reviewId
            )

            response.onSuccess {
                deleteReview(reviewId)
                SuccessEventBus.successFlow.emit(it.message)
            }.onFailure {
                ErrorEventBus.errorFlow.emit(it.message)
            }
        }
    }
}