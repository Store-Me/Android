package com.store_me

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.store_me.storeme.BuildConfig
import com.store_me.storeme.BuildConfig.KAKAO_KEY
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class StoreMe : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, KAKAO_KEY)
    }
}