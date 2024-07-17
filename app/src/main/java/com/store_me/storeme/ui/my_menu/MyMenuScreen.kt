package com.store_me.storeme.ui.my_menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.BannerLayout
import com.store_me.storeme.ui.component.NotificationIcon
import com.store_me.storeme.ui.theme.DefaultDividerColor
import com.store_me.storeme.ui.theme.MyMenuIconColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography

val LocalMyCouponViewModel = staticCompositionLocalOf<MyMenuViewModel> {
    error("No MyMenuViewModel provided")
}

@Composable
fun MyMenuScreen(
    navController: NavController,
    myMenuViewModel: MyMenuViewModel = hiltViewModel()
) {

    CompositionLocalProvider(LocalMyCouponViewModel provides myMenuViewModel) {
        Scaffold(
            containerColor = White,
            topBar = { MyMenuTitleSection(navController) },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item { MyProfileSection(navController) }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                    item { BannerLayout(navController = navController) }
                    item { MyActivitySection(navController = navController) }
                    item { HorizontalDivider(color = DefaultDividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 20.dp)) }
                    item { StoreMeNoticeSection(navController = navController) }
                    item { HorizontalDivider(color = DefaultDividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 20.dp)) }
                    item { MyMenuNormalItemSection(navController = navController) }
                    item { Spacer(modifier = Modifier.height(300.dp)) }

                }
            }
        )
    }

}

@Composable
fun MyMenuTitleSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_mymenu),
            contentDescription = "로고",
            modifier = Modifier
                .height(20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        NotificationIcon(navController = navController)

        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_my_menu_setting),
            contentDescription = "설정",
            modifier = Modifier
                .size(26.dp)
                .clickable(
                    onClick = { /* TODO 설정 */ },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                )
        )
    }
}

@Composable
fun MyProfileSection(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .wrapContentHeight()
            .background(color = Black, shape = RoundedCornerShape(15.dp))
    ) {
        MyProfileItem()

        HorizontalDivider(color = White, thickness = 1.dp)

        MyProfileMenu()
    }
}

@Composable
fun MyProfileItem() {
    Row(
        modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImageSection()

        Spacer(modifier = Modifier.width(20.dp))

        NickNameText()

        Spacer(modifier =Modifier.weight(1f))

        EditProfileButton {

        }
    }
}

@Composable
fun ProfileImageSection() {
    val myMenuViewModel = LocalMyCouponViewModel.current
    val userData by myMenuViewModel.userData.collectAsState()

    Box(
        modifier = Modifier
            .size(60.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            model = userData.profileImage,
            contentDescription = "${userData.nickName} 프로필 이미지",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(color = White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                modifier = Modifier.size(16.dp),
                contentDescription = "카메라 아이콘",
                tint = Unspecified
            )
        }

    }
}

@Composable
fun EditProfileButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(26.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, White),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = "프로필 편집", style = storeMeTypography.titleSmall)
    }
}


@Composable
fun NickNameText() {
    val myMenuViewModel = LocalMyCouponViewModel.current
    val userData by myMenuViewModel.userData.collectAsState()

    Text(
        text = userData.nickName,
        fontFamily = appFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        maxLines = 2,
        color = White,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun MyProfileMenu() {
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyProfileMenuItem(MyMenuViewModel.MyProfileMenuItem.FAVORITE_STORE) {

        }

        MyProfileMenuItem(MyMenuViewModel.MyProfileMenuItem.MY_COUPON) {

        }

        MyProfileMenuItem(MyMenuViewModel.MyProfileMenuItem.LIKE_LIST) {

        }
    }
}

@Composable
fun MyProfileMenuItem(menu: MyMenuViewModel.MyProfileMenuItem, onClick: () -> Unit) {
    val text = menu.displayName
    val icon = menu.icon

    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier
                .height(16.dp)
                .width(20.dp),
            contentDescription = "$text 아이콘",
            tint = MyMenuIconColor
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            style = storeMeTypography.titleSmall,
            color = White
        )
    }
}

@Composable
fun MyActivitySection(navController: NavController) {
    Column(

    ) {
        Text(
            text = "나의 활동",
            fontFamily = appFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = Black,
            modifier = Modifier.padding(top = 20.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
        )

        MyMenuIconWithText(MyMenuViewModel.MyMenuItem.PURCHASE_HISTORY) {

        }

        MyMenuIconWithText(MyMenuViewModel.MyMenuItem.RESERVATION_HISTORY) {

        }
    }
}

@Composable
fun StoreMeNoticeSection(navController: NavController) {
    Column(

    ) {
        Text(
            text = "스토어미 소식",
            fontFamily = appFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = Black,
            modifier = Modifier.padding(top = 20.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
        )

        MyMenuIconWithText(MyMenuViewModel.MyMenuItem.EVENT) {

        }

        MyMenuIconWithText(MyMenuViewModel.MyMenuItem.NOTICE) {

        }
    }
}

@Composable
fun MyMenuIconWithText(menu: MyMenuViewModel.MyMenuItem, onClick: () -> Unit) {
    val text = menu.displayName
    val icon = menu.icon

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))

        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier
                .size(20.dp),
            contentDescription = "$text 아이콘",
            tint = Unspecified
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            style = storeMeTypography.titleMedium,
            fontSize = 14.sp,
            color = Black
        )
    }
}

@Composable
fun MyMenuNormalItemSection(navController: NavController) {
    MyMenuNormalItem(MyMenuViewModel.MyMenuNormalItem.FQA) {

    }

    MyMenuNormalItem(MyMenuViewModel.MyMenuNormalItem.INQUIRY) {

    }

    MyMenuNormalItem(MyMenuViewModel.MyMenuNormalItem.POLICY) {

    }

    MyMenuNormalItem(MyMenuViewModel.MyMenuNormalItem.LOGOUT) {

    }
    MyMenuNormalItem(MyMenuViewModel.MyMenuNormalItem.QUIT) {

    }

}

@Composable
fun MyMenuNormalItem(menu: MyMenuViewModel.MyMenuNormalItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = menu.displayName,
            style = storeMeTypography.titleMedium,
            fontSize = 12.sp,
            color = Black
        )
    }
}

