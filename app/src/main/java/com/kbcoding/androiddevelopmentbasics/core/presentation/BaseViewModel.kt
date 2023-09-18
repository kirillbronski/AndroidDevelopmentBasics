package com.kbcoding.androiddevelopmentbasics.core.presentation

import androidx.lifecycle.ViewModel
import com.kbcoding.androiddevelopmentbasics.core.tasks.Task

abstract class BaseViewModel : ViewModel() {

    private val tasks = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        tasks.forEach { it.cancel() }
    }

    fun <T> Task<T>.autoCancel() {
        tasks.add(this)
    }
}