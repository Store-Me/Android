package com.store_me.storeme.ui.home.owner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun OwnerStoreHomeSection(navController: NavController) {
    Column {
        StoreHomeItem.entries
            .forEach { item ->
                Spacer(modifier = Modifier.padding(top = 20.dp))

                StoreHomeItemSection(item) {
                    NavigationUtils().navigateOwnerNav(navController, it)
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))

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
            text = "사진을 업로드하여 메뉴나 서비스를 홍보해 보세요.",
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