package com.kbcoding.androiddevelopmentbasics

import androidx.fragment.app.Fragment

interface Navigator {
    fun launchNext()
    fun generateUuid(): String
    fun update()
}

fun Fragment.navigator(): Navigator = requireActivity() as Navigator