package com.store_me.storeme.ui.signup.owner

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.component.CircleBorderWithIcon
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.signup.NextButton
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.CropUtils
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreDataViewModel
import com.yalantis.ucrop.UCrop

@Composable
fun StoreProfileImageSection(onFinish: () -> Unit) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val storeProfileImage by storeDataViewModel.storeProfileImage.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        SignupTitleText(title = "내 스토어를 나타낼 수 있는\n프로필 사진을 등록해주세요.")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "등록한 사진은 나중에도 자유롭게 변경할 수 있어요.",
            style = storeMeTextStyle(FontWeight.Normal, 2)
        )

        Spacer(modifier = Modifier.height(36.dp))

        ProfileImageSection(
            accountType = Auth.AccountType.OWNER,
            uri = storeProfileImage,
            onDelete = { storeDataViewModel.updateStoreProfileImage(null) },
            onCropResult = { storeDataViewModel.updateStoreProfileImage(it) }
        )

        Spacer(modifier = Modifier.height(48.dp))

        NextButton(buttonText = "다음") {
            onFinish()
        }
    }
}

@Composable
fun ProfileImageSection(
    accountType: Auth.AccountType,
    uri: Uri?,
    onDelete: () -> Unit,
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
            val cropIntent = CropUtils.getCropIntent(context = context, sourceUri = sourceUri)
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