package com.store_me.storeme.ui.store_setting.image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.StoreHomeItem
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.home.owner.StoreDataViewModel
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun ImageSettingScreen(
    navController: NavController,
    imageSettingViewModel: ImageSettingViewModel = hiltViewModel()
) {
    val snackbarHostState = LocalSnackbarHostState.current
    val storeDataViewModel = LocalStoreDataViewModel.current

    val storeData by storeDataViewModel.storeInfoData.collectAsState()

    val errorMessage by imageSettingViewModel.errorMessage.collectAsState()
    val selectedImages by imageSettingViewModel.selectedImages.collectAsState()

    val multipleImagesPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        imageSettingViewModel.updateSelectedImages(uris)
    }

    LaunchedEffect(errorMessage) {
        if(errorMessage != null) {
            snackbarHostState.showSnackbar(errorMessage.toString())

            imageSettingViewModel.updateErrorMessage(null)
        }
    }

    LaunchedEffect(selectedImages) {
        if(selectedImages.isEmpty()) {

        } else {
            imageSettingViewModel.addStoreImage()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TitleWithDeleteButton(
                title = StoreHomeItem.IMAGE.displayName
            ) {
                navController.popBackStack()
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                DefaultButton(
                    buttonText = "사진 추가",
                    leftIconTint = Color.White,
                    leftIconResource = R.drawable.ic_circle_plus
                ) { multipleImagesPickerLauncher.launch("image/*") }

                if(/*storeData?.storeImageInfoList?.isEmpty() == */true) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "대표 사진을 설정하여 내 가게 페이지를 꾸며보세요.",
                            style = storeMeTextStyle(FontWeight.Bold, 2),
                            color = UndefinedTextColor
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        storeData?.let {
                            /*itemsIndexed(it.storeImageInfoList) { item, index ->

                            }*/
                        }
                    }
                }
            }
        }
    )
}