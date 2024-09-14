@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_setting.menu.add

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.MenuPriceType.FIXED
import com.store_me.storeme.data.MenuPriceType.RANGE
import com.store_me.storeme.data.MenuPriceType.VARIABLE
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultFinishButton
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.DefaultOutlineTextField
import com.store_me.storeme.ui.component.LeftCheckButton
import com.store_me.storeme.ui.component.NumberOutLineTextField
import com.store_me.storeme.ui.component.TextFieldErrorType
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.TitleWithSaveButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.store_setting.menu.add.AddMenuViewModel.MenuHighLightType
import com.store_me.storeme.ui.theme.MenuPriceDescriptionColor
import com.store_me.storeme.ui.theme.PopularBoxColor
import com.store_me.storeme.ui.theme.PopularTextColor
import com.store_me.storeme.ui.theme.RecommendBoxColor
import com.store_me.storeme.ui.theme.RecommendTextColor
import com.store_me.storeme.ui.theme.SelectedCheckBoxColorPink
import com.store_me.storeme.ui.theme.SelectedMenuCategoryColor
import com.store_me.storeme.ui.theme.SignatureBoxColor
import com.store_me.storeme.ui.theme.SignatureTextColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.UnselectedHighLightMenuColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.SizeUtils

val LocalAddMenuViewModel = staticCompositionLocalOf<AddMenuViewModel> {
    error("No AddMenuViewModel")
}

@Composable
fun AddMenuScreen(
    navController: NavController,
    addMenuViewModel: AddMenuViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current

    var showDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalAddMenuViewModel provides addMenuViewModel) {
        Scaffold (
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
            containerColor = White,
            topBar = { AddMenuTopLayout(navController = navController, scrollBehavior = scrollBehavior){
                addMenuViewModel.addMenuData()
                navController.popBackStack()
            } },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxWidth()
                ) {
                    item { MenuCategorySection() }

                    item { MenuNameSection() }

                    item { MenuPriceSection() }

                    item { MenuImageSection() }

                    item { MenuDescriptionSection() }

                    item { MenuHighlightSection() }
                }
            }
        )
    }

    if(showDialog) {
        BackWarningDialog(
            onAction = {
                navController.popBackStack()
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }

    BackHandler {
        showDialog = true
    }
}

@Composable
fun MenuImageSection() {
    val addMenuViewModel = LocalAddMenuViewModel.current
    val imageUrl by addMenuViewModel.imageUrl.collectAsState()

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
fun MenuPriceSection() {
    val addMenuViewModel = LocalAddMenuViewModel.current

    val menuPriceType by addMenuViewModel.menuPriceType.collectAsState()

    val fixedPrice by addMenuViewModel.fixedPrice.collectAsState()
    val rangeMinPrice by addMenuViewModel.rangeMinPrice.collectAsState()
    val rangeMaxPrice by addMenuViewModel.rangeMaxPrice.collectAsState()

    Column(
        modifier = Modifier
            .padding(20.dp),
    ) {
        Text(
            text = "가격",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            LeftCheckButton(
                text = "정가",
                fontWeight = FontWeight.ExtraBold,
                selectedColor = SignatureTextColor,
                isSelected = menuPriceType == FIXED,
            ) {
                addMenuViewModel.updateMenuPriceType(FIXED)
            }

            LeftCheckButton(
                text = "범위 설정",
                fontWeight = FontWeight.ExtraBold,
                selectedColor = SignatureTextColor,
                isSelected = menuPriceType == RANGE,
            ) {
                addMenuViewModel.updateMenuPriceType(RANGE)
            }

            LeftCheckButton(
                text = "변동",
                fontWeight = FontWeight.ExtraBold,
                selectedColor = SignatureTextColor,
                isSelected = menuPriceType == VARIABLE,
            ) {
                addMenuViewModel.updateMenuPriceType(VARIABLE)
            }
        }

        AnimatedVisibility(menuPriceType == FIXED) {
            Column {
                Spacer(modifier = Modifier.height(10.dp))

                NumberOutLineTextField(
                    text = if(fixedPrice == null) "" else fixedPrice.toString(),
                    placeholderText = "가격을 입력하세요.",
                    suffixText = "원",
                    errorType = TextFieldErrorType.PRICE,
                    onValueChange = { addMenuViewModel.updateFixedPrice(it) },
                    onErrorChange = { addMenuViewModel.updateFixedError(it) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "정해진 가격의 메뉴의 경우 선택해 주세요.",
                    style = storeMeTextStyle(FontWeight.Bold, 0),
                    color = MenuPriceDescriptionColor
                )
            }
        }

        AnimatedVisibility(menuPriceType == RANGE) {
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
        }
    }

    DefaultHorizontalDivider()
}

@Composable
fun MenuHighlightSection() {
    val addMenuViewModel = LocalAddMenuViewModel.current

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
            MenuHighLightType.SIGNATURE -> SignatureBoxColor
            MenuHighLightType.POPULAR -> PopularBoxColor
            MenuHighLightType.RECOMMEND -> RecommendBoxColor
        }

        Button(
            modifier = Modifier
                .wrapContentWidth()
                .height(SizeUtils().textSizeToDp(LocalDensity.current, 2, 10)),
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

    DefaultHorizontalDivider()
}

@Composable
fun MenuDescriptionSection() {
    val addMenuViewModel = LocalAddMenuViewModel.current
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

    DefaultHorizontalDivider()
}

@Composable
fun MenuNameSection(isEdit: Boolean = false) {
    val addMenuViewModel = LocalAddMenuViewModel.current
    val name by addMenuViewModel.name.collectAsState()

    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = "메뉴 이름",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Spacer(modifier = Modifier.height(10.dp))

        DefaultOutlineTextField(
            text = name,
            onValueChange = { addMenuViewModel.updateName(it) },
            placeholderText = "메뉴 이름을 입력하세요.",
            errorType = TextFieldErrorType.MENU_NAME,
            onErrorChange = { addMenuViewModel.updateNameError(it) },
            exceptText = if(isEdit) addMenuViewModel.originMenuData.name else ""
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextLengthRow(text = name, limitSize = 15)
    }

    DefaultHorizontalDivider()
}

@Composable
fun MenuCategorySection() {
    val addMenuViewModel = LocalAddMenuViewModel.current
    val selectedCategory by remember { addMenuViewModel.selectedCategory }.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Row(
        modifier = Modifier
            .clickable { showBottomSheet = true }
            .padding(20.dp)
    ) {
        Text(
            text = "카테고리 (선택)",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = selectedCategory,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = SelectedMenuCategoryColor
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painter = painterResource(id = R.drawable.arrow_right),
            contentDescription = "이동 아이콘",
            modifier = Modifier.size(SizeUtils().textSizeToDp(LocalDensity.current, 2, 0))
        )
    }

    DefaultHorizontalDivider()

    if(showBottomSheet) {
        DefaultBottomSheet(onDismiss = { showBottomSheet = false }, sheetState = sheetState) {
            MenuCategoryListSection {
                showBottomSheet = false
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun MenuCategoryListSection(onFinish: () -> Unit) {
    val addMenuViewModel = LocalAddMenuViewModel.current
    //val selectedCategory by remember { addMenuViewModel.selectedCategory }.collectAsState()
    var selectedCategory by remember { mutableStateOf(addMenuViewModel.selectedCategory.value) }

    val categoryList = Auth.menuCategoryList.value

    LazyColumn {
        items(categoryList) {
            val isSelected = selectedCategory == it.categoryName

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedCategory = it.categoryName
                    }
                    .padding(horizontal = 20.dp, vertical = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "${it.categoryName} (${it.menuList.size})",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                    )
                }

                Icon(
                    painter = painterResource(id = if(!isSelected) R.drawable.ic_check_off else R.drawable.ic_check_on),
                    contentDescription = "체크 아이콘",
                    modifier = Modifier
                        .size(SizeUtils().textSizeToDp(LocalDensity.current, 2, 4)),
                    tint = if(!isSelected) UndefinedTextColor else SelectedCheckBoxColorPink
                )
            }
        }

        item {
            DefaultFinishButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                addMenuViewModel.updateSelectedCategory(selectedCategory)
                onFinish()
            }
        }
    }
}

@Composable
fun AddMenuTopLayout(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    isEdit: Boolean = false,
    onFinish: () -> Unit
) {
    val title = if(isEdit) "메뉴 수정" else "메뉴 추가"

    val addMenuViewModel = LocalAddMenuViewModel.current

    val name by addMenuViewModel.name.collectAsState()
    val isNameError by addMenuViewModel.isNameError.collectAsState()
    val menuPriceType by addMenuViewModel.menuPriceType.collectAsState()
    val isFixedError by addMenuViewModel.isFixedError.collectAsState()
    val fixedPrice by addMenuViewModel.fixedPrice.collectAsState()
    val isMinRangeError by addMenuViewModel.isMinRangeError.collectAsState()
    val isMaxRangeError by addMenuViewModel.isMaxRangeError.collectAsState()
    val rangeMinPrice by addMenuViewModel.rangeMinPrice.collectAsState()
    val rangeMaxPrice by addMenuViewModel.rangeMaxPrice.collectAsState()
    val isDescriptionError by addMenuViewModel.isDescriptionError.collectAsState()

    fun isEnable():Boolean {
        //이름 유효성 검사
        fun isNameEnable(): Boolean {
            return when {
                name.isEmpty() -> false
                isNameError -> false
                else -> true
            }
        }

        //가격 관련
        fun isPriceEnable(): Boolean {
            return when(menuPriceType) {
                FIXED -> {
                    when {
                        isFixedError -> false
                        fixedPrice == 0 -> false
                        fixedPrice == null -> false
                        else -> true
                    }
                }
                RANGE -> {
                    when {
                        isMinRangeError -> false
                        isMaxRangeError -> false
                        rangeMinPrice == 0 && rangeMaxPrice == 0 -> false
                        rangeMinPrice == null && rangeMaxPrice == null -> false
                        else -> true
                    }
                }
                VARIABLE -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

        //추가 설명 관련
        fun isDescriptionEnable(): Boolean {
            return when {
                isDescriptionError -> false
                else -> true
            }
        }

        return isNameEnable() && isPriceEnable() && isDescriptionEnable()
    }
    TitleWithSaveButton(
        navController = navController,
        title = title,
        scrollBehavior = scrollBehavior,
        finishButtonEnable = isEnable()
    ) {
        onFinish()
    }
}
