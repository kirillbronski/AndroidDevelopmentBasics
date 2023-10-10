package com.kbcoding.core.model.tasks

import com.kbcoding.core.model.ErrorResult
import com.kbcoding.core.model.FinalResult
import com.kbcoding.core.model.SuccessResult
import com.kbcoding.core.model.tasks.dispatchers.Dispatcher
import com.kbcoding.core.model.tasks.dispatchers.ImmediateDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(
    originException: Exception? = null
) : Exception(originException)

/**
 * Base interface for all async operations.
 */
interface Task<T> {

    /**
     * Blocking method for waiting and getting results.
     * Throws exception in case of error.
     * @throws [IllegalStateException] if task has been already executed
     */
    fun await(): T

    /**
     * Non-blocking method for listening task results.
     * If task is cancelled before finishing, listener is not called.
     *
     * Listener is called in main thread.
     * @throws [IllegalStateException] if task has been already executed.
     */
    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    /**
     * Cancel this task and remove listener assigned by [enqueue].
     */
    fun cancel()

    suspend fun suspend(): T = suspendCancellableCoroutine { continuation ->
        enqueue(ImmediateDispatcher()) {
            continuation.invokeOnCancellation { cancel() }
            when (it) {
                is SuccessResult -> continuation.resume(it.data)
                is ErrorResult -> continuation.resumeWithException(it.exception)
            }
        }
    }

}