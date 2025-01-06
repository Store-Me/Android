package com.store_me.storeme.ui.signup.account_data

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth
import com.store_me.storeme.ui.signup.SignupTitleText
import com.store_me.storeme.ui.theme.SignupTextBoxColor
import com.store_me.storeme.ui.theme.storeMeTextStyle

@Composable
fun AccountTypeSection(onFinish: (Auth.AccountType) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        item {
            SignupTitleText(title = "서비스 이용 유형을\n선택해주세요.")
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "설정에서 추후 자유롭게",
                    style = storeMeTextStyle(FontWeight.Normal, 2)
                )

                Row(
                ) {
                    Text(
                        text = "사장님/손님",
                        style = storeMeTextStyle(FontWeight.ExtraBold, 2)
                    )

                    Text(
                        text = " 추가 및 전환이 가능합니다.",
                        style = storeMeTextStyle(FontWeight.Normal, 2)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AccountTypeBox(accountType = Auth.AccountType.CUSTOMER) {
                    onFinish(Auth.AccountType.CUSTOMER)
                }

                AccountTypeBox(accountType = Auth.AccountType.OWNER) {
                    onFinish(Auth.AccountType.OWNER)
                }
            }
        }
    }
}

@Composable
fun AccountTypeBox(accountType: Auth.AccountType, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .background(color = SignupTextBoxColor, shape = RoundedCornerShape(14.dp))
            .clip(shape = RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when(accountType) {
                        Auth.AccountType.OWNER -> { "사장님으로 시작하기" }
                        Auth.AccountType.CUSTOMER -> { "손님으로 시작하기" }
                    },
                    style = storeMeTextStyle(FontWeight.ExtraBold, 2),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "화살표 아이콘",
                    modifier = Modifier
                        .size(12.dp),
                    tint = Color.Black
                )
            }

            Text(
                text = when(accountType) {
                    Auth.AccountType.OWNER -> { "소식 및 이벤트로 내 가게 홍보하기" }
                    Auth.AccountType.CUSTOMER -> { "내 주변 가게 소식 및 이벤트 모아보기" }
                },
                style = storeMeTextStyle(FontWeight.Normal, 1)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painterResource(
                id = when(accountType) {
                    Auth.AccountType.OWNER -> { R.drawable.ic_signup_owner }
                    Auth.AccountType.CUSTOMER -> { R.drawable.ic_signup_customer }
                }
            ),
            contentDescription = "계정 타입 아이콘",
            modifier = Modifier
                .size(36.dp),
            tint = Color.Unspecified
        )
    }
}