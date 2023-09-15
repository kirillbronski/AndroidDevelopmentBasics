package com.kbcoding.androiddevelopmentbasics.core.result

sealed class Result<T> {

    class Success<T>(val data: T) : Result<T>()

    class Error<T>(val error: Throwable) : Result<T>()

    class Pending<T> : Result<T>()

    class Empty<T> : Result<T>()

}
