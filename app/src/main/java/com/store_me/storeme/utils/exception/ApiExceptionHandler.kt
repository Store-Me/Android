package com.store_me.storeme.utils.exception

import android.content.Context
import com.google.gson.JsonSyntaxException
import com.store_me.storeme.R
import timber.log.Timber
import java.io.IOException

object ApiExceptionHandler {
    fun apiException(code: Int?, message: String?): ApiException {
        val errorMessage = message ?: "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요."
        return ApiException(code, errorMessage)
    }

    fun Exception.toResult(context: Context): Result<Nothing> {
        Timber.e(this)

        return when (this) {
            is IOException -> Result.failure(
                apiException(
                    code = null,
                    message = context.getString(R.string.network_error_message)
                )
            )
            is JsonSyntaxException -> Result.failure(
                apiException(
                    code = null,
                    message = context.getString(R.string.json_error_message)
                )
            )
            else -> Result.failure(
                apiException(
                    code = null,
                    message = context.getString(R.string.default_error_message)
                )
            )
        }
    }
}