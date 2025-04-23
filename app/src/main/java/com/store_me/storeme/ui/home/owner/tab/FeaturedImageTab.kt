@file:OptIn(ExperimentalLayoutApi::class)

package com.store_me.storeme.ui.home.owner.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Timestamp
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.data.store.StoreInfoData
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.component.SkeletonBox
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.DisabledColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.LightBlack
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.toTimeAgo
import kotlinx.coroutines.launch

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
            ImageDetailDialog(
                storeInfoData = storeInfoData,
                featuredImages = featuredImages,
                featuredImageData = it,
            ) {
                showDetailFeaturedImage = null

            }
        }
    }
}

@Composable
fun ImageDetailDialog(
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
    storeInfoData: StoreInfoData,
    featuredImages: List<FeaturedImageData>,
    featuredImageData: FeaturedImageData,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = featuredImages.indexOf(featuredImageData),
        pageCount = { featuredImages.size }
    )

    var scale by remember { mutableStateOf(1f) }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = properties
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleWithDeleteButton(
                title = "",
                tint = Color.White
            ) {
                onDismiss()
            }

            HorizontalPager (
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = scale == 1.0f //확대 시, scroll 불가
            ) { page ->

                ZoomableAsyncImage(
                    imageUrl = featuredImages[page].image,
                    width = featuredImages[page].width,
                    height = featuredImages[page].height,
                    onScaleChanged = {
                        scale = it
                    },
                    onSwipe = {
                        scope.launch {
                            pagerState.scrollBy(-it)

                            val targetPage = if (pagerState.currentPageOffsetFraction > 0.5f) {
                                pagerState.currentPage + 1
                            } else {
                                pagerState.currentPage
                            }

                            pagerState.animateScrollToPage(targetPage)
                        }
                    }
                )
            }

            if(scale == 1f) {
                ImageDescription(
                    storeInfoData = storeInfoData,
                    description = featuredImages[pagerState.currentPage].description,
                    createdAt = featuredImages[pagerState.currentPage].createdAt,
                    updatedAt = featuredImages[pagerState.currentPage].updatedAt
                )
            }
        }
    }
}

@Composable
fun ZoomableAsyncImage(
    imageUrl: String,
    width: Int,
    height: Int,
    onScaleChanged: (Float) -> Unit,
    onSwipe: (Float) -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(imageUrl) {
        scale = 1f
        offset = Offset.Zero
    }

    LaunchedEffect(scale) {
        onScaleChanged(scale)
    }

    //이미지 확대 / 축소 및 이동 감지
    val state = rememberTransformableState { zoomChange, panChange, _ ->
        if(zoomChange == 1.0f && scale == 1.0f) {
            //Pager가 이벤트를 받아야 하는 경우
            onSwipe(panChange.x)
        } else {
            //이미지 offset이 이벤트를 받아야 하는 경우

            scale = (scale * zoomChange).coerceIn(1.0f, 10.0f)

            if(scale == 1.0f) {
                offset = Offset.Zero
            } else {
                // 최대 오프셋 계산
                val maxOffsetX = ((containerSize.width * scale) - containerSize.width) / 2
                val maxOffsetY = ((containerSize.height * scale) - containerSize.height) / 2

                // 새로운 오프셋 값 계산
                val newOffsetX = (offset.x + (panChange.x * scale)).coerceIn(-maxOffsetX, maxOffsetX)
                val newOffsetY = (offset.y + (panChange.y * scale)).coerceIn(-maxOffsetY, maxOffsetY)

                offset = Offset(x = newOffsetX, y = newOffsetY)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(ratio = width.toFloat() / height)
                .onSizeChanged { containerSize = it }
                .transformable(state)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                ),
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
}

@Composable
fun ImageDescription(
    modifier: Modifier = Modifier,
    storeInfoData: StoreInfoData,
    description: String,
    createdAt: Timestamp?,
    updatedAt: Timestamp?
) {
    var showAllDescription by remember { mutableStateOf(false) }

    LaunchedEffect(description) {
        showAllDescription = false
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = LightBlack)
            .clickable(
                onClick = { showAllDescription = !showAllDescription },
                indication = null,
                interactionSource = null
            )
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileImage(
                accountType = AccountType.OWNER,
                url = storeInfoData.storeProfileImage,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = CircleShape)
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if(description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = storeMeTextStyle(fontWeight = FontWeight.Normal, 2),
                        color = Color.White,
                        maxLines = if(showAllDescription) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                FlowRow(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = storeInfoData.storeName,
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = DisabledColor
                    )

                    Text(
                        text = "·",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = DisabledColor
                    )

                    Text(
                        text = storeInfoData.storeLocation,
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = DisabledColor
                    )

                    Text(
                        text = if(createdAt != null) " " + createdAt.toTimeAgo() else "",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = DisabledColor
                    )

                    if(createdAt != updatedAt) {
                        Text(
                            text = " (수정됨)",
                            style = storeMeTextStyle(FontWeight.Normal, 0),
                            color = DisabledColor
                        )
                    }
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