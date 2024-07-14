package com.store_me.storeme.data

/**
 * 사용자 Data Class
 * @param userId 사용자 아이디
 * @param nickName 닉네임
 * @param profileImage 프로필 사진
 */
data class UserData(
    val userId: String,
    val nickName: String,
    val profileImage: String?
)