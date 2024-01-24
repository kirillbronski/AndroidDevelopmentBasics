package com.kbcoding.core.model.tasks.factories

import com.kbcoding.core.model.tasks.AbstractTask
import com.kbcoding.core.model.tasks.Task
import com.kbcoding.core.model.tasks.TaskListener
import com.kbcoding.core.model.tasks.SynchronizedTask

class ThreadTasksFactory : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ThreadTask(body))
    }

    private class ThreadTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            thread = Thread {
                executeBody(body, listener)
            }
            thread?.start()
        }

        override fun doCancel() {
            thread?.interrupt()
        }

    }

}