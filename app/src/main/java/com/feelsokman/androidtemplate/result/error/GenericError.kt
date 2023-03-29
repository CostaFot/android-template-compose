package com.feelsokman.androidtemplate.result.error

sealed class GenericError {
    data class HttpError(val code: Int) : GenericError()
    data class UnknownError(val throwable: Throwable) : GenericError()
}
