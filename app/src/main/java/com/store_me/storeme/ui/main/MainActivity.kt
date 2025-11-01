
package com.store_me.storeme.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.store_me.storeme.ui.main.navigation.customer.CustomerFavoriteNavigationGraph
import com.store_me.storeme.ui.main.navigation.customer.CustomerHomeNavigationGraph
import com.store_me.storeme.ui.main.navigation.customer.CustomerNearPlaceNavigationGraph
import com.store_me.storeme.ui.main.navigation.customer.CustomerMyMenuNavigationGraph
import com.store_me.storeme.ui.main.navigation.customer.CustomerRoute
import com.store_me.storeme.ui.main.navigation.customer.CustomerStoreTalkNavigationGraph
import com.store_me.storeme.ui.main.navigation.owner.OwnerAddNavigationGraph
import com.store_me.storeme.ui.main.navigation.owner.OwnerCustomerManagementNavigationGraph
import com.store_me.storeme.ui.main.navigation.owner.OwnerHomeNavigationGraph
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.main.navigation.owner.OwnerSharedRoute
import com.store_me.storeme.ui.main.navigation.owner.OwnerStoreInfoNavigationGraph
import com.store_me.storeme.ui.main.navigation.owner.OwnerStoreTalkNavigationGraph
import com.store_me.storeme.ui.onboarding.OnboardingActivity
import com.store_me.storeme.ui.status_bar.StatusBarProtection
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
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b), android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b))
        )

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
                        snackBarHostState.showSnackbar(message = errorMessage ?: getString(R.string.unknown_error_message), duration = SnackbarDuration.Short)
                    }
                }

                LaunchedEffect(Unit) {
                    SuccessEventBus.successFlow.collect { message ->
                        loadingViewModel.hideLoading()
                        snackBarHostState.showSnackbar(message = message ?: getString(R.string.default_success_message), duration = SnackbarDuration.Short)
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
                            .fillMaxSize(),
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

                StatusBarProtection()
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
        val homeNavController = rememberNavController()
        val favoriteNavController = rememberNavController()
        val nearPlaceNavController = rememberNavController()
        val storeTalkNavController = rememberNavController()
        val profileNavController = rememberNavController()

        val navControllers = mapOf(
            CustomerRoute.Home.path to homeNavController,
            CustomerRoute.Favorite.path to favoriteNavController,
            CustomerRoute.NearPlace.path to nearPlaceNavController,
            CustomerRoute.StoreTalk.path to storeTalkNavController,
            CustomerRoute.MyMenu.path to profileNavController,
        )

        var currentTab by rememberSaveable { mutableStateOf(CustomerRoute.Home.path) }

        val snackbarHostState = LocalSnackbarHostState.current

        Scaffold(
            snackbarHost = { SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { StoreMeSnackbar(snackbarData = it) }
            ) },
            bottomBar = { BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            ) }
        ) {
            Box(Modifier.padding(it)) {
                navControllers.forEach { (tab, navController) ->
                    AnimatedVisibility(
                        visible = currentTab == tab,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        when(tab){
                            CustomerRoute.Home.path -> CustomerHomeNavigationGraph(navController)
                            CustomerRoute.Favorite.path -> CustomerFavoriteNavigationGraph(navController)
                            CustomerRoute.NearPlace.path -> CustomerNearPlaceNavigationGraph(navController)
                            CustomerRoute.StoreTalk.path -> CustomerStoreTalkNavigationGraph(navController)
                            CustomerRoute.MyMenu.path -> CustomerMyMenuNavigationGraph(navController)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun OwnerScreen() {
        val homeNavController = rememberNavController()
        val customerManagementNavController = rememberNavController()
        val addNavController = rememberNavController()
        val storeTalkNavController = rememberNavController()
        val storeInfoNavController = rememberNavController()

        val navControllers = mapOf(
            OwnerRoute.Home.path to homeNavController,
            OwnerRoute.CustomerManagement.path to customerManagementNavController,
            OwnerRoute.Add.path to addNavController,
            OwnerRoute.StoreTalk.path to storeTalkNavController,
            OwnerRoute.StoreInfo.path to storeInfoNavController,
        )

        var currentTab by rememberSaveable { mutableStateOf(OwnerRoute.Home.path) }

        val snackbarHostState = LocalSnackbarHostState.current

        val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val hideBottomBarRoutes = listOf(
            OwnerSharedRoute.PostDetail.path,
            OwnerSharedRoute.EditNormalPost.path
        )

        BackHandler {
            if(currentTab != OwnerRoute.Home.path) {
                currentTab = OwnerRoute.Home.path
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { StoreMeSnackbar(snackbarData = it) }
            ) },
            bottomBar = {
                if (currentRoute !in hideBottomBarRoutes) {
                    BottomNavigationBar(
                        currentTab = currentTab,
                        onTabSelected = { currentTab = it }
                    )
                }
            }
        ) {
            Box {
                navControllers.forEach { (tab, navController) ->
                    AnimatedVisibility(
                        visible = currentTab == tab,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        when(tab){
                            OwnerRoute.Home.path -> OwnerHomeNavigationGraph(navController)
                            OwnerRoute.CustomerManagement.path -> OwnerCustomerManagementNavigationGraph(navController)
                            OwnerRoute.Add.path -> OwnerAddNavigationGraph(navController)
                            OwnerRoute.StoreTalk.path -> OwnerStoreTalkNavigationGraph(navController)
                            OwnerRoute.StoreInfo.path -> OwnerStoreInfoNavigationGraph(navController)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomNavigationBar(
        currentTab: String,
        onTabSelected: (String) -> Unit
    ) {
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
                    .height(67.dp)
            ){
                items.forEach { item ->
                    val isSelected = currentTab == item.screenRoute

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
                           onTabSelected(item.screenRoute)
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