package com.kbcoding.core.model.tasks.factories

import com.kbcoding.core.model.tasks.Task

typealias TaskBody<T> = () -> T

/**
 * Factory for creating async task instances ([Task]) from synchronous code defined by [TaskBody]
 */
interface TasksFactory {

    /**
     * Create a new [Task] instance from the specified body.
     */
    fun <T> async(body: TaskBody<T>): Task<T>

}