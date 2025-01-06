package com.store_me.storeme.repository.storeme

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.store_me.storeme.data.model.check_duplicate.CheckAccountIdDuplicate
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.request.AppLoginRequest
import com.store_me.storeme.data.request.KakaoLoginRequest
import com.store_me.storeme.data.response.LoginResponse
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.signup.CustomerSignupKakao
import com.store_me.storeme.data.model.signup.OwnerSignupApp
import com.store_me.storeme.data.model.signup.OwnerSignupKakao
import com.store_me.storeme.data.model.verification.ConfirmCode
import com.store_me.storeme.data.model.verification.PhoneNumber
import com.store_me.storeme.data.model.verification.PhoneNumberResponse
import com.store_me.storeme.network.storeme.UserApiService
import com.store_me.storeme.utils.ApiException
import com.store_me.storeme.utils.TokenPreferencesHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * 전체 사용자 관련 UserRepository
 */
interface UserRepository {
    //회원가입
    suspend fun customerSignupApp(customerSignupApp: CustomerSignupApp, profileImage: MultipartBody.Part?): Result<Unit>

    suspend fun customerSignupKakao(customerSignupKakao: CustomerSignupKakao, profileImage: MultipartBody.Part?): Result<Unit>

    suspend fun ownerSignupApp(ownerSignupApp: OwnerSignupApp, storeProfileImage: MultipartBody.Part?, storeImageList: List<MultipartBody.Part>?): Result<Unit>

    suspend fun ownerSignupKakao(ownerSignupKakao: OwnerSignupKakao, storeProfileImage: MultipartBody.Part?, storeImageList: List<MultipartBody.Part>?): Result<Unit>

    //로그인
    suspend fun loginWithApp(accountId: String, accountPw: String): Result<LoginResponse>
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

    override suspend fun customerSignupApp(customerSignupApp: CustomerSignupApp, profileImage: MultipartBody.Part?): Result<Unit> {
        return try {
            val response = userApiService.customerSignupApp(
                appCustomerSignupRequestDto = customerSignupApp,
                profileImageFile = profileImage
            )

            Log.d("customerSignupApp", response.message())

            if(response.isSuccessful) {
                when(response.body()?.isSuccess){
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
                Log.d("customerSignupApp", response.errorBody()?.string() ?: "")

                Result.failure(Exception("오류가 발생했습니다. ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.d("customerSignupApp", e.message.toString())

            Result.failure(e)
        }
    }

    override suspend fun customerSignupKakao(customerSignupKakao: CustomerSignupKakao, profileImage: MultipartBody.Part?): Result<Unit> {
        return try {
            val response = userApiService.customerSignupKakao(
                kakaoCustomerSignupRequestDto = customerSignupKakao,
                profileImageFile = profileImage
            )

            Log.d("customerSignupKakao", response.message())

            if(response.isSuccessful) {
                when(response.body()?.isSuccess){
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
                Log.d("customerSignupKakao", response.errorBody()?.string() ?: "")

                Result.failure(Exception("오류가 발생했습니다. ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.d("customerSignupKakao", e.message.toString())

            Result.failure(e)
        }
    }

    override suspend fun ownerSignupApp(ownerSignupApp: OwnerSignupApp, storeProfileImage: MultipartBody.Part?, storeImageList: List<MultipartBody.Part>?): Result<Unit> {
        return try {
            val response = userApiService.ownerSignupApp(
                appOwnerSignupRequestDto = ownerSignupApp,
                storeProfileImageFile = storeProfileImage,
                storeImageFileList = storeImageList,
                storeFeaturedImageFile = null
            )

            Log.d("ownerSignupApp", response.message())
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
                Log.d("ownerSignupApp", response.errorBody()?.string() ?: "")

                Result.failure(Exception("오류가 발생했습니다. ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.d("ownerSignupApp", e.message.toString())

            Result.failure(e)
        }
    }


    override suspend fun ownerSignupKakao(ownerSignupKakao: OwnerSignupKakao, storeProfileImage: MultipartBody.Part?, storeImageList: List<MultipartBody.Part>?): Result<Unit> {
        return try {
            // API 호출
            val response = userApiService.ownerSignupKakao(
                kakaoOwnerSignupRequestDto = ownerSignupKakao,
                storeProfileImageFile = storeProfileImage,
                storeImageFileList = storeImageList,
                storeFeaturedImageFile = null
            )

            Log.d("ownerSignupKakao", response.message())

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
                Log.d("ownerSignupKakao", response.errorBody()?.string() ?: "")

                Result.failure(Exception("오류가 발생했습니다. ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.d("ownerSignupKakao", e.message.toString())

            Result.failure(e)
        }
    }

    override suspend fun loginWithApp(accountId: String, accountPw: String): Result<LoginResponse> {
        return try {
            // API 호출
            val response = userApiService.loginWithApp(
                appLoginRequest = AppLoginRequest(
                    accountId = accountId,
                    password = accountPw
                )
            )

            if(response.isSuccessful) {
                Log.d("loginWithApp", response.body().toString())
                val responseBody = response.body()

                when(responseBody?.isSuccess) {
                    true -> {
                        if(responseBody.result != null){
                            TokenPreferencesHelper.saveTokens(
                                refreshToken = responseBody.result.refreshToken,
                                accessToken = responseBody.result.accessToken,
                                expireTime = responseBody.result.expiredTime
                            )

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

                Log.d("loginWithApp", errorBodyString.toString())
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

    override suspend fun loginWithKakao(kakaoId: String): Result<LoginResponse> {
        return try {
            // API 호출
            val response = userApiService.loginWithKakao(
                kakaoLoginRequest = KakaoLoginRequest(kakaoId = kakaoId)
            )

            if(response.isSuccessful) {
                Log.d("loginWithKakao", response.body().toString())
                val responseBody = response.body()

                when(responseBody?.isSuccess) {
                    true -> {
                        if(responseBody.result != null){
                            TokenPreferencesHelper.saveTokens(
                                refreshToken = responseBody.result.refreshToken,
                                accessToken = responseBody.result.accessToken,
                                expireTime = responseBody.result.expiredTime
                            )

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

                Log.d("loginWithKakao", errorBodyString.toString())
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

            Log.d("ConfirmCode", response.body().toString())

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
                Log.d("checkAccountIdDuplicatae", response.body().toString())
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