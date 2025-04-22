package com.store_me.storeme.ui.home.owner.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.component.SkeletonBox
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE

/**
 * 홈 화면 대표 이미지 탭
 */
@Composable
fun FeaturedImageTab(
    storeInfoData: StoreInfoData,
    featuredImages: List<FeaturedImageData>
) {
    if(featuredImages.isEmpty()) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.store_home_featured_images_none),
                style = storeMeTextStyle(FontWeight.Normal, 0),
                color = GuideColor
            )
        }
    } else {
        val (leftFeaturedImages, rightFeaturedImages) = remember(featuredImages) {
            val columnBalanceTolerance = 0.3f

            val left = mutableListOf<FeaturedImageData>()
            val right = mutableListOf<FeaturedImageData>()
            var leftHeight = 0f
            var rightHeight = 0f

            featuredImages.forEach { image ->
                val ratio = image.height.toFloat() / image.width
                if (leftHeight <= (rightHeight + columnBalanceTolerance)) {
                    left += image
                    leftHeight += ratio
                } else {
                    right += image
                    rightHeight += ratio
                }
            }

            left to right
        }

        var showDetailFeaturedImage by remember { mutableStateOf<FeaturedImageData?>(null) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                leftFeaturedImages.forEach {
                    FeaturedImage(featuredImage = it) {
                        showDetailFeaturedImage = it
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rightFeaturedImages.forEach {
                    FeaturedImage(featuredImage = it) {
                        showDetailFeaturedImage = it
                    }
                }
            }
        }

        showDetailFeaturedImage?.let {
            Dialog(
                onDismissRequest = { showDetailFeaturedImage = null },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = it.image,
                        contentDescription = it.description,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedImage(
    featuredImage: FeaturedImageData,
    onClick: () -> Unit
) {
    SubcomposeAsyncImage(
        model = featuredImage.image,
        contentDescription = featuredImage.description,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = featuredImage.width.toFloat() / featuredImage.height)
            .clip(shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .clickable { onClick() },
        loading = {
            SkeletonBox(
                isLoading = true,
                modifier = Modifier
                    .fillMaxSize(),
            ) {

            }
        }
    )
}