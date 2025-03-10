package com.store_me.storeme.utils.response

import android.content.Context
import com.store_me.storeme.R
import com.store_me.storeme.utils.exception.ApiExceptionHandler
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber


object ResponseHandler {
    fun <T> handleErrorResponse(response: Response<T>, context: Context): Result<Nothing> {
        val statusCode = response.code()
        val errorBodyString = response.errorBody()?.string()
        Timber.w(errorBodyString)

        val errorMessage = try {
            val json = JSONObject(errorBodyString ?: "{}")
            json.optString("message", context.getString(R.string.default_error_message))
        } catch (e: Exception) {
            context.getString(R.string.default_error_message)
        }

        return Result.failure(
            ApiExceptionHandler.apiException(code = statusCode, message = errorMessage)
        )
    }
}