package com.store_me.storeme.data

/**
 * Image Banner Data
 * @param bannerId 배너에 대한 ID 정보
 * @param title 배너 제목
 * @param imageUrl 배너 이미지 정보
 */
data class BannerData(
    val bannerId: String,
    val title: String,
    val imageUrl: String
)

/**
 * Banner Detail Data
 * @param bannerId 배너에 대한 ID 정보
 * @param title 배너 제목
 * @param startDate 시작 날짜
 * @param endDate 종료 날짜
 * @param content 이미지, 텍스트가 올 수 있는 내용
 * @sample
 * val bannerDetails = BannerDetailData(
 *     bannerId = "BN001",
 *     title = "새해 맞이 대세일",
 *     startDate = "2024-01-01",
 *     endDate = "2024-01-31",
 *     content = listOf(
 *         BannerContent.TextContent("신년 맞이 할인!"),
 *         BannerContent.ImageContent("https://example.com/image1.jpg", "신년 할인 이미지")
 *     )
 * )
 *
 * @sample
 * JSON
 * {
 *   "bannerId": "BN001",
 *   "title": "새해 맞이 대세일",
 *   "startDate": "2024-01-01",
 *   "endDate": "2024-01-31",
 *   "content": [
 *     {
 *       "type": "TextContent",
 *       "text": "신년 맞이 할인!"
 *     },
 *     {
 *       "type": "ImageContent",
 *       "imageUrl": "https://example.com/image1.jpg",
 *       "description": "신년 할인 이미지"
 *     }
 *   ]
 * }
 *
 */
data class BannerDetailData(
    val bannerId: String,
    val title: String,
    val subTitle: String?,
    val startDate: String,
    val endDate: String,
    val content: List<BannerContent>
)

sealed class BannerContent {
    data class TextContent(val text: String) : BannerContent()
    data class ImageContent(val imageUrl: String) : BannerContent()
}