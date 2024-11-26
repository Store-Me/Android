package com.store_me.storeme.repository.storeme

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.net.toFile
import com.google.gson.GsonBuilder
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.network.storeme.UserApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

/**
 * 사용자 관련 UserRepository
 */
interface UserRepository {
    suspend fun customerSignupApp(customerSignupApp: CustomerSignupApp, profileImage: Uri?): Result<Unit>
}

class UserRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userApiService: UserApiService
): UserRepository {
    override suspend fun customerSignupApp(customerSignupApp: CustomerSignupApp, profileImage: Uri?): Result<Unit> {
        fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
            var name: String? = null
            val returnCursor = contentResolver.query(uri, null, null, null, null)
            returnCursor?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex >= 0) {
                    name = cursor.getString(nameIndex)
                }
            }
            return name
        }


        val imagePart = if (profileImage != null) {
            val contentResolver = context.contentResolver

            // 파일 이름 얻기
            val fileName = getFileName(contentResolver, profileImage) ?: "image"

            // 파일의 MIME 타입 얻기
            val mimeType = contentResolver.getType(profileImage) ?: "image/*"

            // InputStream 얻기
            val inputStream = contentResolver.openInputStream(profileImage)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                val requestFile = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
                MultipartBody.Part.createFormData("profileImageFile", fileName, requestFile)
            } else {
                null
            }
        } else {
            null
        }



        return try {

            // API 호출
            val response = userApiService.customerSignupApp(
                customerSignupApp,
                profileImageFile = imagePart
            )

            Log.d("signup", response.message())

            if(response.isSuccessful) {
                when(response.body()?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(Exception(response.message()))
                    }
                    else -> {
                        Result.failure(Exception(response.message()))
                    }
                }
            } else {
                Log.d("signup", response.errorBody()?.string() ?: "")

                Result.failure(Exception("오류가 발생했습니다. ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}