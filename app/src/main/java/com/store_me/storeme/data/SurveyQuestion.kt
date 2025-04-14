package com.store_me.storeme.data

data class SurveyQuestion(
    val type: String,
    val question: String,
    val options: List<String>
)