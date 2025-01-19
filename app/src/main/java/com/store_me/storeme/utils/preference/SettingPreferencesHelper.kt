package com.store_me.storeme.utils.preference

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "setting")
class SettingPreferencesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val keyboardHeightKey = intPreferencesKey("keyboardHeight")

    /**
     * 키보드 높이 저장
     */
    suspend fun saveKeyboardHeight(height: Int) {
        Timber.d("Keyboard Height Saved: $height px")

        context.dataStore.edit { preferences ->
            preferences[keyboardHeightKey] = height
        }
    }

    /**
     * 키보드 높이 불러오기
     */
    fun getKeyboardHeight(): Flow<Int> {
        return context.dataStore.data
            .map { preferences ->
                val height = preferences[keyboardHeightKey] ?: 300
                height
            }
    }
}