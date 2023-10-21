package com.kbcoding.androiddevelopmentbasics

import android.app.Application
import com.kbcoding.androiddevelopmentbasics.model.colors.InMemoryColorsRepository
import com.kbcoding.core.model.coroutines.IoDispatcher
import com.kbcoding.core.model.coroutines.WorkerDispatcher
import com.kbcoding.core.presentation.BaseApplication
import kotlinx.coroutines.Dispatchers

class App : Application(), BaseApplication {

    private val ioDispatcher = IoDispatcher(Dispatchers.IO)
    private val workerDispatcher = WorkerDispatcher(Dispatchers.Default)

    /**
     * Place your singleton scope dependencies here
     */
    override val singletonScopeDependencies: List<Any> = listOf(
        InMemoryColorsRepository(ioDispatcher)
    )
}