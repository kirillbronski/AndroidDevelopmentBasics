package com.kbcoding.androiddevelopmentbasics.core.result

sealed class Result<T> {

    class Success<T>(val data: T) : Result<T>()

    class Error<T>(val error: Throwable) : Result<T>()

    class Pending<T> : Result<T>()

    class Empty<T> : Result<T>()

    fun <R> map(mapper: (T) -> R): Result<R> {
        if (this is Success) return Success(mapper(data))
        return this as Result<R>
    }

}
