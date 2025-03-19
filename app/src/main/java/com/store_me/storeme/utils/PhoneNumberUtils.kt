package com.store_me.storeme.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneNumberUtils {
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^010\\d{8}\$")
        return regex.matches(phoneNumber)
    }

    fun phoneNumberAddDashes(phoneNumber: String): String {
        return if (phoneNumber.length == 11 && phoneNumber.startsWith("010")) {
            phoneNumber.replaceFirst("(\\d{3})(\\d{4})(\\d{4})".toRegex(), "$1-$2-$3")
        } else {
            phoneNumber
        }
    }

    fun isValidStoreNumber(input: String): Boolean {
        val areaCodes = setOf(
            "02", "032", "042", "051", "052", "053", "062", "064", "031", "033", "041", "043", "054", "055", "061", "063"
        )

        return when {
            input.matches(Regex("^050\\d{1}\\d{8}\$")) -> true // 안심번호
            input.matches(Regex("^010\\d{8}\$")) -> true       // 휴대전화번호
            input.matches(Regex("^070\\d{8}\$")) -> true       // 인터넷전화번호
            areaCodes.any { input.matches(Regex("^$it\\d{7,8}\$")) } -> true // 유선전화
            else -> false
        }
    }

    fun getStorePhoneNumberAddDashes(storePhoneNumber: String): String {
        val areaCodes = setOf(
            "02", "032", "042", "051", "052", "053", "062", "064", "031", "033", "041", "043", "054", "055", "061", "063"
        )

        return when {
            storePhoneNumber.startsWith("050") -> formatCustom(storePhoneNumber, 4, 4, 4)
            storePhoneNumber.startsWith("010") || storePhoneNumber.startsWith("070") -> formatCustom(storePhoneNumber, 3, 4, 4)
            areaCodes.any { storePhoneNumber.startsWith(it) } -> {
                val areaCode = areaCodes.first { storePhoneNumber.startsWith(it) }
                formatCustom(storePhoneNumber, areaCode.length, 3, 4)
            }
            else -> storePhoneNumber
        }
    }

    /**
     * 일반적인 전화번호 포맷팅을 처리하는 함수
     * - first: 첫 번째 그룹 길이
     * - second: 두 번째 그룹 길이
     * - third: 세 번째 그룹 길이
     */
    private fun formatCustom(number: String, first: Int, second: Int, third: Int): String {
        if(first + second + third != number.length) {
            return number
        }

        val parts = mutableListOf<String>()

        if (number.length >= first) parts.add(number.substring(0, first))
        if (number.length > first) parts.add(number.substring(first, (first + second).coerceAtMost(number.length)))
        if (number.length > first + second) parts.add(number.substring(first + second, (first + second + third).coerceAtMost(number.length)))

        return parts.joinToString("-")
    }
}
class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = buildString {
            originalText.forEachIndexed { index, char ->
                append(char)
                if ((index == 2 && originalText.length > 3) || (index == 6 && originalText.length > 7)) {
                    append("-")
                }
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return when {
                        offset <= 3 -> offset // 첫 3자리까지 그대로
                        offset <= 7 -> offset + 1 // 중간에 대시 추가
                        else -> offset + 2 // 마지막 대시 추가
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return when {
                        offset <= 3 -> offset // 첫 3자리까지 그대로
                        offset <= 8 -> offset - 1 // 중간 대시 제거
                        else -> offset - 2 // 마지막 대시 제거
                    }
                }
            }
        )
    }
}

class StoreNumberVisualTransformation : VisualTransformation {

    private val areaCodes = setOf(
        "02", "032", "042", "051", "052", "053", "062", "064", "031", "033", "041", "043", "054", "055", "061", "063"
    )

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = buildString {
            when {
                originalText.startsWith("050") -> {
                    appendSafe(originalText, 0, 4)
                    if (originalText.length > 4) append("-")
                    appendSafe(originalText, 4, 8)
                    if (originalText.length > 8) append("-")
                    appendSafe(originalText, 8, 12)
                    if (originalText.length > 12) append(originalText.substring(12)) // 초과 입력 그대로 추가
                }
                originalText.startsWith("010") || originalText.startsWith("070") -> {
                    appendSafe(originalText, 0, 3)
                    if (originalText.length > 3) append("-")
                    appendSafe(originalText, 3, 7)
                    if (originalText.length > 7) append("-")
                    appendSafe(originalText, 7, 11)
                    if (originalText.length > 11) append(originalText.substring(11)) // 초과 입력 그대로 추가
                }
                isAreaCode(originalText) -> {
                    val areaCode = getAreaCode(originalText)
                    appendSafe(originalText, 0, areaCode.length)
                    if (originalText.length > areaCode.length) append("-")
                    val nextChunkStart = areaCode.length
                    appendSafe(originalText, nextChunkStart, nextChunkStart + 3)
                    if (originalText.length > nextChunkStart + 3) append("-")
                    appendSafe(originalText, nextChunkStart + 3, nextChunkStart + 7)
                    if (originalText.length > nextChunkStart + 7) append(originalText.substring(nextChunkStart + 7)) // 초과 입력 그대로 추가
                }
                else -> append(originalText)
            }
        }

        val offsetMapping = createOffsetMapping(originalText, formattedText)

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }

    private fun StringBuilder.appendSafe(text: String, start: Int, end: Int) {
        if (start < text.length) {
            append(text.substring(start, end.coerceAtMost(text.length)))
        }
    }

    private fun isAreaCode(text: String): Boolean {
        return areaCodes.any { text.startsWith(it) }
    }

    private fun getAreaCode(text: String): String {
        return areaCodes.firstOrNull { text.startsWith(it) } ?: ""
    }

    private fun createOffsetMapping(originalText: String, formattedText: String): OffsetMapping {
        return object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var extraChars = 0
                val transformedLength = formattedText.length

                if (originalText.startsWith("050")) {
                    if (offset > 4) extraChars++ // 첫 번째 대시
                    if (offset > 8) extraChars++ // 두 번째 대시
                } else if (originalText.startsWith("010") || originalText.startsWith("070")) {
                    if (offset > 3) extraChars++ // 첫 번째 대시
                    if (offset > 7) extraChars++ // 두 번째 대시
                } else if (isAreaCode(originalText)) {
                    val areaCode = getAreaCode(originalText)
                    val nextChunkStart = areaCode.length
                    if (offset > nextChunkStart) extraChars++ // 첫 번째 대시
                    if (offset > nextChunkStart + 3) extraChars++ // 두 번째 대시
                }

                return (offset + extraChars).coerceAtMost(transformedLength) // 반환값 제한
            }

            override fun transformedToOriginal(offset: Int): Int {
                var extraChars = 0
                val originalLength = originalText.length

                if (originalText.startsWith("050")) {
                    if (offset > 5) extraChars++ // 첫 번째 대시
                    if (offset > 9) extraChars++ // 두 번째 대시
                } else if (originalText.startsWith("010") || originalText.startsWith("070")) {
                    if (offset > 4) extraChars++ // 첫 번째 대시
                    if (offset > 8) extraChars++ // 두 번째 대시
                } else if (isAreaCode(originalText)) {
                    val areaCode = getAreaCode(originalText)
                    val nextChunkStart = areaCode.length
                    if (offset > nextChunkStart + 1) extraChars++ // 첫 번째 대시
                    if (offset > nextChunkStart + 4) extraChars++ // 두 번째 대시
                }

                return (offset - extraChars).coerceAtMost(originalLength) // 반환값 제한
            }
        }
    }
}
