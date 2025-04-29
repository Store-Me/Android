package com.store_me.storeme.data.review

import com.google.firebase.Timestamp

/**
 * 특정 리뷰의 답글 데이터
 * @property text 답글 내용
 * @property createdAt 답글 작성 시간
 * @property updatedAt 답글 수정 시간
 */
data class ReplyData(
    val text: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)
