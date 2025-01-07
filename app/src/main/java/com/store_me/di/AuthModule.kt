package com.store_me.di

import android.content.Context
import com.store_me.auth.Auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuth(
        @ApplicationContext context: Context
    ): Auth {
        val auth = Auth(context)

        auth.init()

        return auth
    }
}