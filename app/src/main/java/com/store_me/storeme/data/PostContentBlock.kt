package com.store_me.storeme.data

import android.net.Uri
import com.mohamedrejeb.richeditor.model.RichTextState
import java.util.UUID

sealed class PostContentBlock(open val id: String = UUID.randomUUID().toString()) {
    data class TextBlock(
        val state: RichTextState,
        override val id: String = UUID.randomUUID().toString(),
        val name: String = "Text",
    ) : PostContentBlock(id = id)

    data class ImageBlock(
        val uri: Uri,
        val url: String,
        val progress: Float = 0.0f,
        override val id: String = UUID.randomUUID().toString(),
        val name: String = "Image",
    ) : PostContentBlock(id = id)

    data class EmojiBlock(
        val url: String,
        override val id: String = UUID.randomUUID().toString(),
        val name: String = "Emoji",
    ) : PostContentBlock(id = id)
}
