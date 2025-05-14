package com.store_me.storeme.ui.home.owner.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.StoreHomeItem
import com.store_me.storeme.data.enums.menu.MenuTag
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.data.store.coupon.CouponData
import com.store_me.storeme.data.store.menu.MenuCategoryData
import com.store_me.storeme.data.store.menu.MenuData
import com.store_me.storeme.data.store.post.NormalPostData
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.NormalPostPreviewItem
import com.store_me.storeme.ui.component.SkeletonBox
import com.store_me.storeme.ui.main.navigation.owner.OwnerSharedRoute
import com.store_me.storeme.ui.store_setting.coupon.setting.CouponInfo
import com.store_me.storeme.ui.store_setting.post.PostViewModel
import com.store_me.storeme.ui.store_setting.review.ReviewViewModel
import com.store_me.storeme.ui.store_setting.review.StoreReviewCount
import com.store_me.storeme.ui.store_setting.story.setting.StoryDetailDialog
import com.store_me.storeme.ui.store_setting.story.setting.StoryThumbnailItem
import com.store_me.storeme.ui.store_setting.story.setting.StoryViewModel
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.LighterHighlightColor
import com.store_me.storeme.ui.theme.PopularBoxColor
import com.store_me.storeme.ui.theme.PopularTextColor
import com.store_me.storeme.ui.theme.RecommendBoxColor
import com.store_me.storeme.ui.theme.RecommendTextColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.PriceUtils
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

/**
 * 사장님 홈 화면의 스토어 홈 탭 Composable
 */
@Composable
fun StoreHomeTab(
    navController: NavController,
    storyViewModel: StoryViewModel,
    reviewViewModel: ReviewViewModel,
    postViewModel: PostViewModel
) {
    Column {
        StoreHomeItem.entries
            .forEach { item ->
                Spacer(modifier = Modifier.height(20.dp))

                StoreHomeItemSection(
                    navController = navController,
                    storeHomeItem = item,
                    storyViewModel = storyViewModel,
                    reviewViewModel = reviewViewModel,
                    postViewModel = postViewModel
                ) {
                    navController.navigate(item.route.fullRoute)
                }
            }
    }
}

@Composable
fun StoreHomeItemSection(
    navController: NavController,
    storeHomeItem: StoreHomeItem,
    storyViewModel: StoryViewModel,
    reviewViewModel: ReviewViewModel,
    postViewModel: PostViewModel,
    onClick: (StoreHomeItem) -> Unit
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val loadingViewModel = LocalLoadingViewModel.current

    val notice by storeDataViewModel.notice.collectAsState()
    val featuredImages by storeDataViewModel.featuredImages.collectAsState()
    val menuCategories by storeDataViewModel.menuCategories.collectAsState()
    val coupons by storeDataViewModel.coupons.collectAsState()
    val stampCoupon by storeDataViewModel.stampCoupon.collectAsState()

    //리뷰
    val reviewCount by reviewViewModel.reviewCounts.collectAsState()

    //게시글
    val normalPosts by postViewModel.normalPostByLabel.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        if(storeHomeItem != StoreHomeItem.REVIEW) {
            Text(
                text = storeHomeItem.displayName,
                style = storeMeTextStyle(FontWeight.ExtraBold, 4),
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
        }

        when(storeHomeItem) {
            StoreHomeItem.NOTICE -> NoticeSection(notice)
            StoreHomeItem.FEATURED_IMAGES -> FeaturedImagePreview(featuredImages)
            StoreHomeItem.MENU -> MenuPreview(menuCategories)
            StoreHomeItem.COUPON -> CouponPreview(coupons)
            StoreHomeItem.STAMP_COUPON -> StampTab(stampCoupon)
            StoreHomeItem.STORY -> RecentStory(storyViewModel)
            StoreHomeItem.REVIEW -> { StoreReviewCount(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp),
                reviewCount = reviewCount
            ) }
            StoreHomeItem.POST -> RecentPost(
                normalPosts = normalPosts[null] ?: emptyList(),
                onPostClick = {
                    postViewModel.updateSelectedNormalPost(it)
                    navController.navigate(OwnerSharedRoute.PostDetail.path)
                },
                onProfileClick = {

                },
                onLikeClick = {
                    postViewModel.likeNormalPost(it)
                },
                onCommentClick = {

                },
                onClickEdit = {
                    postViewModel.updateSelectedNormalPost(it)
                    navController.navigate(OwnerSharedRoute.EditNormalPost.path)
                },
                onClickDelete = {
                    loadingViewModel.showLoading()
                    postViewModel.deletePost(it.id)
                },
                onClickReport = {
                    //TODO REPORT
                }
            )
        }

        DefaultButton(
            buttonText = "${storeHomeItem.displayName} 관리",
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

        Spacer(modifier = Modifier.height(12.dp))
    }

    DefaultHorizontalDivider(thickness = 8.dp)
}

@Composable
fun NoneContentText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        style = storeMeTextStyle(FontWeight.Normal, 0),
        color = GuideColor,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun NoticeSection(
    notice: String
) {
    var showAll by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        if(notice.isEmpty()) {
            NoneContentText(text = stringResource(R.string.owner_home_notice_none))
        } else {
            Text(
                text = notice,
                style = storeMeTextStyle(FontWeight.Normal, 0),
                color = Color.Black,
                maxLines = if(showAll) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .clickable {
                        showAll = !showAll
                    }
            )
        }
    }
}

@Composable
fun FeaturedImagePreview(
    featuredImages: List<FeaturedImageData>
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    var showDetailFeaturedImage by remember { mutableStateOf<FeaturedImageData?>(null) }

    if(featuredImages.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            NoneContentText(text = stringResource(R.string.owner_home_photo_none))
        }
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 20.dp)
        ) {
            items(featuredImages) {
                FeaturedImageItem(it) {
                    showDetailFeaturedImage = it
                }
            }
        }
    }

    showDetailFeaturedImage?.let {
        FeaturedImageDetailDialog(
            storeInfoData = storeInfoData!!,
            featuredImages = featuredImages,
            featuredImageData = it,
        ) {
            showDetailFeaturedImage = null

        }
    }
}

@Composable
fun FeaturedImageItem(
    featuredImageData: FeaturedImageData,
    onClick: () -> Unit
) {
    SubcomposeAsyncImage(
        model = featuredImageData.image,
        contentDescription = featuredImageData.description,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .aspectRatio(1f)
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

@Composable
fun MenuPreview(
    menuCategories: List<MenuCategoryData>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if(menuCategories.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                NoneContentText(text = stringResource(R.string.owner_home_menu_none))
            }
        } else {
            if(menuCategories.size == 1) {
                //기본 카테고리만 있는 경우
                menuCategories[0].menus.forEachIndexed { index, menuData ->
                    MenuItem(menuData)

                    if(index != menuCategories[0].menus.lastIndex)
                        DefaultHorizontalDivider()
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))

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
fun CouponPreview(
    coupons: List<CouponData>
) {
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
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if(coupons.isEmpty()) {
            NoneContentText(text = stringResource(R.string.owner_home_coupon_none))
        } else if(validCoupons.isEmpty()) {
            NoneContentText(text = stringResource(R.string.owner_home_valid_coupon_none))
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
    }
}

@Composable
fun RecentStory(
    storyViewModel: StoryViewModel,
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val stories by storyViewModel.stories.collectAsState()
    var showStory by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    val recentStories = remember(stories) {
        stories.take(2)
    }

    if(recentStories.isEmpty()) {
        NoneContentText(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            text = stringResource(R.string.owner_home_story_none)
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recentStories.forEachIndexed { index, recentStory ->
                StoryThumbnailItem(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedIndex = index
                            showStory = true
                        },
                    thumbnailUrl = recentStory.thumbNail,
                )
            }

            if(recentStories.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    if(showStory) {
        StoryDetailDialog(
            storeInfoData = storeInfoData!!,
            selectedIndex = selectedIndex,
            stories = stories,
            onDismiss = { showStory = false },
            onLike = {
                storyViewModel.updateStoryLike(it)
            }
        )
    }
}

/**
 * 최근 게시글을 최대 2개까지 보여주는 Composable
 */
@Composable
fun RecentPost(
    normalPosts: List<NormalPostData>,
    onPostClick: (NormalPostData) -> Unit,
    onProfileClick: () -> Unit,
    onLikeClick: (NormalPostData) -> Unit,
    onCommentClick: (NormalPostData) -> Unit,
    onClickEdit: (NormalPostData) -> Unit,
    onClickDelete: (NormalPostData) -> Unit,
    onClickReport: (NormalPostData) -> Unit,
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val recentNormalPosts = remember(normalPosts) {
        normalPosts
            .take(2)
    }

    if(recentNormalPosts.isEmpty()) {
        NoneContentText(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            text = stringResource(R.string.owner_home_post_none)
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            recentNormalPosts.forEach {
                NormalPostPreviewItem(
                    normalPost = it,
                    storeInfoData = storeDataViewModel.storeInfoData.value!!,
                    onPostClick = {
                        onPostClick(it)
                    },
                    onProfileClick = {
                        onProfileClick()
                    },
                    onLikeClick = {
                        onLikeClick(it)
                    },
                    onCommentClick = {
                        onCommentClick(it)
                    },
                    onClickEdit = {
                        onClickEdit(it)
                    },
                    onClickDelete = {
                        onClickDelete(it)
                    },
                    onClickReport = {
                        onClickReport(it)
                    }
                )
            }
        }
    }
}