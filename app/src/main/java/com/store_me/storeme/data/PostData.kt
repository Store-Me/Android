package com.store_me.storeme.data


/**
 * 기본 Post 정보
 * @param postId 게시글 ID 정보
 * @param storeInfoData 가게 정보
 * @param label label text
 * @param type Post Type
 * @param datetime 업로드 시각
 * @param imageList 업로드 이미지 URL 목록
 * @param title 제목
 * @param content 내용
 */
data class NormalPostWithStoreInfoData(
    val postId: String,
    val storeInfoData: StoreInfoData,
    val label: String,
    val type: PostType,

    val datetime: String,
    val imageList: List<String>?,
    val title: String,
    val content: String,
)

/**
 * 게시글 Type 정보
 * @property NORMAL 일반 SNS 형태의 게시글
 * @property BLOG 사진과 텍스트가 나열 된 블로그 형태의 게시글
 * @property NOTICE 공지 게시글
 * @property VOTE 투표 게시글
 * @property SURVEY 설문 게시글
 * @property EVENT 이벤트 게시글
 */
enum class PostType{
    NORMAL, BLOG, NOTICE, VOTE, SURVEY, EVENT
}