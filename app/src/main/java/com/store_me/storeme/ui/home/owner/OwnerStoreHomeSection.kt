package com.store_me.storeme.ui.home.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.CouponData
import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.MenuData
import com.store_me.storeme.data.enums.StoreHomeItem
import com.store_me.storeme.data.enums.menu.MenuTag
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponInfo
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.LighterHighlightColor
import com.store_me.storeme.ui.theme.PopularBoxColor
import com.store_me.storeme.ui.theme.PopularTextColor
import com.store_me.storeme.ui.theme.RecommendBoxColor
import com.store_me.storeme.ui.theme.RecommendTextColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.PriceUtils
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun OwnerStoreHomeSection(navController: NavController) {
    Column {
        StoreHomeItem.entries
            .forEach { item ->
                Spacer(modifier = Modifier.padding(top = 20.dp))

                StoreHomeItemSection(item) {
                    navController.navigate(item.route.fullRoute)
                }
            }

        Spacer(modifier = Modifier.height(200.dp))
    }
}

@Composable
fun StoreHomeItemSection(storeHomeItem: StoreHomeItem, onClick: (StoreHomeItem) -> Unit) {
    val storeDataViewModel = LocalStoreDataViewModel.current

    val notice by storeDataViewModel.notice.collectAsState()
    val featuredImages by storeDataViewModel.featuredImages.collectAsState()
    val menuCategories by storeDataViewModel.menuCategories.collectAsState()
    val coupons by storeDataViewModel.coupons.collectAsState()
    val stampCoupon by storeDataViewModel.stampCoupon.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = storeHomeItem.displayName,
            style = storeMeTextStyle(FontWeight.ExtraBold, 4),
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        when(storeHomeItem) {
            StoreHomeItem.NOTICE -> NoticeSection(notice) { onClick(it) }
            StoreHomeItem.FEATURED_IMAGES -> FeaturedImageSection(featuredImages) { onClick(it) }
            StoreHomeItem.MENU -> MenuSection(menuCategories) { onClick(it) }
            StoreHomeItem.COUPON -> CouponSection(coupons) { onClick(it) }
            StoreHomeItem.STAMP_COUPON -> StampCouponSection(stampCoupon) { onClick(it) }
            else -> {

            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }

    DefaultHorizontalDivider(thickness = 8.dp)
}

@Composable
fun NoticeSection(notice: String, onClick: (StoreHomeItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = notice.ifEmpty { "가게의 공지사항을 입력해주세요." },
            style = storeMeTextStyle(FontWeight.Normal, 0),
            color = if(notice.isEmpty()) GuideColor else Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultButton(
            buttonText = "공지사항 수정",
            diffValue = 2,
            colors = ButtonDefaults.buttonColors(
                containerColor = SubHighlightColor,
                contentColor = Color.Black
            )
        ) {
            onClick(StoreHomeItem.NOTICE)
        }
    }
}

@Composable
fun FeaturedImageSection(featuredImages: List<FeaturedImageData>, onClick: (StoreHomeItem) -> Unit) {
    if(featuredImages.isEmpty()) {
        Text(
            text = "사진을 업로드하여 메뉴나 서비스를 홍보해보세요.",
            style = storeMeTextStyle(FontWeight.Normal, 0),
            color = GuideColor,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 20.dp)
        ) {
            items(featuredImages) {
                FeaturedImageItem(it)
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    DefaultButton(
        buttonText = "사진 관리",
        diffValue = 2,
        colors = ButtonDefaults.buttonColors(
            containerColor = SubHighlightColor,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        onClick(StoreHomeItem.FEATURED_IMAGES)
    }
}

@Composable
fun FeaturedImageItem(featuredImageData: FeaturedImageData) {
    AsyncImage(
        model = featuredImageData.image,
        contentDescription = featuredImageData.description,
        modifier = Modifier
            .size(150.dp)
            .clip(shape = RoundedCornerShape(20))
            .aspectRatio(1f),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MenuSection(menuCategories: List<MenuCategoryData>, onClick: (StoreHomeItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if(menuCategories.isEmpty()) {
            Text(
                text = "메뉴를 추가하여 손님들에게 메뉴 정보를 제공해보세요.",
                style = storeMeTextStyle(FontWeight.Normal, 0),
                color = GuideColor,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        } else {
            if(menuCategories.size == 1) {
                //기본 카테고리만 있는 경우
                menuCategories[0].menus.forEachIndexed { index, menuData ->
                    MenuItem(menuData)

                    if(index != menuCategories[0].menus.lastIndex)
                        DefaultHorizontalDivider()
                }
            } else {
                Text(
                    text = menuCategories[1].categoryName,
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )

                DefaultHorizontalDivider()

                menuCategories[1].menus.forEachIndexed { index, menuData ->
                    MenuItem(menuData)

                    if(index != menuCategories[1].menus.lastIndex)
                        DefaultHorizontalDivider()
                }
            }

        }

        Spacer(modifier = Modifier.height(12.dp))

        DefaultButton(
            buttonText = "메뉴 관리",
            diffValue = 2,
            colors = ButtonDefaults.buttonColors(
                containerColor = SubHighlightColor,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            onClick(StoreHomeItem.MENU)
        }
    }
}

@Composable
fun MenuItem(menuData: MenuData) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            //메뉴 이름
            Text(
                text = menuData.name,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = Color.Black
            )

            if(!menuData.description.isNullOrEmpty()){
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = menuData.description,
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = GuideColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = PriceUtils().numberToPrice(menuData.priceType, menuData.price, menuData.minPrice, menuData.maxPrice),
                style = storeMeTextStyle(FontWeight.ExtraBold, 0),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if(menuData.isSignature){
                    Text(
                        text = MenuTag.Signature.displayName,
                        style = storeMeTextStyle(FontWeight.Bold, -2),
                        modifier = Modifier
                            .background(
                                color = LighterHighlightColor,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(4.dp),
                        color = HighlightColor
                    )
                }

                if(menuData.isPopular){
                    Text(
                        text = MenuTag.Popular.displayName,
                        style = storeMeTextStyle(FontWeight.Bold, -2),
                        modifier = Modifier
                            .background(
                                color = PopularBoxColor,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(4.dp),
                        color = PopularTextColor
                    )
                }

                if(menuData.isRecommend){
                    Text(
                        text = MenuTag.Recommend.displayName,
                        style = storeMeTextStyle(FontWeight.Bold, -2),
                        modifier = Modifier
                            .background(
                                color = RecommendBoxColor,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(4.dp),
                        color = RecommendTextColor
                    )
                }
            }
        }

        if(menuData.image != null) {
            AsyncImage(
                model = menuData.image,
                contentDescription = "메뉴 이미지",
                error = painterResource(id = R.drawable.store_null_image),
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = RoundedCornerShape(18.dp))
            )
        }
    }
}

@Composable
fun CouponSection(coupons: List<CouponData>, onClick: (StoreHomeItem) -> Unit) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val validCoupons = remember(coupons) {
        coupons
            .filter { DateTimeUtils.isValid(it.dueDate) }
            .take(4)
            .chunked(2)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        if(coupons.isEmpty()) {
            Text(
                text = "쿠폰을 추가하여 손님들에게 혜택을 제공해보세요.",
                style = storeMeTextStyle(FontWeight.Normal, 0),
                color = GuideColor,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        } else if(validCoupons.isEmpty()) {
            Text(
                text = "발급중인 쿠폰이 없습니다. 쿠폰을 새로 생성하거나 수정해보세요.",
                style = storeMeTextStyle(FontWeight.Normal, 0),
                color = GuideColor,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        } else {
            validCoupons.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    it.forEach { couponData ->
                        CouponInfo(
                            coupon = couponData,
                            modifier = Modifier
                                .weight(1f),
                            storeName = storeDataViewModel.storeInfoData.value!!.storeName
                        )
                    }

                    if (it.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        DefaultButton(
            buttonText = "쿠폰 관리",
            diffValue = 2,
            colors = ButtonDefaults.buttonColors(
                containerColor = SubHighlightColor,
                contentColor = Color.Black
            )
        ) {
            onClick(StoreHomeItem.COUPON)
        }
    }
}