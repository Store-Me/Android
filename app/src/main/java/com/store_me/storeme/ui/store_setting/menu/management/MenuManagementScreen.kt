@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.menu.management

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.MenuCategoryData
import com.store_me.storeme.data.MenuData
import com.store_me.storeme.data.enums.MenuPriceType
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.DefaultOutlineTextField
import com.store_me.storeme.ui.component.NumberOutLineTextField
import com.store_me.storeme.ui.component.SimpleNumberOutLinedTextField
import com.store_me.storeme.ui.component.SimpleOutLinedTextField
import com.store_me.storeme.ui.component.TextFieldErrorType
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.store_setting.menu.MenuSettingViewModel
import com.store_me.storeme.ui.store_setting.menu.management.MenuManagementViewModel.MenuHighLightType
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.LighterHighlightColor
import com.store_me.storeme.ui.theme.MenuPriceDescriptionColor
import com.store_me.storeme.ui.theme.PopularBoxColor
import com.store_me.storeme.ui.theme.PopularTextColor
import com.store_me.storeme.ui.theme.RecommendBoxColor
import com.store_me.storeme.ui.theme.RecommendTextColor
import com.store_me.storeme.ui.theme.UnselectedHighLightMenuColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils

@Composable
fun MenuManagementScreen(
    navController: NavController,
    selectedMenuName: String = "",
    menuSettingViewModel: MenuSettingViewModel,
    menuManagementViewModel: MenuManagementViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current

    val menuCategories by menuSettingViewModel.menuCategories.collectAsState()

    val selectedCategory by menuManagementViewModel.selectedCategory.collectAsState()

    val menuData by menuManagementViewModel.menuData.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    fun onClose() {
        navController.popBackStack()
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        topBar = { TopAppBar(title = {
            TitleWithDeleteButton(
                title = "메뉴 추가",
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
                    menuName = menuData.name
                ) {
                    menuManagementViewModel.updateMenuName(it)
                } }

                item { MenuPriceSection(
                    menuData = menuData,
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

                item { MenuImageSection() }

                item { MenuDescriptionSection() }

                item { MenuHighlightSection() }
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
fun MenuImageSection() {
    val imageUrl = ""

    Column(
        modifier = Modifier
            .padding(20.dp),
    ) {
        Text(
            text = "사진 (선택)",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .border(
                    1.dp,
                    if (imageUrl.isEmpty()) MenuPriceDescriptionColor else Transparent,
                    RoundedCornerShape(10.dp)
                )
                .clickable {

                },
            contentAlignment = Alignment.Center
        ) {
            if(imageUrl.isEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "추가 아이콘",
                    modifier = Modifier.size(24.dp),
                    tint = MenuPriceDescriptionColor
                )
            }
        }
    }
}

@Composable
fun MenuPriceSection(
    menuData: MenuData,
    onPriceTypeChange: (MenuPriceType) -> Unit,
    onPriceChange: (Int?, Int?, Int?) -> Unit
) {
    var priceText by remember { mutableStateOf("") }
    var minPriceText by remember { mutableStateOf("") }
    var maxPriceText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Text(
            text = "가격",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                isSelected = menuData.priceType == MenuPriceType.fixed.name,
            ) {
                onPriceTypeChange(MenuPriceType.fixed)
                onPriceChange(0, null, null)
            }

            DefaultCheckButton(
                isCheckIconOnLeft = true,
                text = "범위 설정",
                fontWeight = FontWeight.ExtraBold,
                selectedColor = HighlightColor,
                isSelected = menuData.priceType == MenuPriceType.ranged.name,
            ) {
                onPriceTypeChange(MenuPriceType.fixed)
                onPriceChange(null, 0, 0)
            }

            DefaultCheckButton(
                isCheckIconOnLeft = true,
                text = "변동",
                fontWeight = FontWeight.ExtraBold,
                selectedColor = HighlightColor,
                isSelected = menuData.priceType == MenuPriceType.variable.name
            ) {
                onPriceTypeChange(MenuPriceType.fixed)
                onPriceChange(null, null, null)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(menuData.priceType == MenuPriceType.fixed.name) {
            SimpleNumberOutLinedTextField(
                text = priceText,
                onValueChange = { priceText = it },
                placeholderText = "가격을 입력하세요.",
                suffixText = "원",
                isError = false,
                errorText = ""
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        /*AnimatedVisibility(menuPriceType == RANGE) {
            Column {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "최소",
                        style = storeMeTextStyle(FontWeight.Normal, 2)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    NumberOutLineTextField(
                        text = if(rangeMinPrice == null) "" else rangeMinPrice.toString(),
                        placeholderText = "최소 가격을 입력하세요.",
                        suffixText = "원",
                        errorType = TextFieldErrorType.PRICE,
                        onValueChange = { addMenuViewModel.updateRangeMinPrice(it) },
                        onErrorChange = { addMenuViewModel.updateMinRangeError(it) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "최대",
                        style = storeMeTextStyle(FontWeight.Normal, 2)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    NumberOutLineTextField(
                        text = if(rangeMaxPrice == null) "" else rangeMaxPrice.toString(),
                        placeholderText = "최대 가격을 입력하세요.",
                        suffixText = "원",
                        errorType = TextFieldErrorType.PRICE,
                        onValueChange = { addMenuViewModel.updateRangeMaxPrice(it) },
                        onErrorChange = { addMenuViewModel.updateMaxRangeError(it) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "최소 가격이나 최대 가격만 입력해도 가격이 표시됩니다.",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = MenuPriceDescriptionColor
                )
            }
        }

        AnimatedVisibility(menuPriceType == VARIABLE) {
            Column {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "가격 변동이 있는 메뉴의 경우 선택해 주세요.",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = MenuPriceDescriptionColor
                )
            }
        }*/
    }

    DefaultHorizontalDivider()
}

@Composable
fun MenuHighlightSection() {
    /*val addMenuViewModel = LocalAddMenuViewModel.current

    val isSignature by addMenuViewModel.isSignature.collectAsState()
    val isPopular by addMenuViewModel.isPopular.collectAsState()
    val isRecommend by addMenuViewModel.isRecommend.collectAsState()

    @Composable
    fun SetMenuHighlightButton(type: MenuHighLightType, isSelected: Boolean, onClick: () -> Unit) {
        val borderStroke = if(!isSelected) BorderStroke(1.dp, UnselectedHighLightMenuColor) else null
        val contentColor = if(!isSelected) UnselectedHighLightMenuColor else when(type) {
            MenuHighLightType.SIGNATURE -> SignatureTextColor
            MenuHighLightType.POPULAR -> PopularTextColor
            MenuHighLightType.RECOMMEND -> RecommendTextColor
        }
        val containerColor = if(!isSelected) White else when(type) {
            MenuHighLightType.SIGNATURE -> LighterHighlightColor
            MenuHighLightType.POPULAR -> PopularBoxColor
            MenuHighLightType.RECOMMEND -> RecommendBoxColor
        }

        Button(
            modifier = Modifier
                .wrapContentWidth()
                .height(SizeUtils.textSizeToDp(LocalDensity.current, 2, 10)),
            shape = RoundedCornerShape(6.dp),
            border = borderStroke,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(horizontal = 10.dp),
            onClick = onClick
        ) {
            Text(text = type.displayName, style = storeMeTextStyle(FontWeight.Normal, 0))
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = "표시 (선택/중복가능)",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                SetMenuHighlightButton(MenuHighLightType.SIGNATURE, isSelected = isSignature) {
                addMenuViewModel.updateIsSignature()
            } }

            item {SetMenuHighlightButton(MenuHighLightType.POPULAR, isSelected = isPopular) {
                addMenuViewModel.updateIsPopular()
            } }

            item {SetMenuHighlightButton(MenuHighLightType.RECOMMEND, isSelected = isRecommend) {
                addMenuViewModel.updateIsRecommend()
            } }
        }
    }

    DefaultHorizontalDivider()*/
}

@Composable
fun MenuDescriptionSection() {
    /*val addMenuViewModel = LocalAddMenuViewModel.current
    val description by addMenuViewModel.description.collectAsState()

    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = "추가 설명 (선택)",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Spacer(modifier = Modifier.height(10.dp))

        DefaultOutlineTextField(
            text = description,
            placeholderText = "메뉴에 대한 설명을 작성해 보세요.",
            errorType = TextFieldErrorType.MENU_DESCRIPTION,
            onErrorChange = { addMenuViewModel.updateDescriptionError(it) },
            onValueChange = { addMenuViewModel.updateDescription(it) },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextLengthRow(text = description, limitSize = 50)
    }

    DefaultHorizontalDivider()*/
}

@Composable
fun MenuNameSection(menuName: String, onValueChange: (String) -> Unit) {
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

        Spacer(modifier = Modifier.height(8.dp))

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
fun MenuCategorySection(menuCategories: List<MenuCategoryData>, selectedCategory: String, onSelected: (String) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
            text = selectedCategory,
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
fun SelectCategoryBottomSheetContent(menuCategories: List<MenuCategoryData>, selectedCategory: String, onSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(menuCategories) {
                val isSelected by remember { derivedStateOf {
                    selectedCategory == it.categoryName
                } }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelected(it.categoryName) }
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
        ) {
            onSelected(selectedCategory)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}