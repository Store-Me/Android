@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
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
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.home.HomeScreen
import com.store_me.storeme.ui.login.LoginActivity
import com.store_me.storeme.ui.theme.StoreMeTheme
import com.store_me.storeme.ui.theme.UnselectedItemColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!Auth.isLoggedIn.value){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

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
        NavHost(navController, startDestination = BottomNavItem.UserHome.screenRoute,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }){
            composable(BottomNavItem.UserHome.screenRoute) { UserHomeScreen() }
            composable(BottomNavItem.Favorite.screenRoute) { FavoriteScreen() }
            composable(BottomNavItem.NearPlace.screenRoute) { NearPlaceScreen() }
            composable(BottomNavItem.StoreTalk.screenRoute) { ChatScreen() }
            composable(BottomNavItem.Profile.screenRoute) { ProfileScreen() }
        }
    }

    @Composable
    private fun BottomNavigationBar(navController: NavHostController) {
        val items = listOf(
            BottomNavItem.UserHome,
            BottomNavItem.Favorite,
            BottomNavItem.NearPlace,
            BottomNavItem.StoreTalk,
            BottomNavItem.Profile
        )

        Column {
            Divider(color = UnselectedItemColor, thickness = 0.2.dp)

            BottomNavigation (
                backgroundColor = White,
                modifier = Modifier
                    .height(61.dp),
                elevation = 0.dp
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach{ item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = if (currentRoute == item.screenRoute) item.selectedIcon else item.icon),
                                contentDescription = stringResource(id = item.title),
                                modifier = Modifier
                                    .width(27.dp)
                                    .height(27.dp)
                                    .padding(top = 3.dp),
                                tint = Color.Unspecified
                            )
                        },
                        label = {
                            val textColor = if(currentRoute == item.screenRoute) Black else UnselectedItemColor
                            Text(stringResource(id = item.title), fontSize = 9.sp, color = textColor, modifier = Modifier.padding(top = 3.dp)) },
                        selected = currentRoute == item.screenRoute,
                        alwaysShowLabel = true,
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
    }

    sealed class BottomNavItem(val title: Int, val icon: Int, val selectedIcon: Int, val screenRoute: String) {
        object UserHome : BottomNavItem(R.string.home, R.drawable.bottom_home, R.drawable.bottom_home_selected, USER_HOME)
        object Favorite : BottomNavItem(R.string.favorite, R.drawable.bottom_favorite, R.drawable.bottom_favorite_selected, FAVORITE)
        object NearPlace : BottomNavItem(R.string.near_place, R.drawable.bottom_nearplace, R.drawable.bottom_nearplace_selected, NEAR_PLACE)
        object StoreTalk : BottomNavItem(R.string.store_talk, R.drawable.bottom_storetalk, R.drawable.bottom_storetalk_selected, STORE_TALK)
        object Profile : BottomNavItem(R.string.profile, R.drawable.bottom_mymenu, R.drawable.bottom_mymenu_selected, PROFILE)
    }

    @Composable
    fun UserHomeScreen() {
        HomeScreen()
    }

    @Composable
    fun FavoriteScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Favorite Screen")
        }
    }

    @Composable
    fun NearPlaceScreen() {

    }

    @Composable
    fun ChatScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Chat Screen")
        }
    }

    @Composable
    fun ProfileScreen() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = "Profile Screen")
        }
    }
}