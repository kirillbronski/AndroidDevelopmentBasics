package com.kbcoding.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.takeWhile

private sealed class Element<T> {

    class Item<T>(val item: T) : Element<T>()

    class Error<T>(val error: Throwable) : Element<T>()

    class Completed<T> : Element<T>()

}

fun <T> Flow<T>.finiteShareIn(coroutineScope: CoroutineScope): Flow<T> {
    return this
        .map<T, Element<T>> { item -> Element.Item(item) }
        .onCompletion {
            emit(Element.Completed())
        }
        .catch { exception ->
            emit(Element.Error(exception))
        }
        .map {
            if (it is Element.Error) throw it.error
            return@map it
        }
        .takeWhile { it is Element.Item }
        .map { (it as Element.Item).item }
}