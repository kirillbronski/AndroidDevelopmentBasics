package com.kbcoding.core.model.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)

}