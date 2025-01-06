package com.store_me.storeme.ui.signup.onboarding

import androidx.lifecycle.ViewModel
import com.store_me.storeme.R
import com.store_me.storeme.data.Auth

class SignupOnboardingViewModel: ViewModel() {
    fun getOnboardingImageList(accountType: Auth.AccountType): List<Int> {
        return when(accountType) {
            Auth.AccountType.CUSTOMER -> {
                listOf(R.drawable.onboarding_customer_image_1, R.drawable.onboarding_customer_image_2, R.drawable.onboarding_customer_image_3)
            }
            Auth.AccountType.OWNER -> {
                listOf(R.drawable.onboarding_customer_image_1, R.drawable.onboarding_customer_image_1, R.drawable.onboarding_customer_image_1)
            }
        }
    }

    fun getOnboardingTitleList(accountType: Auth.AccountType): List<String> {
        return when(accountType){
            Auth.AccountType.CUSTOMER -> {
                listOf("내 주변 모든 가게, 내 손안에", "편리한 예약과 실시간 문의", "쿠폰과 스탬프로 합리적인 소비")
            }
            Auth.AccountType.OWNER -> {
                listOf("한 곳에 모아 보여주는 내 가게 정보", "손쉬운 손님과의 소통과 마케팅", "맞춤형 쿠폰으로 고객 만족도 UP")
            }
        }
    }

    fun getOnboardingContentList(accountType: Auth.AccountType): List<String> {
        return when(accountType){
            Auth.AccountType.CUSTOMER -> {
                listOf(
                    "내 주변의 신규 가게, 이벤트 중인 가게를 스토어미로 쉽게 발견하세요!\n다양한 가게 정보부터 쿠폰까지 한 눈에 확인할 수 있어요!",
                    "원하는 시간에 예약하고, 사장님께 궁금한 점을 바로 문의하세요.\n1대1 채팅으로 즉각정인 소통이 가능해요!",
                    "종이 쿠폰 대신 스토어미의 모바일 쿠폰과 가게만의 스탬프로 단골 손님이 되어 편리하게 관리하고 사용해보세요."
                )
            }
            Auth.AccountType.OWNER -> {
                listOf(
                    "여러 사이트에 흩어진 가게 정보를 스토어미가 모아드립니다.\n우리 가게만의 특별한 모바일 페이지로 효과적인 홍보를 경험하세요!",
                    "우리 가게만의 이벤트 진행, 설문조사, 1:1 대화, 단골 관리까지 스토어미 하나로 해결하세요.\n스토어미를 통해 손님과의 소통을 효율적으로 진행해보세요!",
                    "스탬프 쿠폰, 할인 쿠폰을 간단하게 생성할 수 있어요!\n우리 가게만의 차별화된 혜택으로 단골 손님을 모아보세요!"
                )
            }
        }
    }
}