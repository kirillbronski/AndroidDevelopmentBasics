package com.kbcoding.androiddevelopmentbasics

import android.app.Application
import com.kbcoding.androiddevelopmentbasics.model.colors.InMemoryColorsRepository
import com.kbcoding.core.model.tasks.ThreadUtils
import com.kbcoding.core.model.dispatchers.MainThreadDispatcher
import com.kbcoding.core.model.tasks.factories.ExecutorServiceTasksFactory
import com.kbcoding.core.model.tasks.factories.HandlerThreadTasksFactory
import com.kbcoding.core.model.tasks.factories.ThreadTasksFactory
import com.kbcoding.core.presentation.BaseApplication
import java.util.concurrent.Executors

class App : Application(), BaseApplication {

    // instances of all created task factories
    private val singleThreadExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newSingleThreadExecutor())
    private val handlerThreadTasksFactory = HandlerThreadTasksFactory()
    private val cachedThreadPoolExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newCachedThreadPool())

    private val threadUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()

    /**
     * Place your singleton scope dependencies here
     */
    override val singletonScopeDependencies: List<Any> = listOf(
        cachedThreadPoolExecutorTasksFactory, // task factory to be used in view-models
        dispatcher, // dispatcher to be used in view-models

        InMemoryColorsRepository(cachedThreadPoolExecutorTasksFactory, threadUtils)
    )
}