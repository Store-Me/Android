package com.store_me.storeme.utils.response

import com.store_me.storeme.utils.exception.ApiExceptionHandler
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber


object ResponseHandler {
    fun <T> handleErrorResponse(response: Response<T>): Result<Nothing> {
        val statusCode = response.code()
        val errorBodyString = response.errorBody()?.string()
        Timber.w(errorBodyString)

        val errorMessage = try {
            val json = JSONObject(errorBodyString ?: "{}")
            json.optString("message", "서버와의 통신 중 오류가 발생했습니다. 다시 시도해 주세요.")
        } catch (e: Exception) {
            "서버와의 통신 중 오류가 발생했습니다. 다시 시도해 주세요."
        }

        return Result.failure(
            ApiExceptionHandler.apiException(code = statusCode, message = errorMessage)
        )
    }
}