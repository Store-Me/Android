package com.store_me.storeme.ui.home.owner.tab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.data.store.post.LabelData
import com.store_me.storeme.ui.store_setting.post.PostViewModel
import com.store_me.storeme.ui.theme.ExpiredColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun PostTab(
    labels : List<LabelData>,
    postViewModel: PostViewModel
) {
    var selectedLabel by remember { mutableStateOf<LabelData?>(null) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Labels(
            labels = labels,
            selectedLabel = selectedLabel
        ) {
            selectedLabel = it
        }
    }
}

@Composable
fun Labels(
    labels : List<LabelData>,
    selectedLabel: LabelData?,
    onClick: (LabelData?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            LabelItem(
                name = "전체",
                isSelected = selectedLabel == null
            ) {
                onClick(null)
            }
        }

        items(labels) { label ->
            LabelItem(
                name = label.name,
                isSelected = label == selectedLabel
            ) {
                onClick(label)
            }
        }
    }
}

@Composable
fun LabelItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = { onClick() },
        shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if(isSelected) HighlightColor else GuideColor,
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, if(isSelected) HighlightColor else ExpiredColor),
        modifier = Modifier.defaultMinSize(minHeight = 32.dp)
    ) {
        Text (
            text = name,
            style = storeMeTextStyle(FontWeight.ExtraBold, 1)
        )
    }
}