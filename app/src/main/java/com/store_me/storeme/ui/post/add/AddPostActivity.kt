package com.store_me.storeme.ui.post.add

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.store_me.storeme.data.enums.PostType
import com.store_me.storeme.ui.loading.LoadingScreen
import com.store_me.storeme.ui.loading.LoadingViewModel
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.utils.KeyboardHeightObserver
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.preference.SettingPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AddPostActivity : ComponentActivity() {

    @Inject
    lateinit var settingPreferencesHelper: SettingPreferencesHelper

    private lateinit var keyboardHeightObserver: KeyboardHeightObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        setContent {
            val postTypeName = intent.getStringExtra("postType")
            val postType = postTypeName?.let {
                try {
                    PostType.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }

            Timber.d("postType: $postType")

            keyboardHeightObserver = KeyboardHeightObserver(this) { height ->
                lifecycleScope.launch {
                    settingPreferencesHelper.saveKeyboardHeight(height)
                }
            }

            keyboardHeightObserver.startObserving()

            StoreMeTheme {
                val loadingViewModel: LoadingViewModel = viewModel()
                val isLoading by loadingViewModel.isLoading.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                CompositionLocalProvider(
                    LocalSnackbarHostState provides snackbarHostState,
                    LocalLoadingViewModel provides loadingViewModel,
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White),
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            when(postType) {
                                null -> { finish() }
                                else -> { AddPostScreen(postType = postType) }
                            }


                            if(isLoading) {
                                LoadingScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        keyboardHeightObserver.stopObserving()
    }
}