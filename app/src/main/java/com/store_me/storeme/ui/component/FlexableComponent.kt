package com.store_me.storeme.ui.component

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.store_me.storeme.R
import com.store_me.storeme.data.BannerData
import com.store_me.storeme.ui.home.LocationViewModel
import com.store_me.storeme.ui.main.BOTTOM_ITEM_LIST
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.mystore.CategoryViewModel
import com.store_me.storeme.ui.theme.HomeSearchBoxColor
import com.store_me.storeme.ui.theme.NormalCategoryColor
import com.store_me.storeme.ui.theme.SelectedCategoryColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.StoreCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
* 여러 곳에서 사용 되는 Composable 함수 모음
* */
@Composable
fun TitleWithDeleteButton(navController: NavController, title: String){
    val interactionSource = remember { MutableInteractionSource() }
    val clickable = remember { mutableStateOf(true) }

    fun returnBackScreen(){
        if (navController.currentBackStackEntry?.destination?.id != navController.graph.startDestinationId) {
            clickable.value = false
            navController.popBackStack()
        }
    }

    BackHandler {
        returnBackScreen()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            style = storeMeTypography.labelLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
            contentDescription = "닫기",
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    onClick = {
                        returnBackScreen()
                    },
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = false)
                )
                .padding(2.dp)
        )
    }
}

/**
 * 기본 버튼
 */
@Composable
fun DefaultButton(text: String, onClick: () -> Unit){
    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(26.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = text, style = storeMeTypography.labelSmall)
    }
}

/**
 * 검색 완료 결과를 람다 함수로 반환 하는 검색 창
 */
@Composable
fun SearchField(modifier: Modifier = Modifier, hint: String, onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .height(40.dp)
            .background(HomeSearchBoxColor, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            textStyle = storeMeTypography.bodySmall,
            cursorBrush = SolidColor(Color.Black),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                autoCorrectEnabled = false
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(text)
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(
                                text = hint,
                                color = Color.Gray,
                                style = storeMeTypography.bodySmall
                            )
                        }
                        innerTextField()
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
                        contentDescription = "검색",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false, radius = 15.dp),
                                onClick = {
                                    onSearch(text)
                                }
                            )
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationLayout(navController: NavController, locationViewModel: LocationViewModel, bottomItemIndex: Int){
    val lastLocation by locationViewModel.lastLocation.collectAsState()

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            locationViewModel.setLocation()
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, radius = 15.dp),

                    onClick = {
                        NavigationUtils().navigateNormalNav(navController, MainActivity.NormalNavItem.LOCATION)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = lastLocation,
                style = storeMeTypography.labelMedium
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "지역 설정 아이콘",
                modifier = Modifier
                    .size(12.dp)
            )
        }
        Spacer(Modifier.weight(1f)) //중간 공백

        DefaultButton(text = "동네 설정") {
            if (locationPermissionState.permissions.any { it.status.isGranted }) {
                locationViewModel.setLocation()
            } else {
                launcher.launch(arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerLayout(navController: NavController, bottomItemIndex: Int) {
    val bannerUrls = SampleDataUtils.sampleBannerImage()
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    BoxWithConstraints (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        val boxWidth = maxWidth
        val bannerHeight = boxWidth / 5

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
        ) {
            HorizontalPager(
                count = bannerUrls.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bannerHeight)
            ) { page ->
                LoadBannerImages(bannerUrls[page], page) {
                    NavigationUtils().navigateNormalNav(navController, MainActivity.NormalNavItem.BANNER_DETAIL, it)
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 내부 하단 우측에 위치
                    .padding(end = 8.dp, bottom = 8.dp, top = 15.dp, start = 15.dp)
                    .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .clickable {
                        NavigationUtils().navigateNormalNav(navController, MainActivity.NormalNavItem.BANNER_LIST)
                    },

                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1}/${bannerUrls.size}",
                        fontFamily = appFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 9.sp,
                        color = Color.White
                    )

                    Spacer(Modifier.width(2.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        modifier = Modifier.size(9.sp.value.dp),
                        contentDescription = "배너 확장 아이콘",
                        tint = Color.White
                    )
                }

            }
        }

        //자동 슬라이드
        LaunchedEffect(pagerState) {
            while (true) {
                Thread.yield()
                delay(10000)
                scope.launch {
                    val nextPage = (pagerState.currentPage + 1) % bannerUrls.size
                    pagerState.scrollToPage(nextPage)
                }
            }
        }
    }
}

@Composable
fun LoadBannerImages(bannerUrl: BannerData, page: Int, onClick: (String) -> Unit){
    AsyncImage(
        model = bannerUrl.imageUrl,
        contentDescription = "배너 $page",
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick(bannerUrl.bannerId) }
    )
}

/**
 * 카테고리 리스트
 */
@Composable
fun CategorySection(categoryViewModel: CategoryViewModel) {
    val selectedCategory by categoryViewModel.selectedCategory.collectAsState()

    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
        ) {
            items(categoryViewModel.categoryList) { category ->
                CategoryItem(category, selectedCategory == category) {
                    categoryViewModel.selectCategory(category)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: StoreCategory, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) SelectedCategoryColor else NormalCategoryColor
    val textColor = if (isSelected) Color.White else SelectedCategoryColor

    Box(
        modifier = Modifier
            .height(30.dp)
            .clickable(onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category.displayName,
            color = textColor,
            fontFamily = appFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}