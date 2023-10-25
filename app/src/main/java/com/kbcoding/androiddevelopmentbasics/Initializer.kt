package com.kbcoding.androiddevelopmentbasics

import com.kbcoding.androiddevelopmentbasics.model.colors.InMemoryColorsRepository
import com.kbcoding.core.SingletonScopeDependencies
import com.kbcoding.core.model.coroutines.IoDispatcher
import com.kbcoding.core.model.coroutines.WorkerDispatcher

object Initializer {

    // Place your singleton scope dependencies here
    fun initDependencies() = SingletonScopeDependencies.init { applicationContext ->
        // this block of code is executed only once upon the first request

        // holder classes are used because we have 2 dispatchers of the same type
        val ioDispatcher = IoDispatcher() // for IO operations
        val workerDispatcher = WorkerDispatcher() // for CPU-intensive operations

        return@init listOf(
            ioDispatcher,
            workerDispatcher,

            InMemoryColorsRepository(ioDispatcher)
        )
    }


}