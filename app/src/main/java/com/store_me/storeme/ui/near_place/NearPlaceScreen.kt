package com.store_me.storeme.ui.near_place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.NearPlaceStoreWithStoreInfoData
import com.store_me.storeme.data.StoreInfoData
import com.store_me.storeme.data.StorePromotionData
import com.store_me.storeme.ui.component.BannerLayout
import com.store_me.storeme.ui.component.CategorySection
import com.store_me.storeme.ui.component.LocationLayout
import com.store_me.storeme.ui.location.LocationViewModel
import com.store_me.storeme.ui.mystore.CategoryViewModel
import com.store_me.storeme.ui.theme.CouponDividerLineColor
import com.store_me.storeme.ui.theme.PostTimeTextColor
import com.store_me.storeme.ui.theme.SelectedCategoryColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.LikeCountUtils

@Composable
fun NearPlaceScreen(
    navController: NavController,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    nearPlaceViewModel: NearPlaceViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel
) {
    val currentStores = nearPlaceViewModel.currentStores.collectAsState().value

    LaunchedEffect(key1 = categoryViewModel.selectedCategory) {
        nearPlaceViewModel.observeCategoryChanges(categoryViewModel)
    }

    Scaffold(
        containerColor = White,
        topBar = { LocationLayout(navController = navController, locationViewModel = locationViewModel) },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item { CategorySection(categoryViewModel = categoryViewModel) }
                item { BannerLayout(navController = navController) }
                items(currentStores) { store ->
                    NearPlaceStoreItem(store)
                }
            }

        }
    )
}

@Composable
fun NearPlaceStoreItem(store: NearPlaceStoreWithStoreInfoData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = store.title,
            style = storeMeTypography.labelLarge,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp),
        )

        store.storeInfoList.forEachIndexed { index, _ ->
            if(index < 5) {
                StoreItem(storeInfo = store.storeInfoList[index], storePromotion = store.storePromotionList[index])

                HorizontalDivider(color = CouponDividerLineColor, thickness = 1.dp)
            } else {
                ShowMoreTextSection {
                    //TODO 더 보기 화면
                }

                HorizontalDivider(color = CouponDividerLineColor, thickness = 1.dp)
                return@forEachIndexed
            }
        }
    }
}

@Composable
fun ShowMoreTextSection(onClick: () -> Unit){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { onClick() }
    ){
        Text(
            text = "관련 가게 더보기",
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = SelectedCategoryColor,
        )

        Spacer(modifier = Modifier.width(5.dp))

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            tint = SelectedCategoryColor,
            contentDescription = "더보기",
            modifier = Modifier
                .size(12.dp)
        )
    }
}

@Composable
fun StoreItem(storeInfo: StoreInfoData, storePromotion: StorePromotionData) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp),
    ) {
        val (image, column) = createRefs()

        AsyncImage(
            model = storeInfo.storeImage,
            contentDescription = "${storeInfo.storeName} 사진",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .constrainAs(image) {
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .constrainAs(column) {
                    start.linkTo(parent.start)
                    end.linkTo(image.start, margin = 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .padding(end = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = storeInfo.storeName,
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = storeInfo.location,
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = PostTimeTextColor,
                    maxLines = 1,
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (storeInfo.storeDescription != null) {
                Text(
                    text = storeInfo.storeDescription,
                    style = storeMeTypography.bodySmall
                )

                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_fill_like),
                    tint = Color.Unspecified,
                    contentDescription = "좋아요",
                    modifier = Modifier.size(12.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "관심 " + LikeCountUtils().convertLikeCount(storeInfo.favoriteCount),
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.width(5.dp))

                PromotionSection(storePromotion)
            }
        }
    }
}

@Composable
fun PromotionSection(storePromotion: StorePromotionData) {
    if(storePromotion.isCouponExist){
        PromotionBox("쿠폰")
    }

    if(storePromotion.isEventExist) {
        PromotionBox("이벤트")
    }
}

@Composable
fun PromotionBox(text: String){
    Box(
        modifier = Modifier
            .background(SelectedCategoryColor, shape = RoundedCornerShape(4.dp))
            .wrapContentSize()
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = text,
            fontFamily = appFontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = White
        )
    }

    Spacer(modifier = Modifier.width(5.dp))
}