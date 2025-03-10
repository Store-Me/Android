@file:OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.home.owner

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.tab_menu.OwnerHomeTabMenu
import com.store_me.storeme.ui.component.StoreMeTabRow
import com.store_me.storeme.utils.ToastMessageUtils

val LocalStoreDataViewModel = staticCompositionLocalOf<StoreDataViewModel> {
    error("No OwnerHomeViewModel provided")
}

@Composable
fun OwnerHomeScreen(
    navController: NavController,
    storeDataViewModel: StoreDataViewModel
) {
    var backPressedTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current

    val pagerState = rememberPagerState()

    val tabTitles = enumValues<OwnerHomeTabMenu>().map { it.displayName }

    val storeData by storeDataViewModel.storeData.collectAsState()

    CompositionLocalProvider(LocalStoreDataViewModel provides storeDataViewModel) {
        Scaffold(
            containerColor = White,
            content = { innerPadding -> // 컨텐츠 영역
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    LazyColumn {
                        item {
                            BackgroundSection(storeData?.storeBannerImageUrl)
                        }

                        item {
                            StoreHomeProfileSection(navController = navController)
                        }

                        stickyHeader {
                            StoreMeTabRow(pagerState = pagerState, tabTitles = tabTitles)
                        }

                        item {
                            OwnerHomeContentSection(navController = navController, pagerState)
                        }
                    }
                }
            }
        )
    }


    BackHandler {
        val currentTime = System.currentTimeMillis()
        if(currentTime - backPressedTime < 2000){
            (context as? ComponentActivity)?.finishAffinity()
        } else {
            backPressedTime = currentTime
            ToastMessageUtils.showToast(context, R.string.backpress_message)
        }
    }
}

@Composable
fun OwnerHomeContentSection(navController: NavController, pagerState: PagerState) {
    HorizontalPager(
        count = OwnerHomeTabMenu.entries.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) { page ->
        when(OwnerHomeTabMenu.entries[page]) {
            OwnerHomeTabMenu.HOME -> {
                OwnerStoreHomeSection(navController)
            }
            OwnerHomeTabMenu.NEWS -> {
                NewsScreen(navController)
            }
        }
    }
}

@Composable
fun NewsScreen(navController: NavController) {

}