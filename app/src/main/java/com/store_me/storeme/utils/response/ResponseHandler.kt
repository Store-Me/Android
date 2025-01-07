package com.store_me.storeme.utils.response

import android.content.Context
import com.google.gson.Gson
import com.store_me.storeme.R
import com.store_me.storeme.data.response.StoreMeResponse
import com.store_me.storeme.utils.exception.ApiExceptionHandler
import retrofit2.Response
import timber.log.Timber


object ResponseHandler {
    fun <T> handleErrorResponse(response: Response<T>, context: Context): Result<Nothing> {
        val errorBodyString = response.errorBody()?.string()
        Timber.w(errorBodyString)

        return if (errorBodyString != null) {
            val errorResponse = Gson().fromJson(errorBodyString, StoreMeResponse::class.java)
            Result.failure(
                ApiExceptionHandler.apiException(code = errorResponse.code, message = errorResponse.message)
            )
        } else {
            Result.failure(
                ApiExceptionHandler.apiException(code = null, message = context.getString(R.string.default_error_message))
            )
        }
    }
}