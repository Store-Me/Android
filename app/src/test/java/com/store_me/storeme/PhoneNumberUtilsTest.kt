package com.store_me.storeme

import com.store_me.storeme.utils.PhoneNumberUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PhoneNumberUtilsTest {

    private val phoneNumberUtils = PhoneNumberUtils()

    @Test
    fun `test valid 안심번호`() {
        val validSafeNumber = "050123456789"
        assertTrue(phoneNumberUtils.isValidStoreNumber(validSafeNumber))
    }

    @Test
    fun `test valid 휴대전화번호`() {
        val validMobileNumber = "01012345678"
        assertTrue(phoneNumberUtils.isValidStoreNumber(validMobileNumber))
    }

    @Test
    fun `test valid 인터넷전화번호`() {
        val validInternetPhoneNumber = "07012345678"
        assertTrue(phoneNumberUtils.isValidStoreNumber(validInternetPhoneNumber))
    }

    @Test
    fun `test valid 유선전화번호`() {
        val validLandlineNumber = "0311234567" // 경기 지역번호
        assertTrue(phoneNumberUtils.isValidStoreNumber(validLandlineNumber))
    }

    @Test
    fun `test valid 유선전화번호 with 8 digits`() {
        val validLandlineNumberWith8Digits = "03112345678"
        assertTrue(phoneNumberUtils.isValidStoreNumber(validLandlineNumberWith8Digits))
    }

    @Test
    fun `test invalid 번호 - 잘못된 시작 번호`() {
        val invalidNumber = "1234567890"
        assertFalse(phoneNumberUtils.isValidStoreNumber(invalidNumber))
    }

    @Test
    fun `test invalid 번호 - 짧은 번호`() {
        val shortNumber = "0101234"
        assertFalse(phoneNumberUtils.isValidStoreNumber(shortNumber))
    }

    @Test
    fun `test invalid 번호 - 유효하지 않은 지역번호`() {
        val invalidLandline = "0991234567" // 존재하지 않는 지역번호
        assertFalse(phoneNumberUtils.isValidStoreNumber(invalidLandline))
    }

    @Test
    fun `test invalid 번호 - 긴 번호`() {
        val longNumber = "0101234567890"
        assertFalse(phoneNumberUtils.isValidStoreNumber(longNumber))
    }
}
