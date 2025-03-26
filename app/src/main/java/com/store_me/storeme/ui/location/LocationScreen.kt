package com.store_me.storeme.ui.location

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.SearchField
import com.store_me.storeme.ui.component.SmallButton
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.HighlightTextColor
import com.store_me.storeme.ui.theme.SetLocationButtonColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.ToastMessageUtils

@Composable
fun LocationScreen(navController: NavController, locationViewModel: LocationViewModel) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        locationViewModel.setReverseGeocodeCompletedValue(false)
    }
    val reverseGeoCodeCompleted by locationViewModel.reverseGeoCodeCompleted.collectAsState()

    LaunchedEffect(reverseGeoCodeCompleted) {
        if (reverseGeoCodeCompleted) {
            if (navController.currentBackStackEntry?.destination?.id != navController.graph.startDestinationId) {
                navController.popBackStack()
            }
        }
    }

    val observeText by locationViewModel.locationText.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = { TitleWithDeleteButton(title = "동네 설정") {
            navController.popBackStack()
        } },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                SearchField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    observeText = observeText,
                    hint = "주변 가게를 볼 동네를 설정하세요."
                ) { searchText ->
                    //2글자 미만일 경우 제외
                    if(searchText.length < 2) {
                        ToastMessageUtils.showToast(context, R.string.fail_seach_short)
                        return@SearchField
                    }

                    locationViewModel.getSearchResults(searchText)
                }

                Spacer(modifier = Modifier.height(10.dp))

                SetLocationButton(locationViewModel)

                Spacer(modifier = Modifier.height(20.dp))

                SearchListSection(navController, locationViewModel)
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SetLocationButton(locationViewModel: LocationViewModel){
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            locationViewModel.setLocation()
        }
    }

    SmallButton(
        text = "내 위치로 동네 설정",
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 20.dp),
        containerColor = SetLocationButtonColor,
        contentColor = Color.White
    ) {
        if (locationPermissionState.permissions.any { it.status.isGranted }) {
            locationViewModel.setLocation()
        } else {
            launcher.launch(arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ))
        }
    }
}

@Composable
fun SearchListSection(navController: NavController, locationViewModel: LocationViewModel){
    val searchQuery by locationViewModel.searchQuery.collectAsState()
    val searchResults by locationViewModel.searchResults.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(searchResults) {
            val displayName = "${it.first} ${it.second} ${it.third}"
            HighlightedText(text = displayName, query = searchQuery) {
                locationViewModel.saveLocation(it.third.toString(), it.code)
                if (navController.currentBackStackEntry?.destination?.id != navController.graph.startDestinationId) {
                    navController.popBackStack()
                }
            }

        }
    }
}

@Composable
fun HighlightedText(text: String, query: String, onClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        val lowerCaseText = text.lowercase()
        val lowerCaseQuery = query.lowercase()
        var startIndex = 0

        while (startIndex < lowerCaseText.length) {
            val index = lowerCaseText.indexOf(lowerCaseQuery, startIndex)
            if (index == -1) {
                append(text.substring(startIndex))
                break
            }
            append(text.substring(startIndex, index))
            withStyle(style = SpanStyle(color = HighlightTextColor, fontFamily = appFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp)) {
                append(text.substring(index, index + lowerCaseQuery.length))
            }
            startIndex = index + lowerCaseQuery.length
        }
    }

    Text(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
        style = storeMeTypography.bodyMedium,
        fontSize = 14.sp
    )
}