package com.kbcoding.core.model.tasks.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)

}