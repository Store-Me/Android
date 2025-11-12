@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.store_talk

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.ChatRoomWithStoreInfoData
import com.store_me.storeme.ui.component.SearchButton
import com.store_me.storeme.ui.component.TitleSearchSection
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.storeMeTypography

@Composable
fun StoreTalkScreen(navController: NavController, storeTalkViewModel: StoreTalkViewModel = viewModel()){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        containerColor = White,
        topBar = {
            StoreTalkTitleSection(scrollBehavior = scrollBehavior, storeTalkViewModel)
                 },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(innerPadding)
            ) {
                ChatRoomSection(storeTalkViewModel)
            }
        }
    )
}

@Composable
fun StoreTalkTitleSection(scrollBehavior: TopAppBarScrollBehavior, storeTalkViewModel: StoreTalkViewModel) {
    val searchState by storeTalkViewModel.searchState.collectAsState()

    TopAppBar(
        title = {
            if(searchState){
                TitleSearchSection(
                    onSearch = {

                    },
                    onClose = {
                        storeTalkViewModel.setSearchState(false)
                    })
            } else {
                NormalStoreTalkTitleSection {
                    storeTalkViewModel.setSearchState(true)
                }
            }

        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White,
            scrolledContainerColor = White
        )
    )
}

@Composable
fun NormalStoreTalkTitleSection(onClickSearchIcon: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 4.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_storetalk),
            contentDescription = "로고",
            modifier = Modifier
                .height(20.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        SearchButton {
            onClickSearchIcon()
        }
    }
}

@Composable
fun ChatRoomSection(viewModel: StoreTalkViewModel) {
    LazyColumn {
        items(viewModel.sampleChatRoomData) { chatRoom ->
            ChatRoomItem(chatRoom) {

            }
        }
    }
}

@Composable
fun ChatRoomItem(chatRoomWithStoreInfoData: ChatRoomWithStoreInfoData, onClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        val (badge, row) = createRefs()

        Box(
            modifier = Modifier
                .size(60.dp)
                .constrainAs(badge) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            contentAlignment = Alignment.CenterEnd
        ) {

            if(chatRoomWithStoreInfoData.unreadCount > 0){
                val countText =if(chatRoomWithStoreInfoData.unreadCount < 100) chatRoomWithStoreInfoData.unreadCount.toString() else "99+"

                Box(
                    modifier = Modifier
                        .height(18.dp)
                        .padding(end = 20.dp)
                        .background(color = HighlightColor, shape = RoundedCornerShape(9.dp)),
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

        Row(
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .constrainAs(row) {
                    start.linkTo(parent.start)
                    end.linkTo(badge.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
        ) {

            AsyncImage(
                model = chatRoomWithStoreInfoData.storeInfoData.storeImage,
                contentDescription = "${chatRoomWithStoreInfoData.name} 사진",
                modifier = Modifier
                    .size(60.dp)
                    .clip(shape = CircleShape)
            )

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
                        color = GuideColor
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = chatRoomWithStoreInfoData.lastMessage,
                    style = storeMeTypography.titleSmall.copy(
                        lineHeight = 17.sp
                    ),
                    color = GuideColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
