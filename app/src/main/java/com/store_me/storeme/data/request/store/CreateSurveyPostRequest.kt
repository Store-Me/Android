package com.store_me.storeme.data.request.store

import com.store_me.storeme.data.Question

data class CreateSurveyPostRequest(
    val title: String,
    val description: String,
    val questions: List<Question>,
    val startDateTime: String,
    val endDateTime: String
)