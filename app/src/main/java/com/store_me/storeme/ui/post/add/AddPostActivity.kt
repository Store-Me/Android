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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.post.PostType
import com.store_me.storeme.ui.loading.LoadingScreen
import com.store_me.storeme.ui.loading.LoadingViewModel
import com.store_me.storeme.ui.post.add.normal.AddNormalPostScreen
import com.store_me.storeme.ui.post.add.survey.AddSurveyScreen
import com.store_me.storeme.ui.post.add.vote.AddVotePostScreen
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.KeyboardHeightObserver
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.preference.SettingPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

            keyboardHeightObserver = KeyboardHeightObserver(this) { height ->
                lifecycleScope.launch {
                    settingPreferencesHelper.saveKeyboardHeight(height)
                }
            }

            keyboardHeightObserver.startObserving()

            val loadingViewModel: LoadingViewModel = viewModel()
            val isLoading by loadingViewModel.isLoading.collectAsState()

            StoreMeTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                //메시지 처리
                LaunchedEffect(Unit) {
                    ErrorEventBus.errorFlow.collect { errorMessage ->
                        loadingViewModel.hideLoading()
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(message = errorMessage ?: getString(R.string.unknown_error_message))
                    }
                }

                LaunchedEffect(Unit) {
                    SuccessEventBus.successFlow.collect { message ->
                        loadingViewModel.hideLoading()
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(message = message ?: getString(R.string.default_success_message))
                    }
                }

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
                                PostType.NORMAL -> { AddNormalPostScreen() }
                                PostType.VOTE -> { AddVotePostScreen() }
                                PostType.SURVEY -> { AddSurveyScreen() }
                                else -> { AddNormalPostScreen() }
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