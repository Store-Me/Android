@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.component

import android.Manifest
import android.content.Intent
import android.view.HapticFeedbackConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.ripple
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
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
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.data.enums.DragValue
import com.store_me.storeme.ui.location.LocationViewModel
import com.store_me.storeme.ui.mystore.CategoryViewModel
import com.store_me.storeme.ui.theme.DividerColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.NormalCategoryColor
import com.store_me.storeme.ui.theme.SelectedCategoryColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.SwipeDeleteColor
import com.store_me.storeme.ui.theme.SwipeEditColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.SizeUtils
import com.store_me.storeme.utils.SocialMediaAccountUtils
import com.store_me.storeme.utils.StoreCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/*
 * 여러 곳에서 사용되는 Composable 함수 모음
 */
@Composable
fun TitleWithDeleteButton(title: String, isInTopAppBar: Boolean = false, onClose: () -> Unit){
    val modifier =
        if(isInTopAppBar)
            Modifier.padding(start = 4.dp, end = 20.dp)
        else
            Modifier.padding(start = 20.dp, end = 20.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            style = storeMeTextStyle(FontWeight.ExtraBold, 6)
        )

        Spacer(modifier = Modifier.weight(1f))

        DeleteButton {
            onClose()
        }
    }
}

@Composable
fun TitleWithDeleteButtonAndRow(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onClose: () -> Unit,
    row: @Composable RowScope.() -> Unit
){
    Column {
        TopAppBar(title = {
            TitleWithDeleteButton(
                title = title,
                isInTopAppBar = true
            ) {
                onClose()
            } },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White,
                scrolledContainerColor = White
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            content = row
        )
    }
}

/**
 * 저장 및 추가 버튼 Composable
 */
@Composable
fun SaveAndAddButton(
    addButtonText: String,
    hasDifference: Boolean,
    onSaveClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(
            hasDifference,
            modifier = Modifier
                .weight(1f)
        ) {
            DefaultButton(
                buttonText = "저장",
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                onSaveClick()
            }
        }

        DefaultButton(
            buttonText = addButtonText,
            leftIconResource = R.drawable.ic_circle_plus,
            colors = ButtonDefaults.buttonColors(
                containerColor = HighlightColor,
                contentColor = Color.White
            ),
            leftIconTint = Color.White,
            modifier = Modifier
                .weight(1f)
        ) {
            onAddClick()
        }
    }
}

@Composable
fun SubTitleSection(text: String, modifier:Modifier = Modifier) {
    Text(
        text = text,
        style = storeMeTextStyle(FontWeight.ExtraBold, 6),
        modifier = modifier
    )
}

/**
 * Swipe 삭제 및 수정 기능을 제공하는 Composable
 */
@Composable
fun EditAndDeleteRow(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidth = with(density) { (configuration.screenWidthDp.dp).toPx() }
    val halfScreen = with(density) { (configuration.screenWidthDp.dp / 2).toPx() } // 절반 크기 계산

    val oldValue = remember { mutableStateOf(DragValue.Center) }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Center,
            anchors = DraggableAnchors {
                DragValue.Start at -screenWidth //Delete
                DragValue.Center at 0f
                DragValue.End at screenWidth
            },
            positionalThreshold = { halfScreen },
            velocityThreshold = { with(density) { 1000.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = exponentialDecay(),
            confirmValueChange = { newValue ->
                if(newValue != oldValue.value) {
                    oldValue.value = newValue

                    when(newValue) {
                        DragValue.End -> {  }
                        DragValue.Start -> {  }
                        DragValue.Center -> {  }
                    }
                }
                true
            }
        )
    }

    val editAlpha = (state.progress(DragValue.Center, DragValue.End) * 2).coerceIn(0f, 1f)
    val deleteAlpha = (state.progress(DragValue.Center, DragValue.Start) * 2).coerceIn(0f, 1f)

    LaunchedEffect(state.settledValue) {
        when (state.currentValue) {
            DragValue.End -> {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                onEdit()
                state.snapTo(DragValue.Center)
            }
            DragValue.Start -> {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                onDelete()
                state.snapTo(DragValue.Center)
            }
            else -> {  }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                drawRect(
                    color = SwipeEditColor.copy(alpha = editAlpha)
                )

                drawRect(
                    color = SwipeDeleteColor.copy(alpha = deleteAlpha)
                )

                drawContent()
            }
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "수정 아이콘",
                tint = White.copy(alpha = editAlpha),
                modifier = Modifier.size(SizeUtils.textSizeToDp(density, 6))
            )

            Text(
                text = "수정",
                style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                color = White.copy(alpha = editAlpha)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "삭제",
                style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                color = White.copy(alpha = deleteAlpha)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_delete_trashcan),
                contentDescription = "삭제 아이콘",
                tint = White.copy(alpha = deleteAlpha),
                modifier = Modifier.size(SizeUtils.textSizeToDp(density, 6))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal
                )
                .offset {
                    IntOffset(
                        state
                            .requireOffset()
                            .roundToInt(), 0
                    )
                }
        ) { content() }
    }
}

/**
 * 검색 완료 결과를 람다 함수로 반환 하는 검색 창
 */
@Composable
fun SearchField(modifier: Modifier = Modifier, observeText: String? = null, hint: String, onSearch: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .height(40.dp)
            .background(SubHighlightColor, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = observeText ?: text,
            onValueChange = { text = it },
            singleLine = true,
            textStyle = storeMeTypography.bodySmall,
            cursorBrush = SolidColor(Black),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                autoCorrectEnabled = false
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(text)
                    focusManager.clearFocus()
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
                        if (text.isEmpty() && observeText == null) {
                            Text(
                                text = hint,
                                color = Color.Gray,
                                style = storeMeTypography.bodySmall
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun TitleSearchSection(hint: String = "검색어를 입력하세요.", onSearch: (String) -> Unit, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 4.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchField(modifier = Modifier.weight(1f), hint = hint) { onSearch(it) }

        Spacer(modifier = Modifier.width(10.dp))

        DeleteButton {
            onClose()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationLayout(navController: NavController, locationViewModel: LocationViewModel){
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
                    indication = ripple(bounded = false, radius = 15.dp),

                    onClick = {

                        /*navController.navigate(CustomerRoute.LocationSetting())*/
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
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "지역 설정 아이콘",
                modifier = Modifier
                    .size(12.dp)
            )
        }
        Spacer(Modifier.weight(1f)) //중간 공백

        StrokeButton(text = "동네 설정") {
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
fun BannerLayout(navController: NavController) {
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
                    //Detail
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 내부 하단 우측에 위치
                    .padding(end = 8.dp, bottom = 8.dp, top = 15.dp, start = 15.dp)
                    .background(Black.copy(alpha = 0.7f), shape = CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .clickable {
                        //Banners
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
                        color = White
                    )

                    Spacer(Modifier.width(2.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        modifier = Modifier.size(9.sp.value.dp),
                        contentDescription = "배너 확장 아이콘",
                        tint = White
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
    val textColor = if (isSelected) White else SelectedCategoryColor

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

/**
 * Store Profile의 Link 정보
 */
@Composable
fun LinkSection(
    storeLink: List<String>,
    onShareClick: () -> Unit,
    onEditClick: () -> Unit,
    accountType: AccountType
) {
    @Composable
    fun ShareIcon(onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(HighlightColor)
                .clickable(
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_share),
                contentDescription = "공유하기",
                modifier = Modifier
                    .size(20.dp),
                tint = White
            )
        }
    }

    @Composable
    fun EditLinkIcon(onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Black)
                .clickable(
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_link),
                contentDescription = "링크 편집",
                modifier = Modifier
                    .size(20.dp),
                tint = White
            )
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = true
    ) {
        item { ShareIcon { onShareClick() } }

        if(storeLink.isNotEmpty()) {
            items(storeLink) {
                LinkIcon(
                    modifier = Modifier.size(40.dp),
                    it
                )
            }
        }

        if(accountType == AccountType.OWNER)
            item { EditLinkIcon { onEditClick() } }
    }
}

/**
 * 링크 아이콘 Composable
 * @param url 링크 주소
 * @param size 크기
 */
@Composable
fun LinkIcon(modifier: Modifier = Modifier, url: String) {
    val context = LocalContext.current

    val type = SocialMediaAccountUtils().getType(url)

    Icon(
        imageVector = ImageVector.vectorResource(id = SocialMediaAccountUtils().getIcon(type)),
        contentDescription = "프로필 링크",
        modifier = modifier
            .clickable(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        val webIntent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(webIntent)
                    }
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            )
            .clip(CircleShape),
        tint = Unspecified
    )
}


/**
 * 원형 테두리를 가진 아이콘 Composable
 * @param borderColor Border Color
 * @param circleColor Inner Circle Color
 * @param iconResource Icon Resource Id
 * @param size Circle Size
 */
@Composable
fun CircleBorderWithIcon(modifier: Modifier, borderColor: Color, circleColor: Color, iconResource: Int?, iconColor: Color, size: Int) {
    Box(
        modifier = modifier
            .size(size.dp)
            .border(width = 2.dp, shape = CircleShape, color = borderColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size((size - 1).dp)
                .background(
                    color = circleColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            iconResource?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "아이콘",
                    tint = iconColor,
                    modifier = Modifier
                        .size((size / 2).dp)
                )
            }
        }
    }
}

@Composable
fun CircleBorderWithIcon(modifier: Modifier, borderColor: Color, circleColor: Color, iconResource: Int?, iconColor: Color, size: Int, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .size(size.dp)
            .border(width = 2.dp, shape = CircleShape, color = borderColor)
            .clickable(
                onClick = { onClick() },
                indication = ripple(bounded = false),
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size((size - 1).dp)
                .background(
                    color = circleColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            iconResource?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "아이콘",
                    tint = iconColor,
                    modifier = Modifier
                        .size((size / 2).dp)
                )
            }
        }
    }
}

/**
 *
 */
@Composable
fun AlphaBackgroundText(text: String, diffValue: Int, modifier: Modifier = Modifier,iconResource: Int? = null) {
    val density = LocalDensity.current

    val horizontalPadding = 12.dp
    val verticalPadding = 8.dp
    val itemSpace = 7.dp

    Row(
        modifier = modifier
            .background(
                color = Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(6.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = verticalPadding, horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            iconResource?.let {
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = "아이콘",
                    modifier = Modifier
                        .size(SizeUtils.textSizeToDp(density, diffValue, 3)),
                    tint = White
                )

                Spacer(modifier = Modifier.width(itemSpace))
            }

            Text(
                text = text,
                style = storeMeTextStyle(FontWeight.ExtraBold, diffValue),
                color = White
            )
        }
    }
}

/**
 * 기본 HorizontalDivider
 */
@Composable
fun DefaultHorizontalDivider(modifier: Modifier = Modifier, thickness: Dp = 1.dp){
    HorizontalDivider(modifier = modifier, color = DividerColor, thickness = thickness)
}