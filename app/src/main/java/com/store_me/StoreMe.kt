package com.store_me

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class StoreMe : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}