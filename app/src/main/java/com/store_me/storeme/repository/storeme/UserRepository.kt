package com.store_me.storeme.repository.storeme

import android.content.Context
import com.google.gson.Gson
import com.store_me.storeme.R
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
import com.store_me.storeme.utils.TokenPreferencesHelper
import com.store_me.storeme.utils.exception.ApiExceptionHandler
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

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
    @Named("UserApiServiceWithoutAuth") private val userApiServiceWithoutAuth: UserApiService,
    @Named("UserApiServiceWithAuth") private val userApiServiceWithAuth: UserApiService
): UserRepository {

    override suspend fun customerSignupApp(customerSignupApp: CustomerSignupApp, profileImage: MultipartBody.Part?): Result<Unit> {
        return try {
            val response = userApiServiceWithoutAuth.customerSignupApp(
                appCustomerSignupRequestDto = customerSignupApp,
                profileImageFile = profileImage
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess){
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun customerSignupKakao(customerSignupKakao: CustomerSignupKakao, profileImage: MultipartBody.Part?): Result<Unit> {
        return try {
            val response = userApiServiceWithoutAuth.customerSignupKakao(
                kakaoCustomerSignupRequestDto = customerSignupKakao,
                profileImageFile = profileImage
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess){
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun ownerSignupApp(ownerSignupApp: OwnerSignupApp, storeProfileImage: MultipartBody.Part?, storeImageList: List<MultipartBody.Part>?): Result<Unit> {
        return try {
            val response = userApiServiceWithoutAuth.ownerSignupApp(
                appOwnerSignupRequestDto = ownerSignupApp,
                storeProfileImageFile = storeProfileImage,
                storeImageFileList = storeImageList,
                storeFeaturedImageFile = null
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    /**
     * 사장님 Kakao 계정 가입
     */
    override suspend fun ownerSignupKakao(ownerSignupKakao: OwnerSignupKakao, storeProfileImage: MultipartBody.Part?, storeImageList: List<MultipartBody.Part>?): Result<Unit> {
        return try {
            val response = userApiServiceWithoutAuth.ownerSignupKakao(
                kakaoOwnerSignupRequestDto = ownerSignupKakao,
                storeProfileImageFile = storeProfileImage,
                storeImageFileList = storeImageList,
                storeFeaturedImageFile = null
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    /**
     * APP 계정 로그인 요청 함수
     */
    override suspend fun loginWithApp(accountId: String, accountPw: String): Result<LoginResponse> {
        return try {
            val response = userApiServiceWithoutAuth.loginWithApp(
                appLoginRequest = AppLoginRequest(accountId = accountId, password = accountPw)
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        TokenPreferencesHelper.saveTokens(
                            refreshToken = responseBody.result.refreshToken,
                            accessToken = responseBody.result.accessToken,
                            expireTime = responseBody.result.expiredTime
                        )

                        Result.success(responseBody.result)
                    }
                    false -> {
                        //로그인 실패
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    /**
     * 카카오 계정 로그인 요청 험수
     */
    override suspend fun loginWithKakao(kakaoId: String): Result<LoginResponse> {
        return try {
            val response = userApiServiceWithoutAuth.loginWithKakao(
                kakaoLoginRequest = KakaoLoginRequest(kakaoId = kakaoId)
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        //로그인 성공

                        //Token 저장
                        TokenPreferencesHelper.saveTokens(
                            refreshToken = responseBody.result.refreshToken,
                            accessToken = responseBody.result.accessToken,
                            expireTime = responseBody.result.expiredTime
                        )

                        Result.success(responseBody.result)
                    }
                    false -> {
                        //로그인 실패
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }
            } else {
                val errorBodyString = response.errorBody()?.string()

                Timber.w(errorBodyString)

                if (errorBodyString != null) {
                    val errorResponse = Gson().fromJson(errorBodyString, StoreMeResponse::class.java)
                    Result.failure(
                        ApiExceptionHandler.apiException(code = errorResponse.code, message = context.getString(R.string.login_kakao_fail_message))
                    )
                } else {
                    Result.failure(
                        ApiExceptionHandler.apiException(code = null, message = context.getString(R.string.default_error_message))
                    )
                }
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun sendSmsMessage(phoneNumber: String): Result<PhoneNumberResponse> {
        return try {
            val response = userApiServiceWithoutAuth.sendSmsMessage(
                phoneNumber = PhoneNumber(phoneNumber = phoneNumber)
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(PhoneNumberResponse(timeLimit = responseBody.result.timeLimit))
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }

            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun confirmVerificationCode(confirmCode: ConfirmCode): Result<Unit> {
        return try {
            // API 호출
            val response = userApiServiceWithoutAuth.confirmVerificationCode(
                confirmCode = confirmCode
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun checkAccountIdDuplicate(accountId: String): Result<Boolean> {
        return try {
            // API 호출
            val response = userApiServiceWithoutAuth.checkAccountIdDuplication(
                accountId = CheckAccountIdDuplicate(accountId = accountId)
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.i(responseBody.toString())

                when(responseBody?.isSuccess) {
                    true -> {
                        Result.success(response.body()?.result?.isDuplicated ?: true)
                    }
                    false -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody.code, message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = responseBody?.code, message = responseBody?.message
                            ))
                    }
                }


            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }
}