@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.banner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.store_me.storeme.data.BannerData
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.utils.SampleDataUtils
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.NavigationUtils

@Composable
fun BannerListScreen(navController: NavController) {
    val bannerList = SampleDataUtils.sampleBannerImage()

    Scaffold(
        containerColor = White,
        topBar = { TitleWithDeleteButton(navController = navController, title = "전체 보기") },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                BannerListLayout(bannerList, navController)
            }
        }
    )
}

@Composable
fun BannerListLayout(banners: List<BannerData>, navController: NavController) {
    LazyColumn (
        contentPadding = PaddingValues(horizontal = 20.dp)
    ){
        items(banners) {  banner ->
            BannerItem(banner){
                NavigationUtils().navigateCustomerNav(navController, MainActivity.CustomerNavItem.BANNER_DETAIL, it)
            }
        }
    }
}

@Composable
fun BannerItem(banner: BannerData, onClick: (String) -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5 / 1f)
                .clip(RoundedCornerShape(15.dp))
                .clickable { onClick(banner.bannerId)}
        ) {
            AsyncImage(
                model = banner.imageUrl,
                contentDescription = "배너 이미지",
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = banner.title,
            style = storeMeTypography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(25.dp))
    }
}