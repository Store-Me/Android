package com.store_me.storeme.ui.store_talk

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.ChatRoomWithStoreInfoData
import com.store_me.storeme.ui.theme.NewNoticeColor
import com.store_me.storeme.ui.theme.UnselectedItemColor
import com.store_me.storeme.ui.theme.storeMeTypography

@Preview
@Composable
fun StoreTalkScreen(storeTalkViewModel: StoreTalkViewModel = viewModel()){
    Column(
        modifier = Modifier.background(color = White)
    ) {
        StoreTalkTitleSection()

        ChatRoomSection(storeTalkViewModel)
    }
}

@Composable
fun StoreTalkTitleSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_storetalk),
            contentDescription = "로고",
            modifier = Modifier
                .height(20.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
            contentDescription = "검색",
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    onClick = {

                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                )
        )
    }
}

@Composable
fun ChatRoomSection(viewModel: StoreTalkViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(viewModel.sampleChatRoomData) { chatRoom ->
            ChatRoomItem(chatRoom)
        }
    }
}

@Composable
fun ChatRoomItem(chatRoomWithStoreInfoData: ChatRoomWithStoreInfoData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp)
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 60.dp),
            contentAlignment = Alignment.TopStart
        ) {
            AsyncImage(
                model = chatRoomWithStoreInfoData.storeInfoData.storeImage,
                contentDescription = "${chatRoomWithStoreInfoData.name} 사진",
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(60.dp)
                    .clip(shape = CircleShape)
            )

            if(chatRoomWithStoreInfoData.unreadCount > 0) {
                val countText =if(chatRoomWithStoreInfoData.unreadCount < 100) chatRoomWithStoreInfoData.unreadCount.toString() else "99+"

                Box(
                    modifier = Modifier
                        .offset(x = (-39).dp)
                        .height(18.dp)
                        .background(color = NewNoticeColor, shape = RoundedCornerShape(9.dp))
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = countText,
                        style = storeMeTypography.titleSmall,
                        fontSize = 10.sp,
                        color = White,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Spacer(modifier = Modifier.height(5.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = chatRoomWithStoreInfoData.name,
                    style = storeMeTypography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = chatRoomWithStoreInfoData.timeStamp,
                    style = storeMeTypography.titleSmall,
                    fontSize = 10.sp,
                    color = UnselectedItemColor
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = chatRoomWithStoreInfoData.lastMessage,
                style = storeMeTypography.titleSmall.copy(
                    lineHeight = 17.sp
                ),
                color = UnselectedItemColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}
