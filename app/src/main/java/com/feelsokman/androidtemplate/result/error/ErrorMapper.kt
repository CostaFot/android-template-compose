package com.feelsokman.androidtemplate.result.error

import retrofit2.HttpException

// just a silly error mapper
object ErrorMapper {

    fun map(error: Throwable): GenericError {
        return when (error) {
            is HttpException -> GenericError.HttpError(error.code())
            else -> GenericError.UnknownError(error)
        }
    }
}
