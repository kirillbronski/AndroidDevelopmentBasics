package com.kbcoding.core.model.tasks.dispatchers

import com.kbcoding.core.model.tasks.CancelledException
import com.kbcoding.core.model.tasks.Task
import com.kbcoding.core.model.tasks.TaskListener

class SynchronizedTask<T>(
    private val task: Task<T>
) : Task<T> {

    private var cancelled = false
    private var executed = false
    private var listenerCalled = false

    override fun await(): T {
        if (cancelled) throw CancelledException()
        if (executed) throw IllegalStateException("Task has been executed")
        executed = true
        return task.await()
    }

    override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) {
        if (cancelled) return
        if (executed) throw IllegalStateException("Task has been executed")
        executed = true

        val finalListener: TaskListener<T> = { result ->
            if (!listenerCalled) {
                listenerCalled = true
                if (!cancelled) listener(result)
            }
        }

        task.enqueue(dispatcher, finalListener)
    }

    override fun cancel() {
        if (cancelled) return
        cancelled = true
        if (!listenerCalled) {
            task.cancel()
        }
    }
}