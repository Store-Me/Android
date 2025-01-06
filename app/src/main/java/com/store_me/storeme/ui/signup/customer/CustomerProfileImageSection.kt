package com.store_me.storeme.ui.signup.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.signup.NextButton
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.signup.owner.ProfileImageSection
import com.store_me.storeme.utils.composition_locals.signup.LocalCustomerDataViewModel

@Composable
fun CustomerProfileImageSection(onFinish: () -> Unit) {
    val customerDataViewModel = LocalCustomerDataViewModel.current
    val profileImage by customerDataViewModel.profileImage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        SignupTitleText(title = "스토어미에서 사용할\n프로필 사진을 등록해 주세요.")

        Spacer(modifier = Modifier.height(36.dp))

        ProfileImageSection(
            accountType = Auth.AccountType.CUSTOMER,
            uri = profileImage,
            onDelete = { customerDataViewModel.updateProfileImage(null) },
            onCropResult = { customerDataViewModel.updateProfileImage(it) }
        )

        Spacer(modifier = Modifier.height(48.dp))

        NextButton(buttonText = "다음") {
            onFinish()
        }
    }
}