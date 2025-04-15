package com.store_me.storeme.data

data class SurveyData(
    val title: String,
    val description: String,
    val questions: List<Question>,
)

data class Question(
    val title: String,
    val options: List<String>?
)