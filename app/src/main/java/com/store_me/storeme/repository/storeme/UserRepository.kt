package com.store_me.storeme.repository.storeme

import com.store_me.storeme.data.model.StoreMeResponse
import com.store_me.storeme.network.storeme.UserApiService
import javax.inject.Inject

/**
 * 사용자 관련 UserRepository
 */
interface UserRepository {
    suspend fun signupApp(): Result<Unit>
}

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
): UserRepository {
    override suspend fun signupApp(): Result<Unit> {
        return try {
            val response = userApiService.signupApp()

            if(response.isSuccessful) {
                when(response.body()?.isSuccess) {
                    true -> {
                        Result.success(Unit)
                    }
                    false -> {
                        Result.failure(Exception(response.message() ?: "오류가 발생했습니다. 다시 시도해주세요."))
                    }
                    else -> {
                        Result.failure(Exception(response.message() ?: "오류가 발생했습니다. 다시 시도해주세요."))
                    }
                }
            } else {
                Result.failure(Exception("오류가 발생했습니다. ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

        /*return try {
            val response = locationApiService.getLocationList()
            if(response.isSuccessful) {
                //API 호출 성공
                val data = response.body()

                if (data != null) {
                    //데이터 있는 경우
                    Result.success(data)
                } else {
                    //데이터 없는 경우
                    val errorBody = response.errorBody()?.string()

                    val errorResponse = ErrorParser.setErrorResponse(errorBody)

                    Result.failure(ApiException(errorResponse = errorResponse, ApiNames.LOCATIONS.apiName))
                }
            } else {
                //API 호출 실패
                val errorBody = response.errorBody()?.string()

                val errorResponse = ErrorParser.setErrorResponse(errorBody)

                Result.failure(ApiException(errorResponse = errorResponse, ApiNames.LOCATIONS.apiName))
            }
        } catch (e: Exception) {
            //네트워크 오류 혹은 예외 발생
            Result.failure(ApiException(errorResponse = ErrorResponse(null, null), ApiNames.LOCATIONS.apiName))
        }*/
    }

}