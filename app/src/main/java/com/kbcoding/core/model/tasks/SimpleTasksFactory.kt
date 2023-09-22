package com.kbcoding.core.model.tasks

import android.os.Handler
import android.os.Looper
import com.kbcoding.core.model.ErrorResult
import com.kbcoding.core.model.FinalResult
import com.kbcoding.core.model.SuccessResult
import java.lang.Exception

private val handler = Handler(Looper.getMainLooper())

// todo!!!
// Temp implementation for TasksFactory and Task
class SimpleTasksFactory : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SimpleTask(body)
    }

    // todo!!!

    class SimpleTask<T>(
        private val body: TaskBody<T>
    ) : Task<T> {

        var thread: Thread? = null
        var cancelled = false

        override fun await(): T = body()

        override fun enqueue(listener: TaskListener<T>) {
            thread = Thread {
                try {
                    val data = body()
                    publishResult(listener, SuccessResult(data))
                } catch (e: Exception) {
                    publishResult(listener, ErrorResult(e))
                }
            }.apply { start() }
        }

        override fun cancel() {
            cancelled = true
            thread?.interrupt()
            thread = null
        }

        private fun publishResult(listener: TaskListener<T>, result: FinalResult<T>) {
            handler.post {
                if (cancelled) return@post
                listener(result)
            }
        }
    }

}