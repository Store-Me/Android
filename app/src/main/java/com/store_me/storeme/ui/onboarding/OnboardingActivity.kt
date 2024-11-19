package com.store_me.storeme.ui.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.store_me.storeme.ui.create_account.SmsAuthScreen
import com.store_me.storeme.ui.post.AddNormalPostScreen
import com.store_me.storeme.ui.theme.StoreMeTheme

class OnboardingActivity : ComponentActivity() {
    sealed class Screen(val route: OnboardingRoute){
        data object Onboarding : Screen(route = OnboardingRoute.ONBOARDING)
        data object Login : Screen(route = OnboardingRoute.LOGIN)
        data object SmsAuth : Screen(route = OnboardingRoute.SMS_AUTH)
    }

    enum class OnboardingRoute{
        ONBOARDING, LOGIN, SMS_AUTH,
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
    private fun LoginScreen(navController: NavController) {
        /*var backPressedTime by remember { mutableStateOf(0L) }
        val context = LocalContext.current

        BackHandler {
            val currentTime = System.currentTimeMillis()
            if(currentTime - backPressedTime < 2000){
                finishAffinity()
            } else {
                backPressedTime = currentTime
                ToastMessageUtils.showToast(context, R.string.backpress_message)
            }
        }*/

        /*Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DefaultButton(text = "회원가입") {
                navController.navigate(Screen.SmsAuth.route.name)
            }
        }*/

        AddNormalPostScreen()
    }

    @Composable
    fun NavGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = Screen.Onboarding.route.name) {
            composable(Screen.Onboarding.route.name) { OnboardingScreen(navController) }
            composable(Screen.Login.route.name) { LoginScreen(navController) }
            composable(Screen.SmsAuth.route.name) { SmsAuthScreen(navController) }
        }
    }

}