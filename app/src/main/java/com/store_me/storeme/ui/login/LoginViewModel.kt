package com.store_me.storeme.ui.login

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class LoginViewModel(application: Application) : AndroidViewModel(application) {
}