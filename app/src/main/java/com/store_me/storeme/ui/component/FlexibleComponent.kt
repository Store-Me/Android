@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.component

import android.Manifest
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
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
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.BannerData
import com.store_me.storeme.data.SocialMediaAccountData
import com.store_me.storeme.data.SocialMediaAccountType
import com.store_me.storeme.ui.home.LocationViewModel
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.mystore.CategoryViewModel
import com.store_me.storeme.ui.theme.DefaultIconColor
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.HomeSearchBoxColor
import com.store_me.storeme.ui.theme.NormalCategoryColor
import com.store_me.storeme.ui.theme.SaveButtonColor
import com.store_me.storeme.ui.theme.SelectedCategoryColor
import com.store_me.storeme.ui.theme.SelectedCheckBoxColor
import com.store_me.storeme.ui.theme.ToggleButtonBorderColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.WebIconColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.SampleDataUtils
import com.store_me.storeme.utils.SizeUtils
import com.store_me.storeme.utils.SocialMediaAccountUtils
import com.store_me.storeme.utils.StoreCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 여러 곳에서 사용되는 Composable 함수 모음
 */
@Composable
fun TitleWithDeleteButton(navController: NavController, title: String, isInTopAppBar: Boolean = false){
    val modifier =
        if(isInTopAppBar)
            Modifier.padding(start = 4.dp, end = 20.dp)
        else
            Modifier.padding(start = 20.dp, end = 20.dp)

    fun returnBackScreen(){
        if (navController.currentBackStackEntry?.destination?.id != navController.graph.startDestinationId) {
            navController.popBackStack()
        }
    }

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
            returnBackScreen()
        }
    }
}

@Composable
fun TitleWithDeleteButtonAtDetail(title: String, isInTopAppBar: Boolean = false, onBack: () -> Unit){
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
            onBack()
        }
    }
}

@Composable
fun DeleteButton(onClick: () -> Unit) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
        contentDescription = "닫기",
        modifier = Modifier
            .size(20.dp)
            .clickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            )
            .padding(2.dp)
    )
}

@Composable
fun SubTitleSection(text: String, modifier:Modifier = Modifier) {
    Text(
        text = text,
        style = storeMeTextStyle(FontWeight.ExtraBold, 6),
        modifier = modifier
    )
}

@Composable
fun SearchButton(onClick: () -> Unit) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
        contentDescription = "검색",
        modifier = Modifier
            .size(24.dp)
            .clickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            )
    )
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
        border = BorderStroke(1.dp, Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = text, style = storeMeTypography.labelSmall)
    }
}

/**
 * 기본 Toggle 버튼
 * @param text 버튼 내 텍스트
 * @param isSelected 선택 여뷰
 */
@Composable
fun DefaultToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderStroke = if(!isSelected) BorderStroke(1.dp, ToggleButtonBorderColor) else null
    val contentColor = if(!isSelected) Black else White
    val containerColor = if(!isSelected) White else Black

    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(26.dp)
            .defaultMinSize(
                minWidth = 20.dp,
                minHeight = ButtonDefaults.MinHeight
            ),
        shape = RoundedCornerShape(6.dp),
        border = borderStroke,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = text, style = storeMeTextStyle(FontWeight.Normal, 2), modifier = Modifier.padding(horizontal = 3.dp))
    }
}

@Composable
fun CircleToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderStroke = if(!isSelected) BorderStroke(1.dp, ToggleButtonBorderColor) else null
    val contentColor = if(!isSelected) Black else White
    val containerColor = if(!isSelected) White else Black

    Button(
        modifier = Modifier
            .size(40.dp),
        shape = CircleShape,
        border = borderStroke,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = storeMeTextStyle(FontWeight.Bold, 2), modifier = Modifier.padding(horizontal = 3.dp))
    }
}

/**
 * Default Check Button
 */
@Composable
fun DefaultCheckButton(
    text: String,
    fontWeight: FontWeight = FontWeight.Bold,
    diffValue: Int = 0,
    isSelected: Boolean,
    description: String = "",
    onClick: () -> Unit
) {
    val contentColor = if(!isSelected) UndefinedTextColor else SelectedCheckBoxColor
    val iconId = if(!isSelected) R.drawable.ic_check_off else R.drawable.ic_check_on

    Row(
        modifier = Modifier
            .wrapContentSize()
            .clickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text,
            style = storeMeTextStyle(fontWeight, diffValue),
            color = contentColor,
            modifier = Modifier
                .padding(vertical = 4.dp)
        )
        
        Spacer(modifier = Modifier.width(5.dp))

        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "체크 아이콘",
            modifier = Modifier
                .size(SizeUtils().textSizeToDp(LocalDensity.current, diffValue, 4)),
            tint = contentColor
        )
    }

    if(description.isNotEmpty()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SizeUtils().textSizeToDp(LocalDensity.current, diffValue, 4), top = 10.dp)
        ) {
            Text(text = description,
                style = storeMeTextStyle(FontWeight.Normal, diffValue - 2),
                color = contentColor,
            )
        }
    }
}

@Composable
fun LeftCheckButton(
    text: String,
    fontWeight: FontWeight = FontWeight.Bold,
    diffValue: Int = 0,
    isSelected: Boolean,
    description: String = "",
    onClick: () -> Unit
) {
    val contentColor = if(!isSelected) UndefinedTextColor else SelectedCheckBoxColor
    val iconId = if(!isSelected) R.drawable.ic_check_off else R.drawable.ic_check_on

    Row(
        modifier = Modifier
            .wrapContentSize()
            .clickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = null
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
        painter = painterResource(id = iconId),
        contentDescription = "체크 아이콘",
        modifier = Modifier
            .size(SizeUtils().textSizeToDp(LocalDensity.current, diffValue, 4)),
        tint = contentColor
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(text = text,
            style = storeMeTextStyle(fontWeight, diffValue),
            color = contentColor,
            modifier = Modifier
                .padding(vertical = 4.dp)
        )
    }

    if(description.isNotEmpty()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SizeUtils().textSizeToDp(LocalDensity.current, diffValue, 4) + 5.dp, top = 10.dp)
        ) {
            Text(text = description,
                style = storeMeTextStyle(FontWeight.Normal, diffValue - 2),
                color = contentColor,
            )
        }
    }
}

/**
 * 기본 Edit Button
 * @param text 버튼 내 텍스트
 * @param modifier Modifier
 * @param containerColor 박스 색
 */
@Composable
fun DefaultEditButton(
    text: String,
    modifier: Modifier = Modifier
        .height(40.dp)
        .fillMaxWidth(),
    containerColor: Color = EditButtonColor,
    contentColor: Color = Black,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = storeMeTextStyle(FontWeight.ExtraBold, 0))
    }
}

@Composable
fun LargeButton(
    text: String,
    icon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit)
{
    Button(
        modifier = modifier
            .height(50.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
    ) {
        icon?.let {
            it()

            Spacer(modifier = Modifier.width(5.dp))
        }

        Text(text = text, style = storeMeTextStyle(FontWeight.ExtraBold, 2))
    }
}

/**
 * 기본 완료 Button
 */
@Composable
fun DefaultFinishButton(
    text: String = "저장",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(text = text, style = storeMeTextStyle(FontWeight.ExtraBold, 4))
    }
}

@Composable
fun DefaultDialogButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        enabled = enabled
    ) {
        Text(text = text, style = storeMeTextStyle(FontWeight.ExtraBold, 2))
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
            .background(HomeSearchBoxColor, shape = RoundedCornerShape(10.dp)),
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

/**
 * 기본 TextField
 * @param text text 값
 */
@Composable
fun DefaultTextField(text: String, onTextChange: (String) -> Unit){
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = {  },
    )
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
                        NavigationUtils().navigateCustomerNav(
                            navController,
                            MainActivity.CustomerNavItem.LOCATION
                        )
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
                    NavigationUtils().navigateCustomerNav(navController, MainActivity.CustomerNavItem.BANNER_DETAIL, it)
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 내부 하단 우측에 위치
                    .padding(end = 8.dp, bottom = 8.dp, top = 15.dp, start = 15.dp)
                    .background(Black.copy(alpha = 0.7f), shape = CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .clickable {
                        NavigationUtils().navigateCustomerNav(
                            navController,
                            MainActivity.CustomerNavItem.BANNER_LIST
                        )
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

@Composable
fun NotificationIcon(navController: NavController) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_notification_off),
        contentDescription = "알림",
        modifier = Modifier
            .size(26.dp)
            .clickable(
                onClick = {
                    NavigationUtils().navigateCustomerNav(
                        navController,
                        MainActivity.CustomerNavItem.NOTIFICATION
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            )
    )
}

/**
 * Store Profile의 Link 정보
 */
@Composable
fun LinkSection(
    socialMediaAccountData: SocialMediaAccountData?,
    modifier: Modifier = Modifier,
    onShareClick: () -> Unit,
    onEditClick: () -> Unit
) {

    @Composable
    fun ShareIcon(onClick: () -> Unit) {
        Box(
            modifier = modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(DefaultIconColor)
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
                tint = Black
            )
        }
    }

    @Composable
    fun EditLinkIcon(onClick: () -> Unit) {
        Box(
            modifier = modifier
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        item { ShareIcon { onShareClick() } }

        if(socialMediaAccountData != null && socialMediaAccountData.urlList.isNotEmpty()) {
            items(socialMediaAccountData.urlList) {
                SocialMediaIcon(it)
            }
        }

        if(Auth.accountType == Auth.AccountType.OWNER)
            item { EditLinkIcon { onEditClick() } }
    }
}

@Composable
fun SocialMediaIcon(url: String, size: Int = 40) {
    val context = LocalContext.current

    val type = SocialMediaAccountUtils().getType(url)

    Icon(
        imageVector = ImageVector.vectorResource(id = SocialMediaAccountUtils().getIcon(type)),
        contentDescription = "프로필 링크",
        modifier = Modifier
            .size(size.dp)
            .clickable(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
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