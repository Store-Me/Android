package com.store_me.storeme.data.request.store

data class PatchLabelRequest(
    val labels: List<PatchLabelData>
)

data class PatchLabelData(
    val labelId: String?,
    val name: String,
    val order: Int,
)