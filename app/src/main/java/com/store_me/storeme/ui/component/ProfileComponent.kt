package com.store_me.storeme.ui.component

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.utils.CropUtils
import com.yalantis.ucrop.UCrop

/**
 * 프로필 이미지 Composable
 * @param accountType 계정 타입
 * @param uri 이미지 Uri
 * @param modifier Modifier
 */
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    accountType: AccountType,
    uri: Uri?
) {
    val errorImage = when(accountType) {
        AccountType.CUSTOMER -> {
            R.drawable.profile_null_image
        }

        AccountType.OWNER -> {
            R.drawable.store_null_image
        }
    }

    AsyncImage(
        model = uri,
        contentDescription = "프로필 이미지",
        error = painterResource(id = errorImage),
        modifier = modifier
    )
}

/**
 * 프로필 이미지 Composable
 * @param accountType 계정 타입
 * @param url 이미지 Url
 * @param modifier Modifier
 */
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    accountType: AccountType,
    url: String?
) {
    val errorImage = when(accountType) {
        AccountType.CUSTOMER -> {
            R.drawable.profile_null_image
        }

        AccountType.OWNER -> {
            R.drawable.store_null_image
        }
    }

    AsyncImage(
        modifier = modifier,
        model = url,
        contentDescription = "프로필 이미지",
        error = painterResource(id = errorImage)
    )
}


@Composable
fun ProfileImageWithBorder(
    modifier: Modifier = Modifier,
    accountType: AccountType,
    url: String?,
    size: Dp = 100.dp,
    strokeWidth: Dp = 4.dp,
    borderColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .border(width = strokeWidth, color = borderColor, shape = RoundedCornerShape(18.dp))
            .clip(shape = RoundedCornerShape(18.dp))
    ) {
        ProfileImage(
            accountType = accountType,
            url = url,
            modifier = Modifier
                .fillMaxSize()
                .padding(strokeWidth)

        )
    }
}

@Composable
fun EditableProfileImage(
    accountType: AccountType,
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
            ProfileImage(
                accountType = accountType,
                uri = uri,
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = RoundedCornerShape(18.dp))
            )

            if(isLoading) {
                LoadingProgress(
                    progress = progress,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(18.dp))
                )
            }


            if(uri != null) {
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

            if(uri == null) {
                CircleBorderWithIcon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    borderColor = White,
                    circleColor = Black,
                    iconResource = R.drawable.ic_camera,
                    iconColor = White,
                    size = 36
                ) {
                    galleryLauncher.launch("image/*")
                }
            }

        }
    }
}