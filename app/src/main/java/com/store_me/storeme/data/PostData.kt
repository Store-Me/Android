package com.store_me.storeme.data

import com.store_me.storeme.data.enums.post.PostType


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