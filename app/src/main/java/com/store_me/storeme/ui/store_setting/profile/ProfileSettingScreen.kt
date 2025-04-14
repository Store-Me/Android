package com.store_me.storeme.ui.store_setting.profile

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.ui.component.AlphaBackgroundText
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.CircleBorderWithIcon
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.LoadingProgress
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.home.owner.BackgroundSection
import com.store_me.storeme.ui.theme.DividerColor
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.FinishedColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.CropUtils
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.yalantis.ucrop.UCrop


@Composable
fun ProfileSettingScreen(
    navController: NavController,
    profileSettingViewModel: ProfileSettingViewModel = hiltViewModel()
){
    val focusManager = LocalFocusManager.current

    val storeDataViewModel = LocalStoreDataViewModel.current
    val loadingViewModel = LocalLoadingViewModel.current
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val profileImageUri by profileSettingViewModel.profileImageUri.collectAsState()
    val profileImageUrl by profileSettingViewModel.profileImageUrl.collectAsState()
    val profileImageProgress by profileSettingViewModel.profileImageProgress.collectAsState()

    val backgroundImageUri by profileSettingViewModel.backgroundImageUri.collectAsState()
    val backgroundImageUrl by profileSettingViewModel.backgroundImageUrl.collectAsState()
    val backgroundImageProgress by profileSettingViewModel.backgroundImageProgress.collectAsState()

    val showDialog = remember { mutableStateOf(false) }

    val hasDifference by remember(storeInfoData, profileImageUrl, backgroundImageUrl) {
        derivedStateOf {
            backgroundImageUrl != storeInfoData?.backgroundImage || profileImageUrl != storeInfoData?.storeProfileImage
        }
    }

    fun onClose() {
        if(hasDifference)
            showDialog.value = true
        else
            navController.popBackStack()
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(storeInfoData) {
        profileSettingViewModel.updateProfileImageUrl(storeInfoData?.storeProfileImage)
        profileSettingViewModel.updateBackgroundImageUrl(storeInfoData?.backgroundImage)
    }

    LaunchedEffect(profileImageUri) {
        if(profileImageUri != null) {
            storeInfoData?.storeName?.let { profileSettingViewModel.uploadStoreProfileImage() }
        }
    }

    LaunchedEffect(backgroundImageUri) {
        if(backgroundImageUri != null) {
            storeInfoData?.storeName?.let { profileSettingViewModel.uploadStoreBackgroundImage() }
        }
    }

    LaunchedEffect(profileImageProgress) {
        if(profileImageProgress == 100.0f) {
            profileSettingViewModel.clearProfileProgress()
        }
    }

    LaunchedEffect(backgroundImageProgress) {
        if(backgroundImageProgress == 100.0f) {
            profileSettingViewModel.clearBackgroundProgress()
        }
    }

    Scaffold (
        containerColor = White,
        modifier = Modifier
            .addFocusCleaner(focusManager)
            .fillMaxSize(),
        topBar = { TitleWithDeleteButton(
            title = "프로필 수정"
        ) {
            onClose()
        } },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                item {
                    BackgroundImageSettingSection(
                        url = backgroundImageUrl,
                        uri = backgroundImageUri,
                        isLoading = backgroundImageUri != null,
                        progress = backgroundImageProgress,
                        onDelete = { profileSettingViewModel.updateBackgroundImageUri(null) },
                        onCropResult = { profileSettingViewModel.updateBackgroundImageUri(it) }
                    )
                }

                item { ProfileImageSettingSection(
                    accountType = AccountType.OWNER,
                    url = profileImageUrl,
                    uri = profileImageUri,
                    isLoading = profileImageUri != null,
                    progress = profileImageProgress,
                    onDelete = { profileSettingViewModel.updateProfileImageUri(null) },
                    onCropResult = { profileSettingViewModel.updateProfileImageUri(it) }
                ) }

                item { storeInfoData?.storeName?.let { StoreNameSettingSection(storeName = it) } }

                item {
                    Spacer(modifier = Modifier.width(40.dp))
                }

                item { DefaultButton(
                    buttonText = "저장",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    enabled = hasDifference
                ) {
                    loadingViewModel.showLoading()

                    storeDataViewModel.patchStoreProfileImages(
                        profileImage = profileImageUrl,
                        backgroundImage = backgroundImageUrl
                    )
                } }
            }
        }
    )

    if (showDialog.value) {
        BackWarningDialog(
            onDismiss = {
                showDialog.value = false
            },
            onAction = {
                showDialog.value = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun BackgroundImageSettingSection(
    url: String?,
    uri: Uri?,
    onDelete: () -> Unit,
    isLoading: Boolean = false,
    progress: Float = 0.0f,
    onCropResult: (Uri) -> Unit,
) {
    val context = LocalContext.current

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let { uri ->
                onCropResult(uri)
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { sourceUri ->
            val cropIntent = CropUtils.getCropIntent(
                context = context,
                sourceUri = sourceUri,
                aspectRatio = Pair(2f, 1f)
            )
            cropLauncher.launch(cropIntent)
        }
    }

    Box(
        modifier = Modifier
            .clickable(
                onClick = {
                    galleryLauncher.launch("image/*")
                }
            )
            .fillMaxWidth()
            .aspectRatio(2f / 1f),
        contentAlignment = Alignment.Center
    ) {
        if(isLoading)
            BackgroundSection(imageUri = uri, showCanvas = false)
        else
            BackgroundSection(imageUrl = url, showCanvas = true)

        if(isLoading) {
            LoadingProgress(
                progress = progress,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        if(url != null) {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp)
                    .align(Alignment.TopEnd)
            ) {
                CircleBorderWithIcon(
                    modifier = Modifier,
                    borderColor = White,
                    circleColor = ErrorTextFieldColor,
                    iconResource = R.drawable.ic_delete,
                    iconColor = White,
                    size = 26
                ) {
                    onDelete()
                }
            }
        }

        AlphaBackgroundText(
            text = "배경 편집",
            diffValue = -1,
            modifier = Modifier
                .padding(end = 10.dp, bottom = 10.dp)
                .align(Alignment.BottomEnd),
            iconResource = R.drawable.ic_camera)
    }
}

@Composable
fun StoreNameSettingSection(storeName: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "스토어 이름",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            color = Color.Black
        )

        OutlinedTextField(
            value = storeName,
            onValueChange = {  },
            textStyle = storeMeTextStyle(FontWeight.Normal, 1),
            modifier = Modifier
                .fillMaxWidth(),
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = FinishedColor,
                disabledContainerColor = DividerColor,
                disabledTextColor = Black
            )
        )
    }
}

@Composable
fun ProfileImageSettingSection(
    accountType: AccountType,
    url: String?,
    uri: Uri?,
    onDelete: () -> Unit,
    isLoading: Boolean = false,
    progress: Float = 0.0f,
    onCropResult: (Uri) -> Unit
) {
    val context = LocalContext.current

    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let { uri ->
                onCropResult(uri)
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
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(110.dp)
        ) {
            if(isLoading) {
                ProfileImage(
                    accountType = accountType,
                    uri = uri,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(18.dp))
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }
                )
            } else {
                ProfileImage(
                    accountType = accountType,
                    url = url,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(18.dp))
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }
                )
            }


            if(isLoading) {
                LoadingProgress(
                    progress = progress,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(18.dp))
                )
            }


            if(url != null) {
                CircleBorderWithIcon(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    borderColor = White,
                    circleColor = ErrorTextFieldColor,
                    iconResource = R.drawable.ic_delete,
                    iconColor = White,
                    size = 26
                ) {
                    onDelete()
                }
            }
        }
    }
}