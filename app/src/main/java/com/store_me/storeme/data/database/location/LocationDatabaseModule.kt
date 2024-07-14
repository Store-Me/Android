package com.store_me.storeme.data.database.location

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.io.FileOutputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(@ApplicationContext context: Context): LocationDataBaseHelper {
        copyDatabase(context)
        return LocationDataBaseHelper(context)
    }

    private fun copyDatabase(context: Context) {
        val dbName = "locationData.db"
        val dbPath = context.getDatabasePath(dbName).absolutePath

        if (!File(dbPath).exists()) {
            context.assets.open("database/$dbName").use { inputStream ->
                FileOutputStream(dbPath).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }
}