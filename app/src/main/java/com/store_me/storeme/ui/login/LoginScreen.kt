@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.LoginType
import com.store_me.storeme.data.response.CustomerInfoResponse
import com.store_me.storeme.data.response.MyStore
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.PwOutlinedTextField
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.onboarding.OnboardingActivity
import com.store_me.storeme.ui.theme.AddPostCouponIconColor
import com.store_me.storeme.ui.theme.CouponDueDateIconColor
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.KakaoLoginButtonColor
import com.store_me.storeme.ui.theme.LoginDividerColor
import com.store_me.storeme.ui.theme.PopularBoxColor
import com.store_me.storeme.ui.theme.RecommendBoxColor
import com.store_me.storeme.ui.theme.StoreMeLoginButtonColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.KakaoLoginHelper
import com.store_me.storeme.utils.ValidationUtils
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.preference.TokenPreferencesHelper
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val snackbarHostState = LocalSnackbarHostState.current

    val loadingViewModel = LocalLoadingViewModel.current

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val isKakaoLoginFailed by loginViewModel.isKakaoLoginFailed.collectAsState()

    val accountId by loginViewModel.accountId.collectAsState()
    val accountPw by loginViewModel.accountPw.collectAsState()

    val isIdError = remember { mutableStateOf(false) }
    val isPwError = remember { mutableStateOf(false) }

    val myStores by loginViewModel.myStores.collectAsState()
    val customerInfo by loginViewModel.customerInfo.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(isKakaoLoginFailed) {
        if(isKakaoLoginFailed){
           loginViewModel.clearKakaoLoginFailedState()
            navController.navigate(OnboardingActivity.Screen.Signup.route.name + "/${LoginType.KAKAO}?additionalData=${loginViewModel.kakaoId.value}")
        }
    }

    LaunchedEffect(accountId) {
        if(isIdError.value) {
            isIdError.value = !ValidationUtils.isValidId(accountId)
        }
    }

    LaunchedEffect(accountPw) {
        if(isPwError.value) {
            isPwError.value = !ValidationUtils.isValidPw(accountPw)
        }
    }

    LaunchedEffect(myStores, customerInfo) {
        if(myStores != null || customerInfo != null) {
            loadingViewModel.hideLoading()
            showBottomSheet = true
        }
    }

    LaunchedEffect(Unit) {
        if(TokenPreferencesHelper.getAccessToken() != null && TokenPreferencesHelper.getRefreshToken() != null) {
            loginViewModel.onLoginSuccess()
        }
    }

    Scaffold(

        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .imePadding(),
        containerColor = Color.White,
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.logo_color),
                        contentDescription = "로고",
                        modifier = Modifier
                            .size(120.dp)
                    )
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.logo_home),
                        contentDescription = "로고 텍스트",
                        modifier = Modifier
                            .width(120.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "내 주변 나만의 가게들",
                        style = storeMeTextStyle(FontWeight.Normal, 6)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(48.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "카카오 계정으로 시작하기",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = KakaoLoginButtonColor,
                            contentColor = Black
                        ),
                        diffValue = 1,
                        fontWeight = FontWeight.Bold,
                        leftIconResource = R.drawable.ic_kakao,
                        leftIconTint = Black
                    ) {
                        loadingViewModel.showLoading()

                        coroutineScope.launch {
                            when (val kakaoId = KakaoLoginHelper.getKakaoId(context)) {
                                null -> {
                                    loadingViewModel.hideLoading()
                                }
                                else -> {
                                    loginViewModel.loginWithKakao(kakaoId)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "회원가입 하기",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StoreMeLoginButtonColor,
                            contentColor = Black
                        ),
                        diffValue = 1,
                        fontWeight = FontWeight.Bold
                    ) {
                        navController.navigate(OnboardingActivity.Screen.Signup.route.name + "/${LoginType.APP}")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    LoginHorizontalDivider()
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    AccountIdSection(
                        value = accountId,
                        onValueChange = { loginViewModel.updateAccountId(it) },
                        onClearText = { loginViewModel.updateAccountId("") },
                        isError = isIdError.value
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                item {
                    AccountPwSection(
                        value = accountPw,
                        onValueChange = { loginViewModel.updateAccountPw(it) },
                        isError = isPwError.value
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    DefaultButton(
                        buttonText = "로그인"
                    ) {
                        if(accountId.isEmpty() || accountPw.isEmpty()){
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("아이디와 비밀번호를 모두 입력해 주세요.")
                                return@launch
                            }

                            return@DefaultButton
                        }

                        when {
                            !ValidationUtils.isValidId(accountId) && !ValidationUtils.isValidPw(accountPw) -> {
                                isIdError.value = true
                                isPwError.value = true
                            }
                            !ValidationUtils.isValidId(accountId) -> {
                                isIdError.value = true
                            }
                            !ValidationUtils.isValidPw(accountPw) -> {
                                isPwError.value = true
                            }
                            else -> {
                                loadingViewModel.showLoading()
                                loginViewModel.loginWithApp()
                            }
                        }
                    }
                }

            }
        }
    )

    if(showBottomSheet) {
        fun dismissBottomSheet() {
            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                if(!sheetState.isVisible) {
                    showBottomSheet = false
                }
            }
        }

        DefaultBottomSheet(
            sheetState = sheetState,
            onDismiss = { dismissBottomSheet() },
        ) {
            SelectProfile(
                customerInfo = customerInfo,
                myStores = myStores,
                onAddCustomerProfile = {
                    //TODO
                    dismissBottomSheet()
                },
                onAddStore = {
                    //TODO
                    dismissBottomSheet()
                },
                onSelectCustomerProfile = {
                    loadingViewModel.showLoading()
                    loginViewModel.loginAsCustomer()
                    dismissBottomSheet()
                },
                onSelectMyStore = {
                    loadingViewModel.showLoading()
                    loginViewModel.loginAsOwner(it)
                    dismissBottomSheet()
                }
            )
        }
    }

}

@Composable
fun SelectProfile(
    customerInfo: CustomerInfoResponse?,
    myStores: List<MyStore>?,
    onAddCustomerProfile: () -> Unit,
    onAddStore: () -> Unit,
    onSelectCustomerProfile: () -> Unit,
    onSelectMyStore: (MyStore) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "로그인할 프로필을 선택하세요.",
            style = storeMeTextStyle(FontWeight.ExtraBold, 6),
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                if(customerInfo == null) {
                    AddItem(isOwner = false) {
                        onAddCustomerProfile()
                    }
                } else {
                    ProfileItem(
                        profileImage = customerInfo.profileImageUrl,
                        name = customerInfo.nickname,
                        isOwner = false,
                    ) {
                        onSelectCustomerProfile()
                    }
                }
            }

            if(myStores == null) {
                item {
                    AddItem(isOwner = true) {
                        onAddStore()
                    }
                }
            } else {
                items(myStores) {
                    ProfileItem(
                        profileImage = it.storeProfileImage,
                        name = it.storeName,
                        isOwner = true
                    ) {
                        onSelectMyStore(it)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AddItem(
    isOwner: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(
                onClick = { onClick() },
                interactionSource = null,
                indication = null
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                model = null,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .clip(CircleShape),
                error = painterResource(if(isOwner) R.drawable.store_null_image else R.drawable.profile_null_image)
            )
        }

        if(isOwner) {
            Text(
                text = "스토어",
                style = storeMeTextStyle(FontWeight.Bold, 0),
                color = AddPostCouponIconColor,
                modifier = Modifier
                    .background(color = PopularBoxColor, shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        } else {
            Text(
                text = "손님",
                style = storeMeTextStyle(FontWeight.Bold, 0),
                color = CouponDueDateIconColor,
                modifier = Modifier
                    .background(color = RecommendBoxColor, shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Text(
            text = if(isOwner) "스토어 추가" else "프로필 생성",
            style = storeMeTextStyle(FontWeight.Bold, 4),
            color = GuideColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun ProfileItem(
    profileImage: String?,
    name: String,
    isOwner: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(
                onClick = { onClick() },
                interactionSource = null,
                indication = null
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .clip(CircleShape),
                error = painterResource(if(isOwner) R.drawable.store_null_image else R.drawable.profile_null_image)
            )
        }

        if(isOwner) {
            Text(
                text = "스토어",
                style = storeMeTextStyle(FontWeight.Bold, 0),
                color = AddPostCouponIconColor,
                modifier = Modifier
                    .background(color = PopularBoxColor, shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        } else {
            Text(
                text = "손님",
                style = storeMeTextStyle(FontWeight.Bold, 0),
                color = CouponDueDateIconColor,
                modifier = Modifier
                    .background(color = RecommendBoxColor, shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Text(
            text = name,
            style = storeMeTextStyle(FontWeight.Bold, 4),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun LoginHorizontalDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 0.5.dp,
            color = LoginDividerColor
        )

        Text(
            text = "또는",
            style = storeMeTextStyle(FontWeight.Bold, 1),
            color = LoginDividerColor
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 0.5.dp,
            color = LoginDividerColor
        )
    }
}

@Composable
fun AccountIdSection(
    value: String,
    onValueChange: (String) -> Unit,
    onClearText: () -> Unit,
    isError: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "아이디",
            style = storeMeTextStyle(FontWeight.Bold, 1),
            color = Black
        )

        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            textStyle = storeMeTextStyle(FontWeight.Normal, 1),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = {
                if(value.isNotEmpty()){
                    IconButton(onClick = { onClearText() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_text_clear),
                            contentDescription = "삭제",
                            modifier = Modifier
                                .size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = "아이디를 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 1),
                    color = UndefinedTextColor
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HighlightTextFieldColor,
                errorBorderColor = ErrorTextFieldColor,
                errorLabelColor = ErrorTextFieldColor,
            ),
            isError = isError,
            supportingText = {
                if(isError){
                    Text(
                        text = "4 ~ 20 글자 영어 혹은 숫자로 구성되어야 합니다.",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = ErrorTextFieldColor
                    )
                }
            }
        )
    }
}

@Composable
fun AccountPwSection(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    val isHidePw = remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "비밀번호",
            style = storeMeTextStyle(FontWeight.Bold, 1)
        )

        PwOutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            isHidePw = isHidePw.value,
            onHideValueChange = { isHidePw.value = !isHidePw.value },
            isError = isError,
            imeAction = ImeAction.Done
        )
    }
}