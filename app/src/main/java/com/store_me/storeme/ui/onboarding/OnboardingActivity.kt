package com.store_me.storeme.ui.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.ui.loading.LoadingScreen
import com.store_me.storeme.ui.loading.LoadingViewModel
import com.store_me.storeme.ui.signup.SignupScreen
import com.store_me.storeme.ui.login.LoginScreen
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {
    sealed class Screen(val route: OnboardingRoute){
        data object Onboarding : Screen(route = OnboardingRoute.ONBOARDING)
        data object Login : Screen(route = OnboardingRoute.LOGIN)
        data object Signup : Screen(route = OnboardingRoute.SIGNUP)
    }

    enum class OnboardingRoute{
        ONBOARDING, LOGIN, SIGNUP,
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            StoreMeTheme {
                val navController = rememberNavController()

                val loadingViewModel: LoadingViewModel = viewModel()
                
                val isLoading by loadingViewModel.isLoading.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                CompositionLocalProvider(
                    LocalLoadingViewModel provides loadingViewModel,
                    LocalSnackbarHostState provides snackbarHostState
                ) {
                    Surface (
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
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