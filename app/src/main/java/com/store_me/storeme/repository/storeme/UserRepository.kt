package com.store_me.storeme.repository.storeme

import androidx.compose.ui.res.stringResource
import com.store_me.storeme.R
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
                        Result.failure(Exception(response.message()))
                    }
                    else -> {
                        Result.failure(Exception(response.message()))
                    }
                }
            } else {
                Result.failure(Exception("오류가 발생했습니다. ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}