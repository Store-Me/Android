@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)

package com.store_me.storeme.ui.mystore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.store_me.storeme.R
import com.store_me.storeme.data.DefaultPostData
import com.store_me.storeme.data.MyPickData
import com.store_me.storeme.ui.main.FAVORITE
import com.store_me.storeme.ui.theme.MyPickStrokeColor
import com.store_me.storeme.ui.theme.NewNoticeColor
import com.store_me.storeme.ui.theme.NormalCategoryColor
import com.store_me.storeme.ui.theme.PostStrokeColor
import com.store_me.storeme.ui.theme.PostTimeTextColor
import com.store_me.storeme.ui.theme.SelectedCategoryColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.SampleDataUtils

@Preview
@Composable
fun MyStoreScreen(myStoreViewModel: MyStoreViewModel = viewModel()){

    Scaffold(
        containerColor = White,
        topBar = { MyStoreTitleSection() },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item { MyPickSection() }
                item { CategorySection(myStoreViewModel) }

                items(myStoreViewModel.postList) { post ->
                    Box(modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 10.dp)) {
                        PostItem(post)
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
            contentDescription = FAVORITE,
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
        Spacer(modifier = Modifier.height(10.dp))
        MyPickList()
    }
}

@Composable
fun MyPickTitleWithButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "\u2B50 마이픽",
            style = storeMeTypography.labelMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        EditButton {
            //TODO 편집
        }
    }
}

@Composable
fun EditButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .wrapContentWidth()
            .height(30.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 10.dp),
        onClick = onClick
    ) {
        Text(text = "편집하기", style = storeMeTypography.labelSmall)
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
fun MyPickItems(myPickData: MyPickData) {
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
            AsyncImage(
                model = myPickData.storeImage,
                contentDescription = "${myPickData.storeName} 사진",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(2.5.dp, MyPickStrokeColor, CircleShape)
            )

            if(myPickData.isNewExist) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color = NewNoticeColor, shape = CircleShape)
                        .border(2.dp, MyPickStrokeColor, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = myPickData.storeName,
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
fun CategorySection(viewModel: MyStoreViewModel) {
    val selectedCategory by viewModel.selectedCategory.observeAsState(initial = "전체")

    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            items(viewModel.categoryList) { category ->
                CategoryItem(category, selectedCategory == category) {
                    viewModel.selectCategory(category)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) SelectedCategoryColor else NormalCategoryColor
    val textColor = if (isSelected) White else SelectedCategoryColor

    Box(
        modifier = Modifier
            .height(30.dp)
            .clickable(onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category,
            color = textColor,
            fontFamily = appFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun PostItem(postData: DefaultPostData) {

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
                model = postData.storeImage,
                contentDescription = "${postData.storeName} 사진",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(

            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = postData.storeName,
                    style = storeMeTypography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = postData.location + " · " + postData.datetime,
                    fontFamily = appFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = PostTimeTextColor
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.bottom_home),
                contentDescription = "메뉴",
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

        Spacer(modifier = Modifier.height(15.dp))

        PostImageSection(postData.imageList)

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = postData.title,
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 2,
            letterSpacing = 0.5.sp,
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

        Row {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_like_off),
                contentDescription = "좋아요",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = {

                        },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    )
            )

            Spacer(modifier = Modifier.width(20.dp))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_comment),
                contentDescription = "댓글",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = {

                        },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_share),
                contentDescription = "메뉴",
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
}

@Composable
fun PostImageSection(imageList: List<String>) {
    val pagerState = rememberPagerState()

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
                count = imageList.size,
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
                    .padding(horizontal = 8.dp, vertical = 4.dp)
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
