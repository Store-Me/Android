package com.store_me.storeme.ui.signup.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.EditableProfileImage
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreSignupDataViewModel

@Composable
fun StoreProfileImageSection(onFinish: () -> Unit) {
    val storeDataViewModel = LocalStoreSignupDataViewModel.current

    val storeProfileImage by storeDataViewModel.storeProfileImage.collectAsState()
    val storeProfileImageUrl by storeDataViewModel.storeProfileImageUrl.collectAsState()
    val progress by storeDataViewModel.storeProfileImageProgress.collectAsState()


    LaunchedEffect(storeProfileImage) {
        if(storeProfileImage != null) {
            storeDataViewModel.uploadStoreProfileImage(storeName = storeDataViewModel.storeName.value)
        }
    }
    
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

        EditableProfileImage(
            accountType = AccountType.OWNER,
            uri = storeProfileImage,
            isLoading = storeProfileImage != null && storeProfileImageUrl == null,
            progress = progress,
            onDelete = { storeDataViewModel.updateStoreProfileImage(null) },
            onCropResult = { storeDataViewModel.updateStoreProfileImage(it) }
        )

        Spacer(modifier = Modifier.height(48.dp))

        DefaultButton(buttonText = "다음") {
            onFinish()
        }
    }
}