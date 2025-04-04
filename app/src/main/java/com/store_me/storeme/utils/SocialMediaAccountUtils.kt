package com.store_me.storeme.utils

import com.store_me.storeme.R
import com.store_me.storeme.data.SocialMediaAccountType

object SocialMediaAccountUtils {
    fun getType(url: String): SocialMediaAccountType {
        return when {
            url.startsWith("https://www.instagram.com") -> { SocialMediaAccountType.INSTAGRAM }
            url.startsWith("https://naver.me") -> { SocialMediaAccountType.NAVER }
            url.startsWith("https://band.us") -> { SocialMediaAccountType.BAND }
            url.startsWith("https://youtube.com") -> { SocialMediaAccountType.YOUTUBE }
            else -> { SocialMediaAccountType.WEB }
        }
    }

    fun getIcon(type: SocialMediaAccountType): Int {
        return when(type) {
            SocialMediaAccountType.INSTAGRAM -> { R.drawable.ic_instagram }
            SocialMediaAccountType.NAVER -> { R.drawable.ic_naver }
            SocialMediaAccountType.BAND -> { R.drawable.ic_band }
            SocialMediaAccountType.YOUTUBE -> { R.drawable.ic_youtube }
            SocialMediaAccountType.WEB -> { R.drawable.ic_web }
        }
    }
}