package com.store_me.storeme.ui.signup.owner

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.CircleBorderWithIcon
import com.store_me.storeme.ui.signup.NextButton
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.StoreImageBoxColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreDataViewModel

@Composable
fun StoreImageSection(onFinish: () -> Unit) {
    val maxImages = 10

    val snackbarHostState = LocalSnackbarHostState.current

    val storeDataViewModel = LocalStoreDataViewModel.current

    val storeImages by storeDataViewModel.storeImages.collectAsState()

    val annotatedString = buildAnnotatedString {
        append("손님들이 사진을 보고 어떤 스토어인지 알 수 있도록\n")
        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) {
            append("내 스토어를 보여줄 수 있는 사진")
        }
        append("을 등록해 주세요.\n등록한 사진은 나중에도 자유롭게 추가 및 변경할 수 있어요.")
    }

    val isExceedingLimit = storeImages.size > maxImages

    val annotatedImagesCountString = buildAnnotatedString {
        append("사진 (")
        withStyle(style = SpanStyle(color = if (isExceedingLimit) ErrorTextFieldColor else Color.Black)) {
            append("${storeImages.size}")
        }
        append("/$maxImages)")
    }


    val multipleImagesPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) {uris: List<Uri> ->
        storeDataViewModel.addStoreImage(uris)
    }

    LaunchedEffect(isExceedingLimit) {
        if(isExceedingLimit) {
            snackbarHostState.showSnackbar("가입단계에서 스토어 이미지는 최대 10장까지 등록 가능합니다.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        SignupTitleText(title = "스토어를 보여줄 수 있는\n사진을 1장 이상 등록해주세요. (선택)")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = annotatedString,
            style = storeMeTextStyle(FontWeight.Normal, 2)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = annotatedImagesCountString,
                style = storeMeTextStyle(FontWeight.Normal, 2),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
        ) {
            item {
                AddStoreImageBox {
                    multipleImagesPickerLauncher.launch("image/*")
                }
            }

            items(storeImages) {
                StoreImageBox(uri = it) {
                    storeDataViewModel.removeStoreImage(it)
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        NextButton(
            buttonText = "다음",
            enabled = !isExceedingLimit
        ) {
            onFinish()
        }
    }
}

@Composable
fun AddStoreImageBox(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .border(
                border = BorderStroke(width = 1.dp, color = StoreImageBoxColor),
                shape = RoundedCornerShape(14.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(14.dp))
            .clip(shape = RoundedCornerShape(14.dp))
            .clickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_plus),
            contentDescription = "스토어 이미지 추가",
            modifier = Modifier
                .size(24.dp),
            tint = StoreImageBoxColor
        )
    }
}

@Composable
fun StoreImageBox(uri: Uri, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(shape = RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "스토어 이미지",
            error = painterResource(id = R.drawable.store_null_image),
            modifier = Modifier
                .size(92.dp)
                .clip(shape = RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        CircleBorderWithIcon(
            modifier = Modifier
                .align(Alignment.TopEnd),
            borderColor = Color.White,
            circleColor = ErrorTextFieldColor,
            iconResource = R.drawable.ic_delete,
            iconColor = Color.White,
            size = 24
        ) {
            onDelete()
        }
    }
}