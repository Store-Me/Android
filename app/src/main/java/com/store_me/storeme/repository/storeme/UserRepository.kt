package com.store_me.storeme.repository.storeme

import android.app.Activity
import android.content.Context
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import com.store_me.storeme.R
import com.store_me.storeme.data.model.check_duplicate.CheckAccountIdDuplicate
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.data.request.login.AppLoginRequest
import com.store_me.storeme.data.request.login.KakaoLoginRequest
import com.store_me.storeme.data.response.LoginResponse
import com.store_me.storeme.data.model.signup.CustomerSignupApp
import com.store_me.storeme.data.model.signup.CustomerSignupKakao
import com.store_me.storeme.data.model.signup.CustomerSignupRequest
import com.store_me.storeme.data.model.signup.OwnerSignupApp
import com.store_me.storeme.data.model.signup.OwnerSignupKakao
import com.store_me.storeme.data.model.verification.ConfirmCode
import com.store_me.storeme.data.model.verification.PhoneNumber
import com.store_me.storeme.data.model.verification.PhoneNumberResponse
import com.store_me.storeme.data.request.login.LoginRequest
import com.store_me.storeme.network.storeme.UserApiService
import com.store_me.storeme.utils.preference.TokenPreferencesHelper
import com.store_me.storeme.utils.exception.ApiExceptionHandler
import com.store_me.storeme.utils.exception.ApiExceptionHandler.toResult
import com.store_me.storeme.utils.response.ResponseHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 전체 사용자 관련 UserRepository
 */
interface UserRepository {
    //회원가입
    suspend fun customerSignup(customerSignupRequest: CustomerSignupRequest): Result<Unit>

    suspend fun ownerSignupApp(ownerSignupApp: OwnerSignupApp, storeProfileImage: MultipartBody.Part?, storeImageList: List<MultipartBody.Part>?): Result<Unit>

    suspend fun ownerSignupKakao(ownerSignupKakao: OwnerSignupKakao, storeProfileImage: MultipartBody.Part?, storeImageList: List<MultipartBody.Part>?): Result<Unit>

    //로그인
    suspend fun login(loginRequest: LoginRequest): Result<Unit>

    //인증 번호 전송
    suspend fun sendSmsMessage(phoneNumber: String, activity: Activity): Result<String?>

    //인증코드 확인
    suspend fun confirmVerificationCode(verificationId: String, verificationCode: String): Result<Boolean>

    //아이디 중복 확인
    suspend fun checkAccountIdDuplicate(accountId: String): Result<Boolean>

    //회원 탈퇴
    suspend fun deleteUser(): Result<Unit>
}

class UserRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("UserApiServiceWithoutAuth") private val userApiServiceWithoutAuth: UserApiService,
    @Named("UserApiServiceWithAuth") private val userApiServiceWithAuth: UserApiService
): UserRepository {
    override suspend fun customerSignup(customerSignupRequest: CustomerSignupRequest): Result<Unit> {
        return try {
            val response = userApiServiceWithoutAuth.customerSignup(
                customerSignupRequest = customerSignupRequest
            )

            if(response.isSuccessful) {
                Timber.d("가입 성공")

                Result.success(Unit)
            } else {
                Timber.d("가입 실패")

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
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
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
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
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

    override suspend fun login(loginRequest: LoginRequest): Result<Unit> {
        return try {
            val response = userApiServiceWithoutAuth.login(
                loginRequest = loginRequest
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d(responseBody.toString())

                if (responseBody != null) {
                    TokenPreferencesHelper.saveTokens(
                        refreshToken = responseBody.refreshToken,
                        accessToken = responseBody.accessToken,
                    )

                    Result.success(Unit)
                } else {
                    ResponseHandler.handleErrorResponse(response, context)
                }
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun sendSmsMessage(phoneNumber: String, activity: Activity): Result<String?> {
        return suspendCoroutine { continuation ->
            val auth = FirebaseAuth.getInstance()

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(120L, TimeUnit.SECONDS) //제한 시간 120초
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        Timber.d("자동 완료")

                        continuation.resume(Result.success(null))
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Timber.d("인증 실패")

                        continuation.resume(Result.failure(ApiExceptionHandler.apiException(code = null, message = e.message)))
                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        Timber.d("메시지 전송 완료.")

                        continuation.resume(Result.success(verificationId))
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override suspend fun confirmVerificationCode(verificationId: String, verificationCode: String): Result<Boolean> {
        return suspendCoroutine { continuation ->
            val credential = PhoneAuthProvider.getCredential(verificationId, verificationCode)

            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d("인증 성공")
                        continuation.resume(Result.success(true))
                    } else {
                        Timber.e("인증 실패: ${task.exception?.message}")
                        continuation.resume(Result.success(false))
                    }
                }
        }
    }

    override suspend fun checkAccountIdDuplicate(accountId: String): Result<Boolean> {
        return try {
            val response = userApiServiceWithoutAuth.checkAccountIdDuplication(
                accountId = accountId
            )

            if(response.isSuccessful) {
                val responseBody = response.body()

                Timber.d("중복 확인 결과: ${responseBody.toString()}")

                Result.success(responseBody?.isDuplicate ?: true)
            } else {
                ResponseHandler.handleErrorResponse(response, context)
            }
        } catch (e: Exception) {
            e.toResult(context)
        }
    }

    override suspend fun deleteUser(): Result<Unit> {
        return try {
            // API 호출
            val response = userApiServiceWithAuth.deleteUser()

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
                                code = response.code(), message = responseBody.message
                            ))
                    }
                    else -> {
                        Result.failure(
                            ApiExceptionHandler.apiException(
                                code = response.code(), message = responseBody?.message
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