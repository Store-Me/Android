package com.store_me.storeme.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object FileUtils {
    /**
     * URI를 MultipartBody.Part로 변환하는 함수
     * @param uri URI of the file
     * @param context Android context to resolve the URI
     * @param key Multipart form key
     * @return MultipartBody.Part
     */
    fun createMultipart(context: Context, uri: Uri, key: String): MultipartBody.Part? {
        val contentResolver = context.contentResolver

        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val mimeType = contentResolver.getType(uri)

        inputStream.use {
            val fileName = "${key}_${System.currentTimeMillis()}"

            val requestBody = it.readBytes().toRequestBody("$mimeType".toMediaTypeOrNull())

            Log.d("MIME Type", mimeType.toString())

            return MultipartBody.Part.createFormData(key, fileName, requestBody)
        }
    }

    fun createMultipart(context: Context, uris: List<Uri>, key: String): List<MultipartBody.Part>? {
        val contentResolver = context.contentResolver

        val parts = mutableListOf<MultipartBody.Part>()

        uris.forEachIndexed { index, uri ->
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val mimeType = contentResolver.getType(uri)

            Log.d("MIME Type", mimeType.toString())

            inputStream.use {
                val fileName = "${key}_${index}_${System.currentTimeMillis()}"

                val requestBody = inputStream.readBytes().toRequestBody("$mimeType".toMediaTypeOrNull())

                parts.add(MultipartBody.Part.createFormData(key, fileName, requestBody))
            }
        }
        return parts
    }
}