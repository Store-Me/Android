package com.store_me

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.store_me.storeme.BuildConfig
import com.store_me.storeme.BuildConfig.KAKAO_KEY
import com.store_me.storeme.utils.preference.TokenPreferencesHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class StoreMe : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, KAKAO_KEY)
        TokenPreferencesHelper.init(this)

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}