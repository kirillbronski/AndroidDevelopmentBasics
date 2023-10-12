package com.kbcoding.core.model

import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun <T> CancellableContinuation<T>.toEmitter(): Emitter<T> {
    return object : Emitter<T> {
        override fun emit(finalResult: FinalResult<T>) {
            when (finalResult) {
                is SuccessResult -> resume(finalResult.data)
                is ErrorResult -> resumeWithException(finalResult.exception)
            }
        }

        override fun setCancelListener(cancelListener: CancelListener) {
            invokeOnCancellation { cancelListener() }
        }
    }
}