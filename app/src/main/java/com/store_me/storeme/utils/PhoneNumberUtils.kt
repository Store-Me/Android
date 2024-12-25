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
    class PhoneNumberVisualTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val originalText = text.text
            val formattedText = buildString {
                if (originalText.length >= 3) {
                    append(originalText.substring(0, 3)) // 앞 3자리
                    if (originalText.length > 3) append("-")
                }
                if (originalText.length in 4..7) {
                    append(originalText.substring(3, originalText.length)) // 중간 4자리
                } else if (originalText.length > 7) {
                    append(originalText.substring(3, 7)) // 중간 4자리
                    append("-")
                    append(originalText.substring(7, originalText.length)) // 마지막 4자리
                }
            }

            return TransformedText(
                text = AnnotatedString(formattedText),
                offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int {
                        return when {
                            offset <= 3 -> offset
                            offset <= 7 -> offset + 1 // 중간 대시 추가
                            else -> offset + 2 // 마지막 대시 추가
                        }
                    }

                    override fun transformedToOriginal(offset: Int): Int {
                        return when {
                            offset <= 3 -> offset
                            offset <= 8 -> offset - 1 // 중간 대시 제거
                            else -> offset - 2 // 마지막 대시 제거
                        }
                    }
                }
            )
        }
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
                }            }
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