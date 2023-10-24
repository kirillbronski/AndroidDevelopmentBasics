package com.kbcoding.core.model

sealed class Progress {
    object EmptyProgress : Progress()

    data class PercentageProgress(val percentage: Int) : Progress()

    companion object {
        val START = PercentageProgress(percentage = 0)
    }
}

fun Progress.isInProgress() = this !is Progress.EmptyProgress

fun Progress.getPercentage() =
    (this as? Progress.PercentageProgress)?.percentage ?: Progress.START.percentage


