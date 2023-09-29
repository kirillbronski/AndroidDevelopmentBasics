package com.kbcoding.androiddevelopmentbasics

import android.app.Application
import com.kbcoding.androiddevelopmentbasics.model.colors.InMemoryColorsRepository
import com.kbcoding.core.model.Repository
import com.kbcoding.core.model.tasks.SimpleTasksFactory
import com.kbcoding.core.model.tasks.ThreadUtils
import com.kbcoding.core.model.tasks.dispatchers.MainThreadDispatcher
import com.kbcoding.core.presentation.BaseApplication

class App : Application(), BaseApplication {

    private val tasksFactory = SimpleTasksFactory()

    private val threadUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val singletonScopeDependencies: List<Any> = listOf(
        tasksFactory,
        dispatcher,

        InMemoryColorsRepository(tasksFactory, threadUtils)
    )
}