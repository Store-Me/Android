package com.store_me.storeme.data.review

import com.google.firebase.Timestamp
import com.store_me.storeme.data.customer.CustomerInfoData

/**
 * 가게 리뷰 데이터
 * @property id 리뷰 아이디
 * @property customerInfoData 리뷰를 작성한 손님 정보
 * @property selectedReviews 선택한 리뷰
 * @property purchasedMenus 구매한 메뉴
 * @property comment 리뷰 내용
 * @property images 리뷰 이미지
 * @property createdAt 리뷰 작성 시간
 * @property updatedAt 리뷰 수정 시간
 * @property reply 리뷰에 대한 답글
 */
data class ReviewData(
    val id: String,
    val customerInfoData: CustomerInfoData,
    val selectedReviews: List<String>,
    val purchasedMenus: List<String>,
    val comment: String,
    val images: List<String>,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val reply: ReplyData?
)
