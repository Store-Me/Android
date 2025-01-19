package com.store_me.storeme.utils.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "location")

@Singleton
class LocationPreferencesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val locationDisplayKey = stringPreferencesKey("locationDisplay")
    private val locationCodeKey = longPreferencesKey("locationCode")

    /**
     * 데이터 저장 함수
     */
    suspend fun saveLocation(location: String, code: Long) {
        context.dataStore.edit { preferences ->
            preferences[locationDisplayKey] = location
            preferences[locationCodeKey] = code
        }
    }

    /**
     * 저장된 위치 정보를 확인하는 함수
     */
    fun getLocation(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[locationDisplayKey] ?: "명동"
            }
    }
}