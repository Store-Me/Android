package com.store_me.storeme.ui.signup.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.signup.owner.ProfileImageSection
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalAccountDataViewModel
import com.store_me.storeme.utils.composition_locals.signup.LocalCustomerDataViewModel

@Composable
fun CustomerProfileImageSection(onFinish: () -> Unit) {
    val accountDataViewModel = LocalAccountDataViewModel.current
    val customerDataViewModel = LocalCustomerDataViewModel.current
    val loadingViewModel = LocalLoadingViewModel.current
    val snackbarHostState = LocalSnackbarHostState.current

    val profileImage by customerDataViewModel.profileImage.collectAsState()
    val profileImageUrl by customerDataViewModel.profileImageUrl.collectAsState()
    val progress by customerDataViewModel.progress.collectAsState()

    val errorMessage by customerDataViewModel.errorMessage.collectAsState()

    LaunchedEffect(profileImage) {
        if(profileImage != null) {
            customerDataViewModel.uploadImage(accountId = accountDataViewModel.accountId.value)
        }
    }

    LaunchedEffect(errorMessage) {
        if(errorMessage != null) {
            loadingViewModel.hideLoading()

            customerDataViewModel.updateProfileImage(null)

            snackbarHostState.showSnackbar(errorMessage.toString())

            customerDataViewModel.updateErrorMessage(null)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        SignupTitleText(title = "스토어미에서 사용할\n프로필 사진을 등록해 주세요.")

        Spacer(modifier = Modifier.height(36.dp))

        ProfileImageSection(
            accountType = AccountType.CUSTOMER,
            uri = profileImage,
            isLoading = profileImage != null && profileImageUrl == null,
            progress = progress,
            onDelete = { customerDataViewModel.updateProfileImage(null) },
            onCropResult = { customerDataViewModel.updateProfileImage(it) }
        )

        Spacer(modifier = Modifier.height(48.dp))

        DefaultButton(
            buttonText = "다음",
            enabled = (profileImage == null && profileImageUrl == null) || (profileImage != null && profileImageUrl != null)
        ) {
            onFinish()
        }
    }
}