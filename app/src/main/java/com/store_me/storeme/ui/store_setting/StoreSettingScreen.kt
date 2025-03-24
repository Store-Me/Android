package com.store_me.storeme.ui.store_setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.store_me.storeme.data.enums.StoreProfileItems
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.NavigationUtils

@Composable
fun StoreSettingScreen(
    navController: NavController
) {

    Scaffold(
        containerColor = Color.White,
        topBar = { TitleWithDeleteButton(title = "가게정보 관리") {
            navController.popBackStack()
        } },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
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
                    if(it != StoreProfileItems.EDIT_PROFILE && it != StoreProfileItems.MANAGEMENT) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .clickable { NavigationUtils().navigateOwnerNav(navController, it) },
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
            }
        }
    )
}