package com.store_me.storeme.data

/**
 * 주소 검색 결과
 * @param roadAddress 도로명 주소
 * @param legalDong 법정동
 * @param administrativeDong 행정동
 * @param sigunguCode 시군구 코드
 */
data class DaumPostcodeResponse(
    val roadAddress: String,
    val legalDong: String,
    val administrativeDong: String,
    val sigunguCode: String
)
