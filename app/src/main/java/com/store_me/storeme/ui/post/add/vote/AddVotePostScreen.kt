@file:OptIn(ExperimentalMaterial3Api::class)

package com.store_me.storeme.ui.post.add.vote

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.store_me.storeme.R
import com.store_me.storeme.data.TimeData
import com.store_me.storeme.data.enums.post.PostType
import com.store_me.storeme.data.getText
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.SimpleTextField
import com.store_me.storeme.ui.component.StoreMeSelectDateCalendar
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.component.StoreMeTimePicker
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.post.add.normal.AddPostTopBar
import com.store_me.storeme.ui.theme.DisabledColor
import com.store_me.storeme.ui.theme.DividerColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.LighterHighlightColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.COMPOSABLE_ROUNDING_VALUE
import com.store_me.storeme.utils.DateTimeUtils
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import java.time.LocalDate

@Composable
fun AddVotePostScreen(
    addVotePostViewModel: AddVotePostViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val snackbarHostState = LocalSnackbarHostState.current
    val loadingViewModel = LocalLoadingViewModel.current

    var showBackWarningDialog by remember{ mutableStateOf(false) }

    val title by addVotePostViewModel.title.collectAsState()
    val options by addVotePostViewModel.options.collectAsState()
    val startLocalDate by addVotePostViewModel.startLocalDate.collectAsState()
    val startTime by addVotePostViewModel.startTime.collectAsState()
    val endLocalDate by addVotePostViewModel.endLocalDate.collectAsState()
    val endTime by addVotePostViewModel.endTime.collectAsState()
    val isSuccess by addVotePostViewModel.isSuccess.collectAsState()

    fun onClose() {
        showBackWarningDialog = true
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(isSuccess) {
        if(isSuccess) {
            (context as Activity).finish()
        }
    }

    Scaffold(
        modifier = Modifier
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { StoreMeSnackbar(snackbarData = it) }
        ) },
        topBar = {
            AddPostTopBar(
                postType = PostType.VOTE,
                onClose = { onClose() },
                onFinish = {
                    loadingViewModel.showLoading()
                    addVotePostViewModel.createVotePost()
                }
            ) },
        content = { innerPadding ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    EditVoteItem(
                        title = title,
                        options = options,
                        onTitleChange = { addVotePostViewModel.updateTitle(it) },
                        onAddOption = {
                            addVotePostViewModel.addOption()
                        },
                        onDeleteOption = { index ->
                            addVotePostViewModel.deleteOption(index)
                        },
                        onChangeOption = { index, value ->
                            addVotePostViewModel.updateOption(index, value)
                        }
                    )
                }

                item {
                    SelectPeriodItem(
                        startLocalDate = startLocalDate,
                        startTime = startTime,
                        endLocalDate = endLocalDate,
                        endTime = endTime,
                        onStartDateTimeChange = { localDate, timeData -> addVotePostViewModel.updateStartDateTime(localDate, timeData) },
                        onEndDateTimeChange = { localDate, timeData -> addVotePostViewModel.updateEndDateTime(localDate, timeData) }
                    )
                }
            }
        }
    )

    if(showBackWarningDialog) {
        BackWarningDialog(
            onDismiss = { showBackWarningDialog = false },
            onAction = {
                showBackWarningDialog = false
                (context as Activity).finish()
            }
        )
    }
}

@Composable
fun EditVoteItem(
    title: String,
    options: List<String>,
    onTitleChange: (String) -> Unit,
    onAddOption: () -> Unit,
    onDeleteOption: (Int) -> Unit,
    onChangeOption: (Int, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .border(
                width = 2.dp,
                color = DividerColor,
                shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)
            )
            .clip(RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SimpleTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            placeholderText = "투표의 제목을 입력해주세요.",
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        //각 항목
        options.forEachIndexed { index, option ->
            OptionTextFieldItem(
                text = option,
                onValueChange = {
                    onChangeOption(index, it)
                },
                placeholderText = "항목을 입력하세요.",
                onDelete = {
                    onDeleteOption(index)
                }
            )
        }

        AddOptionButton {
            onAddOption()
        }
    }
}

@Composable
fun AddOptionButton(onAddOption: () -> Unit) {
    DefaultButton(
        buttonText = "항목 추가",
        leftIconResource = R.drawable.ic_plus,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = DisabledColor
        ),
        fontWeight = FontWeight.Bold,
        leftIconTint = DisabledColor,
        modifier = Modifier
            .border(
                color = DisabledColor,
                width = 1.dp,
                shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)
            )
    ) {
        onAddOption()
    }
}

@Composable
fun OptionTextFieldItem(
    text: String,
    placeholderText: String,
    onValueChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        textStyle = storeMeTextStyle(FontWeight.Bold, 0),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE),
        trailingIcon = {
            AnimatedContent(
                targetState = text.isEmpty()
            ) { isEmpty ->
                if(isEmpty) {
                    IconButton(onClick = { onDelete() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_remove),
                            contentDescription = "삭제",
                            modifier = Modifier
                                .size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                } else {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_text_clear),
                            contentDescription = "삭제",
                            modifier = Modifier
                                .size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        },
        placeholder = {
            Text(
                text = placeholderText,
                style = storeMeTextStyle(FontWeight.Normal, 1),
                color = GuideColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = SubHighlightColor,
            unfocusedContainerColor = SubHighlightColor
        )
    )
}

@Composable
fun SelectPeriodItem(
    startLocalDate: LocalDate?,
    startTime: TimeData,
    endLocalDate: LocalDate?,
    endTime: TimeData,
    onStartDateTimeChange: (LocalDate?, TimeData) -> Unit,
    onEndDateTimeChange: (LocalDate?, TimeData) -> Unit
) {
    var showStartBottomSheet by remember { mutableStateOf(false) }
    var showEndBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = DividerColor,
                shape = RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE)
            )
            .clip(RoundedCornerShape(COMPOSABLE_ROUNDING_VALUE))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "기간 설정",
            style = storeMeTextStyle(FontWeight.ExtraBold, 4),
            color = Color.Black
        )

        PeriodDateTimeRow(
            isStart = true,
            localDate = startLocalDate,
            time = startTime
        ) {
            showStartBottomSheet = true
        }

        PeriodDateTimeRow(
            isStart = false,
            localDate = endLocalDate,
            time = endTime
        ) {
            showEndBottomSheet = true
        }
    }

    if(showStartBottomSheet || showEndBottomSheet) {
        DefaultBottomSheet(
            onDismiss = {
                showStartBottomSheet = false
                showEndBottomSheet = false
            },
            sheetState = sheetState
        ) {
            var showTimePicker by remember { mutableStateOf(false) }

            var localDate by remember {
                mutableStateOf(
                    if(showStartBottomSheet)
                        startLocalDate
                    else
                        endLocalDate
                )
            }
            var timeData by remember {
                if(showStartBottomSheet)
                    mutableStateOf(startTime)
                else
                    mutableStateOf(endTime)
            }

            val dateText =
                if(localDate == null) {
                    "날짜를 선택해주세요."
                } else {
                    DateTimeUtils.formatToDate(localDate!!) + " " + DateTimeUtils.DayOfWeek.entries[DateTimeUtils.getWeekDay(localDate!!)].displayName
                }

            StoreMeSelectDateCalendar(
                date = localDate,
                onDateChange = {
                localDate = it
            })

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if(showStartBottomSheet) "시작 시각" else "종료 시각",
                style = storeMeTextStyle(FontWeight.ExtraBold, 4),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "시계 아이콘",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                    )

                    Text(
                        text = dateText,
                        style = storeMeTextStyle(FontWeight.Bold, 2),
                        color = if(localDate == null) GuideColor else Color.Black
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DefaultButton(
                        modifier = Modifier
                            .weight(1f),
                        buttonText = timeData.getText(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(showTimePicker) LighterHighlightColor else SubHighlightColor,
                            contentColor = if(showTimePicker) HighlightColor else Color.Black
                        ),
                        diffValue = 0
                    ) {
                        showTimePicker = !showTimePicker
                    }

                    Text(
                        text = if(showStartBottomSheet) "부터" else "까지",
                        style = storeMeTextStyle(FontWeight.Bold, 2),
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(visible = showTimePicker) {
                StoreMeTimePicker(
                    initHour = timeData.hour,
                    initMinute = timeData.minute,
                    onHourSelected = { timeData = timeData.copy(hour = it) },
                    onMinuteSelected = { timeData = timeData.copy(minute = it) }
                )
            }

            DefaultButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                buttonText = "확인",
                enabled = localDate != null
            ) {
                if(showStartBottomSheet) {
                    onStartDateTimeChange(localDate, timeData)
                } else {
                    onEndDateTimeChange(localDate, timeData)
                }

                showStartBottomSheet = false
                showEndBottomSheet = false
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PeriodDateTimeRow(
    isStart: Boolean,
    localDate: LocalDate?,
    time: TimeData,
    onClick: () -> Unit
) {
    val dateTimeText =
        if(localDate != null)
            DateTimeUtils.convertLocalDateToKorean(localDate = localDate, withDayOfWeek = true) + " " + time.getText()
        else {
            if(isStart)
                "시작 시간을 설정해주세요."
            else
                "종료 시간을 설정해주세요."
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if(isStart) "시작" else "종료",
            style = storeMeTextStyle(FontWeight.ExtraBold, 2),
            color = Color.Black
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = "시계 아이콘",
            tint = Color.Black,
            modifier = Modifier
                .size(20.dp)
        )

        Text(
            text = dateTimeText,
            style = storeMeTextStyle(FontWeight.Bold, 2),
            color = if(localDate == null) GuideColor else Color.Black
        )
    }
}