@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)

package com.store_me.storeme.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.store_me.storeme.BuildConfig
import com.store_me.storeme.R
import com.store_me.storeme.ui.theme.StoreMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreMeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun MainScreen() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            Box(Modifier.padding(it)) {
                NavigationGraph(navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = BottomNavItem.UserHome.screenRoute){
            composable(BottomNavItem.UserHome.screenRoute) { UserHomeScreen() }
            composable(BottomNavItem.Favorite.screenRoute) { FavoriteScreen() }
            composable(BottomNavItem.NearPlace.screenRoute) { NearPlaceScreen() }
            composable(BottomNavItem.Chat.screenRoute) { ChattingScreen() }
            composable(BottomNavItem.Profile.screenRoute) { ProfileScreen() }
        }
    }

    @Composable
    private fun BottomNavigationBar(navController: NavHostController) {
        val items = listOf(
            BottomNavItem.UserHome,
            BottomNavItem.Favorite,
            BottomNavItem.NearPlace,
            BottomNavItem.Chat,
            BottomNavItem.Profile
        )

        val isDarkMode = isSystemInDarkTheme()

        val backgroundColor = if(isDarkMode) {
            Color.White
        } else {
            Color.White
        }

        BottomNavigation (
            backgroundColor = backgroundColor,
            contentColor = Color(0xFF3F414E)
        ){
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach{ item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = stringResource(id = item.title),
                            modifier = Modifier
                                .width(26.dp)
                                .height(26.dp)
                        )
                    },
                    label = { Text(stringResource(id = item.title), fontSize = 9.sp) },
                    selectedContentColor = Black,
                    unselectedContentColor = Gray,
                    selected = currentRoute == item.screenRoute,
                    alwaysShowLabel = false,
                    onClick = {
                        navController.navigate(item.screenRoute) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    sealed class BottomNavItem(val title: Int, val icon: Int, val screenRoute: String) {
        object UserHome : BottomNavItem(R.string.home, R.drawable.ic_launcher_foreground, USER_HOME)
        object Favorite : BottomNavItem(R.string.favorite,  R.drawable.ic_launcher_foreground, FAVORITE)
        object NearPlace : BottomNavItem(R.string.near_place,  R.drawable.ic_launcher_foreground, NEAR_PLACE)
        object Chat : BottomNavItem(R.string.chat,  R.drawable.ic_launcher_foreground, CHAT)
        object Profile : BottomNavItem(R.string.profile,  R.drawable.ic_launcher_foreground, PROFILE)
    }

    @Composable
    fun UserHomeScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Home Screen")
        }
    }

    @Composable
    fun FavoriteScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Favorite Screen")
        }
    }

    @Composable
    fun NearPlaceScreen() {
        var mapProperties by remember {
            mutableStateOf(
                MapProperties(maxZoom = 10.0, minZoom = 5.0)
            )
        }
        var mapUiSettings by remember {
            mutableStateOf(
                MapUiSettings(isLocationButtonEnabled = false)
            )
        }
        Box(Modifier.fillMaxSize()) {
            NaverMap(properties = mapProperties, uiSettings = mapUiSettings)
        }
    }

    @Composable
    fun ChattingScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Chatting Screen")
        }
    }

    @Composable
    fun ProfileScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Profile Screen")
        }
    }
}