package com.store_me.storeme.utils.exception

import com.google.gson.JsonSyntaxException
import timber.log.Timber
import java.io.IOException

object ApiExceptionHandler {
    fun apiException(code: Int?, message: String?): ApiException {
        val errorMessage = message ?: "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요."
        return ApiException(code, errorMessage)
    }

    fun Exception.toResult(): Result<Nothing> {
        Timber.e(this)

        return when (this) {
            is IOException -> Result.failure(
                apiException(
                    code = null,
                    message = "데이터 또는 와이파이 연결이 원활하지 않습니다. 연결을 확인 후 다시 시도해 주세요."
                )
            )
            is JsonSyntaxException -> Result.failure(
                apiException(
                    code = null,
                    message = "서버가 예상하지 못한 응답을 보냈습니다. 관리자에게 문의하세요."
                )
            )
            else -> Result.failure(
                apiException(
                    code = null,
                    message = "서버와의 통신 중 오류가 발생했습니다. 다시 시도해 주세요."
                )
            )
        }
    }
}