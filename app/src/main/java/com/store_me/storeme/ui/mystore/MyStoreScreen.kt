@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.mystore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.NormalPostWithStoreInfoData
import com.store_me.storeme.data.MyPickWithStoreInfoData
import com.store_me.storeme.ui.component.CategorySection
import com.store_me.storeme.ui.component.StrokeButton
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.MyPickStrokeColor
import com.store_me.storeme.ui.theme.PostStrokeColor
import com.store_me.storeme.ui.theme.PostTimeTextColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.SampleDataUtils

@Composable
fun MyStoreScreenWithBottomSheet(
    myStoreViewModel: MyStoreViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
) {
    var isSheetShow by remember { mutableStateOf(false) }

    if(isSheetShow) {
        MyStoreBottomSheet(
            onDismiss = { isSheetShow = false },
        )
    }

    MyStoreScreen(viewModel = myStoreViewModel, categoryViewModel) {
        isSheetShow = true
    }
}

@Composable
fun MyStoreBottomSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = White,
        onDismissRequest = { onDismiss() },
        content = {
            //TODO BOTTOM

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text("Comment Bottom Sheet Layout")

            }
        }
    )
}

@Composable
fun MyStoreScreen(viewModel: MyStoreViewModel, categoryViewModel: CategoryViewModel, onCommentClick: () -> Unit){
    Scaffold(
        containerColor = White,
        topBar = { MyStoreTitleSection() },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item { MyPickSection() }
                item { CategorySection(categoryViewModel) }

                items(viewModel.postList) { post ->
                    Column(modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 10.dp)) {
                        PostItem(
                            postData = post,
                            onMenuClick = {

                            },
                            onLikeClick = {

                            },
                            onCommentClick = {
                                onCommentClick()
                            },
                            onShareClick = {

                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun MyStoreTitleSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_mystore),
            contentDescription = "Favorite",
            modifier = Modifier
                .height(20.dp)
        )
    }
}

@Composable
fun MyPickSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        MyPickTitleWithButton()
        MyPickList()
    }
}

@Composable
fun MyPickTitleWithButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "\u2B50 마이픽",
            style = storeMeTypography.labelMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        StrokeButton(text = "편집하기") {

        }
    }
}

@Composable
fun MyPickList() {
    val sampleMyPickData = SampleDataUtils.sampleMyPick()

    LazyRow(
        contentPadding = PaddingValues(start = 20.dp),
    ) {
        items(sampleMyPickData) { item ->
            MyPickItems(item)
        }
    }
}

@Composable
fun MyPickItems(myPickWithStoreInfoData: MyPickWithStoreInfoData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            MyPickImageBox(myPickWithStoreInfoData = myPickWithStoreInfoData)

            if(myPickWithStoreInfoData.isNewExist) {
                MyPickNewCircle()
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = myPickWithStoreInfoData.storeInfoData.storeName,
            fontFamily = appFontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(min = 0.dp, max = 60.dp)
        )
    }
}

@Composable
fun MyPickImageBox(myPickWithStoreInfoData: MyPickWithStoreInfoData) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .border(3.dp, MyPickStrokeColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = myPickWithStoreInfoData.storeInfoData.storeImage,
            contentDescription = "${myPickWithStoreInfoData.storeInfoData.storeName} 사진",
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun MyPickNewCircle(){
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .border(2.dp, MyPickStrokeColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(HighlightColor, CircleShape),
        )
    }
}

@Composable
fun PostItem(postData: NormalPostWithStoreInfoData, onMenuClick: () -> Unit, onLikeClick: () -> Unit, onCommentClick: () -> Unit, onShareClick: () -> Unit) {

    Column(
        modifier = Modifier
            .background(shape = RoundedCornerShape(20.dp), color = White)
            .border(1.5.dp, shape = RoundedCornerShape(20.dp), color = PostStrokeColor)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = postData.storeInfoData.storeImage,
                contentDescription = "${postData.storeInfoData.storeName} 사진",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(

            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = postData.storeInfoData.storeName,
                    style = storeMeTypography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = postData.storeInfoData.location + " · " + DateTimeUtils.datetimeAgo(postData.datetime),
                    fontFamily = appFontFamily,
                    fontSize = 10.sp,
                    letterSpacing = 0.3.sp,
                    fontWeight = FontWeight.Normal,
                    color = PostTimeTextColor
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                tint = Color.Unspecified,
                contentDescription = "메뉴",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onMenuClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false)
                    )
                    .padding(2.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        if(postData.imageList?.isNotEmpty() == true) {
            PostImageSection(postData.imageList)
            Spacer(modifier = Modifier.height(15.dp))
        }

        Text(
            text = postData.title,
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 2,
            lineHeight = 18.sp,
            letterSpacing = 0.7.sp,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = postData.content,
            style = storeMeTypography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_like_off),
                contentDescription = "좋아요",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onLikeClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false)
                    )
            )

            Spacer(modifier = Modifier.width(20.dp))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_comment),
                contentDescription = "댓글",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onCommentClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false)
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_share),
                contentDescription = "공유",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onShareClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false)
                    )
            )
        }

    }
}

@Composable
fun PostImageSection(imageList: List<String>) {
    val pagerState = rememberPagerState(pageCount = { imageList.size })

    BoxWithConstraints (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val boxWidth = maxWidth
        val imageHeight = boxWidth / 2

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            ) { page ->
                AsyncImage(
                    model = imageList[page],
                    contentDescription = "사진 $page",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(15.dp))
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 내부 하단 우측에 위치
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${imageList.size}",
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 9.sp,
                    color = White
                )
            }
        }
    }
}