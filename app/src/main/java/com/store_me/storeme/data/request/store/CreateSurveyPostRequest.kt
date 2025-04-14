package com.store_me.storeme.data.request.store

import com.store_me.storeme.data.SurveyQuestion

data class CreateSurveyPostRequest(
    val title: String,
    val description: String,
    val contents: List<SurveyQuestion>,
    val startDateTime: String,
    val endDateTime: String
)