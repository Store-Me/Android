package com.store_me.storeme.ui.onboarding

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.store_me.storeme.ui.create_account.SignupScreen
import com.store_me.storeme.ui.login.LoginScreen
import com.store_me.storeme.ui.theme.StoreMeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {
    sealed class Screen(val route: OnboardingRoute){
        data object Onboarding : Screen(route = OnboardingRoute.ONBOARDING)
        data object Login : Screen(route = OnboardingRoute.LOGIN)
        data object Signup : Screen(route = OnboardingRoute.Signup)
    }

    enum class OnboardingRoute{
        ONBOARDING, LOGIN, Signup,
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            StoreMeTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }

    @Composable
    fun NavGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = Screen.Onboarding.route.name) {
            composable(Screen.Onboarding.route.name) { OnboardingScreen(navController) }
            composable(Screen.Login.route.name) { LoginScreen(navController) }
            composable(Screen.Signup.route.name) { SignupScreen(navController) }
        }
    }
}