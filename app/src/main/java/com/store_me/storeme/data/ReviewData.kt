package com.store_me.storeme.data

data class StoreReviewData(
    val reviewCount: List<ReviewCount>,
)

data class ReviewCount(
    val emoji: String,
    val text: String,
    val count: Int,
)

data class ReviewReply(
    val replyId: String,
    val dateTime: String,
    val storeName: String,
    val replyText: String,
)

data class ReviewComment(
    val commentId: String,
    val dateTime: String,
    val userData: UserData,
    val selectedReviews: List<String>,
    val purchasedMenu: List<String>,
    val commentText: String,
    val replies: List<ReviewReply>
)

object ReviewEmojis {
    val allEmojis = emptyList<String>()
    val restaurantEmojis = listOf(
        "\uD83C\uDF7D", // 음식이 맛있어요
        "\uD83D\uDE0A", // 사장님이 친절해요
        "\uD83C\uDFE0", // 분위기가 좋아요
        "\uD83D\uDCCB", // 메뉴가 다양해요
        "\uD83D\uDCB0", // 가격이 저렴해요
        "\uD83E\uDD57", // 재료가 신선해요
        "\uD83C\uDF5B", // 양이 많아요
        "\u2728",       // 식당이 깨끗해요
        "\uD83C\uDD7F", // 주차 공간이 넓어요
        "\u26A1"        // 음식이 빠르게 나와요
    )

    val cafeEmojis = listOf(
        "\u2615",       // 커피가 맛있어요
        "\uD83D\uDE0A", // 사장님이 친절해요
        "\uD83C\uDF70", // 디저트가 맛있어요
        "\uD83D\uDEB8", // 편안한 분위기에요
        "\uD83C\uDFB5", // 음악이 좋아요
        "\uD83D\uDDBC", // 인테리어가 멋져요
        "\uD83E\uDD2B", // 조용하고 집중하기 좋아요
        "\uD83E\uDE91", // 좌석이 편안해요
        "\uD83C\uDF05", // 뷰가 좋아요
        "\u2728"        // 카페가 깨끗해요
    )

    val beautyEmojis = listOf(
        "\uD83D\uDC87", // 사장님이 전문적이에요
        "\u2728",       // 시설이 깨끗해요
        "\uD83D\uDE0A", // 직원이 친절해요
        "\uD83D\uDCB0", // 가격이 저렴해요
        "\uD83C\uDF1F", // 시술 후 결과가 좋아요
        "\u231B",       // 대기 시간이 짧아요
        "\uD83D\uDCC5", // 예약이 편리해요
        "\uD83C\uDD95", // 최신 트렌드를 잘 알아요
        "\uD83E\uDDF4", // 제품이 고급스러워요
        "\uD83C\uDFE2"  // 시설이 현대적이에요
    )

    val medicalEmojis = listOf(
        "\uD83D\uDC68\u200D⚕️", // 의사분이 친절해요
        "\u2705",               // 진료가 정확해요
        "\uD83C\uDFE5",         // 깨끗한 시설이에요
        "\u231B",               // 대기 시간이 짧아요
        "\uD83D\uDCCB",         // 설명이 자세해요
        "\uD83D\uDC69\u200D⚕️", // 간호사분이 친절해요
        "\uD83E\uDE7A",         // 의료 장비가 최신이에요
        "\uD83D\uDEE3",         // 접근성이 좋아요
        "\u26A1",               // 응대가 빠르고 정확해요
        "\u2764"                // 환자 케어가 좋아요
    )

    val exerciseEmojis = listOf(
        "\uD83C\uDFCB️\u200D♂️️", // 시설이 좋고 깨끗해요
        "\uD83D\uDE0A",          // 강사가 친절해요
        "\uD83D\uDCAA",          // 운동하기 좋아요
        "\uD83C\uDFC3",          // 장비가 최신이에요
        "\uD83D\uDCC6",          // 프로그램이 다양해요
        "\uD83D\uDCA5",          // 운동 분위기가 활기차요
        "\u23F0",                // 강의 시간이 유연해요
        "\uD83D\uDCB0",          // 가격이 합리적이에요
        "\uD83D\uDCC2",          // 회원 관리가 철저해요
        "\uD83C\uDD7F"           // 주차 공간이 편리해요
    )

    val salonEmojis = listOf(
        "\uD83D\uDC87\u200D♀️", // 스타일링이 좋아요
        "\uD83D\uDE0A",          // 미용사가 친절해요
        "\u2728",               // 시설이 깨끗해요
        "\uD83D\uDCB5",          // 가격이 적당해요
        "\uD83D\uDCC5",          // 예약이 쉬워요
        "\uD83E\uDDF4",          // 헤어케어 제품이 좋아요
        "\uD83C\uDF08",          // 디자이너의 실력이 뛰어나요
        "\uD83E\uDE91",          // 편안한 의자가 있어요
        "\uD83D\uDEB8",          // 분위기가 좋고 편안해요
        "\uD83E\uDDFC"           // 서비스가 세심해요
    )
}

object ReviewTexts {
    val allReviews = emptyList<String>()
    val restaurantReviews = listOf(
        "음식이 맛있어요",
        "사장님이 친절해요",
        "분위기가 좋아요",
        "메뉴가 다양해요",
        "가격이 저렴해요",
        "재료가 신선해요",
        "양이 많아요",
        "식당이 깨끗해요",
        "주차 공간이 넓어요",
        "음식이 빠르게 나와요"
    )
    val cafeReviews = listOf(
        "커피가 맛있어요",
        "사장님이 친절해요",
        "디저트가 맛있어요",
        "편안한 분위기에요",
        "음악이 좋아요",
        "인테리어가 멋져요",
        "조용하고 집중하기 좋아요",
        "좌석이 편안해요",
        "뷰가 좋아요",
        "카페가 깨끗해요"
    )
    val beautyReviews = listOf(
        "사장님이 전문적이에요",
        "시설이 깨끗해요",
        "직원이 친절해요",
        "가격이 저렴해요",
        "시술 후 결과가 좋아요",
        "대기 시간이 짧아요",
        "예약이 편리해요",
        "최신 트렌드를 잘 알아요",
        "제품이 고급스러워요",
        "시설이 현대적이에요"
    )
    val medicalReviews = listOf(
        "의사분이 친절해요",
        "진료가 정확해요",
        "깨끗한 시설이에요",
        "대기 시간이 짧아요",
        "설명이 자세해요",
        "간호사분이 친절해요",
        "의료 장비가 최신이에요",
        "접근성이 좋아요",
        "응대가 빠르고 정확해요",
        "환자 케어가 좋아요"
    )
    val exerciseReviews = listOf(
        "시설이 좋고 깨끗해요",
        "강사가 친절해요",
        "운동하기 좋아요",
        "장비가 최신이에요",
        "프로그램이 다양해요",
        "운동 분위기가 활기차요",
        "강의 시간이 유연해요",
        "가격이 합리적이에요",
        "회원 관리가 철저해요",
        "주차 공간이 편리해요"
    )
    val salonReviews = listOf(
        "스타일링이 좋아요",
        "미용사가 친절해요",
        "시설이 깨끗해요",
        "가격이 적당해요",
        "예약이 쉬워요",
        "헤어케어 제품이 좋아요",
        "디자이너의 실력이 뛰어나요",
        "편안한 의자가 있어요",
        "분위기가 좋고 편안해요",
        "서비스가 세심해요"
    )
}


