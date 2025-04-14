package com.store_me.storeme.ui.post.add.survey

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.store_me.storeme.R
import com.store_me.storeme.data.SurveyData
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.DefaultToggleButton
import com.store_me.storeme.ui.component.SimpleOutLinedTextField
import com.store_me.storeme.ui.component.StoreMeSnackbar
import com.store_me.storeme.ui.theme.DisabledColor
import com.store_me.storeme.ui.theme.DividerColor
import com.store_me.storeme.ui.theme.ErrorTextFieldColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.HighlightTextFieldColor
import com.store_me.storeme.ui.theme.SubHighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.LocalSnackbarHostState

@Composable
fun AddSurveyScreen(
    addSurveyViewModel: AddSurveyViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = LocalSnackbarHostState.current

    val surveys by addSurveyViewModel.surveys.collectAsState()

    Scaffold(
        topBar = {

        },
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { StoreMeSnackbar(snackbarData = it) }
        ) },
        content = { innerPadding ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(surveys) { index, survey ->
                    var visible by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        visible = true
                    }

                    AnimatedVisibility(visible) {
                        SurveyItem(
                            survey = survey,
                            onTitleChange = {
                                addSurveyViewModel.updateSurveyTitle(index = index, title = it)
                            },
                            onSurveyTypeChange = {
                                addSurveyViewModel.clearSurveyItems(index = index)
                            },
                            onItemChange = { itemIndex, item ->
                                addSurveyViewModel.updateSurveyItem(index = index, item = item, itemIndex = itemIndex)
                            },
                            onAddChoice = {
                                addSurveyViewModel.addSurveyItem(index)
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
                        addSurveyViewModel.addSurvey(SurveyData(title = "", items = emptyList()))
                    }
                }
            }
        }
    )
}

@Composable
fun SurveyItem(
    survey: SurveyData,
    onTitleChange: (String) -> Unit,
    onSurveyTypeChange: () -> Unit,
    onItemChange: (Int, String) -> Unit,
    onAddChoice: () -> Unit
) {
    var isMultipleChoice by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .border(width = 2.dp, color = DividerColor, shape = RoundedCornerShape(14.dp))
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SurveyTitle(
                modifier = Modifier
                    .weight(1f),
                title = survey.title
            ) { onTitleChange(it) }

            DefaultToggleButton(
                buttonText = "주관식",
                isSelected = !isMultipleChoice
            ) {
                isMultipleChoice = !isMultipleChoice
                onSurveyTypeChange()
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        survey.items.forEachIndexed { index, item ->
            SimpleOutLinedTextField(
                text = item,
                onValueChange = {
                    onItemChange(index, it)
                },
                placeholderText = "항목을 입력하세요.",
                isError = false,
                errorText = "",
                colors =  OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = SubHighlightColor,
                    unfocusedContainerColor = SubHighlightColor
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if(isMultipleChoice) {
            DefaultButton(
                buttonText = "항목 추가",
                leftIconResource = R.drawable.ic_plus,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = DisabledColor
                ),
                leftIconTint = DisabledColor,
                modifier = Modifier
                    .border(color = DisabledColor, width = 1.dp, shape = RoundedCornerShape(30))
            ) {
                onAddChoice()
            }
        }
    }
}

@Composable
fun SurveyTitle(
    modifier: Modifier,
    title: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = title,
            onValueChange = { onValueChange(it) },
            textStyle = storeMeTextStyle(FontWeight.Bold, 4),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        if (title.isEmpty()) {
            Text(
                text = "질문을 입력하세요.",
                style = storeMeTextStyle(FontWeight.Bold, 4),
                color = GuideColor
            )
        }
    }
}