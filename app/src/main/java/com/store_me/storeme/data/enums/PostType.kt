package com.store_me.storeme.data.enums


/**
 * 게시글 Type 정보
 * @property NORMAL 일반 게시글
 * @property COUPON 쿠폰 홍보 게시글
 * @property EVENT 이벤트 게시글
 * @property SURVEY 투표, 설문 게시글
 * @property NOTICE 공지 게시글
 * @property STORY 스토어 스토리 게시글
 */
enum class PostType(val displayName: String, val description: String?) {
    NORMAL(
        displayName = "일반 게시글 작성",
        description = "가게 관련 소식이나 정보를 작성 해보세요."
    ),
    COUPON(
        displayName = "쿠폰 홍보",
        description = null
    ),
    SURVEY(
        displayName = "설문",
        description = null
    ),
    VOTE(
        displayName = "투표",
        description = null
    ),
    NOTICE(
        displayName = "공지 게시글",
        description = null
    ),
    STORY(
        displayName = "스토어 스토리",
        description = "가게와 관련된 짧은 영상을 업로드 해보세요."
    )
}