@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalPermissionsApi::class)

package com.store_me.storeme.ui.signup.owner

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.store_me.storeme.R
import com.store_me.storeme.ui.location.HighlightedText
import com.store_me.storeme.ui.location.LocationViewModel
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.TextClearIconColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalStoreSignupDataViewModel

@Composable
fun SearchStoreLocationSection(
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    locationViewModel: LocationViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    val storeDataViewModel = LocalStoreSignupDataViewModel.current

    val storeLocationAddress by storeDataViewModel.storeLocationAddress.collectAsState()

    val reverseGeoCodeCompleted by locationViewModel.reverseGeoCodeCompleted.collectAsState()

    BackHandler {
        onClick()
    }
    
    LaunchedEffect(reverseGeoCodeCompleted) {
        if(reverseGeoCodeCompleted) {
            storeDataViewModel.updateStoreLocationAddress(locationViewModel.storeLocationAddress.value)
            
            storeDataViewModel.updateStoreLocation(locationViewModel.storeLocation.value)

            storeDataViewModel.updateStoreLocationCode(locationViewModel.storeLocationCode.value ?: 1111000000)

            storeDataViewModel.updateStoreLatLng(locationViewModel.storeLatLng.value)

            locationViewModel.setReverseGeocodeCompletedValue(false)

            onClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        with(sharedTransitionScope) {
            OutlinedTextField(
                value = storeLocationAddress,
                onValueChange = {
                    storeDataViewModel.updateStoreLocationAddress(it)
                    if(it.length >= 2)
                        locationViewModel.getSearchResults(query = it)
                },
                textStyle = storeMeTextStyle(FontWeight.Normal, 1),
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState(key = "store_location_search"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "검색",
                        modifier = Modifier
                            .size(24.dp),
                        tint = TextClearIconColor
                    )
                },
                placeholder = {
                    Text(
                        text = "지역을 검색해 주세요.",
                        style = storeMeTextStyle(FontWeight.Normal, 1),
                        color = UndefinedTextColor
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HighlightTextFieldColor,
                    errorBorderColor = ErrorTextFieldColor,
                    errorLabelColor = ErrorTextFieldColor,
                    disabledBorderColor = Color.Black,
                    disabledTextColor = if(storeLocationAddress.isNotEmpty()) Color.Black else Color.Unspecified
                ),
                supportingText = {  }
            )

            SetLocationButton(locationViewModel)

            SearchResultListSection(locationViewModel) {
                onClick()
            }
        }
    }
}

@Composable
fun SearchResultListSection(locationViewModel: LocationViewModel, onClick: () -> Unit) {
    val storeDataViewModel = LocalStoreSignupDataViewModel.current

    val searchQuery by locationViewModel.searchQuery.collectAsState()
    val searchResults by locationViewModel.searchResults.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(searchResults) {
            val displayName = "${it.first} ${it.second} ${it.third}"
            HighlightedText(text = displayName, query = searchQuery) {
                storeDataViewModel.updateStoreLocation(it.third.toString())
                storeDataViewModel.updateStoreLocationCode(it.code)
                storeDataViewModel.updateStoreLatLng(null)
                storeDataViewModel.updateStoreLocationAddress(displayName)

                onClick()
            }
        }
    }
}

@Composable
fun SetLocationButton(locationViewModel: LocationViewModel) {
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            locationViewModel.setLocationOwner()
        }
    }

    Button(
        onClick = {
            if(locationPermissionState.permissions.any { it.status.isGranted }) {
                locationViewModel.setLocationOwner()
            } else {
                launcher.launch( arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Black
        )
    ) {
        Text(
            text = "현재 위치로 설정",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}