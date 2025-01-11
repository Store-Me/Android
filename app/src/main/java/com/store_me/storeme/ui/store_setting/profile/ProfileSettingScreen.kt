package com.store_me.storeme.ui.store_setting.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.ui.component.AlphaBackgroundText
import com.store_me.storeme.ui.component.CircleBorderWithIcon
import com.store_me.storeme.ui.component.DefaultOutlineTextField
import com.store_me.storeme.ui.component.TextFieldErrorType
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.home.owner.BackgroundSection
import com.store_me.storeme.ui.theme.storeMeTextStyle


val LocalProfileSettingViewModel = staticCompositionLocalOf<ProfileSettingViewModel> {
    error("No ProfileSettingViewModel provided")
}


@Composable
fun ProfileSettingScreen(
    navController: NavController,
    profileSettingViewModel: ProfileSettingViewModel = viewModel()
){
    val focusManager = LocalFocusManager.current
    var isError by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalProfileSettingViewModel provides profileSettingViewModel) {
        Scaffold (
            containerColor = White,
            modifier = Modifier
                .addFocusCleaner(focusManager)
                .fillMaxSize(),
            topBar = { TitleWithDeleteButton(navController = navController, title = "프로필 수정") },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    item { BackgroundSettingSection {

                    } }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    
                    item { ProfileImageSettingSection {

                    } }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    item { StoreNameSettingSection{
                        isError = it
                    } }
                }
            }
        )
    }
}

@Composable
fun ProfileImageSettingSection(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    val profileSettingViewModel = LocalProfileSettingViewModel.current
    val profileImage by profileSettingViewModel.profileImage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(110.dp)
                .clickable(
                    onClick = { onClick() },
                    indication = ripple(bounded = false),
                    interactionSource = interactionSource
                )
        ) {
            ProfileImage(
                accountType = AccountType.OWNER,
                url = profileImage,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.TopCenter),
            )

            CircleBorderWithIcon(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                borderColor = White,
                circleColor = Black,
                iconResource = R.drawable.ic_camera,
                iconColor = White,
                size = 36
            )
        }
    }
}

@Composable
fun BackgroundSettingSection(onClick: () -> Unit) {
    val profileSettingViewModel = LocalProfileSettingViewModel.current
    val backgroundImage by profileSettingViewModel.backgroundImage.collectAsState()

    Box(
        modifier = Modifier
            .clickable(
                onClick = { onClick() }
            )
            .fillMaxWidth()
            .aspectRatio(2f / 1f),
        contentAlignment = Alignment.BottomEnd
    ) {
        BackgroundSection(backgroundImage)

        AlphaBackgroundText(
            text = "배경 편집",
            diffValue = -1,
            modifier = Modifier
                .padding(end = 10.dp, bottom = 10.dp),
            iconResource = R.drawable.ic_camera)
    }
}

@Composable
fun StoreNameSettingSection(onErrorChange: (Boolean) -> Unit) {
    val profileSettingViewModel = LocalProfileSettingViewModel.current
    val storeName by profileSettingViewModel.storeName.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "가게 이름",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2)
        )

        DefaultOutlineTextField(
            text = storeName,
            placeholderText = "가게 이름을 입력해 주세요.",
            onValueChange = { profileSettingViewModel.updateStoreName(it) },
            errorType = TextFieldErrorType.STORE_NAME,
            onErrorChange = { onErrorChange(it) }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextLengthRow(
            text = storeName, limitSize = 30
        )
    }
}