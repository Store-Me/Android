package com.store_me.storeme.data.request.store

data class CreateVotePostRequest(
    val title: String,
    val options: List<String>,
    val startDateTime: String,
    val endDateTime: String
)
