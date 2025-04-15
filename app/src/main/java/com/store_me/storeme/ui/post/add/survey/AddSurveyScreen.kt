package com.store_me.storeme.ui.post.add.survey

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.store_me.storeme.R
import com.store_me.storeme.data.Question
import com.store_me.storeme.data.enums.post.PostType
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultCheckButton
import com.store_me.storeme.ui.component.SimpleTextField
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.post.add.normal.AddPostTopBar
import com.store_me.storeme.ui.post.add.vote.AddOptionButton
import com.store_me.storeme.ui.post.add.vote.OptionTextFieldItem
import com.store_me.storeme.ui.post.add.vote.SelectPeriodItem
import com.store_me.storeme.ui.theme.DividerColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.BACKGROUND_ROUNDING_VALUE
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel

@Composable
fun AddSurveyScreen(
    addSurveyViewModel: AddSurveyViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val snackbarHostState = LocalSnackbarHostState.current
    val loadingViewModel = LocalLoadingViewModel.current

    var showBackWarningDialog by remember{ mutableStateOf(false) }

    val title by addSurveyViewModel.title.collectAsState()
    val description by addSurveyViewModel.description.collectAsState()
    val questions by addSurveyViewModel.questions.collectAsState()
    val startLocalDate by addSurveyViewModel.startLocalDate.collectAsState()
    val startTime by addSurveyViewModel.startTime.collectAsState()
    val endLocalDate by addSurveyViewModel.endLocalDate.collectAsState()
    val endTime by addSurveyViewModel.endTime.collectAsState()
    val isSuccess by addSurveyViewModel.isSuccess.collectAsState()

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
                postType = PostType.SURVEY,
                onClose = { onClose() },
                onFinish = {
                    loadingViewModel.showLoading()
                    addSurveyViewModel.createSurveyPost()
                }
            ) },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement =Arrangement.spacedBy(20.dp)
            ) {
                item {
                    SurveyTitleItem(
                        title = title,
                        onTitleChange = { addSurveyViewModel.updateTitle(it) },
                    )
                }

                item {
                    SurveyDescriptionItem(
                        description = description,
                        onDescriptionChange = { addSurveyViewModel.updateDescription(it) }
                    )
                }

                itemsIndexed(questions) { index, question ->
                    var visible by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        SurveyQuestionItem(
                            question = question,
                            onTitleChange = {
                                addSurveyViewModel.updateQuestionTitle(index = index, title = it)
                            },
                            onChangeToSubjective = {
                                addSurveyViewModel.updateQuestionType(index = index, changeToSubjective = it)
                            },
                            onAddOption = {
                                addSurveyViewModel.addQuestionOption(index = index)
                            },
                            onDeleteOption = {
                                addSurveyViewModel.deleteQuestionOption(questionIndex = index, optionIndex = it)
                            },
                            onChangeOption = { optionIndex, option ->
                                addSurveyViewModel.updateQuestionOption(questionIndex = index, optionIndex = optionIndex, option = option)
                            },
                            onDelete = {
                                addSurveyViewModel.deleteQuestion(index = index)
                            }
                        )
                    }
                }

                item {
                    DefaultButton(
                        buttonText = "질문 추가",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SubHighlightColor,
                            contentColor = Color.Black
                        ),
                        diffValue = 2
                    ) {
                        addSurveyViewModel.addQuestion()
                    }
                }

                item {
                    SelectPeriodItem(
                        startLocalDate = startLocalDate,
                        startTime = startTime,
                        endLocalDate = endLocalDate,
                        endTime = endTime,
                        onStartDateTimeChange = { localDate, timeData -> addSurveyViewModel.updateStartDateTime(localDate, timeData) },
                        onEndDateTimeChange = { localDate, timeData -> addSurveyViewModel.updateEndDateTime(localDate, timeData) }
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

/**
 * 설문의 제목 Composable
 */
@Composable
fun SurveyTitleItem(
    title: String,
    onTitleChange: (String) -> Unit,
) {
    SimpleTextField(
        value = title,
        onValueChange = { onTitleChange(it) },
        placeholderText = "설문의 제목을 입력하세요.",
        singleLine = true
    )
}

/**
 * 설문의 설명 Composable
 */
@Composable
fun SurveyDescriptionItem(
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    SimpleTextField(
        value = description,
        onValueChange = { onDescriptionChange(it) },
        textStyle = storeMeTextStyle(FontWeight.Bold, 0),
        placeholderText = "설문에 대한 간략한 설명을 입력해주세요.",
        singleLine = false
    )
}

/**
 * 설문 각 질문 Composable
 */
@Composable
fun SurveyQuestionItem(
    question: Question,
    onTitleChange: (String) -> Unit,
    onChangeToSubjective: (Boolean) -> Unit, //주관식으로 변경 시 true, 객관식으로 변경 시 false
    onAddOption: () -> Unit,
    onDeleteOption: (Int) -> Unit,
    onChangeOption: (Int, String) -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .border(
                width = 2.dp,
                color = DividerColor,
                shape = RoundedCornerShape(BACKGROUND_ROUNDING_VALUE)
            )
            .clip(RoundedCornerShape(BACKGROUND_ROUNDING_VALUE))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //질문
            Box(
                modifier = Modifier.weight(1f)
            ) {
                SimpleTextField(
                    value = question.title,
                    onValueChange = { onTitleChange(it) },
                    placeholderText = "질문을 입력해주세요.",
                    singleLine = true,
                )
            }

            //질문 삭제 버튼
            IconButton(
                onClick = {
                    onDelete()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "삭제",
                    modifier = Modifier
                        .size(16.dp),
                    tint = Color.Black
                )
            }
        }

        SelectSurveyQuestionType(
            options = question.options
        ) {
            onChangeToSubjective(it)
        }

        if(question.options != null) {
            question.options.forEachIndexed { index, option ->
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
}

@Composable
fun SelectSurveyQuestionType(
    options: List<String>?,
    onChangeToSubjective: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DefaultCheckButton(
            text = "객관식 질문",
            fontWeight = FontWeight.ExtraBold,
            selectedColor = HighlightColor,
            isCheckIconOnLeft = true,
            isSelected = options != null
        ) {
            if(options == null) {
                onChangeToSubjective(false)
            }
        }

        DefaultCheckButton(
            text = "주관식 질문",
            fontWeight = FontWeight.ExtraBold,
            selectedColor = HighlightColor,
            isCheckIconOnLeft = true,
            isSelected = options == null
        ) {
            if(options != null) {
                onChangeToSubjective(true)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}