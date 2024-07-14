package com.store_me.storeme.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private val Context.dataStore by preferencesDataStore(name = "settings")

private val locationDisplay = stringPreferencesKey(Settings.LOCATION_DISPLAY.value)
private val locationCode = intPreferencesKey(Settings.LOCATION_CODE.value)

enum class Settings(val value: String) {
    LOCATION_DISPLAY("locationDisplay"),
    LOCATION_CODE("locationCode")
}

/**
 * 데이터 저장 함수
 */
suspend fun saveLocation(context: Context, location: String, code: Int) {
    context.dataStore.edit { preferences ->
        preferences[locationDisplay] = location
        preferences[locationCode] = code
    }
}

/**
 * 저장된 위치 정보를 확인하는 함수
 */
fun getLocation(context: Context): Flow<String> {
    return context.dataStore.data
        .map { preferences ->
            preferences[locationDisplay] ?: "명동"
        }
}