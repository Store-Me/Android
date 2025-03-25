@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.store_me.storeme.ui.store_setting.location

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.naver.maps.geometry.LatLng
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.signup.owner.AddressWebView
import com.store_me.storeme.ui.signup.owner.LocationSection
import com.store_me.storeme.ui.signup.owner.LocationTitle
import com.store_me.storeme.ui.signup.owner.NoAddressSection
import com.store_me.storeme.ui.signup.owner.SearchStoreLocationSection
import com.store_me.storeme.ui.signup.owner.StoreSignupDataViewModel
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun LocationSettingScreen(
    navController: NavController,
    locationSettingViewModel: LocationSettingViewModel = hiltViewModel(),
    storeLocationDataViewModel: StoreSignupDataViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val storeInfoDataViewModel = LocalStoreDataViewModel.current
    val auth = LocalAuth.current

    val storeInfoData by storeInfoDataViewModel.storeInfoData.collectAsState()

    val storeLocationAddress by storeLocationDataViewModel.storeLocationAddress.collectAsState()
    val storeLocationDetail by storeLocationDataViewModel.storeLocationDetail.collectAsState()
    val storeLocationCode by storeLocationDataViewModel.storeLocationCode.collectAsState()
    val storeLocation by storeLocationDataViewModel.storeLocation.collectAsState()
    val storeLatLng by storeLocationDataViewModel.storeLatLng.collectAsState()

    val hasAddress by storeLocationDataViewModel.hasAddress.collectAsState()

    val hasSameValue by locationSettingViewModel.hasSameValue.collectAsState()

    val showWebView = remember { mutableStateOf(false) }

    val isError = remember { mutableStateOf(false) }

    val onSearch = remember { mutableStateOf(false) }

    val hasDifference by remember {
        derivedStateOf {
            storeInfoData?.storeLocation != storeLocation
                    || storeInfoData?.storeLocationAddress != storeLocationAddress
                    || storeInfoData?.storeLocationDetail != storeLocationDetail
                    || storeInfoData?.storeLocationCode != storeLocationCode
                    || storeLatLng?.latitude != storeInfoData?.storeLat
                    || storeLatLng?.longitude != storeInfoData?.storeLng
        }
    }

    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(storeInfoData) {
        storeLocationDataViewModel.updateStoreLocation(storeInfoData?.storeLocation ?: "")
        storeLocationDataViewModel.updateStoreLocationAddress(storeInfoData?.storeLocationAddress ?: "")
        storeLocationDataViewModel.updateStoreLocationDetail(storeInfoData?.storeLocationDetail ?: "")
        storeLocationDataViewModel.updateStoreLocationCode(storeInfoData?.storeLocationCode ?: 1111000000)
        if(storeInfoData?.storeLat != null && storeInfoData?.storeLng != null) {
            storeLocationDataViewModel.updateStoreLatLng(LatLng(storeInfoData?.storeLat!!, storeInfoData?.storeLng!!))
        }

        locationSettingViewModel.hasExactMatch(storeLocationAddress)
    }

    LaunchedEffect(hasSameValue) {
        storeLocationDataViewModel.updateHasAddress(!hasSameValue)
    }

    fun onClose() {
        if(hasDifference)
            showDialog.value = true
        else
            navController.popBackStack()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { TitleWithDeleteButton(title = "위치 정보 수정") {
            onClose()
        } },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .addFocusCleaner(focusManager)
            ) {
                if(showWebView.value) {
                    AddressWebView(
                        onAddressSelected = { daumPostcodeResponse ->
                            storeLocationDataViewModel.updateDaumPostcodeResponse(daumPostcodeResponse)
                            showWebView.value = false
                        },
                        onCloseWebView = {
                            showWebView.value = false
                        }
                    )
                } else {
                    SharedTransitionLayout {
                        AnimatedContent(
                            targetState = onSearch.value,
                            label = "Location"
                        ) { targetState ->
                            if(targetState) {
                                //검색
                                SearchStoreLocationSection(
                                    animatedVisibilityScope = this@AnimatedContent,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    onFail = {
                                        onSearch.value = false
                                    },
                                    onSuccess = { locationAddress, location, locationCode, latLng ->
                                        storeLocationDataViewModel.updateStoreLocationAddress(locationAddress)
                                        storeLocationDataViewModel.updateStoreLocation(location)
                                        storeLocationDataViewModel.updateStoreLocationCode(locationCode)
                                        storeLocationDataViewModel.updateStoreLatLng(latLng)

                                        onSearch.value = false
                                    }
                                )
                            } else {
                                //일반
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                ) {
                                    item {
                                        LocationTitle()
                                    }

                                    item {
                                        Spacer(modifier = Modifier.height(36.dp))
                                    }

                                    item {
                                        LocationSection(
                                            hasAddress = hasAddress,
                                            isError = isError.value,
                                            storeLocationAddress = storeLocationAddress,
                                            storeLocationDetail = storeLocationDetail,
                                            onErrorChange = {
                                                isError.value = it
                                            },
                                            onShowWebViewClick = {
                                                showWebView.value = true
                                            },
                                            onStoreLocationDetailChange = {
                                                storeLocationDataViewModel.updateStoreLocationDetail(it)
                                            },
                                            onHasAddressClick = {
                                                storeLocationDataViewModel.updateStoreLocationAddress("")
                                                storeLocationDataViewModel.updateStoreLocationDetail("")
                                                storeLocationDataViewModel.updateHasAddress(!hasAddress)
                                            }
                                        )
                                    }

                                    item {
                                        AnimatedVisibility(!hasAddress) {
                                            NoAddressSection(
                                                sharedTransitionScope = this@SharedTransitionLayout,
                                                animatedVisibilityScope = this@AnimatedContent,
                                                hasAddress = hasAddress,
                                                storeLocationAddress = storeLocationAddress,
                                                onSearchClick = { onSearch.value = true }
                                            )
                                        }
                                    }

                                    item {
                                        Spacer(modifier = Modifier.height(48.dp))
                                    }

                                    item {
                                        DefaultButton(
                                            buttonText = "다음",
                                            enabled = storeLocationAddress.isNotEmpty() && storeLocationCode != null && hasDifference
                                        ) {
                                            storeInfoDataViewModel.patchStoreLocation(
                                                storeId = auth.storeId.value!!,
                                                storeLocationAddress = storeLocationAddress,
                                                storeLocationDetail = storeLocationDetail,
                                                storeLocationCode = storeLocationCode!!,
                                                storeLocation = storeLocation,
                                                storeLat = if(storeLatLng != null) storeLatLng!!.latitude else null,
                                                storeLng = if(storeLatLng != null) storeLatLng!!.longitude else null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

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
    )
}