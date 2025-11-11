package com.store_me.storeme.ui.store_setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.enums.StoreHomeItem
import com.store_me.storeme.data.enums.StoreProfileItems
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.main.navigation.owner.OwnerRoute
import com.store_me.storeme.ui.status_bar.StatusBarPadding
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun StoreSettingScreen(
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            StatusBarPadding()
        }

        item {
            TitleWithDeleteButton(title = "가게정보 관리") {
                navController.popBackStack()
            }
        }

        item {
            Text(
                text = "기본 정보",
                style = storeMeTextStyle(fontWeight = FontWeight.ExtraBold, changeSizeValue = 6),
                color = Color.Black,
                modifier = Modifier
                    .padding(20.dp)
            )

            DefaultHorizontalDivider()
        }

        items(StoreProfileItems.entries) {
            if(it != StoreProfileItems.ProfileEdit && it != StoreProfileItems.StoreManagement) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(it.route.fullRoute) }
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.displayName,
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "이동 아이콘",
                        modifier = Modifier
                            .size(18.dp),
                        tint = Color.Black
                    )
                }

                DefaultHorizontalDivider()
            }
        }

        item {
            Text(
                text = "가게 정보",
                style = storeMeTextStyle(fontWeight = FontWeight.ExtraBold, changeSizeValue = 6),
                color = Color.Black,
                modifier = Modifier
                    .padding(20.dp)
            )

            DefaultHorizontalDivider()
        }

        item {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(OwnerRoute.LinkSetting.fullRoute) }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "링크",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "이동 아이콘",
                    modifier = Modifier
                        .size(18.dp),
                    tint = Color.Black
                )
            }

            DefaultHorizontalDivider()
        }

        items(StoreHomeItem.entries) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(it.route.fullRoute) }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.displayName,
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "이동 아이콘",
                    modifier = Modifier
                        .size(18.dp),
                    tint = Color.Black
                )
            }

            DefaultHorizontalDivider()
        }

        item {
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}