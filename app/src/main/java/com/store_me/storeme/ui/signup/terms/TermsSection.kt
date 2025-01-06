package com.store_me.storeme.ui.signup.terms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.ui.component.DefaultHorizontalDivider
import com.store_me.storeme.ui.signup.NextButton
import com.store_me.storeme.ui.theme.DeleteTextColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.signup.LocalTermsViewModel

@Composable
fun TermsSection(onFinish: () -> Unit) {
    val termsViewModel = LocalTermsViewModel.current

    val requiredTerms by termsViewModel.requiredTermsState.collectAsState()
    val optionalTerms by termsViewModel.optionalTermsState.collectAsState()

    val isAllTermsAgreed by termsViewModel.isAllTermsAgreed.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {

        item {
            Text(
                text = "서비스 이용을 위한\n약관에 동의해주세요.",
                style = storeMeTextStyle(FontWeight.ExtraBold, 6),
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)
            )
        }

        item { DefaultHorizontalDivider() }

        //전체 동의
        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "전체 동의",
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = if(isAllTermsAgreed) R.drawable.ic_check_on else R.drawable.ic_check_off),
                    contentDescription = "체크",
                    tint = if(isAllTermsAgreed) Color.Black else DeleteTextColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            onClick = {
                                if (isAllTermsAgreed) {
                                    termsViewModel.updateAllTerms(false)
                                } else {
                                    termsViewModel.updateAllTerms(true)
                                }
                            },
                            indication = null,
                            interactionSource = null
                        )
                )

            }
        }

        item { TermItem("서비스 이용 약관 (필수)", stringResource(id = R.string.example_terms), requiredTerms[RequiredTerms.USE] ?: false) {
            termsViewModel.updateRequiredTerms(RequiredTerms.USE)
        } }

        item { TermItem("개인정보 이용 약관 (필수)", stringResource(id = R.string.example_terms), requiredTerms[RequiredTerms.PRIVACY] ?: false) {
            termsViewModel.updateRequiredTerms(RequiredTerms.PRIVACY)
        } }

        item { TermItem("마케팅 활용 정보 동의 약관 (선택)", stringResource(id = R.string.example_terms), optionalTerms[OptionalTerms.MARKETING] ?: false) {
            termsViewModel.updateOptionalTerms(OptionalTerms.MARKETING)
        } }

        item {
            NextButton(
                buttonText = "다음",
                modifier = Modifier
                    .padding(top = 48.dp)
                    .padding(horizontal = 20.dp),
                enabled = requiredTerms.all { it.value }
            ) {
                onFinish()
            }
        }
    }
}

@Composable
fun TermItem(title: String, content: String, isChecked: Boolean, onFinish: () -> Unit) {
    val isFolded = remember { mutableStateOf(true) }

    fun onClickTitle() {
        isFolded.value = !isFolded.value
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                color = Color.Black,
                modifier = Modifier
                    .clickable(
                        onClick = { onClickTitle() }
                    )
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(id = if(isFolded.value) R.drawable.ic_arrow_right else R.drawable.ic_arrow_down),
                contentDescription = "화살표",
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        onClick = { onClickTitle() },
                        indication = ripple(bounded = false),
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = if(isChecked) R.drawable.ic_check_on else R.drawable.ic_check_off),
                contentDescription = "체크",
                tint = if(isChecked) Color.Black else DeleteTextColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = {
                            onFinish()
                            isFolded.value = true
                        },
                        indication = null,
                        interactionSource = null
                    )
            )
        }

        AnimatedVisibility(visible = !isFolded.value) {
            Text(
                text = content,
                style = storeMeTextStyle(FontWeight.Normal, 0),
                modifier = Modifier
                    .padding(top = 4.dp, end = 24.dp)
            )
        }
    }
}