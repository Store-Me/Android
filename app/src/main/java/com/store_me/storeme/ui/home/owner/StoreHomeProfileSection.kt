package com.store_me.storeme.ui.home.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.data.enums.AccountType
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.LinkSection
import com.store_me.storeme.ui.component.ProfileImage
import com.store_me.storeme.ui.main.MainActivity
import com.store_me.storeme.ui.theme.EditButtonColor
import com.store_me.storeme.ui.theme.ManagementButtonColor
import com.store_me.storeme.ui.theme.OwnerHomeLikeCountColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.LikeCountUtils
import com.store_me.storeme.utils.NavigationUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth

/**
 * Owner Home 화면의 StoreProfile Composable
 */
@Composable
fun StoreHomeProfileSection(navController: NavController) {
    val auth = LocalAuth.current

    val accountType by auth.accountType.collectAsState()

    val linkListData by Auth.linkListData.collectAsState()

    Column {
        Spacer(modifier = Modifier.height(15.dp))

        ProfileInfoSection()

        Spacer(modifier = Modifier.height(15.dp))

        EditProfileSection(navController)

        Spacer(modifier = Modifier.height(15.dp))

        LinkSection(
            linkListData,
            modifier= Modifier.weight(1f),
            onShareClick = {  },
            onEditClick = {
                NavigationUtils().navigateOwnerNav(
                    navController,
                    MainActivity.OwnerNavItem.LINK_SETTING
                )
            },
            accountType = accountType
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}

/**
 * StoreProfile 내의 StoreProfileInfo Composable
 */
@Composable
fun ProfileInfoSection() {
    val ownerHomeViewModel = LocalStoreDataViewModel.current

    val storeData by ownerHomeViewModel.storeData.collectAsState()

    val subText =
        if(storeData?.storeDetailCategory?.isEmpty() == true)
            storeData?.storeLocation
        else
            storeData?.storeLocation + " · " + storeData?.storeDetailCategory

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileImage(
            accountType = AccountType.OWNER,
            url = storeData?.storeProfileImageUrl,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = storeData?.storeName ?: "",
                style = storeMeTextStyle(FontWeight.ExtraBold, 6)
            )

            Text(
                text = subText ?: "",
                style = storeMeTextStyle(FontWeight.Bold, 0)
            )
        }

        LikeIconWithCount(0)
    }

}

@Composable
fun EditProfileSection(navController: NavController) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        DefaultButton(
            buttonText = "프로필 수정",
            modifier = Modifier
                .weight(1f),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = EditButtonColor
            ),
            diffValue = 0
        ) {
            NavigationUtils().navigateOwnerNav(navController, MainActivity.OwnerNavItem.EDIT_PROFILE)
        }

        Spacer(modifier = Modifier.width(10.dp))

        DefaultButton(
            buttonText = "가게정보 관리",
            modifier = Modifier
                .weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = ManagementButtonColor,
                contentColor = Color.White
            ),
            diffValue = 0
        ) {
            NavigationUtils().navigateOwnerNav(
                navController,
                MainActivity.OwnerNavItem.STORE_SETTING
            )
        }
    }
}

@Composable
fun LikeIconWithCount(count: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .clickable { }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_like_off),
            contentDescription = "좋아요",
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    onClick = { },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false)
                )
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = LikeCountUtils().convertLikeCount(count),
            style = storeMeTextStyle(FontWeight.Bold, -1),
            maxLines = 1,
            color = OwnerHomeLikeCountColor
        )
    }
}