
package com.store_me.storeme.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.store_me.auth.Auth
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.home.owner.StoreDataViewModel
import com.store_me.storeme.ui.loading.LoadingScreen
import com.store_me.storeme.ui.loading.LoadingViewModel
import com.store_me.storeme.ui.main.navigation.BottomNavItem
import com.store_me.storeme.ui.main.navigation.customer.CustomerNavigationGraph
import com.store_me.storeme.ui.main.navigation.owner.OwnerNavigationGraph
import com.store_me.storeme.ui.onboarding.OnboardingActivity
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.ErrorEventBus
import com.store_me.storeme.utils.KeyboardHeightObserver
import com.store_me.storeme.utils.SuccessEventBus
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.store_me.storeme.utils.preference.SettingPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: Auth

    @Inject
    lateinit var settingPreferencesHelper: SettingPreferencesHelper

    private lateinit var keyboardHeightObserver: KeyboardHeightObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            keyboardHeightObserver = KeyboardHeightObserver(this) { height ->
                lifecycleScope.launch {
                    settingPreferencesHelper.saveKeyboardHeight(height)
                }
            }

            val loadingViewModel: LoadingViewModel = viewModel()
            val isLoading by loadingViewModel.isLoading.collectAsState()

            val storeDataViewModel: StoreDataViewModel = hiltViewModel()

            keyboardHeightObserver.startObserving()

            StoreMeTheme {
                val snackBarHostState = remember { SnackbarHostState() }

                //메시지 처리
                LaunchedEffect(Unit) {
                    ErrorEventBus.errorFlow.collect { errorMessage ->
                        loadingViewModel.hideLoading()
                        snackBarHostState.showSnackbar(message = errorMessage ?: getString(R.string.unknown_error_message))
                    }
                }

                LaunchedEffect(Unit) {
                    SuccessEventBus.successFlow.collect { message ->
                        loadingViewModel.hideLoading()
                        snackBarHostState.showSnackbar(message = message ?: getString(R.string.default_success_message))
                    }
                }

                CompositionLocalProvider(
                    LocalAuth provides auth,
                    LocalSnackbarHostState provides snackBarHostState,
                    LocalLoadingViewModel provides loadingViewModel,
                    LocalStoreDataViewModel provides storeDataViewModel
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White),
                        color = Color.Transparent
                    ) {
                        val isLoggedIn by auth.isLoggedIn.collectAsState()
                        val accountType by auth.accountType.collectAsState()

                        if(!isLoggedIn) {
                            NavigateToOnboarding()
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                when(accountType){
                                    AccountType.CUSTOMER -> { CustomerScreen() }
                                    AccountType.OWNER -> { OwnerScreen() }
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
    }
    @Composable
    private fun NavigateToOnboarding() {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            val intent = Intent(context, OnboardingActivity::class.java)
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }
    }

    @Composable
    private fun CustomerScreen() {
        val navController = rememberNavController()
        val snackbarHostState = LocalSnackbarHostState.current

        Scaffold(
            snackbarHost = { SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { StoreMeSnackbar(snackbarData = it) }
            ) },
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            Box(Modifier.padding(it)) {
                CustomerNavigationGraph(navController)
            }
        }
    }

    @Composable
    private fun OwnerScreen() {
        val navController = rememberNavController()
        val snackbarHostState = LocalSnackbarHostState.current

        Scaffold(
            snackbarHost = { SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { StoreMeSnackbar(snackbarData = it) }
            ) },
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            Box(Modifier.padding(it)) {
                OwnerNavigationGraph(navController)
            }
        }
    }

    @Composable
    private fun BottomNavigationBar(navController: NavHostController) {
        val auth = LocalAuth.current

        val accountType by auth.accountType.collectAsState()

        val items = when(accountType){
            AccountType.CUSTOMER -> BottomNavItem.Customer.items
            AccountType.OWNER -> BottomNavItem.Owner.items
        }

        Column {
            HorizontalDivider(color = UnselectedItemColor, thickness = 0.2.dp)

            NavigationBar (
                containerColor = Color.White,
                modifier = Modifier
                    .height(67.dp),
                tonalElevation = 0.dp
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    val isSelected = currentDestination
                        ?.hierarchy
                        ?.any { it.route?.startsWith(item.screenRoute) == true } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = if (isSelected) item.selectedIcon else item.icon),
                                contentDescription = stringResource(id = item.title),
                                modifier = Modifier
                                    .width(27.dp)
                                    .height(27.dp),
                                tint = Color.Unspecified
                            )
                        },
                        label = {
                            val textColor = if(isSelected) Color.Black else UnselectedItemColor
                            Text(stringResource(id = item.title), style = storeMeTypography.titleSmall, fontSize = 10.sp, color = textColor) },
                        selected = isSelected,
                        alwaysShowLabel = true,
                        onClick = {
                            navController.navigate(item.screenRoute) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) { saveState = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                        ),
                    )
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        keyboardHeightObserver.stopObserving()
    }
}