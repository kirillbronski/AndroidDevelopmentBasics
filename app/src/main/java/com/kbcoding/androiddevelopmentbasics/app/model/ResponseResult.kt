package com.kbcoding.androiddevelopmentbasics.app.model

sealed class ResponseResult<T> {

    /**
     * Convert Result<T> into Result<R>.
     */
    fun <R> map(mapper: ((T) -> R)? = null): ResponseResult<R> {
        return when (this) {
            is Success<T> -> {
                if (mapper == null) {
                    throw IllegalStateException("Can't map Success<T> result without mapper.")
                } else {
                    Success(mapper(this.value))
                }
            }
            is Error<T> -> Error(this.error)
            is Empty<T> -> Empty()
            is Pending<T> -> Pending()
        }
    }

    fun getValueOrNull(): T? {
        if (this is Success<T>) return this.value
        return null
    }

    fun isFinished() = this is Success<T> || this is Error<T>
}

class Success<T>(
    val value: T
) : ResponseResult<T>()

class Error<T>(
    val error: Throwable
) : ResponseResult<T>()

class Empty<T> : ResponseResult<T>()

class Pending<T> : ResponseResult<T>()