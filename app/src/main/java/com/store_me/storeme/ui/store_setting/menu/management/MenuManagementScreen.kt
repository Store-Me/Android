@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.menu.management

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.store.menu.MenuCategoryData
import com.store_me.storeme.data.store.menu.MenuData
import com.store_me.storeme.data.enums.menu.MenuPriceType
import com.store_me.storeme.data.enums.menu.MenuTag
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.DefaultToggleButton
import com.store_me.storeme.ui.component.LoadingProgress
import com.store_me.storeme.ui.component.SimpleNumberOutLinedTextField
import com.store_me.storeme.ui.component.SimpleOutLinedTextField
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.store_setting.menu.MenuSettingViewModel
import com.store_me.storeme.ui.theme.DisabledColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.LighterHighlightColor
import com.store_me.storeme.ui.theme.PopularBoxColor
import com.store_me.storeme.ui.theme.RecommendBoxColor
import com.store_me.storeme.ui.theme.RecommendTextColor
import com.store_me.storeme.ui.theme.SwipeEditColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.CropUtils
import com.store_me.storeme.utils.DEFAULT_MENU_CATEGORY_NAME
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.yalantis.ucrop.UCrop

@Composable
fun MenuManagementScreen(
    navController: NavController,
    selectedMenuName: String = "",
    menuSettingViewModel: MenuSettingViewModel,
    menuManagementViewModel: MenuManagementViewModel = hiltViewModel()
) {
    val storeDataViewModel = LocalStoreDataViewModel.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current

    val originalMenuData = menuSettingViewModel.getMenuWithCategory(menuName = selectedMenuName)

    val menuCategories by menuSettingViewModel.menuCategories.collectAsState()

    val selectedCategory by menuManagementViewModel.selectedCategory.collectAsState()

    val name by menuManagementViewModel.name.collectAsState()

    val priceType by menuManagementViewModel.priceType.collectAsState()
    val price by menuManagementViewModel.price.collectAsState()
    val minPrice by menuManagementViewModel.minPrice.collectAsState()
    val maxPrice by menuManagementViewModel.maxPrice.collectAsState()

    val image by menuManagementViewModel.image.collectAsState()
    val imageUri by menuManagementViewModel.imageUri.collectAsState()
    val progress by menuManagementViewModel.uploadProgress.collectAsState()

    val description by menuManagementViewModel.description.collectAsState()

    val isSignature by menuManagementViewModel.isSignature.collectAsState()
    val isPopular by menuManagementViewModel.isPopular.collectAsState()
    val isRecommend by menuManagementViewModel.isRecommend.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    val hasDifference by remember { derivedStateOf {
        if(selectedMenuName.isEmpty()) {
            //추가
            true
        } else {
            //수정
            if(originalMenuData == null)
                false
            else {
                originalMenuData.second != MenuData(
                    name = name,
                    priceType = priceType ?: "",
                    price = price,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    image = image,
                    description = description,
                    isSignature = isSignature,
                    isPopular = isPopular,
                    isRecommend = isRecommend
                ) || originalMenuData.first != selectedCategory
            }
        }
    } }

    fun onClose() {
        if(hasDifference) {
            showDialog = true
        } else {
            navController.popBackStack()
        }
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(originalMenuData) {
        if(originalMenuData == null)
            return@LaunchedEffect

        menuManagementViewModel.updateMenuData(menuData = originalMenuData.second)
        menuManagementViewModel.updateSelectedCategory(selectedCategory = originalMenuData.first)
    }

    LaunchedEffect(imageUri) {
        menuManagementViewModel.uploadStoreMenuImage()
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        topBar = { TopAppBar(title = {
            TitleWithDeleteButton(
                title = if(selectedMenuName.isEmpty()) "메뉴 추가" else "메뉴 수정",
                isInTopAppBar = true
            ) {
                onClose()
            } },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                scrolledContainerColor = Color.White
            )
        ) },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxWidth()
            ) {
                item { MenuCategorySection(
                    menuCategories = menuCategories,
                    selectedCategory = selectedCategory
                ) {
                    menuManagementViewModel.updateSelectedCategory(it)
                } }

                item { MenuNameSection(
                    menuName = name
                ) {
                    menuManagementViewModel.updateMenuName(it)
                } }

                item { MenuPriceSection(
                    priceType = priceType,
                    initPrice = price,
                    initMinPrice = minPrice,
                    initMaxPrice = maxPrice,
                    onPriceTypeChange = {
                        menuManagementViewModel.updateMenuPriceType(it)
                    },
                    onPriceChange = { price, minPrice, maxPrice ->
                        menuManagementViewModel.updateMenuPrice(
                            price = price,
                            minPrice = minPrice,
                            maxPrice = maxPrice
                        )
                    }
                ) }

                item { MenuImageSection(
                    image = image,
                    imageUri = imageUri,
                    progress = progress
                ) {
                    menuManagementViewModel.updateImageUri(it)
                } }

                item { MenuDescriptionSection(
                    description = description
                ) {
                    menuManagementViewModel.updateMenuDescription(it)
                } }

                item { MenuHighlightSection(
                    isSignature = isSignature,
                    isPopular = isPopular,
                    isRecommend = isRecommend,
                    onChangeIsSignature = {
                        menuManagementViewModel.updateMenuSignature(it)
                    },
                    onChangeIsPopular = {
                        menuManagementViewModel.updateMenuPopular(it)
                    },
                    onChangeIsRecommend = {
                        menuManagementViewModel.updateMenuRecommend(it)
                    }
                ) }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "저장",
                        enabled = hasDifference && name.isNotEmpty() && !priceType.isNullOrEmpty() && imageUri == null &&
                                when (priceType) {
                                    MenuPriceType.Fixed.name -> { price != null }
                                    MenuPriceType.Ranged.name -> { !(minPrice == null && maxPrice == null) }
                                    else -> { true }
                                },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        if(selectedMenuName.isEmpty()) {
                            //추가인 경우
                            menuSettingViewModel.addMenu(
                                MenuData(
                                    name = name,
                                    priceType = priceType!!,
                                    price = menuManagementViewModel.price.value,
                                    minPrice = menuManagementViewModel.minPrice.value,
                                    maxPrice = menuManagementViewModel.maxPrice.value,
                                    image = image,
                                    description = description,
                                    isSignature = isSignature,
                                    isPopular = isPopular,
                                    isRecommend = isRecommend
                                ),
                                targetCategoryName = selectedCategory
                            )
                        } else {
                            //수정인 경우
                            menuSettingViewModel.editMenu(
                                originalMenuName = selectedMenuName,
                                newMenu = MenuData(
                                    name = name,
                                    priceType = priceType!!,
                                    price = menuManagementViewModel.price.value,
                                    minPrice = menuManagementViewModel.minPrice.value,
                                    maxPrice = menuManagementViewModel.maxPrice.value,
                                    image = image,
                                    description = description,
                                    isSignature = isSignature,
                                    isPopular = isPopular,
                                    isRecommend = isRecommend
                                ),
                                newCategoryName = selectedCategory
                            )
                        }

                        navController.popBackStack()
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    )


    if(showDialog) {
        BackWarningDialog(
            onAction = {
                navController.popBackStack()
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }
}

@Composable
fun MenuCategorySection(menuCategories: List<MenuCategoryData>, selectedCategory: String, onSelected: (String) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    DefaultHorizontalDivider()

    Row(
        modifier = Modifier
            .clickable { showBottomSheet = true }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "카테고리",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = selectedCategory.ifEmpty { "카테고리를 선택해주세요." },
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            color = GuideColor
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "이동 아이콘",
            modifier = Modifier
                .size(18.dp),
            tint = GuideColor
        )
    }

    DefaultHorizontalDivider()

    if(showBottomSheet) {
        DefaultBottomSheet(onDismiss = { showBottomSheet = false }, sheetState = sheetState) {
            SelectCategoryBottomSheetContent(
                menuCategories = menuCategories,
                selectedCategory = selectedCategory
            ) {
                onSelected(it)
            }
        }
    }
}

@Composable
fun SelectCategoryBottomSheetContent(
    menuCategories: List<MenuCategoryData>,
    selectedCategory: String,
    onSelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf(selectedCategory) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(menuCategories.ifEmpty { listOf(MenuCategoryData(DEFAULT_MENU_CATEGORY_NAME, emptyList())) }) {
                val isSelected by remember { derivedStateOf {
                    selected == it.categoryName
                } }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selected = it.categoryName }
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${it.categoryName} (${it.menus.size})",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = if(!isSelected) R.drawable.ic_check_off else R.drawable.ic_check_on),
                        contentDescription = "체크 아이콘",
                        modifier = Modifier
                            .size(24.dp),
                        tint = if(!isSelected) GuideColor else HighlightColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton(
            buttonText = "저장",
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            onSelected(selected)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun MenuNameSection(
    menuName: String,
    onValueChange: (String) -> Unit
) {
    val isError by remember { derivedStateOf {
        menuName.length > 30
    } }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "메뉴 이름",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SimpleOutLinedTextField(
            text = menuName,
            placeholderText = "메뉴 이름을 입력하세요.",
            onValueChange = { onValueChange(it) },
            isError = isError,
            errorText = "메뉴 이름은 30자 이하여야 합니다.",
        )

        TextLengthRow(text = menuName, limitSize = 30)
    }

    DefaultHorizontalDivider()
}

@Composable
fun MenuPriceSection(
    priceType: String?,
    initPrice: Long?,
    initMinPrice: Long?,
    initMaxPrice: Long?,
    onPriceTypeChange: (MenuPriceType) -> Unit,
    onPriceChange: (Long?, Long?, Long?) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Text(
            text = "가격",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.height(12.dp))

        //메뉴 타입 설정
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            DefaultCheckButton(
                isCheckIconOnLeft = true,
                text = "정가",
                fontWeight = FontWeight.ExtraBold,
                selectedColor = HighlightColor,
                isSelected = priceType == MenuPriceType.Fixed.name,
            ) {
                onPriceTypeChange(MenuPriceType.Fixed)
                onPriceChange(null, null, null)
            }

            DefaultCheckButton(
                isCheckIconOnLeft = true,
                text = "범위 설정",
                fontWeight = FontWeight.ExtraBold,
                selectedColor = HighlightColor,
                isSelected = priceType == MenuPriceType.Ranged.name,
            ) {
                onPriceTypeChange(MenuPriceType.Ranged)
                onPriceChange(null, null, null)
            }

            DefaultCheckButton(
                isCheckIconOnLeft = true,
                text = "변동",
                fontWeight = FontWeight.ExtraBold,
                selectedColor = HighlightColor,
                isSelected = priceType == MenuPriceType.Variable.name
            ) {
                onPriceTypeChange(MenuPriceType.Variable)
                onPriceChange(null, null, null)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        //정가 가격 입력
        AnimatedVisibility(priceType == MenuPriceType.Fixed.name) {
            SimpleNumberOutLinedTextField(
                text = initPrice?.toString() ?: "",
                onValueChange = { onPriceChange(it.toLongOrNull(), null, null) },
                placeholderText = "가격을 입력하세요.",
                suffixText = "원",
                isError = false,
                errorText = ""
            )
        }

        AnimatedVisibility(priceType == MenuPriceType.Ranged.name) {
            Column {
                //최소 가격 입력
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "최소",
                        style = storeMeTextStyle(FontWeight.Normal, 2)
                    )

                    SimpleNumberOutLinedTextField(
                        text = initMinPrice?.toString() ?: "",
                        onValueChange = { onPriceChange(null, it.toLongOrNull(), initMaxPrice) },
                        placeholderText = "최소 가격을 입력하세요.",
                        suffixText = "원",
                        isError = false,
                        errorText = ""
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "최대",
                        style = storeMeTextStyle(FontWeight.Normal, 2)
                    )

                    SimpleNumberOutLinedTextField(
                        text = initMaxPrice?.toString() ?: "",
                        onValueChange = { onPriceChange(null, initMinPrice, it.toLongOrNull()) },
                        placeholderText = "최대 가격을 입력하세요.",
                        suffixText = "원",
                        isError = false,
                        errorText = ""
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "최소 가격이나 최대 가격만 입력해도 가격이 표시됩니다.",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = GuideColor
                )
            }
        }

        AnimatedVisibility(priceType == MenuPriceType.Variable.name) {
            Column {
                Text(
                    text = "가격 변동이 있는 메뉴의 경우 선택해 주세요.",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = GuideColor
                )
            }
        }
    }

    DefaultHorizontalDivider()
}

@Composable
fun MenuImageSection(
    image: String?,
    imageUri: Uri?,
    progress: Float,
    onUriChange: (Uri) -> Unit,
) {
    val context = LocalContext.current
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let { uri ->
                onUriChange(uri)
            }
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { sourceUri ->
            val cropIntent = CropUtils.getCropIntent(context = context, sourceUri = sourceUri, aspectRatio = Pair(1f, 1f))
            cropLauncher.launch(cropIntent)
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Text(
            text = "사진",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.height(12.dp))

        ImageBox(modifier = Modifier.size(100.dp), image = image, imageUri = imageUri, progress = progress) {
            galleryLauncher.launch("image/*")
        }
    }
}

@Composable
fun ImageBox(
    modifier: Modifier = Modifier,
    image: String?,
    imageUri: Uri?,
    progress: Float,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(color = GuideColor, shape = RoundedCornerShape(14))
            .clip(shape = RoundedCornerShape(14))
            .clickable { onClick() }
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .clip(shape = RoundedCornerShape(14))
        ) {
            drawRect(color = Color.White)
        }

        when {
            imageUri != null -> {
                AsyncImage(
                    model = imageUri,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(shape = RoundedCornerShape(14)),
                    contentDescription = "이미지"
                )
            }
            image != null -> {
                AsyncImage(
                    model = image,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(shape = RoundedCornerShape(14)),
                    contentDescription = "이미지"
                )
            }
            else -> {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "추가 아이콘",
                    modifier = Modifier.size(24.dp),
                    tint = GuideColor
                )
            }
        }

        if(imageUri != null) {
            LoadingProgress(
                progress = progress,
                modifier = Modifier
                    .matchParentSize()
            )
        }
    }
}

@Composable
fun MenuDescriptionSection(
    description: String,
    onValueChange: (String) -> Unit
) {
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(description) {
        isError = description.length > 50
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "추가 설명",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.height(8.dp))

        SimpleOutLinedTextField(
            text = description,
            placeholderText = "메뉴에 대한 추가 설명을 입력하세요.",
            onValueChange = { onValueChange(it) },
            isError = isError,
            errorText = "메뉴 설명은 50자 이하여야 합니다.",
        )

        TextLengthRow(text = description, limitSize = 50)
    }

    DefaultHorizontalDivider()
}

@Composable
fun MenuHighlightSection(
    isSignature: Boolean,
    isPopular: Boolean,
    isRecommend: Boolean,
    onChangeIsSignature: (Boolean) -> Unit,
    onChangeIsPopular: (Boolean) -> Unit,
    onChangeIsRecommend: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = "표시 (선택/중복가능)",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                DefaultToggleButton(
                    buttonText = MenuTag.Signature.displayName,
                    isSelected = isSignature,
                    unSelectedBorderColor = DisabledColor,
                    selectedContentColor = HighlightColor,
                    unSelectedContentColor = DisabledColor,
                    selectedContainerColor = LighterHighlightColor,
                    diffValue = 0
                ) {
                    onChangeIsSignature(!isSignature)
                }
            }

            item { DefaultToggleButton(
                buttonText = MenuTag.Popular.displayName,
                isSelected = isPopular,
                unSelectedBorderColor = DisabledColor,
                selectedContentColor = SwipeEditColor,
                unSelectedContentColor = DisabledColor,
                selectedContainerColor = PopularBoxColor,
                diffValue = 0
            ) {
                onChangeIsPopular(!isPopular)
            } }

            item { DefaultToggleButton(
                buttonText = MenuTag.Recommend.displayName,
                isSelected = isRecommend,
                unSelectedBorderColor = DisabledColor,
                selectedContentColor = RecommendTextColor,
                unSelectedContentColor = DisabledColor,
                selectedContainerColor = RecommendBoxColor,
                diffValue = 0
            ) {
                onChangeIsRecommend(!isRecommend)
            } }
        }
    }
}