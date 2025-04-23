package com.store_me.storeme.ui.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.store_me.auth.Auth
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.loading.LoadingScreen
import com.store_me.storeme.ui.loading.LoadingViewModel
import com.store_me.storeme.ui.login.LoginScreen
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.signup.SignupScreen
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.onboarding.LocalOnboardingComposition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {
    sealed class Screen(val route: OnboardingRoute){
        data object Onboarding : Screen(route = OnboardingRoute.ONBOARDING)
        data object Login : Screen(route = OnboardingRoute.LOGIN)
        data object Signup : Screen(route = OnboardingRoute.SIGNUP)
    }

    enum class OnboardingRoute {
        ONBOARDING, LOGIN, SIGNUP,
    }

    @Inject
    lateinit var auth: Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            val onboardingComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.onboarding_animation)
            )

            StoreMeTheme {
                val navController = rememberNavController()

                val loadingViewModel: LoadingViewModel = viewModel()
                val isLoading by loadingViewModel.isLoading.collectAsState()

                val context = LocalContext.current

                val snackbarHostState = remember { SnackbarHostState() }

                //메시지 처리
                LaunchedEffect(Unit) {
                    ErrorEventBus.errorFlow.collect { errorMessage ->
                        loadingViewModel.hideLoading()
                        snackbarHostState.showSnackbar(message = errorMessage ?: getString(R.string.unknown_error_message))
                    }
                }

                LaunchedEffect(Unit) {
                    SuccessEventBus.successFlow.collect { message ->
                        loadingViewModel.hideLoading()
                        snackbarHostState.showSnackbar(message = message ?: getString(R.string.default_success_message))
                    }
                }

                CompositionLocalProvider(
                    LocalOnboardingComposition provides onboardingComposition,
                    LocalLoadingViewModel provides loadingViewModel,
                    LocalSnackbarHostState provides snackbarHostState,
                    LocalAuth provides auth
                ) {
                    Surface (
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White),
                        color = Color.Transparent
                    ) {
                        val isLoggedIn by auth.isLoggedIn.collectAsState()

                        LaunchedEffect(isLoggedIn) {
                            if(isLoggedIn) {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                (context as? Activity)?.finish()
                            }
                        }

                        Scaffold(
                            snackbarHost = { SnackbarHost(
                                hostState = snackbarHostState,
                                snackbar = { StoreMeSnackbar(snackbarData = it) }
                            ) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = Color.Black)
                                    .padding(it)
                            ) {
                                NavGraph(navController = navController)

                                if(isLoading) {
                                    LoadingScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun NavGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = Screen.Onboarding.route.name) {
            composable(Screen.Onboarding.route.name) { OnboardingScreen(navController) }
            composable(Screen.Login.route.name) { LoginScreen(navController) }
            composable(
                route = Screen.Signup.route.name + "/{loginType}?additionalData={additionalData}",
                arguments = listOf(
                    navArgument("loginType") { type = NavType.StringType },
                    navArgument("additionalData") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backstackEntry ->
                val loginType = backstackEntry.arguments?.getString("loginType") ?: LoginType.APP.name
                val additionalData = backstackEntry.arguments?.getString("additionalData") ?: ""
                SignupScreen(navController, loginType = LoginType.valueOf(loginType), additionalData = additionalData)
            }
        }
    }
}