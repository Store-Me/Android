package com.store_me.storeme.ui.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.data.BannerContent
import com.store_me.storeme.ui.component.TitleWithDeleteButton
import com.store_me.storeme.ui.theme.AddCommentButtonColor
import com.store_me.storeme.ui.theme.BannerBeforeStartBoxColor
import com.store_me.storeme.ui.theme.BannerEndedBoxColor
import com.store_me.storeme.ui.theme.BannerOngoingBoxColor
import com.store_me.storeme.ui.theme.BannerSubtitleColor
import com.store_me.storeme.ui.theme.CouponDividerLineColor
import com.store_me.storeme.ui.theme.EditCommentStrokeColor
import com.store_me.storeme.ui.theme.appFontFamily
import com.store_me.storeme.ui.theme.storeMeTypography
import com.store_me.storeme.utils.DateTimeUtils

@Composable
fun BannerDetailScreen(
    navController: NavController,
    bannerId: String,
    bannerDetailViewModel: BannerDetailViewModel = viewModel()
) {
    LaunchedEffect(Unit){
        if(bannerId.isNotEmpty())
            bannerDetailViewModel.getBannerData()
    }

    val dataFetched by bannerDetailViewModel.dataFetched.collectAsState()

    if(dataFetched){
        Scaffold(
            containerColor = White,
            topBar = { TitleWithDeleteButton(navController = navController, title = "배너 상세 보기") },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item { BannerTitleSection(bannerDetailViewModel) }
                    item { HorizontalDivider(color = CouponDividerLineColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 15.dp)) }
                    items(bannerDetailViewModel.bannerDetailData.value?.content ?: emptyList()) { content ->
                        when (content) {
                            is BannerContent.TextContent -> TextContentSection(content)
                            is BannerContent.ImageContent -> ImageContentSection(content)
                        }
                    }
                    item { CommentListSection() }
                    item { EditCommentSection() }
                }
            }
        )
    }
}

@Composable
fun BannerTitleSection(bannerDetailViewModel: BannerDetailViewModel) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        Text(
            text = bannerDetailViewModel.bannerDetailData.value!!.title,
            style = storeMeTypography.labelLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        if(bannerDetailViewModel.bannerDetailData.value!!.subTitle != null){
            Text(
                text = bannerDetailViewModel.bannerDetailData.value!!.subTitle!!,
                style = storeMeTypography.titleSmall,
                color = BannerSubtitleColor
                )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BannerStateBox(bannerDetailViewModel = bannerDetailViewModel)

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = bannerDetailViewModel.getBannerPeriodText(),
                style = storeMeTypography.titleSmall
            )
        }
    }
}

@Composable
fun BannerStateBox(bannerDetailViewModel: BannerDetailViewModel){
    val state = bannerDetailViewModel.getBannerState()

    val (text, color) = when(state) {
        DateTimeUtils.PeriodStatus.BEFORE_START -> {
            state.displayName to BannerBeforeStartBoxColor
        }
        DateTimeUtils.PeriodStatus.ONGOING -> {
            state.displayName to BannerOngoingBoxColor
        }
        DateTimeUtils.PeriodStatus.ENDED -> {
            state.displayName to BannerEndedBoxColor
        }
    }

    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(4.dp))
            .wrapContentSize()
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontFamily = appFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = White
        )
    }

    Spacer(modifier = Modifier.width(5.dp))
}

@Composable
fun TextContentSection(content: BannerContent.TextContent) {
    Text(
        text = content.text,
        fontFamily = appFontFamily,
        fontSize = 14.sp,
        letterSpacing = 0.7.sp,
        lineHeight = 18.sp,
        modifier = Modifier.padding(bottom = 5.dp, start = 20.dp, end = 20.dp)
    )
}

@Composable
fun ImageContentSection(content: BannerContent.ImageContent) {
    AsyncImage(
        model = content.imageUrl,
        contentDescription = "배너 내용 이미지",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, start = 20.dp, end = 20.dp)
    )
}

@Composable
fun CommentListSection() {

}

@Composable
fun EditCommentSection() {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .background(White, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, shape = RoundedCornerShape(10.dp), color = EditCommentStrokeColor)
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp, top = 15.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp)
                .background(White),
            decorationBox = { innerTextField ->
                if (text.text.isEmpty()) {
                    Text(
                        text = "댓글 남기기",
                        style = storeMeTypography.titleSmall,
                        color = Gray,
                    )
                }

                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { /* 등록 버튼 클릭 시 동작 */ },
            colors = ButtonDefaults.buttonColors(containerColor = AddCommentButtonColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "등록",
                style = storeMeTypography.titleSmall
            )
        }
    }
}