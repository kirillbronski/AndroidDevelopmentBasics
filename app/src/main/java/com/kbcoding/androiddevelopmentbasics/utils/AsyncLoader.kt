package com.kbcoding.androiddevelopmentbasics.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AsyncLoader<T>(
    private val loader: suspend () -> T,
) {

    // Implementation example by using Mutex.
    // async() and Deferred<T> can be used instead too.

    private val mutex = Mutex()
    private var value: T? = null

    suspend fun get(): T {
        mutex.withLock {
            if (value == null) {
                value = loader()
            }
        }
        return value!!
    }

}