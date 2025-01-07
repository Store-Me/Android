package com.store_me.storeme.ui.component

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.AccountType

/**
 * 프로필 이미지 Composable
 * @param accountType 계정 타입
 * @param uri 이미지 Uri
 * @param modifier Modifier
 */
@Composable
fun ProfileImage(accountType: AccountType, uri: Uri?, modifier: Modifier = Modifier) {
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
fun ProfileImage(accountType: AccountType, url: String?, modifier: Modifier = Modifier) {
    val errorImage = when(accountType) {
        AccountType.CUSTOMER -> {
            R.drawable.profile_null_image
        }

        AccountType.OWNER -> {
            R.drawable.store_null_image
        }
    }

    AsyncImage(
        model = url,
        contentDescription = "프로필 이미지",
        error = painterResource(id = errorImage),
        modifier = modifier
    )
}