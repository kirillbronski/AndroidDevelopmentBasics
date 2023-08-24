package com.kbcoding.androiddevelopmentbasics

import java.io.Serializable

data class Options(
    val boxCount: Int,
    val isTimerEnabled: Boolean
) : Serializable {

    companion object {
        @JvmStatic
        val DEFAULT = Options(boxCount = 3, isTimerEnabled = false)
    }
}
