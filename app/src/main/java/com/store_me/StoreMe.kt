package com.store_me

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
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

        FirebaseApp.initializeApp(this)


        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            Firebase.appCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance(),
            )
        } else {
            Firebase.appCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance(),
            )
        }
    }
}