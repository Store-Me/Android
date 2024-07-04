@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.ui.home.SearchField
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.theme.storeMeTypography

@Composable
fun BackButtonWithTitle(navController: NavController, title: String){
    val interactionSource = remember { MutableInteractionSource() }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_left),
                    contentDescription = "뒤로 가기",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            onClick = {
                                navController.popBackStack()
                            },
                            interactionSource = interactionSource,
                            indication = rememberRipple(bounded = false)
                        )
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = title,
                    style = storeMeTypography.labelLarge
                )
            }
        }
    )
}