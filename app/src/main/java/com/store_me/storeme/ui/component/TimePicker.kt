package com.store_me.storeme.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.store_me.storeme.ui.theme.TimePickerSelectLineColor
import com.store_me.storeme.ui.theme.UndefinedTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun StoreMeTimePicker(
    itemHeight: Dp = 60.dp,
    initHour: Int = 0,
    initMinute: Int = 0,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
) {
    val hourList = (0..23).toList()
    val minuteList = (0..59 step 5).toList()

    val selectedHour = remember { mutableStateOf(initHour) }
    val selectedMinute = remember { mutableStateOf(initMinute) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight * 3),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(TimePickerSelectLineColor)
        ) {
            Text(
                text = " ",
                style = storeMeTextStyle(FontWeight.Normal, 6),
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TimePickerColumn(
                items = hourList,
                selectedItem = selectedHour.value,
                onItemSelected = {
                    selectedHour.value = it
                    onHourSelected(it)
                },
                itemHeight = itemHeight,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = ":",
                style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                color = Black
            )

            TimePickerColumn(
                items = minuteList,
                selectedItem = selectedMinute.value,
                onItemSelected = {
                    selectedMinute.value = it
                    onMinuteSelected(it)
                },
                itemHeight = itemHeight,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TimePickerColumn(
    items: List<Int>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    itemHeight: Dp,
    modifier: Modifier
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 ) % items.size + items.indexOf(selectedItem) - 1
    )

    //Animation 중 Scroll 발생 시 생기는 오류 방지
    var isAnimating by remember { mutableStateOf(false) }
    var currentAnimationJob: Job? by remember { mutableStateOf(null) }

    val scope = rememberCoroutineScope()

    var isScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        isScrolling = listState.isScrollInProgress

        snapshotFlow { listState.isScrollInProgress }
            .filter { !it && !isAnimating  }
            .collect {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                val centerPosition = listState.layoutInfo.viewportEndOffset / 2

                // 중앙에 가장 가까운 항목 찾기
                val closestItem = visibleItems.minByOrNull {
                    abs(it.offset + it.size / 2 - centerPosition)
                }

                closestItem?.let { item ->
                    val selectedValue = items[item.index % items.size]
                    onItemSelected(selectedValue)

                    if (item.offset != 0) {
                        isAnimating = true

                        currentAnimationJob?.cancelAndJoin()

                        currentAnimationJob = launch {
                            try {
                                listState.animateScrollToItem(item.index - 1)
                            } finally {
                                isAnimating = false
                            }
                        }

                    }
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .height(itemHeight * 3),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

    ) {
        items(Int.MAX_VALUE) { index ->
            val item = items[index % items.size]
            val isSelected = item == selectedItem && !isScrolling

            Column(
                modifier = Modifier
                    .height(itemHeight)
                    .clickable(
                        onClick = {
                            scope.launch {
                                onItemSelected(item)
                                listState.scrollToItem(index = index - 1)
                            }
                        },
                        interactionSource = null,
                        indication = null
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.toString().padStart(2, '0'),
                    style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                    color = if (isSelected) Black else UndefinedTextColor,
                    modifier = Modifier
                        .background(Color.Transparent),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}