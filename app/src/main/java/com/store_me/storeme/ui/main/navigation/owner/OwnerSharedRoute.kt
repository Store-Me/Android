package com.store_me.storeme.ui.main.navigation.owner

sealed class OwnerSharedRoute(
    open val path: String,
    open val parent: OwnerSharedRoute? = null
) {
    val fullRoute: String
        get() = parent?.fullRoute?.let { "$it/$path" } ?: path

    data class PostDetail(val postId: String): OwnerSharedRoute("postDetail/$postId") {
        companion object {
            fun template() = "postDetail/{postId}"
        }
    }
}

fun OwnerSharedRoute.PostDetail.withArgs(): String {
    return "postDetail/$postId"
}