@file:OptIn(ExperimentalPagerApi::class)

package com.store_me.storeme.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.store_me.storeme.ui.theme.DividerColor
import com.store_me.storeme.ui.theme.TabDividerLineColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import kotlinx.coroutines.launch

@Composable
fun StoreMeTabRow(
    pagerState: PagerState,
    tabTitles: List<String>
){
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = White,
        contentColor = Black,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .height(2.dp)
                    .fillMaxWidth(0.9f),
                color = Black
            )
        },
        divider = {
            HorizontalDivider(
                color = TabDividerLineColor,
                thickness = 2.dp
            )
        }
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = title,
                        style = storeMeTextStyle(FontWeight.Bold, 0),
                        color = if(index == pagerState.currentPage) Black else TabDividerLineColor
                    )
                }
            )
        }
    }
}

@Composable
fun StoreMeScrollableTabRow(
    pagerState: PagerState,
    tabTitles: List<String>
){
    val coroutineScope = rememberCoroutineScope()

    ScrollableTabRow (
        selectedTabIndex = pagerState.currentPage,
        containerColor = White,
        contentColor = Black,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .height(1.dp)
                    .fillMaxWidth(0.9f),
                color = Black
            )
        },
        divider = {
            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp
            )
        },
        edgePadding = 0.dp
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = title,
                        style = storeMeTextStyle(if(index == pagerState.currentPage) FontWeight.ExtraBold else FontWeight.Bold, 1 ),
                        color = if(index == pagerState.currentPage) Black else TabDividerLineColor,
                    )
                }
            )
        }
    }
}

@Composable
fun StoreMeTabContent(
    tabTitles: List<String>,
    pagerState: PagerState,
    content: @Composable (page: Int) -> Unit
) {
    HorizontalPager(
        count = tabTitles.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) { page ->
        content(page)
    }
}