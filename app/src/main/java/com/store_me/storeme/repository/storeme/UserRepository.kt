package com.store_me.storeme.repository.storeme

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.google.gson.Gson
import com.store_me.storeme.data.check_duplicate.CheckAccountIdDuplicate
import com.store_me.storeme.data.model.StoreMeResponse
import com.store_me.storeme.data.model.login.KakaoLoginRequest
import com.store_me.storeme.data.model.login.LoginResponse
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.verification.ConfirmCode
import com.store_me.storeme.data.model.verification.PhoneNumber
import com.store_me.storeme.data.model.verification.PhoneNumberResponse
import com.store_me.storeme.network.storeme.UserApiService
import com.store_me.storeme.utils.ApiException
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * 사용자 관련 UserRepository
 */
interface UserRepository {
    //회원가입
    suspend fun customerSignupApp(customerSignupApp: CustomerSignupApp, profileImage: Uri?): Result<Unit>

    //로그인
    suspend fun loginWithKakao(kakaoId: String): Result<LoginResponse>

    //인증 번호 전송
    suspend fun sendSmsMessage(phoneNumber: String): Result<PhoneNumberResponse>

    //인증코드 확인
    suspend fun confirmVerificationCode(confirmCode: ConfirmCode): Result<Unit>

    //아이디 중복 확인
    suspend fun checkAccountIdDuplicate(accountId: String): Result<Boolean>
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

    override suspend fun loginWithKakao(kakaoId: String): Result<LoginResponse> {
        return try {
            // API 호출
            val response = userApiService.loginWithKakao(
                kakaoLoginRequest = KakaoLoginRequest(kakaoId = kakaoId)
            )

            if(response.isSuccessful) {
                Log.d("loginWithKakao", response.message())
                val responseBody = response.body()

                when(responseBody?.isSuccess) {
                    true -> {
                        if(responseBody.result != null){
                            Result.success(responseBody.result)
                        } else {
                            Result.failure(ApiException(code = null ,message = "알 수 없는 오류 발생"))
                        }
                    }
                    false -> {
                        Result.failure(ApiException(code = responseBody.code, message = responseBody.message))
                    }
                    else -> {
                        Result.failure(ApiException(code = responseBody?.code, message = responseBody?.message))
                    }
                }
            } else {
                val errorBodyString = response.errorBody()?.string()
                if (errorBodyString != null) {
                    // 서버가 실패 시에도 StoreMeResponse<T> 형식으로 응답한다고 가정
                    val errorResponse = Gson().fromJson(errorBodyString, StoreMeResponse::class.java)
                    Result.failure(ApiException(code = errorResponse.code, message = errorResponse.message))
                } else {
                    Result.failure(ApiException(code = response.code().toString(), message = response.message()))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendSmsMessage(phoneNumber: String): Result<PhoneNumberResponse> {
        return try {
            // API 호출
            val response = userApiService.sendSmsMessage(
                phoneNumber = PhoneNumber(phoneNumber = phoneNumber)
            )

            if(response.isSuccessful) {
                Log.d("sendSmsMessage", response.message())
                val responseBody = response.body()

                Result.success(PhoneNumberResponse(timeLimit = responseBody?.result?.timeLimit ?: 300))
            } else {
                val errorBodyString = response.errorBody()?.string()
                if (errorBodyString != null) {
                    // 서버가 실패 시에도 StoreMeResponse<T> 형식으로 응답한다고 가정
                    val errorResponse = Gson().fromJson(errorBodyString, StoreMeResponse::class.java)
                    Result.failure(ApiException(code = errorResponse.code, message = errorResponse.message))
                } else {
                    Result.failure(ApiException(code = response.code().toString(), message = response.message()))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmVerificationCode(confirmCode: ConfirmCode): Result<Unit> {
        return try {
            // API 호출
            val response = userApiService.confirmVerificationCode(confirmCode = confirmCode)

            if(response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBodyString = response.errorBody()?.string()

                Log.d("confirmCode", errorBodyString.toString())

                if (errorBodyString != null) {
                    val errorResponse = Gson().fromJson(errorBodyString, StoreMeResponse::class.java)
                    Result.failure(ApiException(code = errorResponse.code, message = errorResponse.message))
                } else {
                    Result.failure(ApiException(code = response.code().toString(), message = response.message()))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAccountIdDuplicate(accountId: String): Result<Boolean> {
        return try {
            // API 호출
            val response = userApiService.checkAccountIdDuplication(accountId = CheckAccountIdDuplicate(accountId = accountId))

            if(response.isSuccessful) {
                Result.success(response.body()?.result?.isDuplicated ?: true)
            } else {
                val errorBodyString = response.errorBody()?.string()

                Log.d("checkAccountIdDuplicate", errorBodyString.toString())

                if (errorBodyString != null) {
                    val errorResponse = Gson().fromJson(errorBodyString, StoreMeResponse::class.java)
                    Result.failure(ApiException(code = errorResponse.code, message = errorResponse.message))
                } else {
                    Result.failure(ApiException(code = response.code().toString(), message = response.message()))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}